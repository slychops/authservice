package com.sandbox.playground.blank_spring_projects.newstructure.service;

import com.sandbox.playground.blank_spring_projects.newstructure.service.exception.InsufficientResourceException;
import com.sandbox.playground.blank_spring_projects.newstructure.service.exception.UnknownContextException;
import com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts.RedirectContext;
import com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts.TokenScope;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Test;

import javax.naming.InsufficientResourcesException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OAuthServiceTest {

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
    private OAuthTokenRequestBuilder oAuthTokenRequestBuilder =
            (OAuthTokenRequestBuilder) OAuthTokenRequestBuilder.prepareRequest("1234", "clientSecret");

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
                oAuthTokenRequestBuilder,
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