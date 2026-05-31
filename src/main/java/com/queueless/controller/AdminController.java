package com.queueless.controller;

import com.queueless.entity.QueueSession;
import com.queueless.enums.QueueSessionStatus;
import com.queueless.repository.QueueSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class AdminController {

    private final QueueSessionRepository
            queueSessionRepository;

    public AdminController(
            QueueSessionRepository queueSessionRepository) {

        this.queueSessionRepository =
                queueSessionRepository;
    }

    @GetMapping("/close-session")
    public String closeSession(Model model) {

        QueueSession session =
                queueSessionRepository
                        .findTopByOrderByIdDesc();

        if (session == null) {

            model.addAttribute(
                    "message",
                    "No session found");

            return "success";
        }

        session.setStatus(
                QueueSessionStatus.CLOSED);

        queueSessionRepository.save(session);

        model.addAttribute(
                "message",
                "Queue session closed");

        return "success";
    }

    @GetMapping("/open-session")
    public String openSession(Model model) {

        QueueSession latestSession =
                queueSessionRepository
                        .findTopByOrderByIdDesc();

        if (latestSession == null) {

            model.addAttribute(
                    "message",
                    "No previous session found");

            return "success";
        }

        QueueSession newSession =
                new QueueSession();

        newSession.setSessionDate(
                LocalDate.now());

        newSession.setStartTime(
                LocalDateTime.now());

        newSession.setStatus(
                QueueSessionStatus.OPEN);

        newSession.setBusinessCenter(
                latestSession.getBusinessCenter());

        newSession.setServiceType(
                latestSession.getServiceType());

        queueSessionRepository.save(
                newSession);

        model.addAttribute(
                "message",
                "New queue session opened");

        return "success";
    }
}