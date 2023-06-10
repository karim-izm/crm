package com.example.application.views.admin.dashboard;

import com.example.application.models.User;
import com.example.application.service.MyApplication;
import com.example.application.service.ProdService;
import com.example.application.service.UserService;
import com.example.application.service.VenteService;
import com.example.application.views.layout.MainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


@Route(value = "/Dashboard" , layout = MainLayout.class)
@PageTitle("DashBoard | Storactive")

public class Dashboard extends VerticalLayout {
    private UserService userService;
    private VenteService venteService;

    private ProdService prodService;
    private Grid<String> connectedUsersGrid;

    public Dashboard(UserService service, VenteService venteService, ProdService prodService) {
        this.userService = service;
        this.venteService = venteService;
        this.prodService = prodService;
        setSizeFull();
        VerticalLayout mainContentLayout = new VerticalLayout();
        mainContentLayout.setSpacing(true);

        // Create stat cards layout
        HorizontalLayout statCardsLayout = new HorizontalLayout();
        statCardsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        statCardsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        statCardsLayout.getStyle()
                .set("border-bottom", "1px solid #000000")
                .set("padding", "1rem")
                .set("width", "100%");

        statCardsLayout.add(
                getUserStats(),
                getUserInProdStats(),
                getVenteStats(),
                getAcceptedVentesStats(),
                getEffectifVentesStats(),
                getEnAttenteVentesStats(),
                getKOVentesStats(),
                getPasEncoreVentesStats()
        );

        // Add stat cards and content layout to the main layout
        mainContentLayout.add(statCardsLayout, getContentLayout());

        add(
                getHeader(),
                mainContentLayout
        );

        updateConnectedUsers();
    }

    private Component getContentLayout() {
        // Create a horizontal layout to hold the connected users grid and stats grid
        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setSizeFull();
        contentLayout.setSpacing(true);

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

        H3 header = new H3("Agents Connectés");
        H3 header2 = new H3("Top Agents");
        connectedUsersGrid.getElement()
                .executeJs("this.$.header.style.justifyContent = 'center';");
        connectedUsersGrid.getElement().executeJs("this.$.header.style.alignItems = 'center';");

        VerticalLayout vl = new VerticalLayout(header, connectedUsersGrid);
        VerticalLayout vl2 = new VerticalLayout(header2, getStatsGrid());

        vl.setJustifyContentMode(JustifyContentMode.CENTER);
        vl.setAlignItems(Alignment.CENTER);
        vl2.setJustifyContentMode(JustifyContentMode.CENTER);
        vl2.setAlignItems(Alignment.CENTER);

        Div verticalLine = new Div();
        verticalLine.setWidth("1px");
        verticalLine.getStyle().set("background-color", "#000000");

        contentLayout.add(vl, verticalLine, vl2);

        return contentLayout;
    }


    private void updateConnectedUsers() {
        Set<String> connectedUsers = MyApplication.getConnectedUsers();
        connectedUsersGrid.setItems(connectedUsers);
    }

    private Component getStatsGrid() {
        Grid<UserStats> statsGrid = new Grid<>();
        statsGrid.setWidth("700px");
        statsGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        statsGrid.setSelectionMode(Grid.SelectionMode.NONE);
        statsGrid.addColumn(UserStats::getFullName).setHeader("Agent");
        statsGrid.addColumn(UserStats::getTotalSalesThisMonth).setHeader("Ventes réalisé ce mois");
        statsGrid.addColumn(UserStats::getTotalHoursWorked).setHeader("Total heures travailé ce mois");
        ListDataProvider<UserStats> dataProvider = new ListDataProvider<>(getUserStatsList());
        statsGrid.setDataProvider(dataProvider);
        return statsGrid;
    }

    private List<UserStats> getUserStatsList() {
        List<User> agents = userService.findAllUsers("");
        List<UserStats> userStatsList = new ArrayList<>();
        int currentMonth = LocalDateTime.now().getMonthValue();
        int currentYear = LocalDateTime.now().getYear();

        for (User agent : agents) {
            String fullName = agent.getFullName();
            double totalHours = agent.getTotalHoursWorkedThisMonth();
            int totalSalesThisMonth = venteService.getTotalSalesForUserAndMonth(agent, currentMonth, currentYear);
            userStatsList.add(new UserStats(fullName, totalSalesThisMonth , totalHours));
        }
        userStatsList.sort(Comparator.comparingInt(UserStats::getTotalSalesThisMonth).reversed());

        return userStatsList;
    }


    private Component getHeader() {
        H3 title = new H3("Dashboard");
        title.getStyle().set("margin", "0");
        title.getStyle().set("color", "black");
        title.getStyle().set("font-weight", "bold");

        HorizontalLayout headerLayout = new HorizontalLayout(title);
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        headerLayout.setSpacing(true);
        headerLayout.setWidth("100%");
        headerLayout.getStyle().set("border-bottom", "1px solid #000000");
        headerLayout.setPadding(true);
        headerLayout.setMargin(false);

        return headerLayout;
    }

    private Component getUserStats() {
        Card card = new Card("Agents  ", String.valueOf(userService.countUsers() - 1), VaadinIcon.USER);
        return new Div(card);
    }

    private Component getUserInProdStats() {
        Card card = new Card("Agents En Prod", String.valueOf(userService.countUsersInProd()), VaadinIcon.USER_CHECK);
        return new Div(card);
    }

    private Component getVenteStats() {
        Card card = new Card("Ventes Total", String.valueOf(venteService.countVentes()), VaadinIcon.CHART);
        return new Div(card);
    }

    private Component getAcceptedVentesStats() {
        Card card = new Card("Ventes Acceptés", String.valueOf(venteService.getAcceptedVentesCount()), VaadinIcon.CHECK);
        return new Div(card);
    }

    private Component getKOVentesStats() {
        Card card = new Card("Ventes KO", String.valueOf(venteService.getKoVentesCount()), VaadinIcon.THUMBS_DOWN);
        return new Div(card);
    }

    private Component getEnAttenteVentesStats() {
        Card card = new Card("Ventes En Attente", String.valueOf(venteService.getEnAttentVentesCount()), VaadinIcon.CLOCK);
        return new Div(card);
    }

    private Component getEffectifVentesStats() {
        Card card = new Card("Ventes Effectif", String.valueOf(venteService.getEffectifVentesCount()), VaadinIcon.ENVELOPE);
        return new Div(card);
    }

    private Component getPasEncoreVentesStats() {
        Card card = new Card("Ventes Pas Encore Qualifié", String.valueOf(venteService.getPasEncoreVenteStats()), VaadinIcon.WARNING);
        return new Div(card);
    }


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        updateConnectedUsers();
    }

    public class Card extends Div {
        private final H3 title;
        private final Span value;
        private final Icon icon;

        public Card(String titleText, String valueText, VaadinIcon vaadinIcon) {
            setClassName("card");
            getStyle().set("padding" , "1rem")
                    .set("border-radius", "0.5rem")
                    .set("box-shadow", "0 0.125rem 0.25rem rgba(0, 0, 0, 0.075)")
                    .set("background-color", "#FFFFFF");
            setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

            title = new H3(titleText);
            title.getStyle()
                    .set("margin", "0")
                    .set("color", "#333333")
                    .set("font-weight", "bold")
                    .set("font-size", "1.2rem")
                    .set("display", "flex")
                    .set("align-items", "center");

            value = new Span(valueText);
            value.getStyle()
                    .set("margin", "0")
                    .set("color", "#666666")
                    .set("font-size", "1rem")
                    .set("display", "flex")
                    .set("align-items", "center");

            icon = vaadinIcon.create();
            icon.getStyle()
                    .set("margin-right", "0.5rem")
                    .set("color", "#333333")
                    .set("font-size", "1.5rem");

            title.add(icon);
            add(title, value);
        }
    }
    private static class UserStats {
        private final String fullName;
        private final int totalSalesThisMonth;

        private final double totalHoursWorked;

        public UserStats(String fullName, int totalSalesThisMonth , double totalHoursWorked) {
            this.fullName = fullName;
            this.totalSalesThisMonth = totalSalesThisMonth;
            this.totalHoursWorked = totalHoursWorked;
        }

        public String getFullName() {
            return fullName;
        }

        public int getTotalSalesThisMonth() {
            return totalSalesThisMonth;
        }

        public double getTotalHoursWorked() {
            return totalHoursWorked;
        }

    }
}

