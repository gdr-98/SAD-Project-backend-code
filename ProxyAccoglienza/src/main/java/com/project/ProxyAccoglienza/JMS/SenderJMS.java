package com.project.ProxyAccoglienza.JMS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SenderJMS {

        @Autowired
        private JmsTemplate JmsTemp;

        @Value("CodaAccoglienza")
        private String JmsQueue;

        /*
         * Da testare come il sender invia oggetti JSON
         */
        public void sendMessage (String order) {
            JmsTemp.convertAndSend(JmsQueue, order);
        }
}

