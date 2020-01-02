package com.sandbox.playground.blank_spring_projects.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

class EncodingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncodingService.class);

    private static String aValue;
    static {aValue = System.getenv("privateKey");}

    private static String privateKeyString;
    private static  PrivateKey PRIVATE_KEY = getPrivateKey();

    private EncodingService() {
    }


    static String prepareBase64Encoding(String clientId, String clientSecret) {
        String baseValue = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
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
            LOGGER.info(e.getLocalizedMessage());
        }
        return null;
    }

    public static String generateSignature(String signingString) {

        try {
            Signature sig = Signature.getInstance("SHA512withRSA");
            sig.initSign(PRIVATE_KEY);

            byte[] signature = sig.sign();
            String signatureString = Arrays.toString(signature);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            LOGGER.info("No such signature algorithm found, {}", e.getMessage());
        }
        return null;

    }

    private static PrivateKey getPrivateKey() {
        String privateKeyString = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDaAI6hllIAiJthftLAUhMC2pfk97vrpXS4pKcRwgfIi9Rg+xa0Y3APjFbMYbx5cu70bByvrUMjEmniu/NkR6WN96sTy3PC5qEC29hLokMV4/TzmJGAUO+KpK2Z7ylZUd1qMkFngzl50mNsGzRX5D/BE+BJcMriCBK4sGIWm9DiK556zvF/Nv0j2cR8BHd2kI0OroYsPKWg7KSW0uVoxH7sGNeilVVGJYW7QMBo73OmitDQZvix/XzC6QBOoHxmTctIMzA6K1TW5ECtSKABg/KfWR3QdBdxcJCCxI1IkFupyvv3ygzSdEwW6YGVKWPG0eOtKs8SnXmdcjw6l9go4en5AgMBAAECggEAB8nsTqalwGIhFw8mbXuhNUFlGuek/arYLD6pv28swwQH7v0ZlxFUcCHF+iBl0PsDwZTZQ4ePtgGS6ehoLkWHCzb1lEv5E1YVG5qKNE2UUwRlfIyPakO6AzyV/UF3uzq7C+/GuXGNTKZxKewg5yD/DCFvKoCOpxu9u36FyqP/hw0SADVlmp35/zoPDPZzu1j4FiCo0pJ9LwJcHxeJHopNAKDw9k6I4z/grskdgupsGzK2BiGiQ/+wmmO68/6Xa6KWfpr1PQ6ODJHgzZsdGCVi6Ebaqlj6BbsYWxP6h3lrsGt+LmHBaN2jCD6cDp+lihqFgnm8hfdv0lmbPilp71EDfwKBgQD6U8PBzZtN8yXm5WuSNL+/8q5GjNmeTJBSo1gM6Y8vOT4QAE147LbVuVBDwyHPoSrNejePae6Q14PswjByT7B8DZ0OeQyGa3trrFg/ib7Vv4ZMvJqX9+WzBrzZsxTg7oCKHzmCR4vIRItKHH3zWnnhqlo8ic2AZ2O43cdJosbO1wKBgQDe8UODOLu0vnHohOKeUqF3w/ZOB2+83/jsYyUbSkzsGvHIwTjObuMUFTQvdMZ6IkIyJdfnDZIbvlBSD8tzL5iKFTNCK2nL1i4GiFr0CYLaHAlhJ5GEbTrTMDoJeBPerZq83HPrSa/Wb0xO18QTWsoVQPFfPFbbcQyI9ryJ2iIDrwKBgQC6kuAefG46ZPVk6K2KZUJdgDUgZC52a75NuW0RAqszmUiGiJM1g8ip9tq6BqAWrprGV0c93shusBKlzf5p1LdHXqYmeVY6gbWVhPipMrNHgN5KJ3BZv+w1yNnMsErpcxne2HL2hPjMJTpj3GSLkm2xIlTrNhIyl9ydlr7IRUhENQKBgQCvi6HxbXa/90WSJTCcIcxqla8X+dsOCf3jhJ3vQy4Wq5C+1wZ35fCAG8Ifq/+so9Ujz5CVqqXlmpF8TFuSs2OVNuRJsg14J4nOMwgLKIIUZAcurQ10DN5I9Kx+UEK1EFXLaHsORdNjMfgQDO2jn9WHrr9gkg6CdB2+qyoCEfS+mQKBgBW08lcy9V5RzRWb/v/jxsc7ovmgAhCJhDeV7dPbx4HbFeoQJlbA8g1thdcFlcatSGyNDbvNE1GPSd4NhkpRY6Hfv53kdEzjVkEtU8lUdL7HNVJqX7bU7oZlfbYcwxWQ1Gg8C1oLIAyEt71slQtdRiNYBRZTQe2F0wxbXnuUqLAw";

        byte[] pvtKey = privateKeyString.getBytes();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(pvtKey);
        KeyFactory fact;
        try {
            fact = KeyFactory.getInstance("RSA");
            return fact.generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.info("No such instance was able to be created, {}", e.getLocalizedMessage());
        }
        return null;
    }


}