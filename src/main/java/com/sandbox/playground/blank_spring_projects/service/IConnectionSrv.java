package com.sandbox.playground.blank_spring_projects.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URI;

public interface IConnectionSrv {
    <T> ResponseEntity<T> doGet(URI uri, HttpMethod requestType, HttpEntity<String> request, Class<T> responseType);

    <T> ResponseEntity<T> doPost(URI uri, HttpMethod requestType, HttpEntity<String> request, Class<T> responseType);
}
