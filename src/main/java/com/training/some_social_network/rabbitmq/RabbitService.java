package com.training.some_social_network.rabbitmq;

import com.training.some_social_network.dto.PostStateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.training.some_social_network.rabbitmq.RabbitConfiguration.postRoutingKeyName;
import static com.training.some_social_network.rabbitmq.RabbitConfiguration.postTopicExchangeName;

@Service
@RequiredArgsConstructor
public class RabbitService {

    private final RabbitTemplate rabbitTemplate;

    public void sendPostState(PostStateDto postState) {
        rabbitTemplate.convertAndSend(postTopicExchangeName, postRoutingKeyName, postState);
    }
}
