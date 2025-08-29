package com.pranay.gatelog.consumer;


import com.pranay.gatelog.config.RabbitMQConfig;
import com.pranay.gatelog.entity.UserInfo;
import com.pranay.gatelog.service.UserInfoService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;



@Component
@AllArgsConstructor
public class UserConsumer {

    private final UserInfoService userInfoService;

    public record UserInfoDto(String username, String houseNumber){}

    @RabbitListener(queues = RabbitMQConfig.QUEUE_USER)
    public void listenUserInfo(UserInfoDto userInfoDto) {
        System.out.println("\nMessage read from myQueue : " + userInfoDto+"\n");

        UserInfo userInfo = UserInfo.builder()
                .username(userInfoDto.username())
                .houseNumber(userInfoDto.houseNumber())
                .build();
        userInfoService.createUserInfo(userInfo);
        //notification to admin(OPTIONAL)
    }
}
