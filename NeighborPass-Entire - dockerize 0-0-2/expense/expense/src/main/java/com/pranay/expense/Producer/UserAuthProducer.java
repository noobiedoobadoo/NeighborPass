package com.pranay.expense.Producer;


import com.pranay.expense.config.RabbitMQConfig;
import com.pranay.expense.service.RegisterDTO;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAuthProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendUserAuth(RegisterDTO registerDTO) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.DEFAULT_EXCHANGE,
                RabbitMQConfig.USER_AUTH_ROUTING_KEY,
                registerDTO);

        System.out.println("sent registerDTO : "+ registerDTO);
    }


}
