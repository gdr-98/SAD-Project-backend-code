package com.project.ProxyCameriere;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/*
 * Ricezione sulla coda CodaCamerieriBroker ed in conseguenza all'evento
 * invia la notifica agli iscritti al webhook
 */

@Service
public class ReceiverJMS implements MessageListener {

    public Webhook webh = new Webhook();

    private Post poster = new Post();

    private final Logger log = LoggerFactory.getLogger(ReceiverJMS.class);

    @JmsListener(destination = "CodaCamerieriBroker")
    @Override
    public void onMessage(Message message) {

        /*
        String notifica = "";
        try {
            notifica = (String) message.getBody(Object.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
        */

        String notifica = "";
        try {
            notifica = (String) message.getBody(Object.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }

        log.info("Event received: " + notifica);
        HttpStatus status;
        for (String temp : webh.urls) {
            // 127.0.0.1:8081
            status = poster.createPost(("http://" + temp + "/notification/" + notifica), "").getStatusCode();
        }
        /*
        HttpURLConnection connection = null;
        String notifica = "";
        try {
            notifica = (String) message.getBody(Object.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }

        for (String temp : webh.urls) {
            // Manda notifica ai camerieri registrati della verifica di un evento

            try {
                //Create connection
                URL url = new URL("http", temp.substring(0, temp.indexOf(':')),
                        Integer.parseInt(temp.substring(temp.indexOf(':')+1, temp.length())), "/notification/");

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/json; utf-8");

                connection.setUseCaches(false);
                connection.setDoOutput(true);

                //Send request
                try (OutputStream os = connection.getOutputStream()) {
                    byte [] input = notifica.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
         */
    }
}
