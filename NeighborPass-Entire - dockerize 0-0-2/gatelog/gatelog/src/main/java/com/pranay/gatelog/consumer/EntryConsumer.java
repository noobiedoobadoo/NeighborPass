package com.pranay.gatelog.consumer;

import com.pranay.gatelog.config.RabbitMQConfig;
import com.pranay.gatelog.dto.EntryDto;
import com.pranay.gatelog.dto.VisitorDto;
import com.pranay.gatelog.service.EntryService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.transaction.annotation.Transactional;


@Component
@AllArgsConstructor
public class EntryConsumer {

    private final EntryService entryService;

    public record EntryRequestDto(String name, String contact, String houseNumber){}

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ENTRY)
    @Transactional
    public void listenEntry(EntryRequestDto entryRequestDto) {
        System.out.println("\nMessage read from myQueue : " + entryRequestDto+"\n");

        VisitorDto visitorDtoRequest = VisitorDto.builder()
                .name(entryRequestDto.name())
                .contact(entryRequestDto.contact())
                .build();
        entryService.createEntry(visitorDtoRequest, entryRequestDto.houseNumber());
        //sending notification to user
    }
}
