package com.queueless.controller;

import com.queueless.dto.UserRegistrationDto;
import com.queueless.entity.*;
import com.queueless.enums.QueueSessionStatus;
import com.queueless.repository.QueueSessionRepository;
import com.queueless.service.QueueTicketService;
import com.queueless.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

@Controller
public class HomeController {

    private final UserService userService;
    private final QueueTicketService queueTicketService;
    private final QueueSessionRepository
            queueSessionRepository;

    public HomeController(UserService userService, QueueTicketService queueTicketService, QueueSessionRepository queueSessionRepository) {
        this.userService = userService;
        this.queueTicketService = queueTicketService;
        this.queueSessionRepository = queueSessionRepository;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }


    @GetMapping("/register")
    public String showRegisterPage(Model model) {

        model.addAttribute("userRegistrationDto",
                new UserRegistrationDto());

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute UserRegistrationDto registrationDto,
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

        model.addAttribute("message",
                "User registered successfully!");

        return "success";
    }

    //  endpoint to fetch all users and send them to view.
    @GetMapping("/users")
    public String showUsers(Model model) {

        model.addAttribute("users",
                userService.getAllUsers());

        return "users";
    }

    @GetMapping("/join-queue")
    public String joinQueue(Model model) {

        User customer =
                userService.getUserByEmail(
                        "test@gmail.com"
                );

        QueueSession session =
                queueSessionRepository
                        .findTopByOrderByIdDesc();
        System.out.println(customer);

        System.out.println(session);
        if (customer == null || session == null) {

            model.addAttribute(
                    "message",
                    "User or Queue Session not found"
            );

            return "success";
        }

        QueueTicket ticket =
                queueTicketService
                        .createTicket(
                                customer,
                                session
                        );

        model.addAttribute("ticket", ticket);

        return "ticket-success";
    }

    @GetMapping("/call-next")
    public String callNextCustomer(Model model) {

        QueueSession session =
                queueSessionRepository
                        .findTopByOrderByIdDesc();

        QueueTicket ticket =
                queueTicketService
                        .callNextTicket(session);

        if (ticket == null) {

            model.addAttribute(
                    "message",
                    "No waiting customers"
            );

            return "success";
        }

        model.addAttribute(
                "ticket",
                ticket
        );

        return "call-ticket";
    }

}