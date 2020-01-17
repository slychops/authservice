package com.sandbox.playground.blank_spring_projects.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandbox.playground.blank_spring_projects.model.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ConnectionServiceTest {

    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    ConnectionService connectionService;

    @Test
    void whenPostReceived_andHttpEntityIsNull_throwNullPointerException() {
        URI tokenEndpoint = URI.create("tokenEndpoint");
        ConnectionService conService = new ConnectionService(new RestTemplate());
        assertThrows(NullPointerException.class,
                () -> conService.doPost(tokenEndpoint, HttpMethod.POST, null, Object.class));
    }

    @Test
    void whenDoPostIsInvoked_andResponse_andIs2xx_returnResponseEntityOfString() {
        URI uri = URI.create("irrelevant");
        HttpEntity httpEntity = HttpEntity.EMPTY;
        when(restTemplate.exchange(uri.toString(), HttpMethod.POST, httpEntity, String.class))
                .thenReturn(new ResponseEntity<>("accepted", HttpStatus.ACCEPTED));

        ResponseEntity<String> actualResponse = connectionService.doPost(uri, HttpMethod.POST, httpEntity, String.class);

        assertTrue(actualResponse.getStatusCode().is2xxSuccessful());
    }

    @Test
    void whenDoPostIsInvoked_andResponse_IsNot2xx_HttpStatusCodeException() {
        URI uri = URI.create("irrelevant");
        HttpEntity httpEntity = HttpEntity.EMPTY;
        when(restTemplate.exchange(uri.toString(), HttpMethod.POST, httpEntity, Token.class))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(HttpClientErrorException.class,
                () -> connectionService.doPost(uri, HttpMethod.POST, httpEntity, Token.class));
    }

    @Test
    void whenDoPostIsInvoked_andResponse_Is2xx_returnToken() throws Exception {
        //SETUP
        URI uri = URI.create("irrelevant");
        HttpEntity httpEntity = HttpEntity.EMPTY;
        String json = Files.readString(Paths.get("./src/test/java/com/sandbox/playground/blank_spring_projects/newstructure/resource/token_response"));
        Token mockToken = new ObjectMapper().readValue(json, Token.class);

        //EXPECT
        when(restTemplate.exchange(uri.toString(), HttpMethod.POST, httpEntity, Token.class))
                .thenReturn(ResponseEntity.ok(mockToken));

        verify(restTemplate, never()).exchange(uri.toString(), HttpMethod.POST, httpEntity, Token.class);
        assertEquals(mockToken, connectionService.doPost(uri, HttpMethod.POST, httpEntity, Token.class).getBody());
        assertTrue(connectionService.doPost(uri, HttpMethod.POST, httpEntity, Token.class).getStatusCode().is2xxSuccessful());

        verify(restTemplate, times(2)).exchange(uri.toString(), HttpMethod.POST, httpEntity, Token.class);
        verify(restTemplate, never()).exchange(uri.toString(), HttpMethod.POST, httpEntity, String.class);
    }

    @ParameterizedTest
    @ValueSource(classes = {String.class, Token.class})
    void ensureThatClassPassedToConnectionService_respondsWithSameType(Class<?> classT) {
        HttpEntity entity = HttpEntity.EMPTY;


        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                eq(HttpEntity.EMPTY),
                eq(classT))
        ).thenReturn(new ResponseEntity(classT, HttpStatus.OK));

        assertEquals(classT,
                Objects.requireNonNull(
                        connectionService.doPost(
                                URI.create("URI"), HttpMethod.POST, entity, classT).
                                getBody())
        );
    }
}