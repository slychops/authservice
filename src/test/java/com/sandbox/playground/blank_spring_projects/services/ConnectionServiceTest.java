package com.sandbox.playground.blank_spring_projects.services;

import com.sandbox.playground.blank_spring_projects.exceptions.UnsupportedContentTypeException;
import com.sandbox.playground.blank_spring_projects.utils.Headers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConnectionServiceTest {

    private ConnectionService conService = new ConnectionService();
    private String supportedContentType = "application/x-www-form-urlencoded";
    private String clientId = "dummyClient";
    private String clientSecret = "dummySecret";

    @Test
    void expectUnsupportedContentTypeException_when_unsupportedContentTypePassedAsHeader() {
        String unsupportedContentType = "should fail";
        assertThrows(UnsupportedContentTypeException.class,
                () -> conService.prepareHeaders(unsupportedContentType, clientId, clientSecret));
    }

    @Test
    void supportedContentTypePassed() throws UnsupportedContentTypeException {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map = conService.prepareHeaders(supportedContentType, clientId, clientSecret);
        assertEquals(4, map.size());
        assertEquals("UTF-8", map.get(Headers.ACCEPT_ENCODING).get(0));
    }
}
