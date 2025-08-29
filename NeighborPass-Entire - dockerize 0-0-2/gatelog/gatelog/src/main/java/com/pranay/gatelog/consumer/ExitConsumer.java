package com.pranay.gatelog.consumer;


import com.pranay.gatelog.config.RabbitMQConfig;
import com.pranay.gatelog.dto.EntryDto;
import com.pranay.gatelog.service.EntryService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class ExitConsumer {

    private final EntryService entryService;

    public record ExitDto(
            String contact
    ) {
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_EXIT)
    public void listenExit(ExitDto exitDto){
        System.out.println("\nMessage read from myQueue : " + exitDto+"\n");

        String contact = exitDto.contact();
        List<EntryDto> entryDtoList = entryService.logExit(contact);

        //notification to user
    }
}
