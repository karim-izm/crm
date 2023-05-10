package com.example.application.views.User;

import com.example.application.models.User;
import com.example.application.models.Vente;
import com.example.application.service.VenteService;
import com.example.application.views.layout.AgentLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Route(value = "ventes" , layout = AgentLayout.class)
@PageTitle("Ventes")

public class VenteView extends VerticalLayout{
    User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
    private final VenteService service;
    Grid<Vente> grid = new Grid<>(Vente.class);

    List<String> options = Arrays.asList("All" , "Last 15 days", "This month", "Last 3 months");
    Select<String> select = new Select<>();

    @Autowired
    public VenteView(VenteService service) {
        this.service = service;
        setSizeFull();
        configureGrid();
        add(
                getToolBar(),
                new H3("Votre ventes "+currentUser.getFullName()),
                getContent()
        );
        updateList();
    }

    private void updateList() {
        select.addValueChangeListener(selectStringComponentValueChangeEvent -> {
            grid.setItems(service.getFilteredVentes(selectStringComponentValueChangeEvent.getValue() , currentUser));
        });
    }

    private Component configureGrid() {
        select.setItems(options);
        select.setValue(options.get(0));
        grid.setItems(service.getFilteredVentes("All" , currentUser));
        grid.addClassNames("vente-grid");
        grid.setColumnReorderingAllowed(false);
        setSizeFull();
        grid.setColumns( "venteId" , "contract"  , "date" , "typeVente" , "nomClient" , "numTel" , "emailClient" , "dateNaiss");
        grid.getColumnByKey("contract").setHeader("REF Client");
        grid.addColumn(vente -> vente.getAgent().getFullName()).setHeader("Agent").setSortable(true);
        Div gridWrapper = new Div(grid);
        gridWrapper.setSizeFull();
        gridWrapper.getStyle().set("overflow", "auto");
        VerticalLayout layout = new VerticalLayout();
        layout.add(gridWrapper);
        return layout;
    }

    private Component getToolBar() {



        Button addUser = new Button("Ajouter Une Vente");
        addUser.addClickListener(buttonClickEvent -> {
            VenteForm form = new VenteForm(service , grid);
            form.open();
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
