package com.queueless.config;

import com.queueless.entity.*;
import com.queueless.enums.QueueSessionStatus;
import com.queueless.repository.QueueSessionRepository;
import com.queueless.repository.UserRepository;
import com.queueless.service.QueueTicketService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    private final QueueSessionRepository queueSessionRepository;

    public DataSeeder(UserRepository userRepository,
                      QueueSessionRepository queueSessionRepository) {

        this.userRepository = userRepository;
        this.queueSessionRepository = queueSessionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {

        if (userRepository.count() > 0) {
            return;
        }

        User user = new User();

        user.setName("Test User");

        user.setEmail("test@gmail.com");

        user.setPassword("123");

        userRepository.save(user);

        BusinessCenter businessCenter =
                new BusinessCenter();

        businessCenter.setBusinessName(
                "CityCare Clinic");

        businessCenter.setAddress(
                "Main Street");

        businessCenter.setCity(
                "Dallas");

        businessCenter.setPhone(
                "1234567890");

        ServiceType serviceType =
                new ServiceType();

        serviceType.setServiceName(
                "Consultation");

        serviceType.setAverageServiceTime(15);

        serviceType.setBusinessCenter(
                businessCenter);

        QueueSession session =
                new QueueSession();

        session.setSessionDate(
                LocalDate.now());

        session.setStartTime(
                LocalDateTime.now());

        session.setStatus(
                QueueSessionStatus.OPEN);

        session.setBusinessCenter(
                businessCenter);

        session.setServiceType(
                serviceType);

        queueSessionRepository.save(session);

        System.out.println(
                "Initial test data seeded");
    }
}