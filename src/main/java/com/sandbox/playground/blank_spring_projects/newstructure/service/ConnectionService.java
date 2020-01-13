package com.sandbox.playground.blank_spring_projects.newstructure.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.Null;
import java.net.URI;

@Service
class ConnectionService implements IConnectionSrv {

    private RestTemplate client;

    @Autowired
    public ConnectionService(RestTemplate client) {
        this.client = client;
    }

    @Override
    public <T> ResponseEntity<T> doGet(URI uri, HttpMethod requestType, HttpEntity<String> request, Class<T> responseType) {
        return client.exchange(uri.toString(), HttpMethod.GET, request, responseType);
    }

    @Override
    public <T> ResponseEntity<T> doPost(URI uri, HttpMethod requestType, HttpEntity<String> request, Class<T> responseType) {
        if(request == null){
            throw new NullPointerException();
        }
        return client.exchange(uri.toString(), HttpMethod.POST, request, responseType);
    }

}