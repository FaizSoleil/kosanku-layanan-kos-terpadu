package kosanku.model;

import java.util.Date;

public class Payments {
    private int id;
    private int billId;
    private Date paymentDate;
    private double jumlah;
    private String metodePembayaran;

    public Payments(){}

    public Payments(int id, int billId, Date paymentDate, double jumlah, String metodePembayaran) {
        this.id = id;
        this.billId = billId;
        this.paymentDate = paymentDate;
        this.jumlah = jumlah;
        this.metodePembayaran = metodePembayaran;
    }

    public int getId() {return id;}
    public int getBillId() {return billId;}
    public Date getPaymentDate() {return paymentDate;}
    public double getJumlah() {return jumlah;}
    public String getMetodePembayaran() {return metodePembayaran;}

    public void setId(int id) {this.id = id;}
    public void setBillId(int billId) {this.billId = billId;}
    public void setPaymentDate(Date paymentDate) {this.paymentDate = paymentDate;}
    public void setJumlah(double jumlah) {this.jumlah = jumlah;}
    public void setMetodePembayaran(String metodePembayaran) {this.metodePembayaran = metodePembayaran;}
}
