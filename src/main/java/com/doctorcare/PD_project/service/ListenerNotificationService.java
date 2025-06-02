package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.configure.RabbitMQConfig;
import com.doctorcare.PD_project.dto.request.NotificationMessage;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ListenerNotificationService {
    private final WebClient webClient;

    @Value("${app.webhook.uri}")
    private String webhookurl;


    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(Message message, Channel channel, NotificationMessage payload) throws IOException {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        List<Map<String, Object>> xDeath = (List<Map<String, Object>>) headers.get("x-death");

        long retryCount = 0;
        if (xDeath != null && !xDeath.isEmpty()) {
            for (Map<String, Object> death : xDeath) {
                String queue = (String) death.get("queue");
                if (RabbitMQConfig.QUEUE_NAME.equals(queue)) {
                    retryCount = (Long) death.get("count");
                }
            }
        }

        if (retryCount >= 3) {
            // ✅ Đẩy sang DLQ bằng cách reject không requeue
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            System.err.println("❌ Đã retry quá 3 lần, chuyển vào DLQ: " + message);
            return;
        }

        // Thử xử lý logic
        try {
            System.out.println("In consumer " + payload.getUsername());
            try {
                webClient.post()
                        .uri(webhookurl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(payload)
                        .retrieve()
                        .bodyToMono(Void.class)
                        .subscribe();
            } catch (Exception e) {
                System.err.println("Lỗi khi xử lý message: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("⚠️ Lỗi xử lý, sẽ retry lần " + (retryCount + 1));
            throw new RuntimeException("Lỗi xử lý, để RabbitMQ retry");
        }
    }

}
