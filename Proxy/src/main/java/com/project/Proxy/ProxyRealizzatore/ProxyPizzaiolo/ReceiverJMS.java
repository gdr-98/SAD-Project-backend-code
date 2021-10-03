package com.project.Proxy.ProxyRealizzatore.ProxyPizzaiolo;

import org.springframework.jms.annotation.JmsListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ReceiverJMS implements MessageListener {

    @JmsListener(destination = "CodaPizzaioliBroker")
    @Override
    public void onMessage (Message message) {
        /*
         * Retrieve body of the message sent by ActiveMQ.
         */
        String message_type = "";
        String msg_received = "";
        try {
            msg_received = (String) message.getBody(Object.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
}