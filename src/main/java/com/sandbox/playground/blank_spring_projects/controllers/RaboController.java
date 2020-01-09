package com.sandbox.playground.blank_spring_projects.controllers;

import com.sandbox.playground.blank_spring_projects.model.Token;
import com.sandbox.playground.blank_spring_projects.services.AccountService;
import com.sandbox.playground.blank_spring_projects.services.OAuthService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RaboController {
    private final String scope;
    private final OAuthService authService;
    private final AccountService accountService;

    public RaboController(OAuthService authService,
                          AccountService accountService,
                          //ToDo: how should we be treating the scope??
                          @Value("${security.oauth.scope.read}") String scope) {
        this.authService = authService;
        this.accountService = accountService;
        this.scope = scope;
    }

    @GetMapping(value = "home")
    public RedirectView redirectForToken(RedirectAttributes attributes) {
        attributes.addFlashAttribute("flashattributes", "redirect")
                //ToDo: client id should be dynamic
//                .addAttribute("client_id", "494520a3-c627-4b76-a3f5-76bcd9f7b7e1")
                .addAttribute("client_id", "10aa9bf7-6192-4a32-ba98-1fa9808949b3")
                .addAttribute("response_type", "code")
                .addAttribute("scope", scope);

        return new RedirectView(authService.getOAuthEndpoint());
    }

    @GetMapping(value = "auth", params = "code")
    public RedirectView getToken(@RequestParam String code) {
        log.info("In getToken, authorization code -->\t {}", code);
        authService.setCode(code);
        return new RedirectView("http://localhost:8080/api/v1/token");
    }

    @GetMapping(value = "token")
    public Token fetchToken() {
        return authService.retrieveToken();
    }

    @GetMapping(value = "accounts")
    public String getAccounts() {
        return accountService.getAccountList();
    }
}
