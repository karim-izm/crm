package com.example.application.service;

import com.example.application.models.User;
import com.example.application.models.Vente;
import com.example.application.models.WorkTime;
import com.example.application.repository.ProdRepository;
import com.example.application.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ProdService {

    private ProdRepository prodRepository;
    private UserRepository userRepository;
    public ProdService(ProdRepository prodRepository , UserRepository userRepository) {
        this.prodRepository = prodRepository;
        this.userRepository = userRepository;
    }


    public List<WorkTime> getAll() {
        return prodRepository.findAll();
    }

    public WorkTime startWorking(User user) {
        WorkTime workTime = new WorkTime();
        user.setEnProd(true);
        userRepository.save(user);
        workTime.setAgent(user);
        workTime.startWork();
        prodRepository.save(workTime);
        return workTime;
    }

    public void stopWorking(WorkTime workTime) {
        User agent = workTime.getAgent();
        workTime.getAgent().setEnProd(false);
        Duration realTimeWorked;
        workTime.stopWork();
        workTime.setInProd(false);
        userRepository.save(agent);
        Duration totalWorked = workTime.getTotalWorked();
        Duration totalPause = workTime.getTotalPause();
        if(totalPause == null){
            realTimeWorked = totalWorked;
        }
        else{
            realTimeWorked = totalWorked.minus(totalPause);
        }
        String realTimeWorkedString = formatDuration(realTimeWorked);
        workTime.setRealTimeWorked(realTimeWorkedString);

        workTime.setTotalWorkedTime(formatDuration(totalWorked));
        prodRepository.save(workTime);
    }

    public List<WorkTime> findByAgent(User agent){
        return prodRepository.findByAgent(agent);
    }

    public void addPause(WorkTime workTime, Duration pauseDuration) {
        if (pauseDuration != null) {
            workTime.setTotalPause(pauseDuration);
            String pauseTime = formatDuration(pauseDuration);
            workTime.setTotalPauseTime(pauseTime);
        }
        prodRepository.save(workTime);
    }


    // Helper method to format duration as "x hours, x minutes, x seconds"
    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return hours + " hours, " + minutes + " minutes, " + seconds + " seconds";
    }

    public long countUsersInProd() {
        return prodRepository.countUsersInProd();
    }

    public List<WorkTime> getFilteredWorkTimesAll(String filterOption) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        LocalDate today = LocalDate.now();

        if (filterOption.equals("Ajourdhui")) {
            startDateTime = today.atStartOfDay();
            endDateTime = today.atTime(23, 59, 59);
        } else if (filterOption.equals("Hier")) {
            LocalDate yesterday = today.minusDays(1);
            startDateTime = yesterday.atStartOfDay();
            endDateTime = yesterday.atTime(23, 59, 59);
        } else if (filterOption.equals("Cette Semaine")) {
            LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
            LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
            startDateTime = startOfWeek.atStartOfDay();
            endDateTime = endOfWeek.atTime(23, 59, 59);
        } else if (filterOption.equals("Ce mois")) {
            LocalDate startOfMonth = today.withDayOfMonth(1);
            LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
            startDateTime = startOfMonth.atStartOfDay();
            endDateTime = endOfMonth.atTime(23, 59, 59);
        } else {
            // Handle other filter options if needed
            return Collections.emptyList();
        }

        return prodRepository.findByDateBetween(startDateTime, endDateTime);
    }


}
