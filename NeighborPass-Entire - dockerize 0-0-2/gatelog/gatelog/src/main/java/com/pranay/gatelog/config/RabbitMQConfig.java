package com.pranay.gatelog.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "myQueue";
    public static final String QUEUE_ENTRY = "entryQueue";
    public static final String QUEUE_USER = "userQueue";
    public static final String QUEUE_EXIT = "exitQueue";
    public static final String QUEUE_USER_DELETE = "userDeleteQueue";

    public static final String USER_ROUTING_KEY = "userRoutingKey";
    public static final String ENTRY_BINDING_KEY = "entryBindingKey";
    public static final String EXIT_ROUTING_KEY = "exitRoutingKey";

    public static final String DEFAULT_EXCHANGE = "defaultExchange";

//    @Bean
//    public Queue queue() {
//        return QueueBuilder.nonDurable(QUEUE_NAME).build();
//    }
//
//    @Bean
//    public Queue entryQueue() {
//        return QueueBuilder.nonDurable(QUEUE_ENTRY).build();
//    }
//
//    @Bean
//    public Queue userQueue() {
//        return QueueBuilder.nonDurable(QUEUE_USER).build();
//    }
//
//    @Bean
//    public Queue exitQueue() {
//        return QueueBuilder.nonDurable(QUEUE_EXIT).build();
//    }

//    @Bean
//    public Binding entryBinding(@Qualifier("entryQueue") Queue queue, DirectExchange directExchange) {
//        return BindingBuilder.bind(queue)
//                .to(directExchange)
//                .with(ENTRY_BINDING_KEY);
//    }
//
//    @Bean
//    public Binding userBinding(@Qualifier("userQueue") Queue queue, DirectExchange directExchange) {
//        return BindingBuilder.bind(queue)
//                .to(directExchange)
//                .with(USER_ROUTING_KEY);
//    }
//
//    @Bean
//    public Binding exitBinding(@Qualifier("exitQueue") Queue queue, DirectExchange directExchange) {
//        return BindingBuilder.bind(queue)
//                .to(directExchange)
//                .with(EXIT_ROUTING_KEY);
//    }
//
//    @Bean
//    public DirectExchange exchange() {
//        return new DirectExchange(DEFAULT_EXCHANGE);
//    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

}