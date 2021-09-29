package com.project.ProxyCameriere;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/*
 * 1) POST mapping -> Iscrizione dell'applicazione al webhook del proxy
 * 2) POST mapping -> Creazione di un ordine
 */

@Controller
public class ProxyCameriereController {

    @Autowired
    private ReceiverJMS receiver;

    @Autowired
    private SenderJMS sender;

    private final Logger log = LoggerFactory.getLogger(ProxyCameriereController.class);

    @PostMapping (value = "/register/{url}")
    public ResponseEntity<String> registerUrl (@PathVariable String url) {
        log.info("URL registered: " + url);
        receiver.webhook.addUrl(url);
        return new ResponseEntity<>("URL registered", HttpStatus.OK);
    }

    @PostMapping (value = "/send/{order}")
    public ResponseEntity<String> sendJMS (@PathVariable OrderJSON order) {
        log.info("Order sent");
        sender.sendMessage(order);
        return new ResponseEntity<>("Order created", HttpStatus.OK);
    }
}
