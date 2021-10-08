package com.project.ProxyAccoglienza.JMS;

import com.google.gson.Gson;
import com.project.ProxyAccoglienza.web.BaseMessage;
import com.project.ProxyAccoglienza.web.LoginResponse;
import com.project.ProxyAccoglienza.web.Post;
import com.project.ProxyAccoglienza.web.Webhook;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;


/*
 * Listen queue "CodaAccoglienzaBroker" and if detect an event
 * notify every client registered to the webhook
 */

@Service
public class ReceiverJMS implements MessageListener {

    private final Post poster = new Post();

    private final Logger log = LoggerFactory.getLogger(ReceiverJMS.class);


    @Value("${server.port}")
    public String address_port;

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
            helper = (String) message.getBody(String.class);
            msg_received=gson.fromJson(helper,BaseMessage.class);
            log.info("Returned is" +helper);
            msg_to_send = (String) message.getBody(Object.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }

        /*
        tableRequest,
        freeTableRequest,
        userWaitingForOrdersRequest
        registerNotification
         */

        switch (msg_received.messageName) {
            //Semplici messaggi di conferma che vanno al singolo
            case "tableRequest" : case "userWaitingForOrderRequest": case "freeTableRequest":
                if(Webhook.Acceptance.containsKey(msg_received.user))
                    poster.createPost("http://"+ Webhook.Acceptance.get(msg_received.user)+"/notification",msg_to_send);
                break;
            //adds a new user and notify
            case "registerNotification":
                LoginResponse resp=gson.fromJson(msg_to_send,LoginResponse.class);
                Webhook.Add_Acceptance(resp.user,resp.url);
                log.info("Added user "+resp.user+"with url"+resp.url);

                //NNow set the url to post
                resp.url= getPostAddress()+"/acceptanceSend";
                msg_to_send=gson.toJson(resp,LoginResponse.class);
                if( Webhook.Acceptance.containsKey(msg_received.user)) //if the name exists
                    poster.createPost("http://"+ Webhook.Acceptance.get(msg_received.user)+"/notification",msg_to_send);
                break;
            default:
                log.info("Message does not match with any of the expected ones");
                break;
        }

        log.info("Event received: " + msg_received);
    }
    private String getPostAddress(){
        String address="";
        try {
            address= InetAddress.getLocalHost().getHostAddress()+":"+this.address_port;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address;
    }
}