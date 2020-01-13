package com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts;

public enum TokenScope {
    READ_BALANCE ("ais.balances.read"),
    RETRIEVE_TRANSACTIONS ("ais.transactions.read-90days");

    private final String propertyName;

    TokenScope(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
