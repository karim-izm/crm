package com.example.application.views.User;

import com.example.application.models.User;
import com.example.application.models.Vente;
import com.example.application.models.Vente;
import com.example.application.service.VenteService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import jakarta.validation.constraints.Email;
import org.aspectj.weaver.loadtime.Agent;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class VenteForm extends Dialog {
    User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
    VenteService service;
    private Grid<Vente> venteGrid;
    Binder<Vente> binder = new BeanValidationBinder<>(Vente.class);
    private Vente v;
    private TextField contract = new TextField("REF client");
    private TextField nomClient = new TextField("Nom Client");
    private TextField numClient = new TextField("Telephone Client");
    private EmailField emailClient = new EmailField("E-Mail Client");

    DatePicker dateNaiss = new DatePicker();

    List<String> options = Arrays.asList("elect", "gaz", "assurance");
    Select<String> assuranceType = new Select<String>();

    CheckboxGroup<String> venteType = new CheckboxGroup<>("Vente type", options);
    public VenteForm(VenteService service , Grid<Vente> grid) {
        this.service = service;
        this.venteGrid = grid;
        setHeaderTitle("Ajouter Une Vente");
        FormLayout form = new FormLayout();
        form.setHeight("400px");
        form.setWidth("800px");
        contract.setValue("cm-");
        contract.setPattern("cm-[0-9]+");
        contract.addValueChangeListener(event -> {
            String value = event.getValue();
            if (!value.startsWith("cm-")) {
                contract.setValue("cm-" + value);
            }
        });
        venteType.setReadOnly(false); // Enable editing
        venteType.setRequiredIndicatorVisible(true);
        venteType.addSelectionListener(event -> {
            Set<String> selectedItems = event.getAllSelectedItems();
            boolean assuranceSelected = selectedItems.contains(options.get(2));
            assuranceType.setVisible(assuranceSelected);
            if (!assuranceSelected) {
                assuranceType.clear();
            }
        });

        assuranceType.setItems("Assurance 1", "Assurance 2", "Assurance 3");
        assuranceType.setLabel("Type D'assurance");
        assuranceType.setVisible(false);

        dateNaiss.setLabel("Date Naissance Client");
        dateNaiss.setValue(LocalDate.of(2000 , 01 , 01));
        dateNaiss.setClearButtonVisible(true);
        dateNaiss.setRequiredIndicatorVisible(true);
        dateNaiss.getElement().setProperty("pattern" , "dd/MM/yyyy");


        Button save = new Button("Save", e -> save());

        Button cancel = new Button("Cancel", e -> close());
        form.add(contract ,  nomClient , numClient , emailClient, dateNaiss , venteType , assuranceType);
        add(form, save, cancel);
    }

    private void save() {
        if(contract.getValue().isEmpty() || nomClient.getValue().isEmpty() || numClient.getValue().equals("") || nomClient.getValue().isEmpty() || emailClient.getValue().isEmpty() || venteType.isEmpty())
            Notification.show("Please fill in all the inputs");
        else {
        Vente vente = new Vente();
        vente.setContract(contract.getValue());
        vente.setDate((LocalDate.now()));
        vente.setDateNaiss(String.valueOf(dateNaiss.getValue()));
        vente.setNomClient(nomClient.getValue());
        vente.setNumTel(numClient.getValue());
        vente.setEmailClient(emailClient.getValue());
        vente.setQualification("pas encore qualifié");
        if(currentUser != null) vente.setAgent(currentUser);
        if(venteType.isSelected(options.get(0))){
            vente.setTypeVente("Electricité");
        }
        else if(venteType.isSelected(options.get(1))){
            vente.setTypeVente("Gaz");
        }
        if(venteType.isSelected(options.get(0)) && venteType.isSelected(options.get(1))){
            vente.setTypeVente("Eelectricité & Gaz");
        }
        else if(venteType.isSelected(options.get(0)) && venteType.isSelected(options.get(2)) && assuranceType.equals("Assurance 1")){
            vente.setTypeVente("Eelectricité & Assurance 1");
        }
        service.save(vente);
        venteGrid.setItems(service.getFilteredVentes("All" , currentUser));
        close();

        }
    }

}




