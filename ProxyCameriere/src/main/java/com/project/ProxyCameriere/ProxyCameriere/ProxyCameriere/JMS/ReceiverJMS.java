package com.project.ProxyCameriere.ProxyCameriere.ProxyCameriere.JMS;

import com.project.ProxyCameriere.ProxyCameriere.web.BaseMessage;
import com.project.ProxyCameriere.ProxyCameriere.web.Post;
import com.project.ProxyCameriere.ProxyCameriere.web.Webhook;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Map;


/*
 * Listen queue "CodaCamerieriBroker" and if detect an event
 * notify every client registered to the webhook
 */

@Service
public class ReceiverJMS implements MessageListener {

    private final Post poster = new Post();

    private final Logger log = LoggerFactory.getLogger(ReceiverJMS.class);

    @JmsListener(destination = "CodaCamerieriBroker")
    @Override
    public void onMessage(@NotNull Message message) {
        /*
         * Retrieve body of the message sent by ActiveMQ.
         */

        String url;
        BaseMessage msg_received = new BaseMessage() ;
        String msg_to_send = "";
        try {
            msg_received = (BaseMessage) message.getBody(Object.class);
            msg_to_send = (String) message.getBody(Object.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
        /*Message Types
        tableRequest,
                userWaitingForOrderRequest,  [n]
                itemCompleteRequest,    [n]
                menuRequest,    [1]
                orderToTableGenerationRequest,  [1]
                cancelOrderRequest,     [1]
                cancelOrderedItemRequest,   [1]*/


        switch (msg_received.request){

            case "itemCompleteRequest" : case "userWaitingForOrderRequest" :
                for (Map.Entry<String, String> me : Webhook.Waiters.entrySet()) {
                     poster.createPost("http://"+ me.getValue()+"/notification",msg_to_send);
                }
                break;

            case "menuRequest" : case "cancelOrderedItemRequest" : case "orderToTableGenerationRequest" :
                case "cancelOrderRequest" : case "orderRequest" : case "itemWorkingRequest" :
                poster.createPost("http://"+ Webhook.Waiters.get(msg_received.user)+"/notification",msg_to_send);
                break;

            default :
                log.info("Message does not match with any of the expected ones");

        }

        log.info("Event received: ");
    }
}