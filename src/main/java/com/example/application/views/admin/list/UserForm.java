package com.example.application.views.admin.list;

import com.example.application.models.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class UserForm extends FormLayout {
    Binder<User> binder = new BeanValidationBinder<>(User.class);
    TextField fullName = new TextField("Full Name");
    TextField email = new TextField("Email");
    TextField userName = new TextField("UserName");
    PasswordField password = new PasswordField("Password");
    TextField id = new TextField("id");

    Button save = new Button("save");
    Button delete = new Button("delete");
    Button cancel = new Button("cancel");
    private User u;

    public UserForm() {
        binder.bindInstanceFields(this);
        VerticalLayout vl = new VerticalLayout(configureFields(), createButtonsLayout());
        add(vl);
    }

    private Component configureFields() {
        VerticalLayout vl = new VerticalLayout(fullName, userName, email , password);
        return vl;
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickShortcut(Key.ESCAPE);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(buttonClickEvent -> saveUser());
        delete.addClickListener(buttonClickEvent -> fireEvent(new DeleteEvent(this , u)));
        cancel.addClickListener(buttonClickEvent -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, delete, cancel);
    }

    private void saveUser() {
        try {
            binder.writeBean(u);
            fireEvent(new SaveEvent(this , u));
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUser(User u) {
        this.u = u;
        binder.readBean(u);
    }

    // Events
    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
        private User user;

        protected UserFormEvent(UserForm source, User user) {
            super(source, false);
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        SaveEvent(UserForm source, User user) {
            super(source, user);
        }
    }

    public static class DeleteEvent extends UserFormEvent {
        DeleteEvent(UserForm source, User user) {
            super(source, user);
        }

    }

    public static class CloseEvent extends UserFormEvent {
        CloseEvent(UserForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener (ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
