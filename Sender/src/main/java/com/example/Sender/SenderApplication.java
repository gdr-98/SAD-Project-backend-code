package com.example.Sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class SenderApplication {

	@Autowired
	private Sender senderMessage;

	private final Logger log = LoggerFactory.getLogger(SenderApplication.class);

	/*
	@Autowired
	Sender senderMessage;
	*/

	@PostMapping(value = "/notification")
	public ResponseEntity<String> send(@RequestBody String message) {
		log.info("Message received: " + message);
		// senderMessage.sendMessage(message);
		return new ResponseEntity<>("Message received.", HttpStatus.OK);
	}

}


