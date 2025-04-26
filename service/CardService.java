package ration.service;
import ration.model.Ben;
import ration.model.Card;

public interface CardService {
    
    Card newCard(String type);
    
    boolean addMember(String cardId, Ben person);
    
    Card findCard(String cardId);
    
    boolean changeType(String cardId, String type);
    
    void showAll();
    
    int count();
}