package com.sandbox.playground.blank_spring_projects;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
@Slf4j
public class Config {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) throws Exception {
        char[] password = "Test1234".toCharArray();

        SSLContext context = SSLContextBuilder.create()
                .loadKeyMaterial(keystore("classpath:./keys/my-keystore.jks", password), password)
                .loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();

        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(ResourceUtils.getFile("classpath:./keys/my-keystore.jks"),
                        password, password)
                .build();

        HttpClient client = HttpClients.custom()
                .setSSLContext(sslContext)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(client);
        return new RestTemplate(requestFactory);
    }

    private KeyStore keystore(String file, char[] password) throws FileNotFoundException {
        String keyStoreInstance = "PKCS12";
        KeyStore keyStore = null;
        File key = ResourceUtils.getFile(file);

        try (InputStream in = new FileInputStream(key)) {
            keyStore = KeyStore.getInstance(keyStoreInstance);
            keyStore.load(in, password);
        } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
            log.info("An error occurred while trying to load in from the keystore, {}", e.getLocalizedMessage());
        } catch (KeyStoreException e) {
            log.info("No keystore instance for {}: {}", keyStoreInstance, e.getStackTrace());
        }
        if (keyStore == null) throw new NullPointerException();
        return keyStore;
    }
}
