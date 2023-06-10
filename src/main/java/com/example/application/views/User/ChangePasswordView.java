package com.example.application.views.User;

import com.example.application.models.User;
import com.example.application.service.UserService;
import com.example.application.views.User.VenteView;
import com.example.application.views.layout.AgentLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "change-password" , layout = AgentLayout.class)
@PageTitle("Change Password")
public class ChangePasswordView extends VerticalLayout {
    private final UserService userService;
    private final User currentUser = VaadinSession.getCurrent().getAttribute(User.class);

    private final TextField username = new TextField("Username");
    private final PasswordField currentPassword = new PasswordField("Current password");
    private final PasswordField newPassword = new PasswordField("New password");
    private final Button save = new Button("Save");

    private final Binder<User> binder = new Binder<>(User.class);

    @Autowired
    public ChangePasswordView(UserService userService) {
        this.userService = userService;
        setAlignItems(Alignment.CENTER);


        binder.bindInstanceFields(this);
        binder.setBean(currentUser);

        username.setReadOnly(true);
        username.setValue(currentUser.getUsername());

        currentPassword.setRequired(true);
        newPassword.setRequired(true);
        add(username, currentPassword, newPassword, save);
        save.addClickListener(e -> save());
    }

    private void save() {
        try {
            binder.writeBean(currentUser);
            boolean success = userService.changeUserPassword(currentUser, currentPassword.getValue(), newPassword.getValue());
            if (success) {
                Notification.show("Password changed successfully").addThemeVariants(NotificationVariant.LUMO_PRIMARY);
                currentPassword.clear();
                newPassword.clear();
                UI.getCurrent().navigate(VenteView.class);
            } else {
                Notification.show("Current password is incorrect").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        } catch (ValidationException e) {
            Notification.show("Failed to save: " + e.getMessage());
        }
    }
}
