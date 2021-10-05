package com.project.ProxyAccoglienza.JMS;

import com.google.gson.Gson;
import com.project.ProxyAccoglienza.web.BaseMessage;
import com.project.ProxyAccoglienza.web.Post;
import com.project.ProxyAccoglienza.web.Webhook;
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
 * Listen queue "CodaAccoglienzaBroker" and if detect an event
 * notify every client registered to the webhook
 */

@Service
public class ReceiverJMS implements MessageListener {

    private final Post poster = new Post();

    private final Logger log = LoggerFactory.getLogger(ReceiverJMS.class);

    @JmsListener(destination = "CodaAccoglienzaBroker")
    @Override
    public void onMessage(@NotNull Message message) {
        /*
         * Retrieve body of the message sent by ActiveMQ.
         */

        String msg_to_send = "";
        String helper ;
        BaseMessage msg_received = new BaseMessage();
        Gson gson = new Gson();
        try {
            helper = (String) message.getBody(Object.class);
            msg_received=gson.fromJson(helper,BaseMessage.class);
            log.info("Returned is" +helper);
            msg_to_send = (String) message.getBody(Object.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }

        /*
        tableRequest,
        freeTableRequest,
         */

        switch (msg_received.request) {
            case "tableRequest" :
                poster.createPost("http://"+ Webhook.Acceptance.get(msg_received.user)+"/notification",msg_to_send);
                break;

            case "freeTableRequest":
                for (Map.Entry<String, String> me : Webhook.Acceptance.entrySet()) {
                    poster.createPost("http://"+ me.getValue()+"/notification",msg_to_send);
                }
                break;

            default:
                log.info("Message does not match with any of the expected ones");
                break;
        }

        log.info("Event received: " + msg_received);
    }
}