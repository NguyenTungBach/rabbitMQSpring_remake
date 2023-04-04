package com.example.demo_rabbit_received_remake.controller;

import com.example.demo_rabbit_received_remake.publisher.SendManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RestController
public class RabbitController {
    @Autowired
    private SendManager sendManager;

    @RequestMapping(path = "queue2",method = RequestMethod.GET)
    public ResponseEntity<?> response() throws IOException, TimeoutException {
        sendManager.sendMessage();
        return new ResponseEntity("OK", HttpStatus.OK);
    }
}
