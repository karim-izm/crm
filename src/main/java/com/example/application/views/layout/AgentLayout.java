package com.example.application.views.layout;

import com.example.application.models.User;
import com.example.application.service.MyApplication;
import com.example.application.service.NotificationTask;
import com.example.application.views.User.ChangePasswordView;
import com.example.application.views.User.DashboardAgent;
import com.example.application.views.User.VenteView;
import com.example.application.views.auth.Login;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
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
import java.util.Set;
import java.util.Timer;

public class AgentLayout extends AppLayout implements RouterLayout , BeforeEnterObserver {
    private final Timer timer = new Timer();
    private static final LocalTime TIME_TO_NOTIFY = LocalTime.of(14, 21);
    User currentUser;

    public AgentLayout() {
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        timer.scheduleAtFixedRate(new NotificationTask(VaadinSession.getCurrent()), 0, 1000);
        createHeader();
        createSideBar();
    }

    private void createHeader() {
        H1 header = new H1("CRM STORACTIVE | Agent");
        header.addClassNames( LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);
        Button logout = new Button("Logout");
        HorizontalLayout hl = new HorizontalLayout(new DrawerToggle(), header , logout);
        hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        hl.expand(header);
        hl.setWidthFull();
        hl.addClassNames( LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);
        addToNavbar(hl);
        logout.addClickListener(buttonClickEvent -> logout());

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
                new RouterLink("Ventes" , VenteView.class),
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
