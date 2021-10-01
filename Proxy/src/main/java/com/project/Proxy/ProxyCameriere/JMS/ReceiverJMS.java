package com.project.Proxy.ProxyCameriere.JMS;

import com.project.Proxy.web.Post;
import com.project.Proxy.web.Webhook;
import org.apache.tomcat.util.json.JSONParser;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.json.JsonObject;

/*
 * Listen queue "CodaCamerieriBroker" and if detect an event
 * notify every client registered to the webhook
 */

@Service
public class ReceiverJMS implements MessageListener {

    public Webhook webhook = new Webhook();

    private final Post poster = new Post();

    private final Logger log = LoggerFactory.getLogger(ReceiverJMS.class);

    @JmsListener(destination = "CodaCamerieriBroker")
    @Override
    public void onMessage(@NotNull Message message) {
        /*
         * Retrieve body of the message sent by ActiveMQ.
         */
        String notifica = "";
        try {
            notifica = (String) message.getBody(Object.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }



        log.info("Event received: " + notifica);

        /*
         * Advise every client of the event.
         * i.e. Create post request for every url registered in the webhook.
         */
        for (String temp : webhook.getUrls()) {
            poster.createPost(("http://" + temp + "/notification"), notifica);
        }
    }
}
