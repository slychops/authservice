package com.sandbox.playground.blank_spring_projects.newstructure.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandbox.playground.blank_spring_projects.model.ErrorToken;
import com.sandbox.playground.blank_spring_projects.model.Token;
import com.sandbox.playground.blank_spring_projects.newstructure.service.exception.InsufficientResourceException;
import com.sandbox.playground.blank_spring_projects.newstructure.service.exception.UnknownContextException;
import com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts.RedirectContext;
import com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts.TokenScope;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.Null;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


// Request a token from Rabo (POST)
// Build the request
// URI:
// "https://api-sandbox.rabobank.nl/openapi/sandbox/oauth2/token"
// Headers:
// Content-Type: application/x-www-form-urlencoded
// Authorization Code: Base64(  client_id+":"+client_secret )
// Body:
// grant_type: authorization_code
// code: authorization code received from Rabo
// Format of body: grant_type=authorization_code&code={auth code}
// Request a token from Rabo

@ExtendWith(MockitoExtension.class)
class OAuthServiceTest {

    private final String tokenUri = "tokenUri";
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private OAuthTokenRequestMaker tokenRequestMaker;
    private OAuthService authService20;
    private HttpEntity emptyRequest = HttpEntity.EMPTY;

    @BeforeEach
    void setUp() {
//        authService = new OAuthService(
//                tokenRequestMaker,
//                new ConnectionService(restTemplate),
//                "testEndpoint",
//                "clientId",
//                URI.create(tokenUri),
//                "clientSecret");
//
//        lenient().when(tokenRequestMaker.returnHttpEntity(anyString())).thenReturn(emptyRequest);
    }


    //ToDo: Need to add test for actual Token response values -- Create stub for Token
//    @Test
//    void whenCalling_fetchToken_and2xxResponse_returnToken() throws InsufficientResourceException {
//        mockRestTemplateResponse(HttpStatus.ACCEPTED);
//
//        assertEquals(Token.class, authService.fetchToken("").getClass());
//        verify(restTemplate, Mockito.times(1)).exchange(tokenUri, HttpMethod.POST, emptyRequest, String.class);
//    }

    @Test
    void whenCalling_fetchToken_and2xxResponse_returnTokenWithValues() throws InsufficientResourceException, IOException {
        String clientId = "clientId12345";
        String clientSecret = "secret12345";
        String authCode = "12345";
        HttpEntity<String> entityStub = new HttpEntity<>("this body");
        Token tokenStub = new ObjectMapper().readValue(
                Files.readString(Paths.get("./src/test/java/com/sandbox/playground/blank_spring_projects/newstructure/resource/token_response")),
                Token.class
        );
        ResponseEntity<Token> responseStub = new ResponseEntity<>(tokenStub, HttpStatus.ACCEPTED);
        ConnectionService conServiceMock = mock(ConnectionService.class);
        EncodingService encodingServiceMock = mock(EncodingService.class);
        OAuthTokenRequestMaker requestMaker = new OAuthTokenRequestMaker(
                encodingServiceMock,
                clientId,
                clientSecret
        );
        OAuthTokenRequestMaker requestSpy = spy(requestMaker);
        OAuthService authService = new OAuthService(
                requestSpy,
                conServiceMock,
                "Authendpoint",
                clientId,
                URI.create("tokenUri"),
                clientSecret
        );
        when(requestSpy.returnHttpEntity(authCode)).thenReturn(entityStub);

        when(conServiceMock.doPost(URI.create(tokenUri), HttpMethod.POST, entityStub, Token.class))
                .thenReturn(responseStub);

        assertEquals(Token.class, authService.fetchToken(authCode).getClass());
        assertEquals("bearer", authService.fetchToken(authCode).getToken_type());
        verify(conServiceMock, times(2)).
                doPost(URI.create(tokenUri), HttpMethod.POST, entityStub, Token.class);
        verify(requestSpy, times(2)).
                returnHttpEntity(authCode);

    }
//    @Test
//    void whenCalling_fetchToken_andExceptionThrown_returnErrorToken() throws InsufficientResourceException {
////        ConnectionService conService = mock(ConnectionService.class);
////        lenient().when(conService.doPost(
////                any(),
////                eq(HttpMethod.POST),
////                any(),
////                eq(Token.class)
////        )).thenThrow(RestClientResponseException.class);
//        when(restTemplate.exchange(
//                any(),
//                eq(HttpMethod.POST),
//                any(),
//                eq(Token.class)
//        )).thenThrow(RestClientResponseException.class);
//
////        assertThrows(RestClientResponseException.class,
////                () -> authService.fetchToken("1234"));
//        assertEquals(ErrorToken.class, authService.fetchToken("12345"));
//
//    }

//    //ToDo: Need to add test for actual errorToken response values -- Create stub for ErrorToken
//    @Test
//    void whenCalling_fetchToken_andErrorResponse_returnErrorToken() throws InsufficientResourceException {
//        String expectedBody = "{responseStatus: 400}";
//        ConnectionService conService = mock(ConnectionService.class);
//
//        when(restTemplate.exchange(
//                anyString(),
//                any(),
//                any(),
//                eq(Token.class)
//        )).thenThrow(RestClientResponseException.class);
//
//        assertEquals(ErrorToken.class, authService.fetchToken("").getClass());
//        verify(restTemplate, atLeastOnce()).exchange(tokenUri, HttpMethod.POST, emptyRequest, Token.class);
//    }

//    void mockRestTemplateResponse(HttpStatus outcome) {
//        when(restTemplate.exchange(
//                anyString(),
//                any(),
//                any(),
//                eq(Token.class)
//        )).thenReturn(new ResponseEntity<>(outcome));
//    }

    @Test
    void whenCalling_fetchToken_withNullValue_expectNullPointerThrown() {

        assertThrows(NullPointerException.class,
                () -> authService20.fetchToken(null));
    }

    @Test
    void whenUserRequestsAuthCodeEndPoint_sendEndPoint_withOnlyUri() throws URISyntaxException, UnknownContextException {
        checkExpectation("fakeuri", "", RedirectContext.AUTH_CODE, TokenScope.READ_BALANCE);
        checkExpectation("www.rabodummyendpoint.com/api/oauth/authorize", "", RedirectContext.AUTH_CODE, TokenScope.READ_BALANCE);
        checkExpectation("fakeuri", "1234-1234-2345", RedirectContext.AUTH_CODE, TokenScope.READ_BALANCE);
        checkExpectation("fakeuri", "asdfsd-sf-sdff", RedirectContext.AUTH_CODE, TokenScope.READ_BALANCE);
        checkExpectation("fakeuri", "", RedirectContext.AUTH_CODE, TokenScope.READ_BALANCE);
        checkExpectation("fakeuri", "", RedirectContext.AUTH_CODE, TokenScope.RETRIEVE_TRANSACTIONS);
    }

    @Test
    void givenTestContext_expectUnknownContextException() throws URISyntaxException, UnknownContextException {
        assertThrows(UnknownContextException.class,
                () -> checkExpectation("fakeuri", "1234-1234-2345", RedirectContext.TEST_CONTEXT, TokenScope.READ_BALANCE));
    }

    private void checkExpectation(String testEndpoint, String clientId, RedirectContext context, TokenScope scope) throws URISyntaxException, UnknownContextException {
        OAuthService authService = new OAuthService(
                tokenRequestMaker,
                new ConnectionService(restTemplate),
                testEndpoint,
                clientId,
                URI.create("tokenUri"),
                "clientSecret");

        URI expectedOutput = new URIBuilder(testEndpoint)
                .addParameter("client_id", clientId)
                .addParameter("response_type", "code")
                .addParameter("scope", scope.getPropertyName())
                .build();
        assertEquals(expectedOutput, authService.getAuthCodeUri(context, scope));
        assertEquals(expectedOutput, authService.getAuthCodeUri(context, scope));
    }

}