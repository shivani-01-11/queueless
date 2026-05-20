package com.queueless.dto;

import com.queueless.entity.QueueTicket;

import java.util.List;

public class QueueDashboardDto {

    private QueueTicket currentServingTicket;

    private List<QueueTicketViewDto> waitingTickets;

    private long waitingCount;

    private long completedCount;

    public QueueTicket getCurrentServingTicket() {
        return currentServingTicket;
    }

    public void setCurrentServingTicket(
            QueueTicket currentServingTicket) {

        this.currentServingTicket =
                currentServingTicket;
    }

    public List<QueueTicketViewDto> getWaitingTickets() {
        return waitingTickets;
    }

    public void setWaitingTickets(
            List<QueueTicketViewDto> waitingTickets) {

        this.waitingTickets = waitingTickets;
    }

    public long getWaitingCount() {
        return waitingCount;
    }

    public void setWaitingCount(long waitingCount) {
        this.waitingCount = waitingCount;
    }

    public long getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(long completedCount) {
        this.completedCount = completedCount;
    }
}