package com.pranay.validation.producer;


import com.pranay.validation.config.RabbitMQConfig;
import com.pranay.validation.dto.EntryRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EntryProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendEntry(EntryRequestDto entryRequestDto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.DEFAULT_EXCHANGE,
                RabbitMQConfig.ENTRY_BINDING_KEY, entryRequestDto);
        System.out.println("SENT : "+ entryRequestDto);
    }
}
