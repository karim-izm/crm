package com.example.application.views.User;

import com.example.application.models.Reminder;
import com.example.application.models.User;
import com.example.application.models.WorkTime;
import com.example.application.service.ProdService;
import com.example.application.service.ReminderService;
import com.example.application.views.layout.AgentLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "/Rappeles" , layout = AgentLayout.class)
public class Reminders extends VerticalLayout {
    User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
    Grid<Reminder> grid = new Grid(Reminder.class);
    ReminderService reminderService ;



    public Reminders(ReminderService reminderService) {
        this.reminderService = reminderService;
        setSizeFull();
        configureGrid();
        add(grid);
    }

    private void configureGrid() {
        grid.setItems(reminderService.getRemindersForAgent(currentUser));
        grid.setColumns("message" , "timestamp");
    }
}
