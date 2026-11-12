package com.mycompany.mavenproject1.model;

import java.util.Date;

public class Contracts {
    private int id;
    private int tenantId;
    private int roomId;
    private Date tanggalMasuk;
    private Date tanggalKeluar;
    private double deposit;
    private int tanggalJatuhTempo;

    public Contracts(){}

    public Contracts(int tenantId, int roomId, Date tanggalMasuk, Date tanggalKeluar, double deposit, int tanggalJatuhTempo) {
        this.tenantId = tenantId;
        this.roomId = roomId;
        this.tanggalMasuk = tanggalMasuk;
        this.tanggalKeluar = tanggalKeluar;
        this.deposit = deposit;
        this.tanggalJatuhTempo = tanggalJatuhTempo;
    }

    public int getId() {return id;}
    public int getTenantId() {return tenantId;}
    public int getRoomId() {return roomId;}
    public Date getTanggalMasuk() {return tanggalMasuk;}
    public Date getTanggalKeluar() {return tanggalKeluar;}
    public double getDeposit() {return deposit;}
    public int getTanggalJatuhTempo() {return tanggalJatuhTempo;}

    public void setId(int id) {this.id = id;}
    public void setTenantId(int tenantId) {this.tenantId = tenantId;}
    public void setRoomId(int roomId) {this.roomId = roomId;}
    public void setTanggalMasuk(Date tanggalMasuk) {this.tanggalMasuk = tanggalMasuk;}
    public void setTanggalKeluar(Date tanggalKeluar) {this.tanggalKeluar = tanggalKeluar;}
    public void setDeposit(double deposit) {this.deposit = deposit;}
    public void setTanggalJatuhTempo(int tanggalJatuhTempo) {this.tanggalJatuhTempo = tanggalJatuhTempo;}
}