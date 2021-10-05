package com.project.Proxy.ProxyRealizzatore.JMS;

import com.project.Proxy.web.LoginResponse;
import com.project.Proxy.web.Webhook;
import org.jetbrains.annotations.NotNull;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@Service
public class LoginReceiverJMS implements MessageListener {


    @JmsListener(destination = "CodaLoginRealizzatori")
    @Override
    public void onMessage(@NotNull Message message) {
        /*
         * Dobbiamo spacchettare il messaggio?
         */
        LoginResponse msg_received = new LoginResponse();
        try {
            msg_received = (LoginResponse) message.getBody(LoginResponse.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
        Webhook.Acceptance.put(msg_received.user, msg_received.url);
    }
}