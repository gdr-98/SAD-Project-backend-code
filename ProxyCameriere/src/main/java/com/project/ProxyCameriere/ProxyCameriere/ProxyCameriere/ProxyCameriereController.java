package com.project.ProxyCameriere.ProxyCameriere.ProxyCameriere;


import com.google.gson.Gson;
import com.project.ProxyCameriere.ProxyCameriere.ProxyCameriere.JMS.ReceiverJMS;
import com.project.ProxyCameriere.ProxyCameriere.ProxyCameriere.JMS.SenderJMS;
import com.project.ProxyCameriere.ProxyCameriere.ProxyCameriere.JMS.LoginReceiverJMS;
import com.project.ProxyCameriere.ProxyCameriere.web.BaseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*
 * 1) POST mapping /register -> Iscrizione dell'applicazione al webhook del proxy
 * 2) POST mapping /send -> Creazione di un ordine
 */

@Controller
public class ProxyCameriereController {

    @Autowired
    private ReceiverJMS receiver;

    @Autowired
    private SenderJMS sender;

    @Autowired
    private LoginReceiverJMS lreceiver;

    private static final String name="waitersProxy";
    private final Logger log = LoggerFactory.getLogger(ProxyCameriereController.class);

    /*
     * Example needed format:
     * http://localhost:8080/register/localhost:8081
     */

    /*
    @PostMapping (value = "/register/{url}")
    public ResponseEntity<String> registerUrl (@PathVariable String url) {
        log.info("URL registered: " + url);
        receiver.webhook.addUrl(url);
        return new ResponseEntity<>("URL registered", HttpStatus.OK);
    }
    */

    /*
     * Example format:
     * http://localhost:8081/sendorder
     *
     * Post request must contain a not null body.
     */
    @PostMapping (value = "/send")
    public ResponseEntity<String> sendJMS (@RequestBody String event) {
        sender.sendMessage(event);
        log.info(event);
        return new ResponseEntity<>("Event sent", HttpStatus.OK);
    }
}
