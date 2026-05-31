package com.queueless.controller;

import com.queueless.dto.QueueTrackingDto;
import com.queueless.service.QueueTicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TrackingController {

    private final QueueTicketService
            queueTicketService;

    public TrackingController(
            QueueTicketService queueTicketService) {

        this.queueTicketService =
                queueTicketService;
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
                    "Ticket not found");

            return "success";
        }

        model.addAttribute(
                "tracking",
                tracking);

        return "track-ticket";
    }
}