package com.sourcery.gymapp.backend.authentication.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @Value("${app.frontend-url}")
    private String frontendUrl;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("frontendUrl", frontendUrl);
        return "authentication/login";
    }
}
