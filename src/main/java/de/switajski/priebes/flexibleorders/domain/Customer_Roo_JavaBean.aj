// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.domain;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.reference.Country;
import java.util.Date;

privileged aspect Customer_Roo_JavaBean {
    
    public String Customer.getShortName() {
        return this.shortName;
    }
    
    public void Customer.setShortName(String shortName) {
        this.shortName = shortName;
    }
    
    public String Customer.getName1() {
        return this.name1;
    }
    
    public void Customer.setName1(String name1) {
        this.name1 = name1;
    }
    
    public String Customer.getName2() {
        return this.name2;
    }
    
    public void Customer.setName2(String name2) {
        this.name2 = name2;
    }
    
    public String Customer.getStreet() {
        return this.street;
    }
    
    public void Customer.setStreet(String street) {
        this.street = street;
    }
    
    public String Customer.getCity() {
        return this.city;
    }
    
    public void Customer.setCity(String city) {
        this.city = city;
    }
    
    public int Customer.getPostalCode() {
        return this.postalCode;
    }
    
    public void Customer.setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }
    
    public Country Customer.getCountry() {
        return this.country;
    }
    
    public void Customer.setCountry(Country country) {
        this.country = country;
    }
    
    public Date Customer.getCreated() {
        return this.created;
    }
    
    public void Customer.setCreated(Date created) {
        this.created = created;
    }
    
    public String Customer.getEmail() {
        return this.email;
    }
    
    public void Customer.setEmail(String email) {
        this.email = email;
    }
    
    public String Customer.getPassword() {
        return this.password;
    }
    
    public void Customer.setPassword(String password) {
        this.password = password;
    }
    
    public Long Customer.getPhone() {
        return this.phone;
    }
    
    public void Customer.setPhone(Long phone) {
        this.phone = phone;
    }
    
}
