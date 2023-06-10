package com.example.application.repository;

import com.example.application.models.Reminder;
import com.example.application.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder , Integer> {
    public List<Reminder> findByAgentOrderByTimestampDesc(User agent);
}
