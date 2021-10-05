package com.project.Proxy.ProxyRealizzatore.ProxyPizzaiolo;

import com.project.Proxy.web.BaseMessage;
import com.project.Proxy.web.Post;
import com.project.Proxy.web.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ReceiverJMS implements MessageListener {

    private final Post poster = new Post();

    private final Logger log = LoggerFactory.getLogger(ReceiverJMS.class);

    @JmsListener(destination = "CodaPizzaioliBroker")
    @Override
    public void onMessage (Message message) {
        /*
         * Retrieve body of the message sent by ActiveMQ.
         */
        String msg_to_send = "";
        BaseMessage msg_received = new BaseMessage();
        try {
            msg_received = (BaseMessage) message.getBody(Object.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }

        /*
        itemCompleteRequest,
         */

        switch (msg_received.request) {
            case "itemCompleteRequest": case "itemWorkingRequest":
                poster.createPost("http://"+ Webhook.Pizza_maker.get(msg_received.user)+"/notification",msg_to_send);
                break;

            default:
                log.info("Message does not match with any of the expected ones");
                break;
        }
    }
}