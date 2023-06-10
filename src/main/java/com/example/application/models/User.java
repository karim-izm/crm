package com.example.application.models;

import com.vaadin.flow.component.UI;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

@Entity
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    @NotEmpty
    private String username;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
    @NotEmpty
    private String fullName;

    private double salary;

    private boolean isAdmin;

    private boolean isSup;

    private boolean enProd;


    @Column(name = "total_hours_worked_this_month")
    @Nullable
    private Double totalHoursWorkedThisMonth = 0.0;


    @Transient
    private transient UI ui; // Transient field to store the UI instance

    // Getter and setter for the UI instance
    public UI getUi() {
        return ui;
    }

    public void setUi(UI ui) {
        this.ui = ui;
    }

    // Getters and setters

    public Double getTotalHoursWorkedThisMonth() {
        return totalHoursWorkedThisMonth;
    }

    public void setTotalHoursWorkedThisMonth(Double totalHoursWorkedThisMonth) {
        this.totalHoursWorkedThisMonth = totalHoursWorkedThisMonth;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isSup() {
        return isSup;
    }

    public void setSup(boolean sup) {
        isSup = sup;
    }

    public int getId() {
        return id;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnProd() {
        return enProd;
    }


    public void setEnProd(boolean enProd) {
        this.enProd = enProd;
    }
}
