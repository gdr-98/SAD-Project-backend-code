package com.project.ProxyAccoglienza;


import com.project.ProxyAccoglienza.JMS.ReceiverJMS;
import com.project.ProxyAccoglienza.JMS.SenderJMS;
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

    private final Logger log = LoggerFactory.getLogger(com.project.ProxyAccoglienza.ProxyAccoglienzaController.class);

    @PostMapping(value = "/acceptanceSend")
    public ResponseEntity<String> sendJMS (@RequestBody String msg) {
        log.info("ProxyAccoglienza message received");
        sender.sendMessage(msg);
        return new ResponseEntity<>("Event sent", HttpStatus.OK);
    }
}