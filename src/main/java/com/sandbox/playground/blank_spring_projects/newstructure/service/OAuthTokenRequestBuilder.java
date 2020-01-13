package com.sandbox.playground.blank_spring_projects.newstructure.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;

public final class OAuthTokenRequestBuilder<T> extends HttpEntity<String> {

    private static EncodingService encodingService = new EncodingService();
    private T body;

    private OAuthTokenRequestBuilder(EncodingService encodingService, String id, String secret) {

        super("happy hippo", createHeaders(id, secret));
    }

    public static OAuthTokenRequestBuilder prepareRequest(String clientId, String clientSecret) {
         OAuthTokenRequestBuilder oAuthTokenRequestBuilder = new OAuthTokenRequestBuilder(new EncodingService(), clientId, clientSecret);
         HttpHeaders entity = oAuthTokenRequestBuilder.getHeaders();
        return oAuthTokenRequestBuilder;

    }

    private static MultiValueMap<String, String> createHeaders(String id, String secret) {
        MultiValueMap<String, String> headersPrep = new LinkedMultiValueMap<>();
        headersPrep.add("accept", "application/json");
        headersPrep.add("content-type", "application/x-www-form-urlencoded");
        headersPrep.add("accept-encoding", StandardCharsets.UTF_8.name());
        headersPrep.add("authorization", encodingService.returnAsBase64(":", id, secret));
        return headersPrep;
    }

//    @Override
//    public HttpHeaders getHeaders() {
//        return super.getHeaders();
//    }
}
