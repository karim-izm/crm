package com.example.application.views.admin;

import com.example.application.models.Vente;
import com.example.application.service.VenteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class VenteEditSup extends Dialog {
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
    DatePicker dateNaiss = new DatePicker();
    public VenteEditSup(Vente v , VenteService service , Grid<Vente> venteGrid) {
        this.venteGrid = venteGrid;
        this.v = v;
        this.service = service;
        FormLayout form = new FormLayout();
        form.setHeight("400px");
        form.setWidth("800px");
        Button save = new Button("Save" , e->save());
        Button delete = new Button("Delete" , e->delete());
        Button cancel = new Button("Cancel", e -> close());
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        form.add(new H3(v.getContract())  , nomClient , numClient , emailClient, dateNaiss , venteType);
        fillForm();
        add(form , save , delete , cancel );
    }

    private void delete() {
        service.deleteVente(v);
        close();
        venteGrid.setItems(service.getFilteredVentesAll("Tous"));
        Notification.show("Vente Suprimmé!").addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    }

    private void save() {
        v.setTypeVente(venteType.getValue());
        v.setEmailClient(emailClient.getValue());
        v.setNumTel(numClient.getValue());
        v.setNomClient(nomClient.getValue());
        v.setDateNaiss(String.valueOf(dateNaiss.getValue()));
        service.updateVente(v);
        venteGrid.setItems(service.getFilteredVentesAll("Tous"));
        close();
        Notification.show("Vente Modifié!").addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    }

    private void fillForm(){
        nomClient.setValue(v.getNomClient());
        numClient.setValue(v.getNumTel());
        emailClient.setValue(v.getEmailClient());
        dateNaiss.setValue(LocalDate.parse(v.getDateNaiss()));
        venteType.setValue(v.getTypeVente());
    }
}
