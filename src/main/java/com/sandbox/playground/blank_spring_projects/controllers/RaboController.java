package com.sandbox.playground.blank_spring_projects.controllers;

import com.sandbox.playground.blank_spring_projects.model.Accounts;
import com.sandbox.playground.blank_spring_projects.model.Token;
import com.sandbox.playground.blank_spring_projects.services.AccountService;
import com.sandbox.playground.blank_spring_projects.services.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequestMapping("api/v1/")
public class RaboController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RaboController.class);
    private final String scope;
    private final OAuthService authService;
    private final AccountService accountService;

    public RaboController(OAuthService authService,
                          AccountService accountService,
                          @Value("${security.oauth.scope.read}") String scope) {
        this.authService = authService;
        this.accountService = accountService;
        this.scope = scope;
    }

    @GetMapping(value = "auth", params = "code")
    public Token getToken(@RequestParam String code) {
        LOGGER.info("In getToken -->\t {}", code);
        authService.setCode(code);
        return authService.retrieveToken();
    }

    @GetMapping(value = "home")
    public RedirectView redirectForToken(RedirectAttributes attributes) {
        attributes.addFlashAttribute("flashattributes", "redirect")
                .addAttribute("client_id", "494520a3-c627-4b76-a3f5-76bcd9f7b7e1")
                .addAttribute("response_type", "code")
                .addAttribute("scope", scope);
//                .addAttribute("scope", "ais.balances.read");

        return new RedirectView(authService.getoAuthEndpoint());
    }

    @GetMapping(value = "accounts")
    public Accounts getAccounts() {

        return accountService.getAccountList();
    }

}
