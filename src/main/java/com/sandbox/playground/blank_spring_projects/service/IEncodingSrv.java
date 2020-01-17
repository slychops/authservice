package com.sandbox.playground.blank_spring_projects.service;

import java.security.NoSuchAlgorithmException;

public interface IEncodingSrv {

    String returnAsBase64(String val);
    String returnAsBase64(String separator, String... vars);
    String encodeWithSHA512(String var) throws NoSuchAlgorithmException;
    String createDigest(String algorithm, String body) throws NoSuchAlgorithmException;
    String signString(String signingString);

}
