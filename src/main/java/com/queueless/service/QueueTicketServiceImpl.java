package com.queueless.service;

import com.queueless.dto.QueueDashboardDto;
import com.queueless.dto.QueueTicketViewDto;
import com.queueless.dto.QueueTrackingDto;
import com.queueless.entity.QueueSession;
import com.queueless.entity.QueueTicket;
import com.queueless.entity.User;
import com.queueless.enums.QueueTicketStatus;
import com.queueless.repository.QueueTicketRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

        long waitingCount =
                queueTicketRepository
                        .countByQueueSessionAndStatus(
                                queueSession,
                                QueueTicketStatus.WAITING
                        );

        Integer averageTime =
                queueSession
                        .getServiceType()
                        .getAverageServiceTime();

        int estimatedWait =
                (int) waitingCount * averageTime;

        ticket.setEstimatedWaitTime(
                estimatedWait
        );

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


    @Override
    public QueueTicket callNextTicket(
            QueueSession queueSession) {

        boolean alreadyCalled =
                queueTicketRepository
                        .existsByQueueSessionAndStatus(
                                queueSession,
                                QueueTicketStatus.CALLED
                        );

        if (alreadyCalled) {
            return null;
        }

        QueueTicket ticket =
                queueTicketRepository
                        .findFirstByQueueSessionAndStatusOrderByJoinedAtAsc(
                                queueSession,
                                QueueTicketStatus.WAITING
                        );

        if (ticket == null) {
            return null;
        }

        ticket.setStatus(
                QueueTicketStatus.CALLED
        );

        ticket.setCalledAt(
                LocalDateTime.now()
        );

        return queueTicketRepository.save(ticket);
    }

    @Override
    public QueueTicket startService(Long ticketId) {

        QueueTicket ticket =
                queueTicketRepository
                        .findById(ticketId)
                        .orElse(null);

        if (ticket == null) {
            return null;
        }

        if (ticket.getStatus()
                != QueueTicketStatus.CALLED) {

            return null;
        }

        boolean alreadyServing =
                queueTicketRepository
                        .existsByQueueSessionAndStatus(
                                ticket.getQueueSession(),
                                QueueTicketStatus.SERVING
                        );

        if (alreadyServing) {
            return null;
        }
        ticket.setStatus(
                QueueTicketStatus.SERVING
        );

        ticket.setServiceStartedAt(
                LocalDateTime.now()
        );

        return queueTicketRepository.save(ticket);
    }

    @Override
    public QueueTicket completeService(Long ticketId) {

        QueueTicket ticket =
                queueTicketRepository
                        .findById(ticketId)
                        .orElse(null);

        if (ticket == null) {
            return null;
        }

        if (ticket.getStatus()
                != QueueTicketStatus.SERVING) {

            return null;
        }

        ticket.setStatus(
                QueueTicketStatus.COMPLETED
        );

        ticket.setCompletedAt(
                LocalDateTime.now()
        );

        return queueTicketRepository.save(ticket);
    }

    @Override
    public QueueDashboardDto getDashboardData(
            QueueSession queueSession) {

        QueueDashboardDto dashboard =
                new QueueDashboardDto();

        QueueTicket servingTicket =
                queueTicketRepository
                        .findFirstByQueueSessionAndStatus(
                                queueSession,
                                QueueTicketStatus.SERVING
                        );

        List<QueueTicket> waitingTickets =
                queueTicketRepository
                        .findByQueueSessionAndStatusOrderByJoinedAtAsc(
                                queueSession,
                                QueueTicketStatus.WAITING
                        );

        List<QueueTicket> allTickets =
                queueTicketRepository
                        .findByQueueSession(
                                queueSession
                        );
        long missedCount =
                allTickets.stream()
                        .filter(ticket ->
                                ticket.getStatus()
                                        == QueueTicketStatus.MISSED
                        )
                        .count();
        double averageWaitTime =
                allTickets.stream()

                        .filter(ticket ->
                                ticket.getEstimatedWaitTime()
                                        != null
                        )

                        .mapToInt(
                                QueueTicket::getEstimatedWaitTime
                        )

                        .average()

                        .orElse(0);
        List<QueueTicketViewDto>
                waitingTicketViews =
                new ArrayList<>();


        for (int i = 0;
             i < waitingTickets.size();
             i++) {

            QueueTicket ticket =
                    waitingTickets.get(i);

            QueueTicketViewDto dto =
                    new QueueTicketViewDto();

            dto.setTokenNumber(
                    ticket.getTokenNumber()
            );

            dto.setEstimatedWaitTime(
                    ticket.getEstimatedWaitTime()
            );

            dto.setPosition(i + 1);

            waitingTicketViews.add(dto);
        }

        long completedCount =
                queueTicketRepository
                        .countByQueueSessionAndStatus(
                                queueSession,
                                QueueTicketStatus.COMPLETED
                        );

        dashboard.setCurrentServingTicket(
                servingTicket
        );

        dashboard.setWaitingTickets(
                waitingTicketViews
        );

        dashboard.setWaitingCount(
                waitingTickets.size()
        );

        dashboard.setCompletedCount(
                completedCount
        );
        dashboard.setMissedCount(
                missedCount
        );

        dashboard.setAverageWaitTime(
                averageWaitTime
        );

        return dashboard;
    }


    @Override
    public QueueTrackingDto trackTicket(
            String tokenNumber) {

        QueueTicket ticket =
                queueTicketRepository
                        .findByTokenNumber(
                                tokenNumber
                        );

        if (ticket == null) {
            return null;
        }

        QueueSession session =
                ticket.getQueueSession();

        List<QueueTicket> waitingTickets =
                queueTicketRepository
                        .findByQueueSessionAndStatusOrderByJoinedAtAsc(
                                session,
                                QueueTicketStatus.WAITING
                        );

        int position = 0;

        for (int i = 0;
             i < waitingTickets.size();
             i++) {

            if (waitingTickets.get(i)
                    .getId()
                    .equals(ticket.getId())) {

                position = i + 1;
                break;
            }
        }

        QueueTicket servingTicket =
                queueTicketRepository
                        .findFirstByQueueSessionAndStatus(
                                session,
                                QueueTicketStatus.SERVING
                        );

        QueueTrackingDto dto =
                new QueueTrackingDto();

        dto.setTokenNumber(
                ticket.getTokenNumber()
        );

        dto.setStatus(
                ticket.getStatus()
        );

        dto.setPosition(position);

        if (ticket.getStatus()
                == QueueTicketStatus.COMPLETED

                ||

                ticket.getStatus()
                        == QueueTicketStatus.MISSED) {

            dto.setEstimatedWaitTime(0);

        } else {

            dto.setEstimatedWaitTime(
                    ticket.getEstimatedWaitTime()
            );
        }

        if (servingTicket != null) {

            dto.setCurrentServingToken(
                    servingTicket.getTokenNumber()
            );
        }

        return dto;
    }

    @Override
    public QueueTicket markTicketAsMissed(
            Long ticketId) {

        QueueTicket ticket =
                queueTicketRepository
                        .findById(ticketId)
                        .orElse(null);

        if (ticket == null) {
            return null;
        }

        if (ticket.getStatus()
                != QueueTicketStatus.CALLED) {

            return null;
        }

        ticket.setStatus(
                QueueTicketStatus.MISSED
        );

        return queueTicketRepository.save(ticket);
    }
}