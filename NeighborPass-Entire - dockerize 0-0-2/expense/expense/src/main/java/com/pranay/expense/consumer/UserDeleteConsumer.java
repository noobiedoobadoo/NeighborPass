package com.pranay.expense.consumer;

import com.pranay.expense.config.RabbitMQConfig;
import com.pranay.expense.service.RegisterDTO;
import com.pranay.expense.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDeleteConsumer {

    private final UserService userService;

    @RabbitListener(queues = RabbitMQConfig.USER_DELETE_QUEUE_FOR_AUTH)
    public void listenDeleteUserInfo(RegisterDTO registerDTO) {
        System.out.println("\nMessage read from myQueue : " + registerDTO+"\n");
        userService.deleteUser(registerDTO.houseNumber());
        System.out.println("deleted : "+ registerDTO.houseNumber());
    }
}