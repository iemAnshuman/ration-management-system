package ration.model;
import java.util.ArrayList;
import java.util.List;



public class Card {
    public void setId(String id) { this.id = id; }
    public void setCount(int count) { this.count = count; }

    public static final String TYPE_APL = "APL"; 
    public static final String TYPE_BPL = "BPL"; 
    public static final String TYPE_AAY = "AAY"; 
    
    private String id;
    private String type;
    private int count;
    private List<Ben> members;
    private List<Hist> log;
    
    private static int next = 100001;
    private static int total = 0;
    
    public Card() {
        id = genId();
        members = new ArrayList<>();
        log = new ArrayList<>();
        count = 0;
        total++;
        addLog("Card created");
    }
    
    public Card(String type) {
        this();
        this.type = type;
        addLog("Type set: " + type);
    }
    
    public void addMember(Ben person) {
        if (person != null) {
            person.setCardId(id);  // Fixed: setCardNo -> setCardId
            members.add(person);
            count++;
            addLog("Added: " + person.getName());
        }
    }
    
    public void addLog(String act) {
        log.add(new Hist(act));
    }
    
    public void showHist() {
        System.out.println("\nHistory for " + id + ":");
        for (Hist e : log) {
            e.show();
        }
    }
    
    public void show() {
        System.out.println("\n----- CARD INFO -----");
        System.out.println("ID: " + id);
        System.out.println("Type: " + type);
        System.out.println("Members: " + count);
        
        System.out.println("\nPeople:");
        if (members.isEmpty()) {
            System.out.println("No one added yet");
        } else {
            for (Ben p : members) {
                String tag = "";
                if (p.isHead()) {  // Fixed: isMain -> isHead
                    tag = " (Main)";
                }
                System.out.println("- " + p.getName() + tag);
            }
        }
    }
    
    private static String genId() {
        return "C" + next++;
    }
    
    public static int getTotal() {
        return total;
    }
    
    public class Hist {
        private String date;
        private String act;
        
        public Hist(String act) {
            date = java.time.LocalDate.now().toString();
            this.act = act;
        }
        
        public void show() {
            System.out.println(date + ": " + act);
        }
        
        public String getDate() {
            return date;
        }
        
        public String getAct() {
            return act;
        }
    }
    
    public String getId() {
        return id;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
        addLog("Type changed to: " + type);
    }
    
    public int getCount() {
        return count;
    }
    
    public List<Ben> getMembers() {
        return members;
    }


}