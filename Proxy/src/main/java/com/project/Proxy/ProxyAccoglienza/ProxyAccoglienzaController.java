package com.project.Proxy.ProxyAccoglienza;

import com.project.Proxy.ProxyCameriere.JMS.LoginReceiverJMS;
import com.project.Proxy.ProxyCameriere.JMS.ReceiverJMS;
import com.project.Proxy.ProxyCameriere.JMS.SenderJMS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ProxyAccoglienzaController {

/*
 * 1) POST mapping /register -> Iscrizione dell'applicazione al webhook del proxy
 * 2) POST mapping /send -> Creazione di un ordine
 */
    @Autowired
    private ReceiverJMS receiver;

    @Autowired
    private SenderJMS sender;

    @Autowired
    private LoginReceiverJMS lreceiver;

    private final Logger log = LoggerFactory.getLogger(com.project.Proxy.ProxyAccoglienza.ProxyAccoglienzaController.class);

    @PostMapping(value = "")
    public ResponseEntity<String> sendJMS (@RequestBody String order) {
        log.info("ProxyAccoglienza message sent");
        sender.sendMessage(order);
        return new ResponseEntity<>("***", HttpStatus.OK);
    }
}
