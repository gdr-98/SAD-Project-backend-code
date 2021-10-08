package com.project.ProxyCameriere.ProxyCameriere.ProxyCameriere.JMS;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.gson.Gson;
import com.project.ProxyCameriere.ProxyCameriere.web.BaseMessage;
import com.project.ProxyCameriere.ProxyCameriere.web.LoginResponse;
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
        String helper;
        BaseMessage msg_received = new BaseMessage() ;
        String msg_to_send = "";
        Gson gson=new Gson();
        try {
             helper= (String) message.getBody(String.class);
             msg_received=gson.fromJson(helper,BaseMessage.class);
             log.info("Returned is" +helper);
             msg_to_send = (String) message.getBody(Object.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
        /*Message Types
        tableRequest,
                userWaitingNotification,  [n]
                itemCompleteRequest,    [n]
                menuRequest,    [1]
                orderToTableGenerationRequest,  [1]
                cancelOrderRequest,     [1]
                cancelOrderedItemRequest,   [1]
                registerNotification*/


        switch (msg_received.messageName){
            //In the case a client is waiting for ordination
             case "userWaitingNotification" :
                for (Map.Entry<String, String> me : Webhook.Waiters.entrySet()) {
                     poster.createPost("http://"+ me.getValue()+"/notification",msg_to_send);
                }
                break;
            //Just a confirmation message to the event creator
            case "menuRequest" : case "cancelOrderedItemRequest" : case "orderToTableGenerationRequest" :
                case "cancelOrderRequest" : case "tableRequest":
                    if(Webhook.Waiters.containsKey(msg_received.user)) {
                        poster.createPost("http://" + Webhook.Waiters.get(msg_received.user) + "/notification", msg_to_send);
                        log.info(msg_to_send);
                    }
                    break;
            //adds a new user and notify
            case "registerNotification":
                LoginResponse resp=gson.fromJson(msg_to_send,LoginResponse.class);
                Webhook.Add_Pizza_maker(resp.user,resp.url);
                if( Webhook.Pizza_maker.containsKey(msg_received.user)) //if the name exists
                    poster.createPost("http://"+ Webhook.Waiters.get(msg_received.user)+"/notification",msg_to_send);
                break;
            default :
                log.info("Message does not match with any of the expected ones");
                break;
        }
    }
}