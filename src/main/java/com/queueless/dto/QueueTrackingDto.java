package com.queueless.dto;

import com.queueless.enums.QueueTicketStatus;

public class QueueTrackingDto {

    private String tokenNumber;

    private QueueTicketStatus status;

    private int position;

    private Integer estimatedWaitTime;

    private String currentServingToken;

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public QueueTicketStatus getStatus() {
        return status;
    }

    public void setStatus(
            QueueTicketStatus status) {

        this.status = status;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Integer getEstimatedWaitTime() {
        return estimatedWaitTime;
    }

    public void setEstimatedWaitTime(
            Integer estimatedWaitTime) {

        this.estimatedWaitTime =
                estimatedWaitTime;
    }

    public String getCurrentServingToken() {
        return currentServingToken;
    }

    public void setCurrentServingToken(
            String currentServingToken) {

        this.currentServingToken =
                currentServingToken;
    }
}