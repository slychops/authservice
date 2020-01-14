package com.sandbox.playground.blank_spring_projects.newstructure.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sandbox.playground.blank_spring_projects.model.ErrorToken;
import com.sandbox.playground.blank_spring_projects.model.Token;
import com.sandbox.playground.blank_spring_projects.newstructure.service.exception.InsufficientResourceException;
import com.sandbox.playground.blank_spring_projects.newstructure.service.exception.UnknownContextException;
import com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts.RedirectContext;
import com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts.TokenScope;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;


class OAuthServiceTest {

    private OAuthTokenRequestMaker requestBuilder = new SetupClasses().getoAuthTokenRequestMaker();
    SetupClasses classes = new SetupClasses();

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




    @Test
    void whenCalling_fetchToken_withNullValue_expectNullPointerThrown() {
        OAuthService service = new OAuthService(
                classes.getoAuthTokenRequestMaker(),
                new ConnectionService(new RestTemplate()),
                "a",
                "c",
                URI.create("SA"),
                "d");
        assertThrows(NullPointerException.class,
                () -> service.fetchToken(null));
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
                requestBuilder,
                new ConnectionService(new RestTemplate()),
                testEndpoint,
                clientId,
                new URI(""),
                "");
        URI expectedOutput = new URIBuilder(testEndpoint)
                .addParameter("client_id", clientId)
                .addParameter("response_type", "code")
                .addParameter("scope", scope.getPropertyName())
                .build();
        assertEquals(expectedOutput, authService.getAuthCodeUri(context, scope));
    }

}