package ration.service;
import java.util.HashMap;
import java.util.Map;
import ration.model.Ben;
import ration.model.Card;

public class Cards implements CardService {
    private Map<String, Card> cards;
    
    public Cards() {
        this.cards = new HashMap<>();
    }
    
    @Override
    public Card newCard(String type) {
        if (!isValidType(type)) {
            System.out.println("Error: Invalid type. Use APL, BPL, or AAY.");
            return null;
        }
        
        Card card = new Card(type);
        cards.put(card.getId(), card);  
        
        return card;
    }
    
    @Override
    public boolean addMember(String cardId, Ben person) {  
        Card card = findCard(cardId);
        if (card == null) {
            System.out.println("Error: Card not found: " + cardId);
            return false;
        }
        
        card.addMember(person);  
        return true;
    }
    
    @Override
    public Card findCard(String cardId) {  
        return cards.get(cardId);
    }
    
    @Override
    public boolean changeType(String cardId, String type) {
        if (!isValidType(type)) {
            System.out.println("Error: Invalid type. Use APL, BPL, or AAY.");
            return false;
        }
        
        Card card = findCard(cardId);  
        if (card == null) {
            System.out.println("Error: Card not found: " + cardId);
            return false;
        }
        
        card.setType(type);  
        return true;
    }
    
    @Override
    public void showAll() {
        if (cards.isEmpty()) {
            System.out.println("No cards in the system.");
            return;
        }
        
        System.out.println("\n----- ALL CARDS -----");
        for (Card card : cards.values()) {  
            System.out.println(card.getId() + " - Type: " + card.getType() +  
                              " - Members: " + card.getCount());  
        }
    }
    
    @Override
    public int count() {
        return cards.size();
    }
    
    private boolean isValidType(String type) {
        return type != null && (
            type.equals(Card.TYPE_APL) ||  
            type.equals(Card.TYPE_BPL) || 
            type.equals(Card.TYPE_AAY)    
        );
    }
}