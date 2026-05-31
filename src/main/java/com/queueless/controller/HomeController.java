package com.queueless.controller;

import com.queueless.dto.UserRegistrationDto;
import com.queueless.entity.User;
import com.queueless.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {

        model.addAttribute(
                "userRegistrationDto",
                new UserRegistrationDto());

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute UserRegistrationDto registrationDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        User user = new User();

        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(registrationDto.getPassword());

        userService.saveUser(user);

        return "redirect:/success";
    }

    @GetMapping("/success")
    public String showSuccessPage(Model model) {

        model.addAttribute(
                "message",
                "User registered successfully!");

        return "success";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {

        model.addAttribute(
                "users",
                userService.getAllUsers());

        return "users";
    }

    @GetMapping("/access-denied")
    public String accessDeniedPage() {

        return "access-denied";
    }
}