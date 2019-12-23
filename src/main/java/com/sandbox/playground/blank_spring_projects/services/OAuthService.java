package com.sandbox.playground.blank_spring_projects.services;

import com.sandbox.playground.blank_spring_projects.model.ErrorToken;
import com.sandbox.playground.blank_spring_projects.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class OAuthService extends ConnectionService {
    private static final String CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String BODY_TYPE_URLENCODED = "x-www-form-urlencoded";
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthService.class);
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

    public Token getToken() {
        String token = null;
        MultiValueMap<String, String> headers = prepareHeaders(CONTENT_TYPE_FORM_URLENCODED,
                clientId, clientSecret);
        String body = prepareBody(BODY_TYPE_URLENCODED, authorizationCode);
        return requestAccessToken(headers, body);
    }

    private Token requestAccessToken(MultiValueMap<String, String> headers, String body) {
        RestTemplate client = new RestTemplate();
        HttpEntity<String> httpRequest = new HttpEntity<>(body, headers);
        ResponseEntity<Token> httpResponse = client.exchange(tokenEndpoint, HttpMethod.POST, httpRequest, Token.class);
        if (Objects.requireNonNull(httpResponse.getBody()).getToken_type().equals("Bearer")) {
            LOGGER.info("Received token");
            return httpResponse.getBody();
        } else {
            LOGGER.error("NO TOKEN RECEIVED : {}", httpResponse.getStatusCode());
        }
        return new ErrorToken();
    }

    public void setCode(String code) {
        this.authorizationCode = code;
    }

    public String getoAuthEndpoint() {
        return this.oAuthEndpoint;
    }
}