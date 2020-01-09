package com.sandbox.playground.blank_spring_projects.services;

import com.sandbox.playground.blank_spring_projects.exceptions.InvalidValuesPassedForBase64EncodingException;
import net.bytebuddy.asm.Advice.Thrown;
import org.junit.jupiter.api.Test;
import org.springframework.expression.spel.ast.ValueRef.TypedValueHolderValueRef;

import java.security.NoSuchAlgorithmException;

import static com.sandbox.playground.blank_spring_projects.services.EncodingService.*;
import static org.junit.jupiter.api.Assertions.*;

public class EncodingServiceTest {
    private String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDaAI6hllIAiJthftLAUhMC2pfk97vrpXS4pKcRwgfIi9Rg+xa0Y3APjFbMYbx5cu70bByvrUMjEmniu/NkR6WN96sTy3PC5qEC29hLokMV4/TzmJGAUO+KpK2Z7ylZUd1qMkFngzl50mNsGzRX5D/BE+BJcMriCBK4sGIWm9DiK556zvF/Nv0j2cR8BHd2kI0OroYsPKWg7KSW0uVoxH7sGNeilVVGJYW7QMBo73OmitDQZvix/XzC6QBOoHxmTctIMzA6K1TW5ECtSKABg/KfWR3QdBdxcJCCxI1IkFupyvv3ygzSdEwW6YGVKWPG0eOtKs8SnXmdcjw6l9go4en5AgMBAAECggEAB8nsTqalwGIhFw8mbXuhNUFlGuek/arYLD6pv28swwQH7v0ZlxFUcCHF+iBl0PsDwZTZQ4ePtgGS6ehoLkWHCzb1lEv5E1YVG5qKNE2UUwRlfIyPakO6AzyV/UF3uzq7C+/GuXGNTKZxKewg5yD/DCFvKoCOpxu9u36FyqP/hw0SADVlmp35/zoPDPZzu1j4FiCo0pJ9LwJcHxeJHopNAKDw9k6I4z/grskdgupsGzK2BiGiQ/+wmmO68/6Xa6KWfpr1PQ6ODJHgzZsdGCVi6Ebaqlj6BbsYWxP6h3lrsGt+LmHBaN2jCD6cDp+lihqFgnm8hfdv0lmbPilp71EDfwKBgQD6U8PBzZtN8yXm5WuSNL+/8q5GjNmeTJBSo1gM6Y8vOT4QAE147LbVuVBDwyHPoSrNejePae6Q14PswjByT7B8DZ0OeQyGa3trrFg/ib7Vv4ZMvJqX9+WzBrzZsxTg7oCKHzmCR4vIRItKHH3zWnnhqlo8ic2AZ2O43cdJosbO1wKBgQDe8UODOLu0vnHohOKeUqF3w/ZOB2+83/jsYyUbSkzsGvHIwTjObuMUFTQvdMZ6IkIyJdfnDZIbvlBSD8tzL5iKFTNCK2nL1i4GiFr0CYLaHAlhJ5GEbTrTMDoJeBPerZq83HPrSa/Wb0xO18QTWsoVQPFfPFbbcQyI9ryJ2iIDrwKBgQC6kuAefG46ZPVk6K2KZUJdgDUgZC52a75NuW0RAqszmUiGiJM1g8ip9tq6BqAWrprGV0c93shusBKlzf5p1LdHXqYmeVY6gbWVhPipMrNHgN5KJ3BZv+w1yNnMsErpcxne2HL2hPjMJTpj3GSLkm2xIlTrNhIyl9ydlr7IRUhENQKBgQCvi6HxbXa/90WSJTCcIcxqla8X+dsOCf3jhJ3vQy4Wq5C+1wZ35fCAG8Ifq/+so9Ujz5CVqqXlmpF8TFuSs2OVNuRJsg14J4nOMwgLKIIUZAcurQ10DN5I9Kx+UEK1EFXLaHsORdNjMfgQDO2jn9WHrr9gkg6CdB2+qyoCEfS+mQKBgBW08lcy9V5RzRWb/v/jxsc7ovmgAhCJhDeV7dPbx4HbFeoQJlbA8g1thdcFlcatSGyNDbvNE1GPSd4NhkpRY6Hfv53kdEzjVkEtU8lUdL7HNVJqX7bU7oZlfbYcwxWQ1Gg8C1oLIAyEt71slQtdRiNYBRZTQe2F0wxbXnuUqLAw";
    private String digestWithNoBody512 = "sha-512=z4PhNX7vuL3xVChQ1m2AB9Yg5AULVxXcg/SpIdNs6c5H0NE8XYXysP+DGNKHfuwvY7kxvUdBeoGlODJ6+SfaPg==";
    private EncodingService encodingService;

    @Test
    void checkIfBase64EncodingIsSuccessful() {
        String actual = prepareBase64Encoding("abcd-efgh-ijkl", "qwertyu");
        assertEquals("Basic YWJjZC1lZmdoLWlqa2w6cXdlcnR5dQ==", actual);
    }

    @Test
    void ifNullAddedToEncoding_thenThrow_invalidValuesPassedForBase64EncodingException() {
        String clientSecret = null;
        assertThrows(InvalidValuesPassedForBase64EncodingException.class,
                () -> prepareBase64Encoding("abcd-efgh-ijkl", clientSecret));
    }

    @Test
    void checkIfDigestThrowsNoSuchAlgorithmException() throws NoSuchAlgorithmException {
        String unknownAlgorithm = "asd";
        String expected = "Unknown hashing algorithm requested: " + unknownAlgorithm;

        Exception thrown =
        assertThrows(NoSuchAlgorithmException.class,
                () -> createDigest(unknownAlgorithm,""));
        assertEquals(expected, thrown.getLocalizedMessage());
    }

    @Test
    void createDigest_ignoreCaseOfAlgorithmValue() throws NoSuchAlgorithmException {
        String digestWithNoCase = "sha-512";
        String digestWithUpper = "SHA-512";

        assertEquals(digestWithNoBody512, createDigest(digestWithNoCase, ""));
        assertEquals(digestWithNoBody512, createDigest(digestWithUpper, ""));
    }

    @Test
    void checkDigestOfSHA512_onEmptyBody_isCorrect() throws NoSuchAlgorithmException {
        assertEquals(digestWithNoBody512, createDigest("SHA-512", ""));
        assertNotEquals(digestWithNoBody512, createDigest("SHA-512", "a body"));
    }

}
