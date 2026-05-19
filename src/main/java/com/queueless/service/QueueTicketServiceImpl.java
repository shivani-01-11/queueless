package com.queueless.service;

import com.queueless.entity.QueueSession;
import com.queueless.entity.QueueTicket;
import com.queueless.entity.User;
import com.queueless.enums.QueueTicketStatus;
import com.queueless.repository.QueueTicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class QueueTicketServiceImpl
        implements QueueTicketService {

    private final QueueTicketRepository queueTicketRepository;

    public QueueTicketServiceImpl(
            QueueTicketRepository queueTicketRepository) {

        this.queueTicketRepository = queueTicketRepository;
    }

    @Override
    public QueueTicket createTicket(User customer,
                                    QueueSession queueSession) {

        QueueTicket ticket = new QueueTicket();

        ticket.setCustomer(customer);

        ticket.setQueueSession(queueSession);

        ticket.setStatus(QueueTicketStatus.WAITING);

        ticket.setJoinedAt(LocalDateTime.now());

        ticket.setEstimatedWaitTime(15);

        ticket.setTokenNumber(generateToken());

        return queueTicketRepository.save(ticket);
    }

    private String generateToken() {

        return "QT-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 5)
                        .toUpperCase();
    }
}