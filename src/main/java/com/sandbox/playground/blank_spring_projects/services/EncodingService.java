package com.sandbox.playground.blank_spring_projects.services;

import java.util.Base64;

public class EncodingService {
    public static String prepareBase64Encoding(String clientId, String clientSecret) {
        String baseValue = Base64.getEncoder().encodeToString((clientId+":"+clientSecret).getBytes());
        return "Basic " + baseValue;
    }

}
