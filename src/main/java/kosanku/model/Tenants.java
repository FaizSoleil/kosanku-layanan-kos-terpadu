package kosanku.model;

public class Tenants {
    private int id; //id for each tenant
    private String govIdNumber; //id number on their identification card
    private String name;
    private String email;
    private String phoneNumber;

    public Tenants(){}

    public Tenants(int id, String govIdNumber, String name, String email, String phoneNumber) {
        this.id = id;
        this.govIdNumber = govIdNumber;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {return id;}
    public String getGovIdNumber() {return govIdNumber;}
    public String getName() {return name;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getEmail() {return email;}

    public void setId(int id) {this.id = id;}
    public void setGovIdNumber(String govIdNumber) {this.govIdNumber = govIdNumber;}
    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
}
