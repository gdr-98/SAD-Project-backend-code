package com.example.Sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    @Autowired
    private JmsTemplate JmsTemp;

    @Value("TestQueue")
    private String JmsQueue;

    public void sendMessage (String message) {
        JmsTemp.convertAndSend(JmsQueue, message);
    }
}
