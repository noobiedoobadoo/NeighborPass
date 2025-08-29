package com.pranay.validation.producer;


import com.pranay.validation.config.RabbitMQConfig;
import com.pranay.validation.dto.CustomMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    //@PostConstruct
    public void initQueue() {
        sendMessage(new CustomMessage("INITIALIZING QUEUES"));
    }

    public void sendMessage(CustomMessage message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, message);

    }
}