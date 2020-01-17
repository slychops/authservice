package com.sandbox.playground.blank_spring_projects.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandbox.playground.blank_spring_projects.model.Token;
import com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts.RedirectContext;
import com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts.TokenScope;
import com.sandbox.playground.blank_spring_projects.service.exception.InsufficientResourceException;
import com.sandbox.playground.blank_spring_projects.service.exception.UnknownContextException;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private OAuthTokenRequestMaker tokenRequestMaker;

    @Test
    void whenCalling_fetchToken_and2xxResponse_returnTokenWithValues() throws InsufficientResourceException, IOException {
        String clientId = "clientId12345";
        String clientSecret = "secret12345";
        String authCode = "12345";
        HttpEntity<String> entityStub = new HttpEntity<>("this body");
        Token tokenStub = new ObjectMapper().readValue(
                Files.readString(Paths.get("./src/test/java/com/sandbox/playground/blank_spring_projects/resource/token_response")),
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
                URI.create(tokenUri),
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

    @Test
    void whenCalling_fetchToken_withNullValue_expectNullPointerThrown() {
        OAuthService authService = new OAuthService(
                mock(OAuthTokenRequestMaker.class),
                mock(ConnectionService.class),
                "d",
                "d",
                URI.create("."),
                "l");
        assertThrows(NullPointerException.class,
                () -> authService.fetchToken(null));
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
                mock(ConnectionService.class),
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