package kosanku.model;

import java.util.Date;

public class Payment {
    private int id;
    private int billId;
    private Date paymentDate;
    private double jumlah;
    public Payment(){}

    public Payment(int id, int billId, Date paymentDate, double jumlah) {
        this.id = id;
        this.billId = billId;
        this.paymentDate = paymentDate;
        this.jumlah = jumlah;
    }

    public int getId() {return id;}
    public int getBillId() {return billId;}
    public Date getPaymentDate() {return paymentDate;}
    public double getJumlah() {return jumlah;}

    public void setId(int id) {this.id = id;}
    public void setBillId(int billId) {this.billId = billId;}
    public void setPaymentDate(Date paymentDate) {this.paymentDate = paymentDate;}
    public void setJumlah(double jumlah) {this.jumlah = jumlah;}
}
