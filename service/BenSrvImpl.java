package ration.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ration.model.Ben;

public class BenSrvImpl implements BenSrv {
    private Map<Integer, Ben> people;
    
    public BenSrvImpl() {
        people = new HashMap<>();
    }
    
    public boolean add(Ben p) {
        if (p == null) {
            return false;
        }
        people.put(p.getId(), p);
        return true;
    }

    @Override
    public Ben add(String name, String addr, String ph, int age, boolean main) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Name empty");
            return null;
        }

        Ben p = new Ben(name, addr, ph, age, main);
        people.put(p.getId(), p);

        ration.utils.DatabaseHandler.saveBeneficiary(p);

        return p;
    }


    @Override
    public boolean update(int id, String name, String addr, String ph) {
        Ben p = get(id);
        if (p == null) {
            System.out.println("Error: ID not found: " + id);
            return false;
        }
        
        if (name != null && !name.trim().isEmpty()) {
            p.setName(name);
        }
        
        if (addr != null) {
            p.setAddr(addr);
        }
        
        if (ph != null) {
            p.setPhone(ph);
        }
        
        return true;
    }
    
    @Override
    public Ben get(int id) {
        return people.get(id);
    }
    
    @Override
    public List<Ben> getAll() {
        return new ArrayList<>(people.values());
    }
    
    @Override
    public void showAll() {
        if (people.isEmpty()) {
            System.out.println("No people in system");
            return;
        }
        
        System.out.println("\n----- PEOPLE -----");
        for (Ben p : people.values()) {
            System.out.println(p.toString());
        }
    }

    public boolean add(Object p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}