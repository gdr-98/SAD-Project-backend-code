
package com.project.ProxyCameriere.ProxyCameriere.ProxyCameriere.JMS;

import com.google.gson.Gson;
import com.project.ProxyCameriere.ProxyCameriere.web.baseMessage;
import com.project.ProxyCameriere.ProxyCameriere.web.loginResponse;
import com.project.ProxyCameriere.ProxyCameriere.web.Post;
import com.project.ProxyCameriere.ProxyCameriere.web.Webhook;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.jms.annotation.JmsListener;

import org.springframework.stereotype.Service;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;


/*
 * Listen queue "CodaCamerieriBroker" and if detect an event
 * notify every client registered to the webhook
 */

@Service
public class ReceiverJMS implements MessageListener {

    private final Post poster = new Post();

    private final Logger log = LoggerFactory.getLogger(ReceiverJMS.class);
    @Value("${server.port}")
    public String address_port;

    @JmsListener(destination = "CodaCamerieriBroker")
    @Override
    public void onMessage(@NotNull Message message) {
        /*
         * Retrieve body of the message sent by ActiveMQ.
         */
        String helper;
        baseMessage msg_received = new baseMessage() ;
        String msg_to_send = "";
        Gson gson=new Gson();
        try {
             helper=  message.getBody(String.class);
             msg_received=gson.fromJson(helper,baseMessage.class);
             log.info("Returned is " +helper);
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
                        poster.createPost("http://" + Webhook.Waiters.get(msg_received.user) + "/request", msg_to_send);
                        log.info(msg_to_send);
                    }
                    break;
            //adds a new user and notify
            case "registerNotification":
                loginResponse resp=gson.fromJson(msg_to_send,loginResponse.class);
                Webhook.Add_Waiter(resp.user,resp.url);
                //Now set the url to post
                resp.proxySource= getProxyAddress()+"/waitersSend";
                msg_to_send=gson.toJson(resp,loginResponse.class);
                if( Webhook.Waiters.containsKey(msg_received.user)) { //if the name exists
                        log.info("Sending to "+"http://" + Webhook.Waiters.get(msg_received.user));
                        poster.createPost("http://" + Webhook.Waiters.get(msg_received.user) + "/login", msg_to_send);
                  }
                break;
            default :
                log.info("Message does not match with any of the expected ones");
                break;
        }
    }
    private String getProxyAddress(){
        String address="";
        try {
            address= InetAddress.getLocalHost().getHostAddress()+":"+this.address_port;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address;
    }

}