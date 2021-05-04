package com.pwr.faktury.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.annotation.Id;

public class User {
    @Id
    private String id;
    
    private Login login;

    private Contractor personal_data;

    private List<UUID> contractors = new ArrayList<>();

    private List<UUID> products = new ArrayList<>();

    private List<UUID> invoices = new ArrayList<>();


    public User() {
    }

    public User(String id, Login login, Contractor personal_data, List<UUID> contractors, List<UUID> products, List<UUID> invoices) {
        this.id = id;
        this.login = login;
        this.personal_data = personal_data;
        this.contractors = contractors;
        this.products = products;
        this.invoices = invoices;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Login getLogin() {
        return this.login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Contractor getPersonal_data() {
        return this.personal_data;
    }

    public void setPersonal_data(Contractor personal_data) {
        this.personal_data = personal_data;
    }

    public List<UUID> getContractors() {
        return this.contractors;
    }

    public void setContractors(List<UUID> contractors) {
        this.contractors = contractors;
    }

    public List<UUID> getProducts() {
        return this.products;
    }

    public void setProducts(List<UUID> products) {
        this.products = products;
    }

    public List<UUID> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(List<UUID> invoices) {
        this.invoices = invoices;
    }

    public User id(String id) {
        setId(id);
        return this;
    }

    public User login(Login login) {
        setLogin(login);
        return this;
    }

    public User personal_data(Contractor personal_data) {
        setPersonal_data(personal_data);
        return this;
    }

    public User contractors(List<UUID> contractors) {
        setContractors(contractors);
        return this;
    }

    public User products(List<UUID> products) {
        setProducts(products);
        return this;
    }

    public User invoices(List<UUID> invoices) {
        setInvoices(invoices);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(login, user.login) && Objects.equals(personal_data, user.personal_data) && Objects.equals(contractors, user.contractors) && Objects.equals(products, user.products) && Objects.equals(invoices, user.invoices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, personal_data, contractors, products, invoices);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", login='" + getLogin() + "'" +
            ", personal_data='" + getPersonal_data() + "'" +
            ", contractors='" + getContractors() + "'" +
            ", products='" + getProducts() + "'" +
            ", invoices='" + getInvoices() + "'" +
            "}";
    }

}
