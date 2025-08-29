package com.pranay.gatelog.consumer;

import com.pranay.gatelog.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    public record CustomMessage(
            String message
    ) {}

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void listen(CustomMessage message) {
        System.out.println("\nMessage read from myQueue : " + message+"\n");
    }
}