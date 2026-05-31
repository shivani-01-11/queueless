package com.queueless.controller;

import com.queueless.dto.QueueDashboardDto;
import com.queueless.entity.QueueSession;
import com.queueless.repository.QueueSessionRepository;
import com.queueless.service.QueueTicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final QueueTicketService
            queueTicketService;

    private final QueueSessionRepository
            queueSessionRepository;

    public DashboardController(
            QueueTicketService queueTicketService,
            QueueSessionRepository queueSessionRepository) {

        this.queueTicketService =
                queueTicketService;

        this.queueSessionRepository =
                queueSessionRepository;
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
                dashboard);

        return "dashboard";
    }
}