package com.mycompany.mavenproject1.model;

import java.util.Date;

public class Bills {
    private int id;
    private int contractId;
    private Date bulanTagihan;
    private double totalTagihan;
    private String status;

    public Bills(){}

    public Bills(int contractId, Date bulanTagihan, double totalTagihan, String status) {
        this.contractId = contractId;
        this.bulanTagihan = bulanTagihan;
        this.totalTagihan = totalTagihan;
        this.status = status;
    }

    public int getId() {return id;}
    public int getContractId() {return contractId;}
    public Date getBulanTagihan() {return bulanTagihan;}
    public double getTotalTagihan() {return totalTagihan;}
    public String getStatus() {return status;}

    public void setId(int id) {this.id = id;}
    public void setContractId(int contractId) {this.contractId = contractId;}
    public void setBulanTagihan(Date bulanTagihan) {this.bulanTagihan = bulanTagihan;}
    public void setTotalTagihan(double totalTagihan) {this.totalTagihan = totalTagihan;}
    public void setStatus(String status) {this.status = status;}
}
