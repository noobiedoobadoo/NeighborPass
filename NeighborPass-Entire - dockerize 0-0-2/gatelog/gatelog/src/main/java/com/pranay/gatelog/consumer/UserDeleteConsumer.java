package com.pranay.gatelog.consumer;


import com.pranay.gatelog.config.RabbitMQConfig;
import com.pranay.gatelog.service.UserInfoService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDeleteConsumer {

    private final UserInfoService userInfoService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_USER_DELETE)
    public void listenDeleteUserInfo(UserConsumer.UserInfoDto userInfoDto) {
        System.out.println("\nMessage read from myQueue : " + userInfoDto+"\n");
        userInfoService.deleteUserInfo(userInfoDto.houseNumber());
        System.out.println("deleted : "+ userInfoDto.houseNumber());
    }
}
