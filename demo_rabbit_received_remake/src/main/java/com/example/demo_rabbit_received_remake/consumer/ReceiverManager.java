package com.example.demo_rabbit_received_remake.consumer;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ReceiverManager {
    @Autowired
    RabbitTemplate rabbitTemplate;


    public void receiveMessage2(String message){
        log.info("tin nhan: "+ message);
    }
}
