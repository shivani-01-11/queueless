package com.queueless.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "service_types")
public class ServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;

    private Integer averageServiceTime;

    @ManyToOne
    @JoinColumn(name = "business_center_id")
    private BusinessCenter businessCenter;

    @OneToMany(mappedBy = "serviceType")
    private List<QueueSession> queueSessions;

    public ServiceType() {
    }

    public ServiceType(String serviceName,
                       Integer averageServiceTime,
                       BusinessCenter businessCenter) {

        this.serviceName = serviceName;
        this.averageServiceTime = averageServiceTime;
        this.businessCenter = businessCenter;
    }

    public Long getId() {
        return id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getAverageServiceTime() {
        return averageServiceTime;
    }

    public void setAverageServiceTime(Integer averageServiceTime) {
        this.averageServiceTime = averageServiceTime;
    }

    public BusinessCenter getBusinessCenter() {
        return businessCenter;
    }

    public void setBusinessCenter(BusinessCenter businessCenter) {
        this.businessCenter = businessCenter;
    }
}