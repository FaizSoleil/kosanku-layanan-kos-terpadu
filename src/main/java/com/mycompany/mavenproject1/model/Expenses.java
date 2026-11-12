package com.mycompany.mavenproject1.model;

import java.util.Date;

public class Expenses {
    private Date expenseDate;
    private String kategori;
    private double jumlah;

    public Expenses(){}

    public Expenses(Date expensDate, String kategori, double jumlah){
        this.expenseDate = expensDate;
        this.kategori = kategori;
        this.jumlah = jumlah;
    }

    Date getExpenseDate(){return this.expenseDate;}
    String getKategori(){return this.kategori;}
    double getJumlah(){return this.jumlah;}

    void setExpenseDate(Date expensDate){this.expenseDate = expensDate;}
    void setKategori(String kategori){this.kategori = kategori;}
    void setJumlah(double jumlah){this.jumlah = jumlah;}

}
