package com.sandbox.playground.blank_spring_projects.newstructure.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
        URI uri = URI.create("improperUri");
        HttpEntity httpEntity = HttpEntity.EMPTY;
        Mockito.when(restTemplate.exchange(uri.toString(), HttpMethod.POST, httpEntity, String.class))
                .thenReturn(new ResponseEntity<>("accepted", HttpStatus.ACCEPTED));
//        Mockito.when(connectionService.doPost(any(), HttpMethod.POST, httpEntity, String.class))
//                .thenReturn(new ResponseEntity<>("accepted", HttpStatus.ACCEPTED));


        ResponseEntity<String> someResponse = connectionService.doPost(uri, HttpMethod.POST, httpEntity, String.class);
        System.out.println("Stop here");
//        assertEquals(ResponseEntity.class,
//                connectionService.doPost(uri, HttpMethod.POST, httpEntity, String.class).getClass());

    }
}