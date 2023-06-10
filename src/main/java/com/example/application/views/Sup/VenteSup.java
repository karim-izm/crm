package com.example.application.views.Sup;

import com.example.application.models.User;
import com.example.application.models.Vente;
import com.example.application.service.ExcelExporter;
import com.example.application.service.VenteService;
import com.example.application.views.User.VenteForm;
import com.example.application.views.layout.MainLayout;
import com.example.application.views.layout.SupLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

@PageTitle("Ventes | Superviseur")
@Route(value = "VentSup" , layout = SupLayout.class)
public class VenteSup extends VerticalLayout {
    User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
    private final VenteService service;
    Grid<Vente> grid = new Grid<>(Vente.class);

    List<String> options = Arrays.asList("Tous" , "15 derniers jours", "Ce mois", "Selectionner Mois");
    Select<String> select = new Select<>();
    Select<Month> monthSelect = new Select<>();


    @Autowired
    public VenteSup(VenteService service) {
        this.service = service;
        Button exportButton = new Button("Exporter en Excel");
        exportButton.addClickListener(event -> {
            ExcelExporter.export(grid, "images/VentesData.xlsx");
            Notification.show("Fichier Excel Telechargé ! ");
        });
        setSizeFull();
        configureGrid();
        grid.addItemClickListener(event -> {
            Vente vente = event.getItem();
            com.example.application.views.admin.VenteEditSup form = new com.example.application.views.admin.VenteEditSup(vente , service , grid);
            form.open();
        });
        add(
                getToolBar(),
                new H3("Ventes Realisés"),
                getContent(),
                exportButton
        );
        updateList();
    }

    private void updateList() {
        select.addValueChangeListener(event -> {
            String selectedOption = event.getValue();
            if (selectedOption.equals("Selectionner Mois")) {
                monthSelect.setVisible(true);
                monthSelect.addValueChangeListener(Monthevent -> {
                    Month selectedMonth = Monthevent.getValue();
                    int monthNumber = selectedMonth.getValue();
                    grid.setItems(service.getFilteredVentesForMonth(select.getValue(), String.valueOf(monthNumber)));
                });
            } else {
                monthSelect.setVisible(false);
                grid.setItems(service.getFilteredVentesAll(selectedOption));
            }
        });


    }



    private Component configureGrid() {
        select.setItems(options);
        select.setValue(options.get(0));
        grid.setItems(service.getFilteredVentesAll("Tous"));
        grid.setMaxWidth("100%");
        grid.setRowsDraggable(true);
        grid.addClassNames("vente-grid");
        grid.setColumnReorderingAllowed(false);
        setSizeFull();
        grid.setColumns("venteId", "contract", "date", "typeVente", "nomClient", "numTel", "emailClient", "dateNaiss");
        grid.getColumnByKey("typeVente").setHeader("Description")
                .setFlexGrow(1)
                .setResizable(true)
                .setAutoWidth(true);
        grid.getColumnByKey("contract").setHeader("REF Client");
        grid.addColumn(vente -> vente.getAgent().getFullName()).setHeader("Agent").setSortable(true);
        Div gridWrapper = new Div(grid);
        gridWrapper.setSizeFull();
        gridWrapper.getStyle().set("overflow", "auto");
        VerticalLayout layout = new VerticalLayout();
        layout.add(gridWrapper);

        return layout;
    }

    private void badge(){

    }
    private Component getToolBar() {
        Button addUser = new Button("Ajouter Une Vente");
        addUser.addClickListener(buttonClickEvent -> {
            VenteForm form1 = new VenteForm(service , grid);
            form1.open();
        });
        monthSelect.setItems(Arrays.asList(Month.values()));
        monthSelect.setVisible(false);
        HorizontalLayout hl = new HorizontalLayout(select, monthSelect, addUser);
        hl.addClassName("toolbar");
        return hl;
    }

    private Component getContent() {
        HorizontalLayout conetnet = new HorizontalLayout(configureGrid());
        conetnet.setFlexGrow(10, grid);
        // conetnet.setFlexGrow(1, form);
        conetnet.addClassName("content");
        conetnet.setSizeFull();
        return conetnet;
    }
}
