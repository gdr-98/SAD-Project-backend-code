package com.project.ProxyRealizzatore.ProxyRealizzatore;

import com.project.ProxyRealizzatore.ProxyRealizzatore.ProxyChef.LoginReceiverJMS;
import com.project.ProxyRealizzatore.ProxyRealizzatore.ProxyChef.ReceiverJMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ProxyRealizzatoreController {

    @Autowired
    ReceiverJMS receiverchef;
    @Autowired
    com.project.ProxyRealizzatore.ProxyRealizzatore.ProxyPizzaiolo.ReceiverJMS receiverpizzaiolo;

    @Autowired
    LoginReceiverJMS lreceiverchef;
    @Autowired
    com.project.ProxyRealizzatore.ProxyRealizzatore.ProxyPizzaiolo.LoginReceiverJMS lreceiverpizzaiolo;

    @Autowired
    SenderJMS sender;

    @PostMapping(value = "/notifyorder")
    public ResponseEntity<String> login(@RequestBody String data) {
        sender.sendMessage(data);
        return new ResponseEntity<>("Notifica inviata con successo", HttpStatus.OK);
    }
}
