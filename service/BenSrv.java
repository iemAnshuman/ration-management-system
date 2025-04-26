package ration.service;
import java.util.List;
import ration.model.Ben;

public interface BenSrv {
    boolean add(Ben p);
    Ben add(String name, String addr, String ph, int age, boolean main);
    boolean update(int id, String name, String addr, String ph);
    Ben get(int id);
    List<Ben> getAll();
    void showAll();
}