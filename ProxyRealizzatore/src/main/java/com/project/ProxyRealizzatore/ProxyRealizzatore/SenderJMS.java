package com.project.ProxyRealizzatore.ProxyRealizzatore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SenderJMS {
    @Autowired
    private JmsTemplate JmsTemp;

    @Value("CodaRealizzatori")
    private String JmsQueue;

    public void sendMessage (String order) {
        JmsTemp.convertAndSend(JmsQueue, order);
    }
}