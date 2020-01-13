package com.sandbox.playground.blank_spring_projects.newstructure.service;

import com.sandbox.playground.blank_spring_projects.model.Token;
import com.sandbox.playground.blank_spring_projects.newstructure.service.exception.InsufficientResourceException;
import com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts.RedirectContext;
import com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts.TokenScope;
import com.sandbox.playground.blank_spring_projects.newstructure.service.exception.UnknownContextException;
import lombok.NonNull;

import java.net.URI;
import java.net.URISyntaxException;

public interface IAuthS {

    URI getAuthCodeUri(RedirectContext context, TokenScope scope) throws UnknownContextException, URISyntaxException;

    Token fetchToken(@NonNull String authorizationCode) throws InsufficientResourceException;
}
