package com.example.application.views.layout;

import com.example.application.models.User;
import com.example.application.service.UserService;
import com.example.application.views.admin.*;
import com.example.application.views.admin.dashboard.Dashboard;
import com.example.application.views.admin.list.ListView;
import com.example.application.views.auth.Login;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout implements RouterLayout, BeforeEnterObserver {
    UserService service;
    User currentUser;
    public MainLayout(UserService service) {
        this.service = service;
        createHeader();
        createSideBar();
    }

    private void createHeader() {
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        H4 header = new H4("CRM STORACTIVE | ADMIN ");
        header.addClassNames(LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);
        Button logout = new Button("Logout");
        HorizontalLayout hl = new HorizontalLayout(new DrawerToggle(), header, logout);
        hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        hl.expand(header);
        hl.setWidthFull();
        hl.addClassNames(LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);
        addToNavbar(hl);
        logout.addClickListener(buttonClickEvent -> {

            VaadinSession.getCurrent().setAttribute("user", null);
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().getPage().executeJs("location.replace('/login');");

        });
    }

    private void createSideBar() {
            addToDrawer(new VerticalLayout(
                    new RouterLink("Dashboard", Dashboard.class),
                    new RouterLink("Agents", ListView.class),
                    new RouterLink("Ventes", VenteAdmin.class),
                    new RouterLink("Vue de contact", SpreadSheet.class ),
                    new RouterLink("Production", Production.class),
                    new RouterLink("Salaires", SalaryView.class),
                    new RouterLink("Rapelles", ReminderAdmin.class),
                    new RouterLink("Rapport", Rapport.class),
                    new RouterLink("Chat", ChatAdmin.class)
            ));
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        if (currentUser == null) {
            beforeEnterEvent.rerouteTo(Login.class);
        }
    }
}
