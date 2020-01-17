package com.sandbox.playground.blank_spring_projects.newstructure.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;

// Given the clientId and clientSecret and authorizationCode create the body and headers to be passed into a HttpEntity
//ToDo: Once we have a need for another OAuthTokenRequestMaker we can think of declaring different beans in config with different clientId's and secrets

@Service
class OAuthTokenRequestMaker  {

    private final EncodingService encodingService;
    private final HttpHeaders headers;

    OAuthTokenRequestMaker(
            EncodingService encodingService,
            @NonNull @Value("${security.general.client_id}") String clientId,
            @NonNull @Value("${security.oauth.client_secret}") String clientSecret) {

        this.encodingService = encodingService;
        this.headers = new HttpHeaders(createHeaders(clientId, clientSecret));
    }

    public HttpEntity<String> returnHttpEntity(String authorizationCode) {

        return new HttpEntity<>(prepareBody(authorizationCode), this.headers);
    }

    private String prepareBody(String authorizationCode){

        return "grant_type=authorization_code&code=" + authorizationCode;
    }

    private MultiValueMap<String, String> createHeaders(String id, String secret) {
        MultiValueMap<String, String> headersPrep = new LinkedMultiValueMap<>();
        headersPrep.add("accept", "application/json");
        headersPrep.add("content-type", "application/x-www-form-urlencoded");
        headersPrep.add("accept-encoding", StandardCharsets.UTF_8.name());
        headersPrep.add("authorization", encodingService.returnAsBase64(":", id, secret));

        return headersPrep;
    }

}
