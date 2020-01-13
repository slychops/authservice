package com.sandbox.playground.blank_spring_projects.newstructure.service;

import com.sandbox.playground.blank_spring_projects.model.Token;
import com.sandbox.playground.blank_spring_projects.newstructure.service.exception.InsufficientResourceException;
import com.sandbox.playground.blank_spring_projects.newstructure.service.exception.UnknownContextException;
import com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts.RedirectContext;
import com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts.TokenScope;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

//@Service
@Slf4j
public class OAuthService implements IAuthS {

    //private final ConnectionService connectionService;
//    private final EncodingService encodingService;
    private final OAuthTokenRequestBuilder<String> requestBuilder;
    private final String authCodeUri;
    private final String clientId;
    private final URI tokenUri;
    private final String clientSecret;

    @Autowired
    public OAuthService(
            OAuthTokenRequestBuilder<String> requestBuilder,
            @NonNull @Value("${security.oauth.authorize_endpoint}") String authEndPoint,
            @NonNull @Value("${security.general.client_id}") String clientId,
            @NonNull @Value("${security.oauth.token_endpoint}") URI tokenUri,
            @NonNull @Value("${security.oauth.client_secret}") String clientSecret) {
        this.requestBuilder = requestBuilder;
        this.authCodeUri = authEndPoint;
        this.clientId = clientId;
        this.tokenUri = tokenUri;
        this.clientSecret = clientSecret;
    }

    @Override
    public Token fetchToken(@NonNull String authorizationCode) throws InsufficientResourceException {
        return null;
        //return ConnectionService.makeRequest(endpoint, rb.getRequest(clientId, clientSecret, authCode), Token.class)
    }

    HttpEntity<String> prepareRequest() {
        return null;
    }

    @Override
    public URI getAuthCodeUri(RedirectContext context, TokenScope scope) throws UnknownContextException {
        if ("authorization_code".equals(context.getPropertyName())) {
            return getAuthorizationEndPoint(scope);
        } else throw new UnknownContextException();
    }

    private URI getAuthorizationEndPoint(TokenScope scope) {
        URI authUri = null;
        try {
            authUri = new URIBuilder(this.authCodeUri)
                    .addParameter("client_id", this.clientId)
                    .addParameter("response_type", "code")
                    .addParameter("scope", scope.getPropertyName())
                    .build();
        } catch (URISyntaxException e) {
            log.info(e.getLocalizedMessage());
        }
        return authUri;
    }

}
