package com.pranay.validation.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
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
    public static final String USER_AUTH_QUEUE = "userAuthQueue";


    public static final String DEFAULT_ROUTING_KEY = "defaultRoutingKey";
    public static final String USER_ROUTING_KEY = "userRoutingKey";
    public static final String ENTRY_BINDING_KEY = "entryBindingKey";
    public static final String EXIT_ROUTING_KEY = "exitRoutingKey";

    public static final String DEFAULT_EXCHANGE = "defaultExchange";
    public static final String USER_DELETE_QUEUE = "userDeleteQueue";
    public static final String USER_DELETE_ROUTING_KEY = "userDeleteRoutingKey";
    public static final String USER_DELETE_QUEUE_FOR_AUTH = "userDeleteQueueForAuth";

    @Bean
    public Queue myQueue() {
        return new Queue(QUEUE_NAME,true);
    }

    @Bean
    public Queue entryQueue() {
        return new Queue(QUEUE_ENTRY,true);
    }

    @Bean
    public Queue userQueue() {
        return new Queue(QUEUE_USER,true);
    }

    @Bean
    public Queue userDeleteQueue() {
        return new Queue(USER_DELETE_QUEUE,true);
    }

    @Bean
    public Queue userDeleteQueueForAuth() {
        return new Queue(USER_DELETE_QUEUE_FOR_AUTH,true);
    }

    @Bean
    public Queue exitQueue() {
        return new Queue(QUEUE_EXIT,true);
    }

    @Bean
    public Binding userDeleteBinding(@Qualifier(USER_DELETE_QUEUE) Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue)
                .to(directExchange)
                .with(USER_DELETE_ROUTING_KEY);
    }

    @Bean
    public Binding userDeleteBindingForAuth(@Qualifier(USER_DELETE_QUEUE_FOR_AUTH) Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue)
                .to(directExchange)
                .with(USER_DELETE_ROUTING_KEY);
    }

    @Bean
    public Binding myBinding(@Qualifier("myQueue") Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue)
                .to(directExchange)
                .with(DEFAULT_ROUTING_KEY);
    }

    @Bean
    public Binding entryBinding(@Qualifier("entryQueue") Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue)
                .to(directExchange)
                .with(ENTRY_BINDING_KEY);
    }

    @Bean
    public Binding userBinding(@Qualifier("userQueue") Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue)
                .to(directExchange)
                .with(USER_ROUTING_KEY);
    }

    @Bean
    public Binding exitBinding(@Qualifier("exitQueue") Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue)
                .to(directExchange)
                .with(EXIT_ROUTING_KEY);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(DEFAULT_EXCHANGE);
    }

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

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.declareQueue(exitQueue());  // Explicitly declare on startup
        admin.declareQueue(myQueue());  // Explicitly declare on startup
        admin.declareQueue(entryQueue());  // Explicitly declare on startup
        admin.declareQueue(userQueue());  // Explicitly declare on startup
        admin.declareQueue(userDeleteQueue());  // Explicitly declare on startup
        admin.declareQueue(userDeleteQueueForAuth());
        return admin;
    }

}