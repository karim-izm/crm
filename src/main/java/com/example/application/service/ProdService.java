package com.example.application.service;

import com.example.application.models.User;
import com.example.application.models.WorkTime;
import com.example.application.repository.ProdRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdService {

    private ProdRepository prodRepository;

    public ProdService(ProdRepository prodRepository) {
        this.prodRepository = prodRepository;
    }


    public List<WorkTime> getAll() {
        return prodRepository.findAll();
    }

    public WorkTime startWorking(User user) {
        WorkTime workTime = new WorkTime();
        workTime.setAgent(user);
        workTime.startWork();
        prodRepository.save(workTime);
        return workTime;
    }

    public void stopWorking(WorkTime workTime) {
        workTime.stopWork();

        Duration totalWorked = workTime.getTotalWorked();
        Duration totalPause = workTime.getTotalPause();
        Duration realTimeWorked = totalWorked.minus(totalPause);

        String realTimeWorkedString = formatDuration(realTimeWorked);
        workTime.setRealTimeWorked(realTimeWorkedString);

        workTime.setTotalWorkedTime(formatDuration(totalWorked));
        prodRepository.save(workTime);
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
}
