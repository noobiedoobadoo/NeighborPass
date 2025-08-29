package com.pranay.expense.config;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

@Configuration
public class RabbitMQConfig {


    public static final String DEFAULT_EXCHANGE = "defaultExchange";
    public static final String USER_AUTH_QUEUE = "userAuthQueue";
    public static final String USER_AUTH_ROUTING_KEY = "userAuthBindingKey";
    public static final String USER_DELETE_QUEUE_FOR_AUTH = "userDeleteQueueForAuth";
    public static final String USER_DELETE_ROUTING_KEY = "userDeleteRoutingKey";


    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public Binding userAuthBinding(@Qualifier("userAuthQueue") Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue)
                .to(directExchange)
                .with(USER_AUTH_ROUTING_KEY);
    }

    @Bean
    public Queue userAuthQueue() {
        return new Queue(USER_AUTH_QUEUE, true);
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
    public AmqpAdmin amqpAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareQueue(userAuthQueue());  // Explicitly declares the queue on the broker
        return rabbitAdmin;
    }
}
