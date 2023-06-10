package com.example.application.views.admin.list;

import com.example.application.models.User;
import com.example.application.service.UserService;
import com.example.application.views.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("List Des Agents")
@Route(value = "/users", layout = MainLayout.class)
public class ListView extends VerticalLayout {
    private final UserService service;
    Grid<User> grid = new Grid<>(User.class);
    TextField search = new TextField();
    UserForm form;

    public ListView(UserService service) {
        this.service = service;
        setSizeFull();

        configureGrid();
        configureForm();
        add(
                getToolBar(),
                new H3("Agents : "),
                getContent()
        );

        updateList();
        closeEditor();

    }

    private void closeEditor() {
        form.setUser(null);
        form.setVisible(false);

    }

    private void updateList() {
        grid.setItems(service.findAllUsers(search.getValue()));
    }

    private Component getContent() {
        HorizontalLayout conetnet = new HorizontalLayout(configureGrid(), form);
        conetnet.setFlexGrow(10, grid);
        conetnet.setFlexGrow(1, form);
        conetnet.addClassName("content");
        conetnet.setSizeFull();
        return conetnet;
    }

    private void configureForm() {
        form = new UserForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveUser);
        form.addDeleteListener(this::deleteUser);
        form.addCloseListener(closeEvent -> closeEditor());


    }

    private void deleteUser(UserForm.DeleteEvent event) {
        ConfirmDialog dialog = new ConfirmDialog("Confirm deletion", "Are you sure you want to delete this record?",
                "Yes", confirmEvent -> {
            service.deleteUser(event.getUser());
            updateList();
            closeEditor();
            Notification notification = new Notification("Agent Supprimé avec success ! ");
            notification.setDuration(3000);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();

        },
                "Cancel", cancelEvent -> {
            closeEditor();
        });
        dialog.open();

    }

    private void saveUser(UserForm.SaveEvent event) {
        service.addUser(event.getUser());
        updateList();
        closeEditor();
        Notification.show("Agent Sauvegardé !").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private Component getToolBar() {

        search.setPlaceholder("Rechercher");
        search.setClearButtonVisible(true);
        search.setValueChangeMode(ValueChangeMode.LAZY);
        search.addValueChangeListener(textFieldStringComponentValueChangeEvent -> updateList());

        Button addUser = new Button("Add User");
        addUser.addClickListener(buttonClickEvent -> addUser());
        HorizontalLayout hl = new HorizontalLayout(search, addUser);
        hl.addClassName("toolbar");
        return hl;
    }

    private void addUser() {
        grid.asSingleSelect().clear();
        editUser(new User());
    }

    private Component configureGrid() {
        grid.addClassNames("user-grid");
        grid.setColumns("id", "fullName", "email", "username" , "salary");
        grid.setHeightFull();
        grid.setColumnReorderingAllowed(false);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        setSizeFull();
        grid.asSingleSelect().addValueChangeListener(gridUserComponentValueChangeEvent -> editUser(gridUserComponentValueChangeEvent.getValue()));
        Div gridWrapper = new Div(grid);
        gridWrapper.setSizeFull();
        gridWrapper.getStyle().set("overflow", "auto");
        VerticalLayout layout = new VerticalLayout();
        layout.add(gridWrapper);
        return layout;
    }

    private void editUser(User value) {
        if (value == null) {
            closeEditor();
        }
        form.setUser(value);
        form.setVisible(true);
    }

}
