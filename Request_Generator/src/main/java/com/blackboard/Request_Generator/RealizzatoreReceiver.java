package com.blackboard.Request_Generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@Service
public class RealizzatoreReceiver implements MessageListener {

    @Autowired
    private DispatcherInfo dispatcherInfo;

    private String infoRealizzatore = "";

    @JmsListener(destination = "CodaRealizzatori")
    @Override
    public void onMessage(Message message) {
        try {
            infoRealizzatore = (String) message.getBody(Object.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
}
