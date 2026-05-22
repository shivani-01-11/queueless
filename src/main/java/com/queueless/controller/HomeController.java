package com.queueless.controller;

import com.queueless.dto.QueueDashboardDto;
import com.queueless.dto.QueueTrackingDto;
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
import java.time.LocalDateTime;

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
        if (ticket == null) {

            model.addAttribute(
                    "message",
                    "Queue session is not open"
            );

            return "success";
        }

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
                    "Cannot call next ticket right now"
            );

            return "success";
        }

        model.addAttribute(
                "ticket",
                ticket
        );

        return "call-ticket";
    }


    @GetMapping("/start-service/{id}")
    public String startService(
            @PathVariable Long id,
            Model model) {

        QueueTicket ticket =
                queueTicketService
                        .startService(id);

        if (ticket == null) {

            model.addAttribute(
                    "message",
                    "Invalid ticket transition"
            );

            return "success";
        }

        model.addAttribute(
                "ticket",
                ticket
        );

        return "call-ticket";
    }

    @GetMapping("/complete-service/{id}")
    public String completeService(
            @PathVariable Long id,
            Model model) {

        QueueTicket ticket =
                queueTicketService
                        .completeService(id);

        if (ticket == null) {

            model.addAttribute(
                    "message",
                    "Invalid ticket transition"
            );

            return "success";
        }

        model.addAttribute(
                "ticket",
                ticket
        );

        return "call-ticket";
    }


    @GetMapping("/dashboard")
    public String showDashboard(Model model) {

        QueueSession session =
                queueSessionRepository
                        .findTopByOrderByIdDesc();

        QueueDashboardDto dashboard =
                queueTicketService
                        .getDashboardData(session);

        model.addAttribute(
                "dashboard",
                dashboard
        );

        return "dashboard";
    }

    @GetMapping("/track/{token}")
    public String trackTicket(
            @PathVariable String token,
            Model model) {

        QueueTrackingDto tracking =
                queueTicketService
                        .trackTicket(token);

        if (tracking == null) {

            model.addAttribute(
                    "message",
                    "Ticket not found"
            );

            return "success";
        }

        model.addAttribute(
                "tracking",
                tracking
        );

        return "track-ticket";
    }

    @GetMapping("/miss-ticket/{id}")
    public String missTicket(
            @PathVariable Long id,
            Model model) {

        QueueTicket ticket =
                queueTicketService
                        .markTicketAsMissed(id);

        if (ticket == null) {

            model.addAttribute(
                    "message",
                    "Invalid missed transition"
            );

            return "success";
        }

        model.addAttribute(
                "ticket",
                ticket
        );

        return "call-ticket";
    }

    @GetMapping("/close-session")
    public String closeSession(
            Model model) {

        QueueSession session =
                queueSessionRepository
                        .findTopByOrderByIdDesc();

        if (session == null) {

            model.addAttribute(
                    "message",
                    "No session found"
            );

            return "success";
        }

        session.setStatus(
                QueueSessionStatus.CLOSED
        );

        queueSessionRepository.save(session);

        model.addAttribute(
                "message",
                "Queue session closed"
        );

        return "success";
    }

    @GetMapping("/open-session")
    public String openSession(
            Model model) {

        QueueSession latestSession =
                queueSessionRepository
                        .findTopByOrderByIdDesc();

        if (latestSession == null) {

            model.addAttribute(
                    "message",
                    "No previous session found"
            );

            return "success";
        }

        QueueSession newSession =
                new QueueSession();

        newSession.setSessionDate(
                LocalDate.now()
        );

        newSession.setStartTime(
                LocalDateTime.now()
        );

        newSession.setStatus(
                QueueSessionStatus.OPEN
        );

        newSession.setBusinessCenter(
                latestSession.getBusinessCenter()
        );

        newSession.setServiceType(
                latestSession.getServiceType()
        );

        queueSessionRepository.save(
                newSession
        );

        model.addAttribute(
                "message",
                "New queue session opened"
        );

        return "success";
    }

    @GetMapping("/access-denied")
    public String accessDeniedPage() {

        return "access-denied";
    }

}