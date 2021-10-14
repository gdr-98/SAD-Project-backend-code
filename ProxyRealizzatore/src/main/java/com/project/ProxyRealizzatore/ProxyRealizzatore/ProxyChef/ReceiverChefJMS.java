package com.project.ProxyRealizzatore.ProxyRealizzatore.ProxyChef;

import com.google.gson.Gson;
import com.project.ProxyRealizzatore.ProxyRealizzatore.web.BaseMessage;
import com.project.ProxyRealizzatore.ProxyRealizzatore.web.LoginResponse;
import com.project.ProxyRealizzatore.ProxyRealizzatore.web.Post;
import com.project.ProxyRealizzatore.ProxyRealizzatore.web.Webhook;
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

@Service
public class ReceiverChefJMS implements MessageListener {

    private final Post poster = new Post();

    private final Logger log = LoggerFactory.getLogger(ReceiverChefJMS.class);

    @Value("${server.port}")
    public String address_port;

    @JmsListener(destination = "CodaChefBroker")
    @Override
    public void onMessage (Message message) {
        /*
         * Retrieve body of the message sent by ActiveMQ.
         */
        String msg_to_send = "";
        BaseMessage msg_received = new BaseMessage();
        Gson gson=new Gson();
        try {
            String helper = (String) message.getBody(Object.class);
            msg_received=gson.fromJson(helper,BaseMessage.class);
            log.info("Returned is" +helper);
            msg_to_send = (String) message.getBody(Object.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }

        /*
        itemCompleteRequest,
        itemWorkingRequest,
        orderNotification,
        registerNotification
         */

        switch (msg_received.messageName) {

            case "itemCompleteRequest": case "itemWorkingRequest": case "orderRequest":
                if( Webhook.Chef.containsKey(msg_received.user)) //if the name exists
                    poster.createPost("http://"+ Webhook.Chef.get(msg_received.user)+"/request",msg_to_send);
                break;
            case "orderNotification":
            for (Map.Entry<String, String> me : Webhook.Pizza_maker.entrySet())
                poster.createPost("http://"+ me.getValue()+"/notification",msg_to_send);
            break;
            //adds a new user and notify
            case "registerNotification":
                LoginResponse resp=gson.fromJson(msg_to_send,LoginResponse.class);
                Webhook.Add_Chef(resp.user,resp.url);
                log.info("Added user "+resp.user+"with url"+resp.url);
                //NNow set the url to post
                resp.proxySource= getProxyAddress()+"/makerSend";
                msg_to_send=gson.toJson(resp,LoginResponse.class);
                if( Webhook.Chef.containsKey(msg_received.user)) //if the name exists
                    log.info("Sending to "+"http://" + Webhook.Chef.get(msg_received.user));
                    poster.createPost("http://"+ Webhook.Chef.get(msg_received.user)+"/login",msg_to_send);
                break;
            default:
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