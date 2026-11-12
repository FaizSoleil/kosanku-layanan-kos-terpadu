package com.mycompany.mavenproject1.model;

import java.util.Date;

public class Payments {
    private Date paymentDate;
    private double jumlah;
    private String metodePembayaran;

    public Payments(){}

    public Payments(Date paymentDate, double jumlah, String metodePembayaran) {
        this.paymentDate = paymentDate;
        this.jumlah = jumlah;
        this.metodePembayaran = metodePembayaran;
    }

    public Date getPaymentDate() {return paymentDate;}
    public double getJumlah() {return jumlah;}
    public String getMetodePembayaran() {return metodePembayaran;}

    public void setPaymentDate(Date paymentDate) {this.paymentDate = paymentDate;}
    public void setJumlah(double jumlah) {this.jumlah = jumlah;}
    public void setMetodePembayaran(String metodePembayaran) {this.metodePembayaran = metodePembayaran;}
}
