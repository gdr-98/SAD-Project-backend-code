package com.blackboard.Request_Generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@Service
public class SalaReceiver implements MessageListener {

    @Autowired
    private DispatcherInfo dispatcherInfo;

    private String infoSala = "";

    @JmsListener(destination = "CodaCamerieri")
    @Override
    public void onMessage(Message message) {
        try {
            infoSala = (String) message.getBody(Object.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }


    }
}
