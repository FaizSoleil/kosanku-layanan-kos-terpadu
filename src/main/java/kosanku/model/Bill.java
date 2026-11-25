package kosanku.model;

import java.util.Date;

public class Bill {
    private Integer id;
    private int contractId;
    private Date bulanTagihan;
    private double totalTagihan;
    private BillStatus status;

    public Bill(){
        this.status = BillStatus.BELUM_DIBAYAR;
    }

    public Bill(int id, int contractId, Date bulanTagihan, double totalTagihan, BillStatus status) {
        this.id = id;
        this.contractId = contractId;
        this.bulanTagihan = bulanTagihan;
        this.totalTagihan = totalTagihan;
        this.status = status;
    }

    public Bill(int contractId, Date bulanTagihan, double totalTagihan, BillStatus status) {
        this.contractId = contractId;
        this.bulanTagihan = bulanTagihan;
        this.totalTagihan = totalTagihan;
        this.status = status;
    }

    public int getId() {return id;}
    public int getContractId() {return contractId;}
    public Date getBulanTagihan() {return bulanTagihan;}
    public double getTotalTagihan() {return totalTagihan;}
    public BillStatus getStatus() {return status;}

    public void setId(int id) {this.id = id;}
    public void setContractId(int contractId) {this.contractId = contractId;}
    public void setBulanTagihan(Date bulanTagihan) {this.bulanTagihan = bulanTagihan;}
    public void setTotalTagihan(double totalTagihan) {this.totalTagihan = totalTagihan;}
    public void setStatus(BillStatus status) {this.status = status;}

    //Needed for combo box dropdown so the program doesn't show mem address like: kosanku.model.Bill@1a2b3c 
    @Override
    public String toString() {
        return id.toString();
    }
}
