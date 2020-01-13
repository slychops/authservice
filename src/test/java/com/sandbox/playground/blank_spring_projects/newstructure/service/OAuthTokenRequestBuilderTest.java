package com.sandbox.playground.blank_spring_projects.newstructure.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OAuthTokenRequestBuilderTest {

    String clientId = "0123";
    String clientSecret = "superultramegatopsecret";
    String authorizationCode = "auth_code";

//    OAuthTokenRequestBuilder<String> request = OAuthTokenRequestBuilder.prepareRequest(clientId, clientSecret);

    @Test
    void checkHeadersOfHttpEntity_whenReturnHttpEntityIsCalled() {

        OAuthTokenRequestMaker<String> requestMaker = new OAuthTokenRequestMaker<>(new EncodingService(), clientId, clientSecret);

        String expectedAuthorization = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        HttpEntity<String> requestEntity = requestMaker.returnHttpEntity(authorizationCode);
        HttpHeaders headers = requestEntity.getHeaders();

        assertNotNull(headers.get("authorization").get(0));
        assertEquals(4, headers.size());
        assertEquals(expectedAuthorization, headers.get("authorization").get(0));
    }

    @Test
    void checkBodyOfHttpEntity_whenReturnHttpEntityIsCalled() {

        OAuthTokenRequestMaker<String> requestMaker = new OAuthTokenRequestMaker<>(new EncodingService(), clientId, clientSecret);

        String expectedBody = "grant_type=authorization_code&code=" + authorizationCode;

        assertEquals(expectedBody, requestMaker.returnHttpEntity(authorizationCode).getBody());
    }

}