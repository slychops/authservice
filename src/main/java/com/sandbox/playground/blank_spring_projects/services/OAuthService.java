package com.sandbox.playground.blank_spring_projects.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class OAuthService extends ConnectionService {
    private static final String CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String BODY_TYPE_URLENCODED = "x-www-form-urlencoded";
    public final String oAuthEndpoint;
    private final String tokenEndpoint;
    private final String clientId;
    private final String clientSecret;
    private String authorizationCode;

    public OAuthService(
            @Value("${security.oauth.authorize_endpoint}") String oAuthEndpoint,
            @Value("${security.oauth.token_endpoint}") String tokenEndpoint,
            @Value("${security.oauth.client_id}") String clientId,
            @Value("${security.oauth.client_secret}") String clientSecret) {
        this.oAuthEndpoint = oAuthEndpoint;
        this.tokenEndpoint = tokenEndpoint;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getToken() {
        String token = null;
        MultiValueMap<String, String> headers = prepareHeaders(CONTENT_TYPE_FORM_URLENCODED,
                clientId, clientSecret);
        String body = prepareBody(BODY_TYPE_URLENCODED, authorizationCode);
        requestAccessToken(headers, body);
        return token;
    }

    private void requestAccessToken(MultiValueMap<String, String> headers, String body) {
//        RestTemplate client = new RestTemplate();
//        HttpEntity<Object> httpRequest = new HttpEntity<>(body, headers);
//        ResponseEntity<String> httpResponse = client.exchange(tokenEndpoint, HttpMethod.POST, httpRequest, String.class);
//        System.out.println(httpResponse.getBody());


        HttpResponse<String> response = null;
        try {
            response = Unirest.post("https://api-sandbox.rabobank.nl/openapi/sandbox/oauth2/token")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", EncodingService.prepareBase64Encoding(clientId, clientSecret))
                    .header("User-Agent", "PostmanRuntime/7.20.1")
                    .header("Accept", "*/*")
                    .header("Cache-Control", "no-cache")
                    .header("Postman-Token", "96c7034c-7fc9-441b-a6a8-887808df3e8c,6a4285d7-a324-4a89-862f-ff7f5bd1f24b")
                    .header("Host", "api-sandbox.rabobank.nl")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Content-Length", "166")
                    .header("Cookie", "BIGipServerpl_api-sandbox-int.rabobank.nl-80=!Z1i+LnAge/KpDb0SGsunPHNuWU8wOmtT3NAS6FNiEGEPc6Y+Vn7AdG6iw66fIm0YJEu/qn0xqAf5mg==; RABOBANK_SESSIE=AHNQo9BacEiuNMiM/7uWdImG1XMEdCQ9UaHjwnBBlkT1Qd+bVw45cr2QBHSOtDByK7ZpS4BNWijZOxs7H8cK4g==; BIGipServerpl_api-sandbox-pif-int.rabobank.nl-80=!Je6d1LvwUcwhqu4SGsunPHNuWU8wOhJv7roME2b8ryovydewYOyx1LMn3tSy2s6yow3Y1X33xsbg9w==; RaboTS=yBkA7GwSnAMDdv17aaHEVQ==")
                    .header("Connection", "keep-alive")
                    .header("cache-control", "no-cache")
                    .body(body)
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
//        ResponseEntity<Token> httpResponse = client.exchange(tokenEndpoint, HttpMethod.POST, httpRequest, Token.class);

        System.out.println("Stop");
    }

    public void setCode(String code) {
        this.authorizationCode = code;
    }
}