package com.example.application.views.admin;

import com.example.application.models.Vente;
import com.example.application.service.VenteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QualificationForm extends Dialog {
    Vente v = new Vente();
    VenteService service ;
    private Grid<Vente> venteGrid;
    private TextField contract = new TextField("Numéro de contract");
    private TextField nomClient = new TextField("Nom Client");
    private TextField numClient = new TextField("Telephone Client");
    private EmailField emailClient = new EmailField("E-Mail Client");

    List<String> typeOptions = Arrays.asList("elect", "gaz", "assurance");
    Select<String> assuranceType = new Select<String>();

    private TextField venteType = new TextField("Type de vente");
    List<String> options = Arrays.asList("Accepté" , "En Attente" , "Effectif" , "KO");
    Select<String> qualification = new Select<String>();
    DatePicker dateNaiss = new DatePicker();
    public QualificationForm(Vente v , VenteService service , Grid<Vente> venteGrid) {
        this.venteGrid = venteGrid;
        this.v = v;
        this.service = service;
        qualification.setItems(options);
        qualification.setLabel("Qualification");
        qualification.setValue(options.get(0));
        FormLayout form = new FormLayout();
        form.setHeight("400px");
        form.setWidth("800px");
        Button save = new Button("Save" , e->save());
        Button cancel = new Button("Cancel", e -> close());
        form.add(new H3(v.getContract()) , qualification , nomClient , numClient , emailClient, dateNaiss , venteType);
        fillForm();
        add(form , save , cancel);
    }

    private void save() {
        v.setQualification(qualification.getValue());
        v.setTypeVente(venteType.getValue());
        v.setEmailClient(emailClient.getValue());
        v.setNumTel(numClient.getValue());
        v.setNomClient(nomClient.getValue());
        v.setDateNaiss(String.valueOf(dateNaiss.getValue()));
       service.updateVente(v);
       venteGrid.setItems(service.getFilteredVentesAll("All"));
       close();
    }

    private void fillForm(){
        nomClient.setValue(v.getNomClient());
        numClient.setValue(v.getNumTel());
        emailClient.setValue(v.getEmailClient());
        dateNaiss.setValue(LocalDate.parse(v.getDateNaiss()));
        venteType.setValue(v.getTypeVente());
    }
}
