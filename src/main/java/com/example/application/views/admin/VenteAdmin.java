package com.example.application.views.admin;

import com.example.application.models.User;
import com.example.application.models.Vente;
import com.example.application.service.VenteService;
import com.example.application.views.User.VenteForm;
import com.example.application.views.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@PageTitle("Vnete")
@Route(value = "VenteAdmin" , layout = MainLayout.class)
public class VenteAdmin extends VerticalLayout {
    User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
    private final VenteService service;
    Grid<Vente> grid = new Grid<>(Vente.class);

    List<String> options = Arrays.asList("All" , "Last 15 days", "This month", "Last 3 months");
    Select<String> select = new Select<>();

    @Autowired
    public VenteAdmin(VenteService service) {
        this.service = service;
        setSizeFull();
        configureGrid();
        grid.addItemClickListener(event -> {
            Vente vente = event.getItem();
            QualificationForm form = new QualificationForm(vente , service , grid);
            form.open();
        });
        add(
                getToolBar(),
                getContent()
        );
        updateList();
    }

    private void updateList() {
        select.addValueChangeListener(selectStringComponentValueChangeEvent -> {
            grid.setItems(service.getFilteredVentesAll(selectStringComponentValueChangeEvent.getValue()));
        });
    }



    private Component configureGrid() {
        select.setItems(options);
        select.setValue(options.get(0));
        grid.setItems(service.getFilteredVentesAll("All"));
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
        grid.addComponentColumn(vente -> {
            String qualification = vente.getQualification();
            String badgeColor = "";
            if (qualification.equals("KO")) {
                badgeColor = "red";
            } else if (qualification.equals("Accepté")) {
                badgeColor = "green";
            }
            else if (qualification.equals("Validé")) {
                badgeColor = "blue";
            }
            else if (qualification.equals("En Attente")) {
                badgeColor = "blue";
            }

            else if (qualification.equals("pas encore qualifié")) {
                badgeColor = "orange";
            }
            Span badge = new Span(qualification);
            badge.getStyle().set("padding", "0.2em 0.5em")
                    .set("background-color", badgeColor)
                    .set("color", "white")
                    .set("border-radius", "999px");
            return badge;
        }).setHeader("Qualification");
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
        HorizontalLayout hl = new HorizontalLayout(select, addUser);
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
