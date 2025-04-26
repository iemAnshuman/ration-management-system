package ration.model;

public class Admin extends Person {
    private String empId;
    private String role;
    
    public Admin() {
        super();
    }
    
    public Admin(String name, String addr, String phone, String empId, String role) {
        super(name, addr, phone);
        this.empId = empId;
        this.role = role;
    }
    
    @Override
    public void display() {
        System.out.println("Admin Data:");
        System.out.println(super.toString());
        System.out.println("ID: " + empId);
        System.out.println("Role: " + role);
    }
    
    public String getEmpId() {
        return empId;
    }
    
    public void setEmpId(String empId) {
        this.empId = empId;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}