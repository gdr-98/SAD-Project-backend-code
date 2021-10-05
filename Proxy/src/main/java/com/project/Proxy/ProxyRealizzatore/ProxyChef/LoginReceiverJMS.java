package com.project.Proxy.ProxyRealizzatore.ProxyChef;

import com.project.Proxy.web.LoginResponse;
import com.project.Proxy.web.Post;
import com.project.Proxy.web.Webhook;
import org.jetbrains.annotations.NotNull;
import org.springframework.jms.annotation.JmsListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class LoginReceiverJMS implements MessageListener {

    private final Post poster = new Post();

    @JmsListener(destination = "CodaLoginChef")
    @Override
    public void onMessage(@NotNull Message message) {

        LoginResponse msg_received = new LoginResponse();
        try {
            msg_received = (LoginResponse) message.getBody(LoginResponse.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }

        Webhook.Chef.put(msg_received.user, msg_received.url);

        poster.createPost("http://"+ msg_received.url+"/login","Login successful");
    }
}
