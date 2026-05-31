package com.queueless.controller;

import com.queueless.entity.QueueSession;
import com.queueless.entity.QueueTicket;
import com.queueless.entity.User;
import com.queueless.repository.QueueSessionRepository;
import com.queueless.service.QueueTicketService;
import com.queueless.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class QueueController {

    private final UserService userService;

    private final QueueTicketService queueTicketService;

    private final QueueSessionRepository
            queueSessionRepository;

    public QueueController(
            UserService userService,
            QueueTicketService queueTicketService,
            QueueSessionRepository queueSessionRepository) {

        this.userService = userService;
        this.queueTicketService = queueTicketService;
        this.queueSessionRepository =
                queueSessionRepository;
    }

    @GetMapping("/join-queue")
    public String joinQueue(Model model) {

        User customer =
                userService.getUserByEmail(
                        "test@gmail.com");

        QueueSession session =
                queueSessionRepository
                        .findTopByOrderByIdDesc();

        if (customer == null || session == null) {

            model.addAttribute(
                    "message",
                    "User or Queue Session not found");

            return "success";
        }

        QueueTicket ticket =
                queueTicketService
                        .createTicket(
                                customer,
                                session);

        if (ticket == null) {

            model.addAttribute(
                    "message",
                    "Queue session is not open");

            return "success";
        }

        model.addAttribute(
                "ticket",
                ticket);

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
                    "Cannot call next ticket right now");

            return "success";
        }

        model.addAttribute(
                "ticket",
                ticket);

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
                    "Invalid ticket transition");

            return "success";
        }

        model.addAttribute(
                "ticket",
                ticket);

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
                    "Invalid ticket transition");

            return "success";
        }

        model.addAttribute(
                "ticket",
                ticket);

        return "call-ticket";
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
                    "Invalid missed transition");

            return "success";
        }

        model.addAttribute(
                "ticket",
                ticket);

        return "call-ticket";
    }
}