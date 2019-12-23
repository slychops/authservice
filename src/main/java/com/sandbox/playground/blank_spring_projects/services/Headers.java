package com.sandbox.playground.blank_spring_projects.services;

final class Headers {
    public static final String AUTHORIZATION = "authorization";
    public static final String GRANT_TYPE = "grant_type";
    public static final String CODE = "code";
    static final String ACCEPT = "Accept";
    static final String CONTENT_TYPE = "content-type";
    static final String ACCEPT_ENCODING = "Accept-Encoding";

    private Headers(){}
}


final class ContentType {
    public static final String AUTHORIZATION_CODE = "authorization_code";
    static final String APPLICATION_JSON = "application/json";
    static final String GZIP_DEFLATE = "gzip, deflate";

    private ContentType(){}
}