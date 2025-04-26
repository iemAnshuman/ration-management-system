package ration.model;

public abstract class Person {
    private int id;
    private String name;
    private String addr;
    private String phone;
    
    private static int next = 1000;
    
    public Person() {
        id = genId();
    }
    
    public Person(String name, String addr, String phone) {
        id = genId();
        this.name = name;
        this.addr = addr;
        this.phone = phone;
    }
    
    public abstract void display();
    
    private static int genId() {
        return next++;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddr() {
        return addr;
    }
    
    public void setAddr(String addr) {
        this.addr = addr;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    @Override
    public String toString() {
        return "ID: " + id + ", " + name + ", " + addr + ", " + phone;
    }
}