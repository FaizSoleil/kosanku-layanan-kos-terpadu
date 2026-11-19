package kosanku.model;

import java.util.Date;

public class Expenses {
    private int id;
    private Date expenseDate;
    private String deskripsi;
    private double jumlah;

    public Expenses(){}

    public Expenses(int id, Date expensDate, String deskripsi, double jumlah){
        this.id = id;
        this.expenseDate = expensDate;
        this.deskripsi = deskripsi;
        this.jumlah = jumlah;
    }

    public int getId() {return id;}
    public Date getExpenseDate(){return this.expenseDate;}
    public String getDeskripsi(){return this.deskripsi;}
    public double getJumlah(){return this.jumlah;}

    public void setId(int id) {this.id = id;}
    public void setExpenseDate(Date expensDate){this.expenseDate = expensDate;}
    public void setDeskripsi(String deskripsi){this.deskripsi = deskripsi;}
    public void setJumlah(double jumlah){this.jumlah = jumlah;}

}
