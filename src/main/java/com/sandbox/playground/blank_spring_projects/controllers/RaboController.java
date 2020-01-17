package com.sandbox.playground.blank_spring_projects.controllers;

import com.sandbox.playground.blank_spring_projects.model.Token;
import com.sandbox.playground.blank_spring_projects.service.OAuthService;
import com.sandbox.playground.blank_spring_projects.service.exception.InsufficientResourceException;
import com.sandbox.playground.blank_spring_projects.service.exception.UnknownContextException;
import com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts.RedirectContext;
import com.sandbox.playground.blank_spring_projects.newstructure.utils.contexts.TokenScope;
import lombok.extern.slf4j.Slf4j;
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
//    private final AccountService accountService;

    public RaboController(OAuthService authService,
//                          AccountService accountService,
                          //ToDo: how should we be treating the scope??
                          @Value("${security.oauth.token.scopes.view_balance}") String scope) {
        this.authService = authService;
//        this.accountService = accountService;
        this.scope = scope;
    }

    @GetMapping(value = "home")
    public RedirectView redirectForToken(RedirectAttributes attributes) throws UnknownContextException {
        attributes.addFlashAttribute("flashattributes", "redirect")
                //ToDo: client id should be dynamic
//                .addAttribute("client_id", "494520a3-c627-4b76-a3f5-76bcd9f7b7e1")
                .addAttribute("client_id", "10aa9bf7-6192-4a32-ba98-1fa9808949b3")
                .addAttribute("response_type", "code")
                .addAttribute("scope", scope);

        return new RedirectView(authService.getAuthCodeUri(RedirectContext.AUTH_CODE, TokenScope.RETRIEVE_TRANSACTIONS).toString());
    }

    @GetMapping(value = "auth", params = "code")
    public RedirectView getToken(@RequestParam String code) {
        log.info("In getToken, authorization code -->\t {}", code);
//        authService.setCode(code);

        return new RedirectView("http://localhost:8080/api/v1/token");
    }

    @GetMapping(value = "token")
    public Token fetchToken() throws InsufficientResourceException {
        Token tok = authService.fetchToken("12345");
        return tok;
//        return authService.fetchToken("12345");
    }

//    @GetMapping(value = "accounts")
//    public String getAccounts() {
//        return accountService.getAccountList();
//    }
}
