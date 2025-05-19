package com.doctorcare.PD_project.configure;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "notificationQueue";
    public static final String EXCHANGE_NAME = "notificationExchange";
    public static final String ROUTING_KEY = "notification.key";

    public static final String RETRY_QUEUE_NAME = "notificationQueue.retry";
    public static final String RETRY_EXCHANGE_NAME = "notificationExchange.retry";
    public static final String RETRY_ROUTING_KEY = "notification.retry.key";

    public static final String DLQ_NAME = "notificationQueue.dlq";
    public static final String DLQ_EXCHANGE_NAME = "notificationExchange.dlq";
    public static final String DLQ_ROUTING_KEY = "notification.dlq.key";

    // 🎯 Main Queue (thực hiện xử lý chính)
    @Bean
    Queue mainQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", RETRY_EXCHANGE_NAME);
        args.put("x-dead-letter-routing-key", RETRY_ROUTING_KEY);
        return new Queue(QUEUE_NAME, true, false, false, args);
    }

    @Bean
    DirectExchange mainExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding mainBinding() {
        return BindingBuilder.bind(mainQueue())
                .to(mainExchange())
                .with(ROUTING_KEY);
    }

    // 🔁 Retry Queue (delay 10s, quay lại main queue)
    @Bean
    Queue retryQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", EXCHANGE_NAME);
        args.put("x-dead-letter-routing-key", ROUTING_KEY);
        args.put("x-message-ttl", 10_000); // 10 giây
        return new Queue(RETRY_QUEUE_NAME, true, false, false, args);
    }

    @Bean
    DirectExchange retryExchange() {
        return new DirectExchange(RETRY_EXCHANGE_NAME);
    }

    @Bean
    Binding retryBinding() {
        return BindingBuilder.bind(retryQueue())
                .to(retryExchange())
                .with(RETRY_ROUTING_KEY);
    }

    // ❌ DLQ (sau 3 lần vẫn lỗi → bỏ vào đây)
    @Bean
    Queue dlqQueue() {
        return new Queue(DLQ_NAME, true);
    }

    @Bean
    DirectExchange dlqExchange() {
        return new DirectExchange(DLQ_EXCHANGE_NAME);
    }

    @Bean
    Binding dlqBinding() {
        return BindingBuilder.bind(dlqQueue())
                .to(dlqExchange())
                .with(DLQ_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name = "rabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }
}