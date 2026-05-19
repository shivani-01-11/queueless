package com.queueless.repository;

import com.queueless.entity.QueueSession;
import com.queueless.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueSessionRepository
        extends JpaRepository<QueueSession, Long> {

    QueueSession findTopByOrderByIdDesc();


}