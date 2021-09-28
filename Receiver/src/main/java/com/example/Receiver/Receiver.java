package com.example.Receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@Service
public class Receiver implements MessageListener {

    private final Logger log = LoggerFactory.getLogger(Receiver.class);

    @JmsListener(destination = "TestQueue")
    @Override
    public void onMessage(Message message) {
        try {
            log.info("Received message: " + message.getBody(Object.class));
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
}
