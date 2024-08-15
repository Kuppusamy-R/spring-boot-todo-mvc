package com.example.todolist.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @Value("${spring.application.name}")
    String applicationName;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("applicationName", this.applicationName);
        return "login";
    }
}
