package com.sandbox.playground.blank_spring_projects.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
class TLSService extends ConnectionService {

    private static final String SHA_512 = "SHA-512";
    static String authorization;

    private final String endpoint;
    private final String xIbmClientId;
    private final String tppSignatureCertificate;
    private final UUID xRequestId;
    private final String digest;
    private String date;
    private String signature;
    private String signingString;

    @Autowired
    TLSService(@Value("${security.general.client_id}") String clientId,
               @Value("${security.psd2.certificate}") String certificate,
               @Value("${accounts.endpoint.accountslist}") String url,
               @Value("") String body) {
        this.xIbmClientId = clientId;
        this.tppSignatureCertificate = certificate;
        this.endpoint = url;
        this.xRequestId = UUID.randomUUID();
        this.digest = EncodingService.createDigest(SHA_512, body);
    }

    public final HttpEntity<String> prepareAccountsRequest() {
        this.date = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        this.signingString = setSigningString();
        this.signature = EncodingService.generateSignature(this.signingString);
        System.out.println("This is where we will now create the headers.");

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

    private String setSigningString() {
        StringBuilder signingStringBuilder = new StringBuilder();
        signingStringBuilder.append("date: ").append(this.date).append("\n");
        signingStringBuilder.append("digest: ").append(this.digest).append("\n");
        signingStringBuilder.append("x-request-id: ").append(this.xRequestId.toString());
        return signingStringBuilder.toString();
    }
}
