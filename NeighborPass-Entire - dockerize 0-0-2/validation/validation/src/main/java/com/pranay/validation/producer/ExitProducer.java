package com.pranay.validation.producer;


import com.pranay.validation.config.RabbitMQConfig;
import com.pranay.validation.dto.ExitDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;





@Service
@AllArgsConstructor
public class ExitProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendExit(ExitDto exitDto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.DEFAULT_EXCHANGE, RabbitMQConfig.EXIT_ROUTING_KEY, exitDto);
        System.out.println("SENT : "+ exitDto);
    }
}
