package com.example.application.views.admin;

import com.example.application.models.User;
import com.example.application.repository.UserRepository;
import com.example.application.service.ProdService;
import com.example.application.service.UserService;
import com.example.application.service.WorkTimeCalculator;
import com.example.application.views.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;

@Route(value = "salary" , layout = MainLayout.class)
public class SalaryView extends VerticalLayout {
    private final UserService userService;
    private final ProdService workTimeService;
    private final IntegerField workingDaysField;
    private UserRepository userRepository;
    private Select<User> users;
    private H3 salaryLabel;
    private final Button calculateButton;

    public SalaryView(UserService userService, ProdService workTimeService, UserRepository userRepository) {
        this.userService = userService;
        this.workTimeService = workTimeService;
        this.userRepository = userRepository;
        salaryLabel = new H3("Salaire Mensuel : ");
        workingDaysField = new IntegerField("Jours De Travail");
        calculateButton = new Button("Calculate", event -> calculateSalary());
        users = new Select<>();
        users.setPlaceholder("Agent a calculer salaire");
        users.setItems(userService.findAllUsers(""));
        users .setItemLabelGenerator(User::getFullName);
        users.setLabel("Agent : ");

        workingDaysField.setPlaceholder("Jours de travail ce mois ci");

        //styling :
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(true);
        setPadding(true);
        setWidth("100%");
        users.getStyle().set("width", "300px");
        workingDaysField.getStyle().set("width", "300px");
        calculateButton.getStyle().set("margin-top", "20px");
        salaryLabel.getStyle().set("margin-top", "20px").set("font-size", "1.5rem");
        addClassName("salary-view");
        getElement().getStyle().set("font-family", "Arial, sans-serif");

        add(new H3("Calculateur de salaire mensuel : ") , users , workingDaysField, calculateButton , salaryLabel);
    }

    private void calculateSalary() {
        User agent = users.getValue();
        WorkTimeCalculator workTimeCalculator = new WorkTimeCalculator(userRepository , workTimeService);
        agent.setTotalHoursWorkedThisMonth(workTimeCalculator.calculateTotalHoursWorked(agent));
        userRepository.save(agent);
        double salary = ((agent.getSalary() / workingDaysField.getValue()) /8)*agent.getTotalHoursWorkedThisMonth();
        salaryLabel.setText("Salary : "+salary +" DH");

    }
}
