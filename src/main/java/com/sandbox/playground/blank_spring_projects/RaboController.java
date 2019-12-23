package com.sandbox.playground.blank_spring_projects;

import com.sandbox.playground.blank_spring_projects.model.Token;
import com.sandbox.playground.blank_spring_projects.services.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private OAuthService authService;

    @Autowired
    public RaboController(OAuthService authService) {
        this.authService = authService;
    }

    @GetMapping(value ="auth", params = "code")
    public Token getToken(@RequestParam String code) {
        LOGGER.info("In getToken -->\t {}", code);
        authService.setCode(code);
        return authService.getToken();
    }

    @GetMapping(value="home")
    public RedirectView redirectForToken(RedirectAttributes attributes) {
        attributes.addFlashAttribute("flashattributes", "redirect")
                .addAttribute("client_id", "494520a3-c627-4b76-a3f5-76bcd9f7b7e1")
                .addAttribute("response_type", "code")
                .addAttribute("scope", "oauth2.consents.read");

        return new RedirectView(authService.getoAuthEndpoint());
    }

}
