package com.javasharehub.controller;

import com.javasharehub.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("error", null);
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String name,
                           @RequestParam String password,
                           Model model) {
        try {
            userService.register(email, name, password);
            return "redirect:/login";
        } catch (RuntimeException e) {
            log.warn("Ошибка регистрации: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
}