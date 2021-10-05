package com.project.ProxyCameriere.ProxyCameriere.web;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class Post {

    private final RestTemplate restTemplate;

    public Post () {
        restTemplate = new RestTemplate();
    }

    public void createPost (String uri, String message) {
        // create headers
        HttpHeaders headers = new HttpHeaders();

        // set `content-type` header
        headers.setContentType(MediaType.TEXT_PLAIN);

        // build the request
        HttpEntity<String> entity = new HttpEntity<>(message, headers);

        // send POST request
        this.restTemplate.postForObject(uri, entity, String.class);
    }
}
