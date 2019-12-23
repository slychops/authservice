package com.sandbox.playground.blank_spring_projects.services;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

public class ConnectionService {

    protected MultiValueMap<String, String> prepareHeaders(
            String contentTypeFormUrlencoded,
            String clientId,
            String clientSecret) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(Headers.ACCEPT, ContentType.APPLICATION_JSON);
        headers.add(Headers.CONTENT_TYPE, contentTypeFormUrlencoded);
        headers.add(Headers.ACCEPT_ENCODING, StandardCharsets.UTF_8.name());
        headers.add(Headers.AUTHORIZATION, EncodingService.prepareBase64Encoding(clientId, clientSecret));
        return headers;
    }

    protected String prepareBody(String bodyTypeUrlencoded, String authorizationCode) {
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        parameters.put(Headers.GRANT_TYPE, ContentType.AUTHORIZATION_CODE);
        parameters.put(Headers.CODE, authorizationCode);

        StringBuilder sb = new StringBuilder();
        parameters.forEach((key, value) -> sb.append(key).append("=").append(value).append("&"));
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
