package com.sandbox.playground.blank_spring_projects.services;

import com.sandbox.playground.blank_spring_projects.model.Accounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountService {

    private final String endpoint;
    private final RestTemplate client;
    private TLSService tlsService;

    @Autowired
    public AccountService(TLSService tlsService,
                          RestTemplate client,
                          @Value("${accounts.endpoint.accountslist}") String url) {
        this.endpoint = url;
        this.client = client;
        //ToDo: This is not a service.. rethink
        this.tlsService = tlsService;
    }

    //ToDo: return a list of accounts
    public String getAccountList() {
        HttpEntity<String> httpRequest = tlsService.prepareAccountsRequest();
        return httpRequest.getBody();
//        return fetchAccounts(httpRequest).getBody();
    }

    //Redundant method. How do I name methods here to improve clarity?
    private ResponseEntity<Accounts> fetchAccounts(HttpEntity<String> request) {
        return doGet(request);
    }

    private ResponseEntity<Accounts> doGet(HttpEntity<String> request) {
        return client.exchange(endpoint, HttpMethod.POST, request, Accounts.class);
    }
}
