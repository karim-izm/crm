package com.example.application.service;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationTask extends TimerTask {

    private static final LocalTime TIME_TO_NOTIFY = LocalTime.of(14, 21);

    private final VaadinSession session;

    public NotificationTask(VaadinSession session) {
        this.session = session;
    }

    @Override
    public void run() {
        if (LocalTime.now().equals(TIME_TO_NOTIFY)) {
            Notification.show("It's 2pm!");
        }
    }
}