package com.example.application.views.User;

import com.example.application.models.User;
import com.example.application.models.WorkTime;
import com.example.application.service.ProdService;
import com.example.application.service.VenteService;
import com.example.application.views.layout.AgentLayout;
import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Timer;

@Route(value = "AgentDashboard" , layout = AgentLayout.class)

    public class DashboardAgent extends VerticalLayout {

        private Label timerLabel;
        private WorkTime workTime;
    private LocalTime startTime;

        private VenteService venteService;
        private ProdService prodService;
        private User currentUser;
        private Timer timer;
    private Button startWorkButton;
    private Button stopWorkButton;
    private Button pauseButton;
    private Button resumeButton;
    private  final UserInfo userInfo;

    private LocalDateTime pauseStartTime;

        public DashboardAgent(VenteService venteService, ProdService prodService) {
            this.venteService = venteService;
            this.prodService = prodService;
            currentUser = (User) VaadinSession.getCurrent().getAttribute("user");

            userInfo=new UserInfo(currentUser.getFullName(),currentUser.getFullName());
            startWorkButton = new Button("Start Working", e -> startWorking());
            stopWorkButton = new Button("Stop Working", e -> stopWorking());
            stopWorkButton.setEnabled(false);
            pauseButton = new Button("Pause" , e->pauseWorkTime());
            pauseButton.setEnabled(false);
             resumeButton = new Button("Resume", e->resumeWorkTime());
             resumeButton.setEnabled(false);
            timerLabel = new Label("Vous etes inactif");
            timerLabel.setId("timerLabel");

            addClassName("dashboard");
            setPadding(true);
            setSpacing(true);
            setDefaultHorizontalComponentAlignment(Alignment.CENTER);
            HorizontalLayout hl = new HorizontalLayout( getChatLayout());
            add(
                    getHeader(),

                    new HorizontalLayout(
                            getVenteStats()
                    ),
                    startWorkButton,
                    stopWorkButton,
                    timerLabel,
                    pauseButton,
                    resumeButton,
hl
            );

        }

    private Component getChatLayout() {
        var chatLayout=new VerticalLayout();

        var messageList= new CollaborationMessageList(userInfo,"chat");
        var messageInput = new CollaborationMessageInput(messageList);
        chatLayout.add(new H2("chat"),messageList,messageInput);
        chatLayout.expand(messageList);
        chatLayout.setHeightFull();
        chatLayout.setWidth(null);
        chatLayout.addClassName("bg-contrast-5");
        return chatLayout;
    }

    private void startWorking() {
        workTime = prodService.startWorking(currentUser);
        Notification.show("Production démarrée!");
        timerLabel.setText("Vous êtes en production...");
        startWorkButton.setEnabled(false);
        stopWorkButton.setEnabled(true);
        pauseButton.setEnabled(true);
        resumeButton.setEnabled(false);

    }

    private void stopWorking() {
        prodService.stopWorking(workTime);

        pauseButton.setEnabled(false);
        startWorkButton.setEnabled(true);
        stopWorkButton.setEnabled(false);
        timerLabel.setText("Vous êtes inactif...");
        Notification.show("Production terminée");
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
            title.getStyle().set("color", "#FFFFFF");
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
            DashboardAgent.Card card = new DashboardAgent.Card("Votre Ventes Total ", String.valueOf(venteService.countVentesByAgent(currentUser)));
            return new Div(card);
        }



        public class Card extends VerticalLayout {
            private final H3 title;
            private final Span value;

            public Card(String title, String value) {
                this.title = new H3(title);
                this.value = new Span(value);

                setClassName("card");
                setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

                add(this.title, this.value);
            }
        }
    }


