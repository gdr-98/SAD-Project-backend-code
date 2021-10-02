package com.project.Proxy.ProxyRealizzatore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ProxyRealizzatoreController {

    @Autowired
    SenderJMS sender;

    @PostMapping(value = "/notifyorder")
    public ResponseEntity<String> login(@RequestBody String data) {
        sender.sendMessage(data);
        return new ResponseEntity<>("Notifica inviata con successo", HttpStatus.OK);
    }
}
