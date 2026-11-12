package com.mycompany.mavenproject1.model;

public class Users {
    private String username;
    private String password;
    private String namaLengkap;

    public Users(){}

    public Users(String username, String password, String namaLengkap) {
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
    }

    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public String getNamaLengkap() {return namaLengkap;}

    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
    public void setNamaLengkap(String namaLengkap) {this.namaLengkap = namaLengkap;}
}
