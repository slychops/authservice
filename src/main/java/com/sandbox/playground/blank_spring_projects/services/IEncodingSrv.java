package com.sandbox.playground.blank_spring_projects.services;

import java.security.NoSuchAlgorithmException;

public interface IEncodingSrv {
    String returnAsBase64(String var1);
    String encodeWithSHA512(String var) throws NoSuchAlgorithmException;
    String createDigest(String algorithm, String body) throws NoSuchAlgorithmException;
    String signString(String signingString);
}
