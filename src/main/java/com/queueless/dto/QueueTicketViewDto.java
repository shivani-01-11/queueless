package com.queueless.dto;

public class QueueTicketViewDto {

    private String tokenNumber;

    private Integer estimatedWaitTime;

    private int position;

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public Integer getEstimatedWaitTime() {
        return estimatedWaitTime;
    }

    public void setEstimatedWaitTime(
            Integer estimatedWaitTime) {

        this.estimatedWaitTime =
                estimatedWaitTime;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}