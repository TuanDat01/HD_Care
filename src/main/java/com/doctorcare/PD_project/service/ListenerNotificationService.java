package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.configure.RabbitMQConfig;
import com.doctorcare.PD_project.dto.request.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ListenerNotificationService {
    private final WebClient webClient;

    @Value("${app.webhook.uri}")
    private String webhookurl;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(NotificationMessage payload ) {
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
    }
}
