package com.sandbox.playground.blank_spring_projects.services;

import com.sandbox.playground.blank_spring_projects.exceptions.UnsupportedContentTypeException;
import com.sandbox.playground.blank_spring_projects.model.ErrorToken;
import com.sandbox.playground.blank_spring_projects.model.Token;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.lang.String;

//@Service
@Slf4j
public class OAuthService extends ConnectionService {
    private static final String CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String BODY_TYPE_URLENCODED = "x-www-form-urlencoded";
    private final String oAuthEndpoint;
    private final String tokenEndpoint;
    private final String clientId;
    private final String clientSecret;
    private final RestTemplate authClient;
    private String authorizationCode;

    public OAuthService(
            @Value("${security.oauth.authorize_endpoint}") String oAuthEndpoint,
            @Value("${security.oauth.token_endpoint}") String tokenEndpoint,
            @Value("${security.general.client_id}") String clientId,
            @Value("${security.oauth.client_secret}") String clientSecret,
            RestTemplate restTemplate) {
        this.oAuthEndpoint = oAuthEndpoint;
        this.tokenEndpoint = tokenEndpoint;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authClient = restTemplate;
    }

    public final Token retrieveToken() {
        MultiValueMap<String, String> headers = null;
        //Todo: Extract to separate prepareHeaders() method -- for readability
        try {
            headers = super.prepareHeaders(CONTENT_TYPE_FORM_URLENCODED,
                    clientId, clientSecret);
        } catch (UnsupportedContentTypeException e) {
            log.info(e.getMessage());
        }
        //Todo: Extract to separate buildBody() method -- for readability
        String body = super.prepareBody(BODY_TYPE_URLENCODED, authorizationCode);
        return requestAccessToken(headers, body);
    }

    private Token requestAccessToken(MultiValueMap<String, String> headers, String body) {
        HttpEntity<String> httpRequest = new HttpEntity<>(body, headers);
        ResponseEntity<Token> httpResponse = authClient.exchange(tokenEndpoint, HttpMethod.POST, httpRequest, Token.class);
        if (Objects.requireNonNull(httpResponse.getBody()).getToken_type().equals("Bearer")) {
            return updateToken(httpResponse.getBody());
        }

        //Todo:Create custom exception --  check if using else is more performant
        log.error("NO TOKEN RECEIVED : {}", httpResponse.getStatusCode());
        return new ErrorToken();
    }

    //Todo: change name as updateToken is misleading (sounds like one is exchanging for a new token)
    private Token updateToken(Token token) {
        log.info("Received token");
        TLSService.authorization = "Bearer " + token.getAccess_token();
        return token;
    }

    public final void setCode(String code) {
        this.authorizationCode = code;
    }

    public String getOAuthEndpoint() {
        return this.oAuthEndpoint;
    }

}