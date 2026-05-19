package com.queueless.repository;

import com.queueless.entity.QueueSession;
import com.queueless.entity.QueueTicket;
import com.queueless.enums.QueueTicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueTicketRepository
        extends JpaRepository<QueueTicket, Long> {
    QueueTicket findFirstByQueueSessionAndStatusOrderByJoinedAtAsc(
            QueueSession queueSession,
            QueueTicketStatus status
    );
}