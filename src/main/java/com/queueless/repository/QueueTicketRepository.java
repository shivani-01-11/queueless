package com.queueless.repository;

import com.queueless.entity.QueueSession;
import com.queueless.entity.QueueTicket;
import com.queueless.enums.QueueTicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueueTicketRepository
        extends JpaRepository<QueueTicket, Long> {
    QueueTicket findFirstByQueueSessionAndStatusOrderByJoinedAtAsc(
            QueueSession queueSession,
            QueueTicketStatus status
    );

    long countByQueueSessionAndStatus(
            QueueSession queueSession,
            QueueTicketStatus status
    );

    List<QueueTicket> findByQueueSessionAndStatusOrderByJoinedAtAsc(
            QueueSession queueSession,
            QueueTicketStatus status
    );

    long countByQueueSessionAndStatusIn(
            QueueSession queueSession,
            List<QueueTicketStatus> statuses
    );

    QueueTicket findFirstByQueueSessionAndStatus(
            QueueSession queueSession,
            QueueTicketStatus status
    );

    QueueTicket findByTokenNumber(
            String tokenNumber
    );

    boolean existsByQueueSessionAndStatus(
            QueueSession queueSession,
            QueueTicketStatus status
    );
}