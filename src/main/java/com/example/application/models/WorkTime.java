package com.example.application.models;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class WorkTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agent_id")
    private User agent;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration totalWorked;
    private Duration totalPause;

    private String totalWorkedTime;
    private String totalPauseTime;

    private String realTimeWorked;

    private boolean paused;

    private boolean inProd;

    public void startWork() {
        startTime = LocalDateTime.now();
        totalWorked = Duration.ZERO;
    }

    public void stopWork() {
        if (startTime != null && endTime == null) {
            endTime = LocalDateTime.now();
            totalWorked = Duration.between(startTime, endTime);
        }
    }

    public void addPause(Duration pauseDuration) {
        if (startTime != null && endTime == null) {
            totalWorked = totalWorked.minus(pauseDuration);
            pauseDuration = pauseDuration.plus(pauseDuration);
        }
    }

    public String getRealTimeWorked() {
        return realTimeWorked;
    }

    public void setRealTimeWorked(String realTimeWorked) {
        this.realTimeWorked = realTimeWorked;
    }

    public Duration getTotalWorked() {
        return totalWorked;
    }

    public void setTotalWorked(Duration totalWorked) {
        this.totalWorked = totalWorked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
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

    public String getTotalWorkedTime() {
        return totalWorkedTime;
    }

    public void setTotalWorkedTime(String totalWorkedTime) {
        this.totalWorkedTime = totalWorkedTime;
    }

    public String getTotalPauseTime() {
        return totalPauseTime;
    }

    public boolean isInProd() {
        return inProd;
    }

    public void setInProd(boolean inProd) {
        this.inProd = inProd;
    }

    public void setTotalPauseTime(String totalPauseTime) {
        this.totalPauseTime = totalPauseTime;
    }

    public Duration getTotalPause() {
        return totalPause;
    }

    public void setTotalPause(Duration totalPause) {
        this.totalPause = totalPause;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
