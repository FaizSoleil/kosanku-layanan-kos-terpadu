package com.mycompany.mavenproject1.model;

public class Rooms {
    private int id;
    private String roomNumber;
    private String type;
    private double price;
    private String facilities;
    private String status;

    public Rooms() {}

    public Rooms(String roomNumber, String type, double price, String facilities, String status) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.facilities = facilities;
        this.status = status;
    }

    // Constructor lengkap dengan ID (kalau butuh buat baca/update data dari database)
    public Rooms(int id, String roomNumber, String type, double price, String facilities, String status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.facilities = facilities;
        this.status = status;
    }

    public int getId() {return id;}
    public String getRoomNumber() {return roomNumber;}
    public String getType() {return type;}
    public double getPrice() {return price;}
    public String getFacilities() {return facilities;}
    public String getStatus() {return status;}

    public void setId(int id) {this.id = id;}
    public void setRoomNumber(String roomNumber) {this.roomNumber = roomNumber;}
    public void setType(String type) {this.type = type;}
    public void setPrice(double price) {this.price = price;}
    public void setFacilities(String facilities) {this.facilities = facilities;}
    public void setStatus(String status) {this.status = status;}

    @Override
    public String toString() {
        return roomNumber + " - " + type + " (" + status + ")";
    }
}