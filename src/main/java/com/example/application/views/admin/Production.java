package com.example.application.views.admin;

import com.example.application.models.User;
import com.example.application.models.WorkTime;
import com.example.application.service.ProdService;
import com.example.application.views.layout.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
 @PageTitle("Production")
 @Route(value = "production" , layout = MainLayout.class)
public class Production extends VerticalLayout {
    User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
    Grid<WorkTime> grid = new Grid(WorkTime.class);
    ProdService prodService ;



    public Production(ProdService prodService) {
        this.prodService = prodService;
        setSizeFull();
        configureGrid();
        add(grid);
    }

    private void configureGrid() {
        grid.setItems(prodService.getAll());
        grid.setColumns("startTime" , "endTime" , "totalWorkedTime" , "totalPauseTime" , "realTimeWorked" , "paused");
        grid.addColumn(prod -> prod.getAgent().getFullName()).setHeader("Agent").setSortable(true);


    }
}
