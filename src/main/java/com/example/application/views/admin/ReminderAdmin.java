package com.example.application.views.admin;

import com.example.application.models.Reminder;
import com.example.application.models.User;
import com.example.application.service.ReminderService;
import com.example.application.service.UserService;
import com.example.application.views.layout.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDateTime;

@Route(value = "/RappelesAdmin", layout = MainLayout.class)
@PageTitle("Rappeles")
public class ReminderAdmin extends VerticalLayout {
    private final ReminderService reminderService;
    private final UserService userService;

    public ReminderAdmin(ReminderService reminderService, UserService userService) {
        this.reminderService = reminderService;
        this.userService = userService;

        // Create UI components for adding reminders
        TextField messageField = new TextField("Message");
        Select<User> agentComboBox = new Select<>();
        Button addButton = new Button("Ajouter Rappele");

        // Set styles
        setPadding(true);
        setSpacing(true);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        messageField.setPlaceholder("Enter your message");
        messageField.setWidth("300px");

        agentComboBox.setPlaceholder("Select an agent");
        agentComboBox.setWidth("300px");

        addButton.getStyle().set("background-color", "#2196f3");
        addButton.getStyle().set("color", "#fff");

        // Populate agentComboBox
        agentComboBox.setItems(userService.findAllUsers(""));
        agentComboBox.setLabel("Agent");
        agentComboBox.setItemLabelGenerator(User::getFullName);

        // Handle addButton click event
        addButton.addClickListener(e -> {
            String message = messageField.getValue();
            User agent = agentComboBox.getValue();

            if (message != null && agent != null) {
                Notification.show("Reminder added successfully!");
                Reminder reminder = new Reminder();
                reminder.setMessage(message);
                reminder.setAgent(agent);
                reminder.setTimestamp(LocalDateTime.now());
                reminderService.createReminder(reminder);

                // Clear form fields
                messageField.clear();
                agentComboBox.clear();
            } else {
                Notification.show("Please enter a message and select an agent.");
            }
        });

        add(messageField, agentComboBox, addButton);
    }
}
