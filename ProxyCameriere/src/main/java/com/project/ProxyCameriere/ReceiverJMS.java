package com.project.ProxyCameriere;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Ricezione sulla coda CodaCamerieriBroker ed in conseguenza all'evento
 * invia la notifica agli iscritti al webhook
 */

@Service
public class ReceiverJMS implements MessageListener {

    public Webhook webh = new Webhook();

    private final Logger log = LoggerFactory.getLogger(ReceiverJMS.class);

    @JmsListener(destination = "CodaCamerieriBroker")
    @Override
    public void onMessage(Message message) {
        log.info("Event received");
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
                URL url = new URL("http", temp.substring(1, 9), Integer.parseInt(temp.substring(11, 14)), "/notification/");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/json)");

                connection.setRequestProperty("Content-Length",
                        Integer.toString(notifica.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");

                connection.setUseCaches(false);
                connection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream (
                        connection.getOutputStream());
                wr.writeBytes(notifica);
                wr.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
}
