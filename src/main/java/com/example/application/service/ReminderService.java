package com.example.application.service;

import com.example.application.models.Reminder;
import com.example.application.models.User;
import com.example.application.repository.ReminderRepository;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReminderService {
    private final ReminderRepository reminderRepository;


    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public void createReminder(Reminder reminder) {
        reminderRepository.save(reminder);
        sendNotificationToAgent(reminder);
    }

    public List<Reminder> getRemindersForAgent(User agent) {
        // Retrieve reminders for the specified agent from the database
        return reminderRepository.findByAgentOrderByTimestampDesc(agent);
    }

    // Other methods for updating and deleting reminders

    private void sendNotificationToAgent(Reminder reminder) {
        Notification.show("New Reminder: " + reminder.getMessage());
    }
}
