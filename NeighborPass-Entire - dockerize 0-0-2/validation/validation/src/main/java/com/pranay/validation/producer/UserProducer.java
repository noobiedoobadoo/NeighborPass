package com.pranay.validation.producer;


import com.pranay.validation.config.RabbitMQConfig;
import com.pranay.validation.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendUser(UserDto userDto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.DEFAULT_EXCHANGE, RabbitMQConfig.USER_ROUTING_KEY, userDto);
        System.out.println("SENT : "+ userDto);
    }

    public void removeUser(UserDto userDto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.DEFAULT_EXCHANGE, RabbitMQConfig.USER_DELETE_ROUTING_KEY , userDto);
        System.out.println("SENT : "+ userDto+" DELETE");
    }
}
