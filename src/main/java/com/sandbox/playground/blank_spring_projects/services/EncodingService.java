package com.sandbox.playground.blank_spring_projects.services;

import com.sandbox.playground.blank_spring_projects.exceptions.InvalidValuesPassedForBase64EncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

//@Service
class EncodingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncodingService.class);
    private final String privateKeyString;
    private PrivateKey privateKey;

    EncodingService(@Value("${security.psd2.private_key}") String pvtKey) {
        this.privateKeyString = pvtKey;
        this.privateKey = convertToPrivateKey();
    }

    static String prepareBase64Encoding(String clientId, String clientSecret)  {
        if(clientId == null || clientSecret == null){
            throw new InvalidValuesPassedForBase64EncodingException("null values passed");

        }
        String baseValue = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        return "Basic " + baseValue;
    }

    static String createDigest(String algorithm, String body) throws NoSuchAlgorithmException {
        if ("SHA-512".equalsIgnoreCase(algorithm)) {
            return algorithm.toLowerCase() + "=" + createSHA512(body);
        } else {
            throw new NoSuchAlgorithmException("Unknown hashing algorithm requested: " + algorithm);
        }
    }

    private static String createSHA512(String body) throws NoSuchAlgorithmException{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(body.getBytes());
            byte[] messageDigest = md.digest();
            return Base64.getEncoder().encodeToString(messageDigest);
    }

    String generateSignature(String signingString) {
        Signature sig;
        byte[] signingStringByte = signingString.getBytes();
        String signedString = null;
        try {
            sig = Signature.getInstance("SHA512withRSA");
            sig.initSign(this.privateKey);
            sig.update(signingStringByte);
            byte[] signature = sig.sign();
            signedString = Base64.getEncoder().encodeToString(signature);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            LOGGER.info("No such signature algorithm found, {}", e.getMessage());
        }
        return signedString;
    }

    private PrivateKey convertToPrivateKey() {
        byte[] pvtKey = privateKeyString.getBytes();
        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(pvtKey));
        KeyFactory fact;

        try {
            fact = KeyFactory.getInstance("RSA");
            return fact.generatePrivate(keySpecPKCS8);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.info("No such instance was able to be created, {}", e.getLocalizedMessage());
        }
        return null;
    }


}