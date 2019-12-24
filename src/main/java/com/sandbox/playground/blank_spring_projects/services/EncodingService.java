package com.sandbox.playground.blank_spring_projects.services;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

class EncodingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncodingService.class);

    private EncodingService() {}

    static String prepareBase64Encoding(String clientId, String clientSecret) {
        String baseValue = Base64.getEncoder().encodeToString((clientId+":"+clientSecret).getBytes());
        return "Basic " + baseValue;
    }

    static String createDigest(String algorithm, String body) {
        if ("SHA-512".equals(algorithm)) {
            return algorithm.toLowerCase() + "=" + createSHA512(body);
        } else {
            LOGGER.warn("Unknown hashing algorithm requested");
        }
        return null;
    }

    private static String createSHA512(String body) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest();
            return Base64.getEncoder().encodeToString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}