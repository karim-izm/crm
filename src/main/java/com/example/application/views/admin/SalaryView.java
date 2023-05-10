package com.example.application.views.admin;

import com.example.application.models.User;
import com.example.application.service.ProdService;
import com.example.application.service.UserService;
import com.example.application.views.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;

import java.awt.*;
import java.time.LocalDate;

@Route(value = "salary" , layout = MainLayout.class)
public class SalaryView extends VerticalLayout {
    private final UserService userService;
    private final ProdService workTimeService;
    private final IntegerField workingDaysField;

    private Select<User> users;
    private H3 salaryLabel;
    private final Button calculateButton;

    public SalaryView(UserService userService, ProdService workTimeService) {
        this.userService = userService;
        this.workTimeService = workTimeService;
        salaryLabel = new H3("Salary : ");
        workingDaysField = new IntegerField("Jours De Travail");
        calculateButton = new Button("Calculate", event -> calculateSalary());

        users = new Select<>();
        users.setItems(userService.findAllUsers(""));
        users .setItemLabelGenerator(User::getFullName);
        users.setLabel("Agent : ");

        add(users , workingDaysField, calculateButton , salaryLabel);
    }

    private void calculateSalary() {
        User agent = users.getValue();
        Double totalHoursWorked =  0.0;
        double salary = ((agent.getSalary() / workingDaysField.getValue()) /8)*agent.getTotalHoursWorkedThisMonth();
        salaryLabel.setText("Salary : "+salary);

    }
}
