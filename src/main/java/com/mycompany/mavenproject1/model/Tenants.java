package com.mycompany.mavenproject1.model;

public class Tenants {
    private String name;
    private String email;
    private String phoneNumber;

    public Tenants(){}

    public Tenants(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {return name;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getEmail() {return email;}

    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
}
