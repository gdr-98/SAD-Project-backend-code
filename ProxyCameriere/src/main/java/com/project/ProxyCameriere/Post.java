package com.project.ProxyCameriere;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class Post {

    private final RestTemplate restTemplate;

    public Post () {
        restTemplate = new RestTemplate();
    }

    public ResponseEntity<Post> createPost (String uri, String message) {
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("body", message);

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request

        return this.restTemplate.postForEntity(uri, entity, Post.class);
    }
}
