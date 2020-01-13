package com.sandbox.playground.blank_spring_projects.newstructure.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionServiceTest {
    private EncodingService encodingService = new EncodingService();
    private OAuthTokenRequestMaker requestBuilder = new SetupClasses().getoAuthTokenRequestMaker();
//    private OAuthTokenRequestMaker requestBuilder = new OAuthTokenRequestMaker(encodingService, "1234", "clientsecret");

    @Test
    void whenPostReceived_andHttpEntityIsNull_throwNullPointerException() {
        URI tokenEndpoint = URI.create("tokenEndpoint");
        OAuthService service = new OAuthService(
                requestBuilder,
                "endpoint",
                "clientA",
                tokenEndpoint,
                "secret");
        ConnectionService conService = new ConnectionService();
        assertThrows(NullPointerException.class,
                () -> conService.doPost(tokenEndpoint, HttpMethod.POST, null, Object.class));
    }

    @Test
    void whenDoPostIsInvoked_thenReceiveTokenResponse(){

    }

}