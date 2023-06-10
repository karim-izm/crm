package com.example.application.views.Sup;

import com.example.application.models.User;
import com.example.application.models.WorkTime;
import com.example.application.service.ProdService;
import com.example.application.views.User.VenteForm;
import com.example.application.views.layout.MainLayout;
import com.example.application.views.layout.SupLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@PageTitle("Production")
@Route(value = "prodSup" , layout = SupLayout.class)
public class ProdSup extends VerticalLayout {
    User currentUser = VaadinSession.getCurrent().getAttribute(User.class);

    List<String> options = Arrays.asList("Ajourdhui" , "Hier", "Cette Semaine", "Ce mois");
    Select<String> select = new Select<>();

    Grid<WorkTime> grid = new Grid(WorkTime.class);
    ProdService prodService ;



    public ProdSup(ProdService prodService) {
        this.prodService = prodService;
        select.setItems(options);
        setSizeFull();
        select.setValue("Ajourdhui");
        configureGrid();
        add(getToolBar(),grid);
        updateList();
    }

    private void updateList(){
        select.addValueChangeListener(selectStringComponentValueChangeEvent -> {
            String selectedOption = selectStringComponentValueChangeEvent.getValue();
            grid.setItems(prodService.getFilteredWorkTimesAll(selectedOption));
        });
    }

    private void configureGrid() {
        grid.setColumnReorderingAllowed(true);
        grid.setItems(prodService.getFilteredWorkTimesAll("Ajourdhui"));
        grid.setColumns("startTime" , "endTime" , "totalPauseTime" , "realTimeWorked");
        grid.addColumn(prod -> prod.getAgent().getFullName()).setHeader("Agent").setSortable(true);
        grid.getColumnByKey("totalPauseTime").setResizable(true).setHeader("Temps Pause").setAutoWidth(true);
        grid.getColumnByKey("realTimeWorked").setResizable(true).setHeader("Total Temps").setAutoWidth(true);
        grid.getColumnByKey("startTime").setResizable(true).setHeader("Temps Debut").setAutoWidth(true);
        grid.getColumnByKey("endTime").setResizable(true).setHeader("Temps Fin").setAutoWidth(true);

    }

    private Component getToolBar() {
        HorizontalLayout hl = new HorizontalLayout(select);
        hl.addClassName("toolbar");
        return hl;
    }
}
