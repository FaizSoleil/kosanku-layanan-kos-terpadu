package kosanku.model;

import java.util.Date;

public class Expenses {
    private int id;
    private Date expenseDate;
    private String kategori;
    private double jumlah;

    public Expenses(){}

    public Expenses(int id, Date expensDate, String kategori, double jumlah){
        this.id = id;
        this.expenseDate = expensDate;
        this.kategori = kategori;
        this.jumlah = jumlah;
    }

    public int getId() {return id;}
    public Date getExpenseDate(){return this.expenseDate;}
    public String getKategori(){return this.kategori;}
    public double getJumlah(){return this.jumlah;}

    public void setId(int id) {this.id = id;}
    public void setExpenseDate(Date expensDate){this.expenseDate = expensDate;}
    public void setKategori(String kategori){this.kategori = kategori;}
    public void setJumlah(double jumlah){this.jumlah = jumlah;}

}
