package com.sandbox.playground.blank_spring_projects.newstructure.service;

import com.sandbox.playground.blank_spring_projects.model.ErrorToken;
import com.sandbox.playground.blank_spring_projects.model.Token;
import com.sandbox.playground.blank_spring_projects.newstructure.service.exception.InsufficientResourceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@ExtendWith(MockitoExtension.class)
public class OAuthServiceIT {

    SetupClasses setup = new SetupClasses();
    private URI endpoint = URI.create("http://ww.notreal.co.djk");
//    @Mock

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private ConnectionService connectionService;

    private OAuthService authService = new OAuthService(
            null,
            connectionService,
            "doesntmatter",
            "client",
            endpoint,
            "secret"
    );


    @Test
    void whenFetchingToken_andResponse_isNot2xx_thenReturnErrorToken() throws InsufficientResourceException, URISyntaxException {
        String authCode = "code";
        Token token = new ErrorToken();

        Mockito.when(restTemplate.exchange(endpoint, HttpMethod.POST, null, Token.class))
            .thenReturn(new ResponseEntity<>(token, HttpStatus.BAD_REQUEST));

        Token newToken = new Token();
        newToken = authService.fetchToken(authCode);


        System.out.println("Stop here");
    }
    @Test
    void whenFetchingToken_andResponse_isNot2xx_thenErrorToken_StatusCode_isStored() {

    }
    @Test
    void whenFetchingToken_andResponse_isNot2xx_thenErrorToken_messageIs_errorFetchingToken() {

    }

    @Test
    void whenFetchingToken_andResponse_is2xx_thenReturnToken() {

    }
    @Test
    void whenFetchingToken_andResponse_is2xx_thenTokenValue_isNotNull() {

    }
}
