package com.project.ProxyLogin;

import com.project.ProxyLogin.JMS.SenderJMS;
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

    @PostMapping(value = "/loginSend")
    public ResponseEntity<String> login (@RequestBody String Login_msg) {
        /*  data:
            "id": pepped
            "url": 192.168.1.x:YYYY
        */

        sender.sendMessage(Login_msg);  //Invio su CodaLogin
        return new ResponseEntity<>("[Login Request] - Received from Proxy", HttpStatus.OK);
    }

}
