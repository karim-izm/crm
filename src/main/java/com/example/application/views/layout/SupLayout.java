package com.example.application.views.layout;

import com.example.application.models.Reminder;
import com.example.application.models.User;
import com.example.application.service.MyApplication;
import com.example.application.service.NotificationTask;
import com.example.application.service.ReminderService;
import com.example.application.views.Sup.ProdSup;
import com.example.application.views.Sup.VenteSup;
import com.example.application.views.User.*;
import com.example.application.views.User.ChatSup;
import com.example.application.views.admin.Production;
import com.example.application.views.auth.Login;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.Timer;

public class SupLayout extends AppLayout implements RouterLayout , BeforeEnterObserver {
    private final Timer timer = new Timer();
    private ReminderService reminderService;
    private VerticalLayout notificationDropdown;
    User currentUser;

    public SupLayout(ReminderService reminderService) {
        this.reminderService = reminderService;
        timer.scheduleAtFixedRate(new NotificationTask(VaadinSession.getCurrent()), 0, 1000);
        createHeader();
        createSideBar();
    }

    private void createHeader() {
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        H4 header = new H4("CRM STORACTIVE | Superviseur");
        Button logout = new Button("Logout");
        Button notificationButton = createNotificationButton();
        HorizontalLayout hl = new HorizontalLayout(new DrawerToggle(), header, notificationButton, logout);
        hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        hl.expand(header);
        hl.setWidthFull();
        hl.addClassNames( LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);
        addToNavbar(hl);
        logout.addClickListener(buttonClickEvent -> logout());
    }

    private Button createNotificationButton() {
        Button button = new Button(new Icon(VaadinIcon.BELL));
        button.addClassName("notification-button");

        Dialog notificationDialog = createNotificationDialog();

        button.addClickListener(e -> notificationDialog.open());

        return button;
    }

    private Dialog createNotificationDialog() {
        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);
        dialog.setWidthFull();

        List<Reminder> reminders = reminderService.getRemindersForAgent(currentUser); // Replace with your logic to fetch reminders

        VerticalLayout content = new VerticalLayout();
        content.getStyle().set("padding", "16px");
        content.getStyle().set("spacing", "8px");

        H3 title = new H3("Notifications");
        title.getStyle().set("margin", "0");
        content.add(title);

        for (Reminder reminder : reminders) {



            Span badge = new Span();
            badge.getStyle().set("background-color", "red");
            badge.getStyle().set("color", "white");
            badge.getStyle().set("padding", "4px 8px");
            badge.getStyle().set("border-radius", "20px");
            badge.setText("New");
            Span reminderTitle = new Span(badge);
            Span reminderContent = new Span(reminder.getTimestamp() + " : " + reminder.getMessage());
            reminderTitle.add("      "+reminderContent.getText());
            reminderTitle.getStyle().set("margin", "0");
            content.add(reminderTitle);
        }

        dialog.add(content);

        return dialog;
    }
    private void logout() {
        Set<String> connectedUsers = MyApplication.getConnectedUsers();
        connectedUsers.remove(currentUser.getFullName());
        VaadinSession.getCurrent().setAttribute("user", null);
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().getPage().executeJs("location.replace('/login');");
    }

    private void createSideBar() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Dashboard" , DashboardAgent.class),
                new RouterLink("Ventes" , VenteSup.class),
                new RouterLink("Production" , ProdSup.class),
                new RouterLink("Chat" , ChatSup.class),
                new RouterLink("Changer Mot De Pass" , ChangePasswordView.class)
        ));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        timer.cancel();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        if (currentUser == null) {
            beforeEnterEvent.rerouteTo(Login.class);
        }
    }
}
