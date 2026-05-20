package com.queueless.service;

import com.queueless.entity.QueueSession;
import com.queueless.entity.QueueTicket;
import com.queueless.entity.User;

public interface QueueTicketService {

    QueueTicket createTicket(User customer,
                             QueueSession queueSession);

    QueueTicket callNextTicket(
            QueueSession queueSession
    );

    QueueTicket startService(Long ticketId);

    QueueTicket completeService(Long ticketId);
}