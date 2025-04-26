package ration.model;

public class Shop extends Person {
    private String sid;
    private String saddr;
    private String lic;
    
    public Shop() {
        super();
    }
    
    public Shop(String name, String addr, String phone, String sid, String saddr, String lic) {
        super(name, addr, phone);
        this.sid = sid;
        this.saddr = saddr;
        this.lic = lic;
    }
    
    @Override
    public void display() {
        System.out.println("Shop Owner:");
        System.out.println(super.toString());
        System.out.println("Shop ID: " + sid);
        System.out.println("Shop Addr: " + saddr);
        System.out.println("License: " + lic);
    }
    
    public String getSid() {
        return sid;
    }
    
    public void setSid(String s) {
        sid = s;
    }
    
    public String getSaddr() {
        return saddr;
    }
    
    public void setSaddr(String s) {
        saddr = s;
    }
    
    public String getLic() {
        return lic;
    }
    
    public void setLic(String l) {
        lic = l;
    }
}