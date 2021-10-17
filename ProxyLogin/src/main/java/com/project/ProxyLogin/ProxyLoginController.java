package com.project.ProxyLogin;

import com.project.ProxyLogin.JMS.SenderJMS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(com.project.ProxyLogin.ProxyLoginController.class);

    @PostMapping(value = "/loginSend")
    public ResponseEntity<String> login (@RequestBody String Login_msg) {
        /*  data:
            "id": pepped
            "url": 192.168.1.x:YYYY
        */
        log.info("Login Request received :"+Login_msg);
        sender.sendMessage(Login_msg);  //Invio su CodaLogin
        return new ResponseEntity<>("[Login Request] - Received from Proxy", HttpStatus.OK);
    }

}
