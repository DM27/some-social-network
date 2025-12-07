package com.training.some_social_network.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@EnableRabbit
@Configuration
public class RabbitConfiguration {

    static final String postTopicExchangeName = "post-cache-exchange";
    static final String postQueueName = "post-cache-queue";
    static final String postRoutingKeyName = "post.cache";


    @Bean
    public Queue postQueue() {
        return new Queue(postQueueName, true);
    }

    @Bean
    public TopicExchange postExchange() {
        return new TopicExchange(postTopicExchangeName);
    }

    @Bean
    public Binding postBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(postRoutingKeyName);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        DefaultClassMapper defaultClassMapper = new DefaultClassMapper();
        defaultClassMapper.setTrustedPackages("*");
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        jackson2JsonMessageConverter.setClassMapper(defaultClassMapper);
        return jackson2JsonMessageConverter;
    }
}
