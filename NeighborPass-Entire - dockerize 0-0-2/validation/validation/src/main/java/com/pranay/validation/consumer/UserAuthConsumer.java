package com.pranay.validation.consumer;


import com.pranay.validation.config.RabbitMQConfig;
import com.pranay.validation.controller.ValidationController;
import com.pranay.validation.dto.UserDto;
import com.pranay.validation.entity.User;
import com.pranay.validation.producer.UserProducer;
import com.pranay.validation.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserAuthConsumer {

    private final UserProducer userProducer;

    private final UserService userService;

    @RabbitListener(queues = RabbitMQConfig.USER_AUTH_QUEUE)
    public void listenUserAuth(ValidationController.RegisterDTO registerDTO) {
        System.out.println("received registerDto : "+registerDTO);

        userService.addUser(User.builder()
                        .username(registerDTO.username())
                        .houseNumber(registerDTO.houseNumber())
                .build());

        userProducer.sendUser(UserDto.builder()
                        .username(registerDTO.username())
                        .houseNumber(registerDTO.houseNumber())
                .build());
    }
}
