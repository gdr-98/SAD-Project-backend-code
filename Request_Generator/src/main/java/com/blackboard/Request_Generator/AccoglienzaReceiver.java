package com.blackboard.Request_Generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@Service
public class AccoglienzaReceiver implements MessageListener {

    @Autowired
    private DispatcherInfo dispatcherInfo;

    private String infoAccoglienza = "";

    @JmsListener(destination = "CodaAccoglienza")
    @Override
    public void onMessage(Message message) {
        try {
            infoAccoglienza = (String) message.getBody(Object.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
}
