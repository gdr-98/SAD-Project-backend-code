package com.project.Proxy.ProxyCameriere.JMS;

import com.google.gson.Gson;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Listen queue "CodaCamerieriBroker" and if detect an event
 * notify every client registered to the webhook
 */

@Service
public class ReceiverJMS implements MessageListener {

    public Webhook webhook = new Webhook();

    private final Post poster = new Post();

    Gson g = new Gson();

    private final Logger log = LoggerFactory.getLogger(ReceiverJMS.class);

    @JmsListener(destination = "CodaCamerieriBroker")
    @Override
    public void onMessage(@NotNull Message message) {
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

        Pattern p = Pattern.compile("\\.response\\.:\\.\\w+\\.");   // the pattern to search for
        Matcher m = p.matcher(msg_received);

        log.info("Event received: " + msg_received);
        if (m.find()) {
            message_type = m.group(0); //Controlla : Ritorna una stringa effettivamente ?
            switch (message_type) {

                case "LoginResponse" :
                    LoginResponse msg_object = g.fromJson(msg_received, LoginResponse.class);
                    webhook.addUrl(msg_object.URL);
                    poster.createPost(("http://" + msg_object.URL + "/login"), "127.0.0.1:8080");
                    break;

                case "DA DEFINIRE - Ordine completato parzialmente/completamente" :
                    for (String temp : webhook.getUrls()) {
                        poster.createPost(("http://" + temp + "/msg_receivedtion"), msg_received);
                    }
                    break;

                case "Ordine Creato con successo" :
                    
                    break;
            }
        }

        /*
         * Advise every client of the event.
         * i.e. Create post request for every url registered in the webhook.
         */
        for (String temp : webhook.getUrls()) {
            poster.createPost(("http://" + temp + "/msg_receivedtion"), msg_received);
        }
    }
}
