package com.project.ProxyRealizzatore.ProxyRealizzatore;

import com.project.ProxyRealizzatore.ProxyRealizzatore.ProxyChef.ReceiverChefJMS;
import com.project.ProxyRealizzatore.ProxyRealizzatore.ProxyPizzaiolo.ReceiverPizzaioloJMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ProxyRealizzatoreController {

    @Autowired
    ReceiverChefJMS receiverchef;
    @Autowired
    ReceiverPizzaioloJMS receiverpizzaiolo;
    @Autowired
    SenderJMS sender;

    @PostMapping(value = "/makerSend")
    public ResponseEntity<String> login(@RequestBody String data) {
        sender.sendMessage(data);
        return new ResponseEntity<>("Event Sent", HttpStatus.OK);
    }
}
