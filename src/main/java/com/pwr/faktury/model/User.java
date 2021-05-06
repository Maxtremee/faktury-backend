package com.pwr.faktury.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;

    @DBRef
    private Set<Role> roles = new HashSet<>();
    
    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private Contractor personal_data;
    
    private Set<Contractor> contractors = new HashSet<>();
    
    private Set<Product> products = new HashSet<>();
   
    private Set<Invoice> invoices = new HashSet<>();


    public User() {
    }

    public User(String id, Set<Role> roles, String login, String password, Contractor personal_data, Set<Contractor> contractors, Set<Product> products, Set<Invoice> invoices) {
        this.id = id;
        this.roles = roles;
        this.login = login;
        this.password = password;
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

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Contractor getPersonal_data() {
        return this.personal_data;
    }

    public void setPersonal_data(Contractor personal_data) {
        this.personal_data = personal_data;
    }

    public Set<Contractor> getContractors() {
        return this.contractors;
    }

    public void setContractors(Set<Contractor> contractors) {
        this.contractors = contractors;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    public User id(String id) {
        setId(id);
        return this;
    }

    public User roles(Set<Role> roles) {
        setRoles(roles);
        return this;
    }

    public User login(String login) {
        setLogin(login);
        return this;
    }

    public User password(String password) {
        setPassword(password);
        return this;
    }

    public User personal_data(Contractor personal_data) {
        setPersonal_data(personal_data);
        return this;
    }

    public User contractors(Set<Contractor> contractors) {
        setContractors(contractors);
        return this;
    }

    public User products(Set<Product> products) {
        setProducts(products);
        return this;
    }

    public User invoices(Set<Invoice> invoices) {
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
        return Objects.equals(id, user.id) && Objects.equals(roles, user.roles) && Objects.equals(login, user.login) && Objects.equals(password, user.password) && Objects.equals(personal_data, user.personal_data) && Objects.equals(contractors, user.contractors) && Objects.equals(products, user.products) && Objects.equals(invoices, user.invoices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roles, login, password, personal_data, contractors, products, invoices);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", roles='" + getRoles() + "'" +
            ", login='" + getLogin() + "'" +
            ", password='" + getPassword() + "'" +
            ", personal_data='" + getPersonal_data() + "'" +
            ", contractors='" + getContractors() + "'" +
            ", products='" + getProducts() + "'" +
            ", invoices='" + getInvoices() + "'" +
            "}";
    }
    

}
