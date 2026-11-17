package kosanku.model;

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

    public Date getExpenseDate(){return this.expenseDate;}
    public String getKategori(){return this.kategori;}
    public double getJumlah(){return this.jumlah;}

    public void setExpenseDate(Date expensDate){this.expenseDate = expensDate;}
    public void setKategori(String kategori){this.kategori = kategori;}
    public void setJumlah(double jumlah){this.jumlah = jumlah;}

}
