package com.example.application.views.admin.dashboard;

import com.example.application.service.MyApplication;
import com.example.application.service.UserService;
import com.example.application.service.VenteService;
import com.example.application.views.layout.MainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashSet;
import java.util.Set;


@Route(value = "/Dashboard" , layout = MainLayout.class)
@PageTitle("DashBoard | Storactive")

public class Dashboard extends VerticalLayout {
    private UserService userService;
    private VenteService venteService;
    private Grid<String> connectedUsersGrid;

    public Dashboard(UserService service, VenteService venteService) {
        this.userService = service;
        this.venteService = venteService;
        setSizeFull();
        connectedUsersGrid = new Grid<>();
        connectedUsersGrid.setWidth("300px");
        connectedUsersGrid.setHeight("200px");
        connectedUsersGrid.addColumn(String::valueOf).setHeader("Connected Users");

        addClassName("dashboard");
        setPadding(true);
        setSpacing(true);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        H3 header = new H3("Agents Connectés");
        connectedUsersGrid.getStyle()
                .set("font-weight", "bold")
                .set("align-self", "center")
                .set("justify-self", "center")
                .set("margin", "0 auto");
        header.getStyle().set("margin", "0").set("color", "#000000").set("font-weight", "bold").set("align-items", "center").set("justify-content" ,"center");
        VerticalLayout vl = new VerticalLayout(header , connectedUsersGrid);
        vl.setJustifyContentMode(JustifyContentMode.CENTER);
        vl.setAlignItems(Alignment.CENTER);
        HorizontalLayout hl = new HorizontalLayout( getUserStats(), getVenteStats());
        hl.setJustifyContentMode(JustifyContentMode.CENTER);
        add(
                getHeader(),
                hl,
                vl
        );
        updateConnectedUsers();
    }

    private void updateConnectedUsers() {
        Set<String> connectedUsers = MyApplication.getConnectedUsers();
        connectedUsersGrid.setItems(connectedUsers);
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

    private Component getUserStats() {
        Card card = new Card("Agents", String.valueOf(userService.countUsers()));
        card.getElement().getStyle().set("background-color", "#E1F5FE");
        return new Div(card);
    }

    private Component getVenteStats() {
        Card card = new Card("Ventes", String.valueOf(venteService.countVentes()));
        card.getElement().getStyle().set("background-color", "#FCE4EC");
        return new Div(card);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        updateConnectedUsers();
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

