package com.queueless.entity;

import com.queueless.enums.QueueSessionStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "queue_sessions")
public class QueueSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate sessionDate;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private QueueSessionStatus status;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "business_center_id")
    private BusinessCenter businessCenter;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;

    @OneToMany(mappedBy = "queueSession")
    private List<QueueTicket> tickets;

    public QueueSession() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public QueueSessionStatus getStatus() {
        return status;
    }

    public void setStatus(QueueSessionStatus status) {
        this.status = status;
    }

    public BusinessCenter getBusinessCenter() {
        return businessCenter;
    }

    public void setBusinessCenter(BusinessCenter businessCenter) {
        this.businessCenter = businessCenter;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
}