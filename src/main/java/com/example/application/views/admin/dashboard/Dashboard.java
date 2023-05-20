package com.example.application.views.admin.dashboard;

import com.example.application.models.User;
import com.example.application.service.MyApplication;
import com.example.application.service.UserService;
import com.example.application.service.VenteService;
import com.example.application.views.layout.MainLayout;
import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Route(value = "/Dashboard" , layout = MainLayout.class)
@PageTitle("DashBoard | Storactive")

public class Dashboard extends VerticalLayout {
    private UserService userService;
    private VenteService venteService;
    private Grid<String> connectedUsersGrid;
    private UI ui;
    private ScheduledExecutorService executorService;
    private  final UserInfo userInfo;
    private User currentUser;


    public Dashboard(UserService service, VenteService venteService) {
        this.ui = UI.getCurrent();
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.userService = service;
        this.venteService = venteService;
        var name= VaadinSession.getCurrent().getAttribute(User.class).getUsername();
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");

        userInfo=new UserInfo(currentUser.getFullName(),currentUser.getFullName());
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
        HorizontalLayout hl = new HorizontalLayout( getUserStats(), getVenteStats(),getVenteAccepte(),getVenteEnattente(),getVenteKo(),getVenteEffectif(),getChatLayout());
        hl.setJustifyContentMode(JustifyContentMode.CENTER);
        add(
                getHeader(),
                hl,
                vl
        );
        updateConnectedUsers();
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

    private void startAutomaticRefresh() {
        executorService.scheduleAtFixedRate(this::updateConnectedUsers, 0, 3, TimeUnit.SECONDS);
    }



    private void updateConnectedUsers() {
        Set<String> connectedUsers = MyApplication.getConnectedUsers();
        ui.access(() -> connectedUsersGrid.setItems(connectedUsers));


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
        Card card = new Card("Total des agents", String.valueOf(userService.countUsers()));
        card.getElement().getStyle().set("background-color", "#D0DEFB");
        return new Div(card);
    }

    private Component getVenteStats() {
        Card card = new Card("Total des Ventes", String.valueOf(venteService.countVentes()));
        card.getElement().getStyle().set("background-color", "#BBCFFA");
        return new Div(card);
    }
    private Component getVenteAccepte() {
        Card card = new Card("Ventes Acceptés", String.valueOf(venteService.countVentesAccepté()));
        card.getElement().getStyle().set("background-color", "#AFC5F7");
        return new Div(card);
    }
    private Component getVenteEnattente() {
        Card card = new Card("Ventes en attente", String.valueOf(venteService.countVentesEn_Attente()));
        card.getElement().getStyle().set("background-color", "#A3BDF6");
        return new Div(card);
    }
    private Component getVenteKo() {
        Card card = new Card("Ventes Ko", String.valueOf(venteService.countVentesKO()));
        card.getElement().getStyle().set("background-color", "#97B6FA");
        return new Div(card);
    }
    private Component getVenteEffectif() {
        Card card = new Card("Ventes Effectif", String.valueOf(venteService.countVentesEffectif()));
        card.getElement().getStyle().set("background-color", "#8CB0FF");
        return new Div(card);
    }


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::updateConnectedUsers, 0, 3, TimeUnit.SECONDS);
    }
    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        executorService.shutdown();
    }

    public class Card extends VerticalLayout {
        private final H3 title;
        private final Span value;

        public Card(String title, String value) {
            this.title = new H3(title);

            this.value = new Span(new H3(value));

            setClassName("card");
            setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

            add( this.value,this.title);

        }
    }
    }

