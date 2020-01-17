package com.sandbox.playground.blank_spring_projects.service;

public class SetupClasses {

    OAuthTokenRequestMaker oAuthTokenRequestMaker;

    public SetupClasses(String clientId, String clientSecret) {
        this.oAuthTokenRequestMaker = new OAuthTokenRequestMaker(new EncodingService(), clientId, clientSecret);
    }

    public SetupClasses() {
        this("client", "secret");
    }

    public OAuthTokenRequestMaker getoAuthTokenRequestMaker() {
        return oAuthTokenRequestMaker;
    }

}
