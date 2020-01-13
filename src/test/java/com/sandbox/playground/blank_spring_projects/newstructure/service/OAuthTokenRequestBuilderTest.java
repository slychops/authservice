package com.sandbox.playground.blank_spring_projects.newstructure.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.Base64;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class OAuthTokenRequestBuilderTest {

    String clientId = "0123";
    String clientSecret = "superultramegatopsecret";

    OAuthTokenRequestBuilder<String> request = OAuthTokenRequestBuilder.prepareRequest(clientId, clientSecret);

    @Test
    void checkRequestHeaders_AreNotNull(){
        assertNotNull(request.getHeaders());
    }

    @Test
    void checkContent_ofPrepareHeaders_containsClientIdAndSecret(){
        String expectedAuthorization = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        HttpHeaders headers = request.getHeaders();

        assertEquals(4, headers.size());
        assertTrue(headers.containsKey("authorization"));
        assertEquals(expectedAuthorization,
                Objects.requireNonNull(headers.get("authorization")).get(0));
    }

    @Test
    void checkSomething() {
        String expectedAuthorization = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        HttpEntity<String> request = OAuthTokenRequestBuilder.prepareRequest(clientId, clientSecret);
        assertEquals(expectedAuthorization, Objects.requireNonNull(request.getHeaders().get("authorization")).get(0));
    }

}