package com.sandbox.playground.blank_spring_projects.services;

import com.sandbox.playground.blank_spring_projects.exceptions.UnsupportedContentTypeException;
import com.sandbox.playground.blank_spring_projects.utils.ContentType;
import com.sandbox.playground.blank_spring_projects.utils.Headers;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.UUID;

class ConnectionService {

    protected ConnectionService(){}

    protected static MultiValueMap<String, String> prepareHeaders(String authorization,
                                          String date,
                                          String digest,
                                          String signature,
                                          String tppSignatureCertificate,
                                          String xIbmClientId,
                                          UUID xRequestId) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(Headers.ACCEPT, ContentType.APPLICATION_JSON);
        headers.add(Headers.AUTHORIZATION, authorization);
        headers.add(Headers.DATE, date);
        headers.add(Headers.DIGEST, digest);
        headers.add(Headers.SIGNATURE, signature);
        headers.add(Headers.TPP, tppSignatureCertificate);
        headers.add(Headers.X_CLIENT, xIbmClientId);
        headers.add(Headers.X_REQUEST, xRequestId.toString());
        return headers;
    }

    protected MultiValueMap<String, String> prepareHeaders(
            String contentTypeFormUrlencoded,
            String clientId,
            String clientSecret) throws UnsupportedContentTypeException {
        if (!"application/x-www-form-urlencoded".equals(contentTypeFormUrlencoded)) {
            throw new UnsupportedContentTypeException("Unsupported content-type provided. Currently only supporting" +
                    "x-www-form-urlencoded");
        }
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

    //ToDo: Make it work
//    protected T ResponseEntity<T> getRequest(HttpEntity request, Class T) {
//
//        return new RestTemplate().exchange("www.google.com", HttpMethod.GET, request, T.class);
//    }

}
