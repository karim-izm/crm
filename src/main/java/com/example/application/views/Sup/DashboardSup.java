package com.example.application.views.Sup;

;

import com.example.application.models.User;
import com.example.application.models.WorkTime;
import com.example.application.service.MyApplication;
import com.example.application.service.ProdService;
import com.example.application.service.UserService;
import com.example.application.service.VenteService;
import com.example.application.views.admin.dashboard.Dashboard;
import com.example.application.views.layout.AgentLayout;
import com.example.application.views.layout.SupLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.hibernate.jdbc.Work;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@Route(value = "SupDashboard" , layout = SupLayout.class)

public class DashboardSup extends VerticalLayout {

    private Label timerLabel;
    private WorkTime workTime;
    private LocalTime startTime;

    private Grid<String> connectedUsersGrid;

    private VenteService venteService;
    private ProdService prodService;
    private User currentUser;
    private Button startWorkButton;
    private Button stopWorkButton;
    private Button pauseButton;
    private Button resumeButton;

    private LocalDateTime pauseStartTime;

    public DashboardSup(VenteService venteService, ProdService prodService) {
        this.venteService = venteService;
        this.prodService = prodService;
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");

        String fontName = "Oswald";
        String fontPath = "/fonts/Oswald-VariableFont_wght.ttf";

        getElement().getStyle().set("font-family", fontName);
        getElement().getStyle().set("src", "url(" + fontPath + ")");

        connectedUsersGrid = new Grid<>();
        connectedUsersGrid.setWidth("300px");
        connectedUsersGrid.setHeight("200px");
        connectedUsersGrid.addColumn(String::valueOf).setHeader("Agents");
        connectedUsersGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        connectedUsersGrid.setSelectionMode(Grid.SelectionMode.NONE);
        connectedUsersGrid.getElement().executeJs(
                "this.$.table.querySelectorAll('td').forEach(function(td) { td.style.justifyContent = 'center'; })");
        connectedUsersGrid.getElement()
                .getStyle()
                .set("border-radius", "0.7rem")
                .set("background-color", "#ffffff");
        connectedUsersGrid.getElement()
                .executeJs("this.$.header.style.justifyContent = 'center';");
        connectedUsersGrid.getElement().executeJs("this.$.header.style.alignItems = 'center';");

        VerticalLayout vl = new VerticalLayout(new H3("Agents connecté : ") , connectedUsersGrid);
        startWorkButton = new Button("Démarrer La Prod", e -> startWorking());
        stopWorkButton = new Button("Arrêter La Prod", e -> stopWorking());
        stopWorkButton.setEnabled(false);
        pauseButton = new Button("Pause" , e->pauseWorkTime());
        pauseButton.setEnabled(false);
        resumeButton = new Button("Reprise", e->resumeWorkTime());
        resumeButton.setEnabled(false);
        timerLabel = new Label("Vous etes inactif");
        timerLabel.setId("timerLabel");
        addClassName("dashboard");
        H4 welcome = new H4("Bienvenue : " + currentUser.getFullName());
        welcome.getStyle()
                .set("margin", "0")
                .set("color", "#333333")
                .set("font-weight", "bold")
                .set("font-size", "1.2rem")
                .set("padding", "1rem").set("text-align" , "left");

        Div welcomeContainer = new Div(welcome);
        welcomeContainer.getStyle()
                .set("display", "flex")
                .set("align-items", "left")
                .set("margin-left", "1rem");
        setPadding(true);
        setSpacing(true);
        getStyle().set("background-color", "#ffffff");
        getStyle().set("padding", "20px");

        startWorkButton.getStyle().set("border", "none");
        startWorkButton.getStyle().set("padding", "10px 20px");
        startWorkButton.getStyle().set("text-align", "center");
        startWorkButton.getStyle().set("text-decoration", "none");
        startWorkButton.getStyle().set("display", "inline-block");
        startWorkButton.getStyle().set("font-size", "16px");
        startWorkButton.getStyle().set("margin", "4px 2px");

        stopWorkButton.getStyle().set("border", "none");
        stopWorkButton.getStyle().set("padding", "10px 20px");
        stopWorkButton.getStyle().set("text-align", "center");
        stopWorkButton.getStyle().set("text-decoration", "none");
        stopWorkButton.getStyle().set("display", "inline-block");
        stopWorkButton.getStyle().set("font-size", "16px");
        stopWorkButton.getStyle().set("margin", "4px 2px");
        stopWorkButton.getStyle().set("cursor", "pointer");

        pauseButton.getStyle().set("border", "none");
        pauseButton.getStyle().set("padding", "10px 20px");
        pauseButton.getStyle().set("text-align", "center");
        pauseButton.getStyle().set("text-decoration", "none");
        pauseButton.getStyle().set("display", "inline-block");
        pauseButton.getStyle().set("font-size", "16px");
        pauseButton.getStyle().set("margin", "4px 2px");
        pauseButton.getStyle().set("cursor", "pointer");


        resumeButton.getStyle().set("border", "none");
        resumeButton.getStyle().set("padding", "10px 20px");
        resumeButton.getStyle().set("text-align", "center");
        resumeButton.getStyle().set("text-decoration", "none");
        resumeButton.getStyle().set("display", "inline-block");
        resumeButton.getStyle().set("font-size", "16px");
        resumeButton.getStyle().set("margin", "4px 2px");
        resumeButton.getStyle().set("cursor", "pointer");

        startWorkButton.getStyle().set("margin-right", "10px");
        stopWorkButton.getStyle().set("margin-left", "10px");

        HorizontalLayout buttonLayout = new HorizontalLayout(startWorkButton, stopWorkButton);
        buttonLayout.setSpacing(true);
        buttonLayout.getStyle().set("margin-bottom", "20px");

        HorizontalLayout timerLayout = new HorizontalLayout(timerLabel);
        timerLayout.setSpacing(true);
        timerLayout.getStyle().set("margin-bottom", "20px");

        HorizontalLayout actionLayout = new HorizontalLayout(pauseButton, resumeButton);
        actionLayout.setSpacing(true);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);





        add(
                getHeader(),
                welcomeContainer,
                new HorizontalLayout(
                        getVenteStats() , getThisMonthVenteStats()
                ),
                vl,
                buttonLayout,
                timerLayout,
                actionLayout
        );

        updateConnectedUsers();


        Boolean workStarted = (Boolean) VaadinSession.getCurrent().getAttribute("workStarted");
        Boolean workPaused = (Boolean) VaadinSession.getCurrent().getAttribute("workPaused");

        if (workStarted != null && workStarted) {
            // Work was started before, update the button states accordingly
            startWorkButton.setEnabled(false);
            stopWorkButton.setEnabled(true);
            pauseButton.setEnabled(workPaused == false);
            resumeButton.setEnabled(workPaused);
            timerLabel.setText("En production ....");
        }

        if (workPaused != null && workPaused) {
            startWorkButton.setEnabled(false);
            stopWorkButton.setEnabled(true);
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(true);
            timerLabel.setText("En Pause");
        }

        workTime = (WorkTime) VaadinSession.getCurrent().getAttribute("workTime");
    }

    private void startWorking() {
        workTime = prodService.startWorking(currentUser);
        Notification.show("Production démarrée!");
        timerLabel.setText("Vous êtes en production...");
        startWorkButton.setEnabled(false);
        stopWorkButton.setEnabled(true);
        pauseButton.setEnabled(true);
        resumeButton.setEnabled(false);

        VaadinSession.getCurrent().setAttribute("workStarted", true);
        VaadinSession.getCurrent().setAttribute("workPaused", false);
        VaadinSession.getCurrent().setAttribute("workTime", workTime);

    }

    private void stopWorking() {
        prodService.stopWorking(workTime);
        pauseButton.setEnabled(false);
        startWorkButton.setEnabled(true);
        stopWorkButton.setEnabled(false);
        timerLabel.setText("Vous êtes inactif...");
        Notification.show("Production terminée");

        VaadinSession.getCurrent().setAttribute("workStarted", false);
        VaadinSession.getCurrent().setAttribute("workPaused", false);
    }

    private void pauseWorkTime() {
        if (workTime != null) {
            Duration currentPauseDuration = calculatePauseDuration();

            // Add the pause to the current work time entry
            prodService.addPause(workTime, currentPauseDuration);
            currentPauseDuration = currentPauseDuration.plus(currentPauseDuration);
            workTime.setPaused(true);
            timerLabel.setText("Vous êtes en pause...");
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(true);
            stopWorkButton.setEnabled(false);
            Notification.show("En pause !");
            VaadinSession.getCurrent().setAttribute("workPaused", true);
            VaadinSession.getCurrent().setAttribute("workStarted", true);


        }
    }

    private void resumeWorkTime() {
        if (workTime != null) {
            workTime.setPaused(false);
            timerLabel.setText("Vous êtes en production...");
            pauseButton.setEnabled(true);
            resumeButton.setEnabled(false);
            stopWorkButton.setEnabled(true);
            Notification.show("Reprise !");
            VaadinSession.getCurrent().setAttribute("workPaused", false);
        }
    }

    private Duration calculatePauseDuration() {
        // Get the current time
        LocalDateTime currentTime = LocalDateTime.now();

        // Calculate the pause duration based on the start time and current time
        Duration pauseDuration = Duration.between(workTime.getStartTime(), currentTime);

        // Convert pause duration to a positive value
        pauseDuration = pauseDuration.abs();

        // Return the pause duration
        return pauseDuration;
    }


    private Duration getElapsedTime() {
        if (pauseStartTime != null) {
            LocalDateTime now = LocalDateTime.now();
            return Duration.between(pauseStartTime, now);
        }
        return Duration.ZERO;
    }



    private Component getHeader() {
        H3 title = new H3("Dashboard");
        title.getStyle().set("margin", "0");
        title.getStyle().set("color", "#000000");
        title.getStyle().set("font-weight", "bold");

        HorizontalLayout headerLayout = new HorizontalLayout(title);
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        headerLayout.setSpacing(true);
        headerLayout.setWidth("100%");
        headerLayout.getStyle().set("border-bottom", "1px solid #e6e6e6");
        headerLayout.setPadding(true);
        headerLayout.setMargin(false);

        return headerLayout;
    }
    private Component getVenteStats() {
        Card card = new Card("Ventes Total", String.valueOf(venteService.countVentesByAgent(currentUser)));
        card.getStyle()
                .set("box-shadow", "0 0.125rem 0.25rem rgba(0, 0, 0, 0.1)")
                .set("border-radius", "0.5rem")
                .set("padding", "1.5rem")
                .set("width", "auto")
                .set("margin", "1rem");
        card.title.getStyle()
                .set("margin", "0")
                .set("font-weight", "bold")
                .set("font-size", "1.2rem");
        card.value.getStyle()
                .set("margin", "0")
                .set("font-size", "2rem");

        return card;
    }

    private void updateConnectedUsers() {
        Set<String> connectedUsers = MyApplication.getConnectedUsers();
        connectedUsersGrid.setItems(connectedUsers);
    }
    private Component getThisMonthVenteStats() {
        Card card = new Card("Ventes Ce Mois", String.valueOf(venteService.countVentesByAgent(currentUser)));
        card.getStyle()
                .set("box-shadow", "0 0.125rem 0.25rem rgba(0, 0, 0, 0.1)")
                .set("border-radius", "0.5rem")
                .set("width", "auto")
                .set("margin", "1rem");
        card.title.getStyle()
                .set("margin", "0")
                .set("font-weight", "bold")
                .set("font-size", "1.2rem");
        card.value.getStyle()
                .set("margin", "0")
                .set("color", "#666666")
                .set("font-size", "2rem");

        return card;
    }



    public class Card extends VerticalLayout {
        private final H3 title;
        private final Span value;

        public Card(String title, String value) {
            this.title = new H3(title);
            this.value = new Span(value);

            setClassName("card");
            getElement().getStyle().set("padding", "1rem");
            getElement().getStyle().set("border-radius", "0.5rem");
            getElement().getStyle().set("box-shadow", "0 0.125rem 0.25rem rgba(0, 0, 0, 0.075)");
            getElement().getStyle().set("background-color", "#ffffff");
            setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

            // Customize the title and value elements
            this.title.getElement().getStyle()
                    .set("margin", "0")
                    .set("color", "#333333")
                    .set("font-weight", "bold")
                    .set("font-size", "1.2rem")
                    .set("display", "flex")
                    .set("align-items", "center");

            this.value.getElement().getStyle()
                    .set("margin", "0")
                    .set("color", "#666666")
                    .set("font-size", "2rem")
                    .set("display", "flex")
                    .set("align-items", "center");


            // Add the title and value to the card
            add(this.title, this.value);
        }
    }
}


