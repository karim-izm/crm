package com.example.application.views.auth;

import com.example.application.models.User;
import com.example.application.repository.UserRepository;
import com.example.application.service.MyApplication;
import com.example.application.service.UserService;
import com.example.application.views.User.DashboardAgent;
import com.example.application.views.User.VenteView;
import com.example.application.views.admin.dashboard.Dashboard;
import com.example.application.views.admin.list.ListView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@Route(value = "/login")
@PageTitle(value = "Login | Storactive CRM")
public class Login extends VerticalLayout {
    @Autowired
    private final UserService userService;

    InputStream imageStream = getClass().getResourceAsStream("/images/Storactive.png");
    Image image = new Image(new StreamResource("image.jpg", () -> imageStream), "Storactive Logo");
    LoginForm loginForm = new LoginForm();
    LoginI18n i18n = LoginI18n.createDefault();
    public Login(UserService service) {
        this.userService = service;
        addClassName("login");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        configureForm();
        add(
                new H1("Storactive CRM"),
                image,
                loginForm

        );
    }

    private void configureForm() {
        image.setWidth("200px");
        image.setHeight("200px");
        loginForm.addLoginListener(loginEvent -> {
            int result = userService.login(loginEvent.getUsername(), loginEvent.getPassword());
            if(result == 1){
                // Get the logged-in user from the user service
                User user = userService.getUserByUsername(loginEvent.getUsername());

                // Save the user information in the session
                VaadinSession session = VaadinSession.getCurrent();
                session.setAttribute(User.class, user);
                Set<String> connectedUsers = MyApplication.getConnectedUsers();
                connectedUsers.add(user.getFullName());

                // Navigate to the VenteView
                UI.getCurrent().navigate(Dashboard.class);
            }
            else if (result == 0){
                User user = userService.getUserByUsername(loginEvent.getUsername());

                // Save the user information in the session
                VaadinSession session = VaadinSession.getCurrent();
                session.setAttribute(User.class, user);
                Set<String> connectedUsers = MyApplication.getConnectedUsers();
                connectedUsers.add(user.getFullName());

                // Navigate to the VenteView
                UI.getCurrent().navigate(DashboardAgent.class);
            }
            else {
                Notification.show("Incorrect username or password",
                                3000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }

        });
    }
}
