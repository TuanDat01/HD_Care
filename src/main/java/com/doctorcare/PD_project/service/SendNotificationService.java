package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.configure.RabbitMQConfig;
import com.doctorcare.PD_project.dto.request.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendNotificationService {
    private final RabbitTemplate rabbitTemplate;

    public void sendNotification(NotificationMessage payload){
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                payload
        );
    }
}
