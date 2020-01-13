package com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts;

public enum RedirectContext {
    AUTH_CODE ("authorization_code"),
    TOKEN ("token"),
    TEST_CONTEXT ("test_context");

    private final String propertyName;

    RedirectContext(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

}
