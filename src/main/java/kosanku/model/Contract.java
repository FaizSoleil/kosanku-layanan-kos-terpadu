package kosanku.model;

import java.util.Date;

public class Contract {
    private Integer id;
    private int tenantId;
    private int roomId;
    private Date tanggalMasuk;
    private Date tanggalKeluar;
    private double deposit;
    private Date tanggalJatuhTempo;
    private ContractStatus status;

    public Contract(){
        this.status = ContractStatus.AKTIF;
    }

    public Contract(int tenantId, int roomId, Date tanggalMasuk, Date tanggalKeluar, double deposit, Date tanggalJatuhTempo, ContractStatus status) {
        this.tenantId = tenantId;
        this.roomId = roomId;
        this.tanggalMasuk = tanggalMasuk;
        this.tanggalKeluar = tanggalKeluar;
        this.deposit = deposit;
        this.tanggalJatuhTempo = tanggalJatuhTempo;
        this.status = status;
    }

    public Contract(int id, int tenantId, int roomId, Date tanggalMasuk, Date tanggalKeluar, double deposit, Date tanggalJatuhTempo, ContractStatus status) {
        this.id = id;
        this.tenantId = tenantId;
        this.roomId = roomId;
        this.tanggalMasuk = tanggalMasuk;
        this.tanggalKeluar = tanggalKeluar;
        this.deposit = deposit;
        this.tanggalJatuhTempo = tanggalJatuhTempo;
        this.status = status;
    }

    public int getId() {return id;}
    public int getTenantId() {return tenantId;}
    public int getRoomId() {return roomId;}
    public Date getTanggalMasuk() {return tanggalMasuk;}
    public Date getTanggalKeluar() {return tanggalKeluar;}
    public double getDeposit() {return deposit;}
    public Date getTanggalJatuhTempo() {return tanggalJatuhTempo;}
    public ContractStatus getStatus() {return status;}

    public void setId(int id) {this.id = id;}
    public void setTenantId(int tenantId) {this.tenantId = tenantId;}
    public void setRoomId(int roomId) {this.roomId = roomId;}
    public void setTanggalMasuk(Date tanggalMasuk) {this.tanggalMasuk = tanggalMasuk;}
    public void setTanggalKeluar(Date tanggalKeluar) {this.tanggalKeluar = tanggalKeluar;}
    public void setDeposit(double deposit) {this.deposit = deposit;}
    public void setTanggalJatuhTempo(Date tanggalJatuhTempo) {this.tanggalJatuhTempo = tanggalJatuhTempo;}
    public void setStatus(ContractStatus status) {this.status = status;}

    //Needed for combo box dropdown so the program doesn't show mem address like: kosanku.model.Bill@1a2b3c 
    @Override
    public String toString() {
        return id.toString();
    }
}