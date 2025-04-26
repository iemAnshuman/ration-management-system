package ration.model;

public class Ben extends Person {
    private String cardId;
    private int age;
    private boolean isHead;
    
    public Ben() {
        super();
    }
    
    public Ben(String name, String address, String phone, int age, boolean isHead) {
        super(name, address, phone);
        this.age = age;
        this.isHead = isHead;
    }
    
    @Override
    public void display() {
        System.out.println("Member Details:");
        System.out.println(super.toString());
        System.out.println("Age: " + age);
        
        String cardStatus = "Not Assigned";
        if (cardId != null) {
            cardStatus = cardId;
        }
        System.out.println("Card ID: " + cardStatus);
        
        String headStatus = "No";
        if (isHead) {
            headStatus = "Yes";
        }
        System.out.println("Family Head: " + headStatus);
    }
    
    public String getCardId() {
        return cardId;
    }
    
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public boolean isHead() {
        return isHead;
    }
    
    public void setHead(boolean isHead) {
        this.isHead = isHead;
    }
}