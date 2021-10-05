package com.project.ProxyCameriere.ProxyCameriere.ProxyCameriere.JMS;

import com.google.gson.Gson;
import com.project.ProxyCameriere.ProxyCameriere.web.BaseMessage;
import com.project.ProxyCameriere.ProxyCameriere.web.LoginResponse;
import com.project.ProxyCameriere.ProxyCameriere.web.Post;
import com.project.ProxyCameriere.ProxyCameriere.web.Webhook;
import org.jetbrains.annotations.NotNull;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@Service
public class LoginReceiverJMS implements MessageListener {

    private final Post poster = new Post();

    @JmsListener(destination = "CodaLoginCamerieri")
    @Override
    public void onMessage(@NotNull Message message) {

        LoginResponse msg_received = new LoginResponse();
        Gson gson = new Gson();
        try {
            String helper = (String) message.getBody(Object.class);
            msg_received=gson.fromJson(helper, LoginResponse.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }

        Webhook.Waiters.put(msg_received.user, msg_received.url);

        poster.createPost("http://"+ msg_received.url+"/login","Login successful");
    }
}
