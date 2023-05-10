package com.example.application.models;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@Entity
public class Vente extends VerticalLayout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int venteId ;
    @Column(name = "vente_contract", nullable = false)
    private String contract;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agent_id", nullable = true)
    private User agent;


    @Column
    private LocalDate date;

    private String typeVente;

    private String nomClient;

    private String numTel;

    private String emailClient;

    private String dateNaiss;

    private String qualification;


    public int getVenteId() {
        return venteId;
    }

    public void setVenteId(int venteId) {
        this.venteId = venteId;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public LocalDate getDate() {
        return date;
    }

    public User getAgent() {
        return agent;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTypeVente() {
        return typeVente;
    }

    public void setTypeVente(String typeVente) {
        this.typeVente = typeVente;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public String getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(String dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
}
