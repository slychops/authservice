package com.sandbox.playground.blank_spring_projects.services;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
class TLSService extends ConnectionService {

    private static final String SHA_512 = "SHA-512";
    static String authorization;

    private final String endpoint;
    private final String xIbmClientId;
    private final String tppSignatureCertificate;
    private final UUID xRequestId;
    private final String digest;
    private final EncodingService encodingService;
    private String date;
    private String signature;
    private String signingString;
    private final RestTemplate client;

    TLSService(@Value("${security.general.client_id}") String clientId,
               @Value("${security.psd2.certificate}") String certificate,
               @Value("${accounts.endpoint.accountslist}") String url,
               @Value("") String body,
               RestTemplate restTemplate,
               EncodingService encodingService) {
        this.encodingService = encodingService;
        this.xIbmClientId = clientId;
        this.tppSignatureCertificate = certificate;
        this.endpoint = url;
        this.client = restTemplate;
        this.xRequestId = UUID.randomUUID();
        this.digest = EncodingService.createDigest(SHA_512, body);
    }

    public final HttpEntity<String> prepareAccountsRequest() {
        this.date = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        this.date = getCurrentDateTime();
        this.signingString = setSigningString();
        this.signature = createSignature(encodingService.generateSignature(this.signingString));
//        System.out.println("This is where we will now create the headers.");
        HttpEntity<String> request = new HttpEntity<>(ConnectionService.prepareHeaders(
                authorization,
                date,
                digest,
                signature,
                tppSignatureCertificate,
                xIbmClientId,
                xRequestId));
        ResponseEntity<String> result = client.exchange(endpoint, HttpMethod.GET, request, String.class);

/**----------------HEADERS----------------**/ // ---**--- added if required for signature value
// ...  /*  .addHeader("x-ibm-client-id", "Client ID")                      <-- ACTUAL CLIENT_ID            */
// ...  /*  .addHeader("authorization", "Bearer REPLACE_BEARER_TOKEN")      <-- TOKEN GOES HERE             */
// ...  /*  .addHeader("tpp-signature-certificate", "MIIDkDCCAnigAwIBAgIEWs3AJDANBgkqhkiG9w0BAQsFADCBiTELMAkGA1UEBhMCTkwxEDAOBgNVBAgMB1V0cmVjaHQxEDAOBgNVBAcMB1V0cmVjaHQxETAPBgNVBAoMCFJhYm9iYW5rMRwwGgYDVQQLDBNPbmxpbmUgVHJhbnNhY3Rpb25zMSUwIwYDVQQDDBxQU0QyIEFQSSBQSSBTZXJ2aWNlcyBTYW5kYm94MB4XDTE4MDQxMTA3NTgyOFoXDTIzMDQxMTA3NTgyOFowgYkxCzAJBgNVBAYTAk5MMRAwDgYDVQQIDAdVdHJlY2h0MRAwDgYDVQQHDAdVdHJlY2h0MREwDwYDVQQKDAhSYWJvYmFuazEcMBoGA1UECwwTT25saW5lIFRyYW5zYWN0aW9uczElMCMGA1UEAwwcUFNEMiBBUEkgUEkgU2VydmljZXMgU2FuZGJveDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANoAjqGWUgCIm2F+0sBSEwLal+T3u+uldLikpxHCB8iL1GD7FrRjcA+MVsxhvHly7vRsHK+tQyMSaeK782RHpY33qxPLc8LmoQLb2EuiQxXj9POYkYBQ74qkrZnvKVlR3WoyQWeDOXnSY2wbNFfkP8ET4ElwyuIIEriwYhab0OIrnnrO8X82/SPZxHwEd3aQjQ6uhiw8paDspJbS5WjEfuwY16KVVUYlhbtAwGjvc6aK0NBm+LH9fMLpAE6gfGZNy0gzMDorVNbkQK1IoAGD8p9ZHdB0F3FwkILEjUiQW6nK+/fKDNJ0TBbpgZUpY8bR460qzxKdeZ1yPDqX2Cjh6fkCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAYL4iD6noMJAt63kDED4RB2mII/lssvHhcxuDpOm3Ims9urubFWEpvV5TgIBAxy9PBinOdjhO1kGJJnYi7F1jv1qnZwTV1JhYbvxv3+vk0jaiu7Ew7G3ASlzruXyMhN6t6jk9MpaWGl5Uw1T+gNRUcWQRR44g3ahQRIS/UHkaV+vcpOa8j186/1X0ULHfbcVQk4LMmJeXqNs8sBAUdKU/c6ssvj8jfJ4SfrurcBhY5UBTOdQOXTPY85aU3iFloerx7Oi9EHewxInOrU5XzqqTz2AQPXezexVeAQxP27lzqCmYC7CFiam6QBr06VebkmnPLfs76n8CDc1cwE6gUl0rMA==")*/
/**... --**--**//*  .addHeader("x-request-id", "REPLACE_THIS_VALUE"                 <-- UUID CREATED ON OUR SIDE    */
/**... ---**--- **//*  .addHeader("digest", "REPLACE_THIS_VALUE")                                                      */
        /** .addHeader("signature", "REPLACE_THIS_VALUE")                  <-- SIGNING STRING HASHED        **/
/**... ---**--- **//*  .addHeader("date", "REPLACE_THIS_VALUE")                       <-- CHECK FORMAT                 */

        return null;
    }

    private String createSignature(String signature) {
        StringBuilder sigString = new StringBuilder();
        sigString.append("keyId=\"").append("1523433508").append("\",")
                .append("algorithm=\"").append("rsa-sha512").append("\",")
                .append("headers=\"").append("date ").append("digest ").append("x-request-id").append("\",")
                .append("signature=").append("\"").append(signature).append("\"");
        return sigString.toString();
    }

    private String getCurrentDateTime() {
        DateFormat dateFormat;
        Calendar calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println(dateFormat.format(calendar.getTime()));
        return dateFormat.format(calendar.getTime());
    }

    private String setSigningString() {
        StringBuilder signingStringBuilder = new StringBuilder();
        signingStringBuilder.append("date: ").append(this.date).append("\n");
        signingStringBuilder.append("digest: ").append(this.digest).append("\n");
        signingStringBuilder.append("x-request-id: ").append(this.xRequestId.toString());
        return signingStringBuilder.toString();
    }
}
