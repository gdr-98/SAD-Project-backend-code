package com.project.Proxy.ProxyLogin;

import com.project.Proxy.ProxyLogin.JMS.SenderJMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ProxyLoginController {

    @Autowired
    SenderJMS sender;

    @PostMapping(value = "/login")
    public ResponseEntity<String> login (@RequestBody String data) {
        /*  data:
            "id": pepped
            "url": 192.168.1.x:YYYY
        */
        sender.sendMessage(data);
        return new ResponseEntity<>("Login successful", HttpStatus.OK);
    }

}
