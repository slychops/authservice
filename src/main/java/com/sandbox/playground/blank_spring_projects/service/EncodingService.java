package com.sandbox.playground.blank_spring_projects.service;

import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;

@Service
class EncodingService implements IEncodingSrv {

    @Override
    public String returnAsBase64(String separator, String... vars) {
        String s = Arrays.stream(vars)
                .map(var -> var + separator)
                .collect(Collectors.joining());

        return returnAsBase64(s.substring(0, s.length()-1));
    }

    @Override
    public String returnAsBase64(String val) {
        return Base64.getEncoder().encodeToString(val.getBytes());
    }

    @Override
    public String encodeWithSHA512(String var) throws NoSuchAlgorithmException {
        return null;
    }

    @Override
    public String createDigest(String algorithm, String body) throws NoSuchAlgorithmException {
        return null;
    }

    @Override
    public String signString(String signingString) {
        return null;
    }
}
