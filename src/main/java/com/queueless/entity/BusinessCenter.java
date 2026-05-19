package com.queueless.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "business_centers")
public class BusinessCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String businessName;

    private String address;

    private String city;

    private String phone;

    @OneToMany(mappedBy = "businessCenter")
    private List<ServiceType> serviceTypes;

    @OneToMany(mappedBy = "businessCenter")
    private List<QueueSession> queueSessions;

    public BusinessCenter() {
    }

    public BusinessCenter(String businessName,
                          String address,
                          String city,
                          String phone) {

        this.businessName = businessName;
        this.address = address;
        this.city = city;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}