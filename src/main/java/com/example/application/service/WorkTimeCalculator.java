package com.example.application.service;

import com.example.application.models.User;
import com.example.application.models.WorkTime;
import com.example.application.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class WorkTimeCalculator {

    private UserRepository userRepository;
     ProdService prodService;

    public WorkTimeCalculator(UserRepository userRepository, ProdService prodService) {
        this.userRepository = userRepository;
        this.prodService = prodService;
    }

    public  double calculateTotalHoursWorked(User user) {
        double totalHoursWorked = 0.0;
        LocalDateTime now = LocalDateTime.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        List<WorkTime> workTimes = prodService.findByAgent(user);
        for (WorkTime workTime : workTimes) {
            LocalDateTime startTime = workTime.getStartTime();
            if (startTime.getMonthValue() == currentMonth && startTime.getYear() == currentYear) {
                Duration totalWorked = parseDuration(workTime.getRealTimeWorked());
                totalHoursWorked += totalWorked.toHours();
            }
        }

        return totalHoursWorked;
    }

    private static Duration parseDuration(String durationString) {
        String[] parts = durationString.split("[,\\s]+");

        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[2]);
        int seconds = Integer.parseInt(parts[4]);

        return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }





}
