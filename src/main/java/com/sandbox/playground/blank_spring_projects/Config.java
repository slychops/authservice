package com.sandbox.playground.blank_spring_projects;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("security.oauth")
@Getter
@Setter/*(value = AccessLevel.PACKAGE)*/
public class Config {

    private String authorizeEndpoint;
    private String tokenEndpoint;
    private String clientId;
    private String clientSecret;
}
