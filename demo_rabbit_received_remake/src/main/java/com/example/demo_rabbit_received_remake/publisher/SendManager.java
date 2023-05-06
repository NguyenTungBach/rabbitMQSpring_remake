package com.example.demo_rabbit_received_remake.publisher;

import com.example.demo_rabbit_received_remake.config.RabbitConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Log4j2
public class SendManager {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendMessage(){
        rabbitTemplate.setExchange(RabbitConfig.exchangeName);
        rabbitTemplate.setRoutingKey(RabbitConfig.routingKey);
        rabbitTemplate.convertAndSend(RabbitConfig.exchangeName,RabbitConfig.routingKey,"queue2");
    }
}
