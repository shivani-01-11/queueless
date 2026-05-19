package com.queueless.entity;

import com.queueless.enums.QueueTicketStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "queue_tickets")
public class QueueTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tokenNumber;

    @Enumerated(EnumType.STRING)
    private QueueTicketStatus status;

    private LocalDateTime joinedAt;

    private LocalDateTime calledAt;

    private LocalDateTime serviceStartedAt;

    private LocalDateTime completedAt;

    private Integer estimatedWaitTime;

    private Integer actualWaitTime;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User customer;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "queue_session_id")
    private QueueSession queueSession;

    public QueueTicket() {
    }

    public Long getId() {
        return id;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public QueueTicketStatus getStatus() {
        return status;
    }

    public void setStatus(QueueTicketStatus status) {
        this.status = status;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LocalDateTime getCalledAt() {
        return calledAt;
    }

    public void setCalledAt(LocalDateTime calledAt) {
        this.calledAt = calledAt;
    }

    public LocalDateTime getServiceStartedAt() {
        return serviceStartedAt;
    }

    public void setServiceStartedAt(LocalDateTime serviceStartedAt) {
        this.serviceStartedAt = serviceStartedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Integer getEstimatedWaitTime() {
        return estimatedWaitTime;
    }

    public void setEstimatedWaitTime(Integer estimatedWaitTime) {
        this.estimatedWaitTime = estimatedWaitTime;
    }

    public Integer getActualWaitTime() {
        return actualWaitTime;
    }

    public void setActualWaitTime(Integer actualWaitTime) {
        this.actualWaitTime = actualWaitTime;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public QueueSession getQueueSession() {
        return queueSession;
    }

    public void setQueueSession(QueueSession queueSession) {
        this.queueSession = queueSession;
    }
}