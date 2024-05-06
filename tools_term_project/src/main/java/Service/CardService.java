package Service;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import Model.BoardModel;
import Model.CardModel;
import Model.ListModel;

@Stateless
public class CardService {

	
    @PersistenceContext(unitName = "hello")
    private EntityManager EM;
	
    public String addCard(String boardName, String listName, String cardName) {
    	
    	try {
    		
    		if (boardName.isEmpty()) {
    			return "Please enter the board name";
    		}
      		if (listName.isEmpty()) {
    			return "Please enter the list name";
    		}
      		if (cardName.isEmpty()) {
    			return "Please enter the card name";
    		}
    		

            BoardModel board =  EM.createQuery(                    
            		"SELECT DISTINCT b " +
                    "FROM BoardModel b " +
                    "LEFT JOIN b.lists c " +
                    "WHERE b.boardName = :boardName AND :listName MEMBER OF b.listNames", BoardModel.class)
                     .setParameter("listName", listName)
                     .getSingleResult();
			
            if (board == null) {
            	return "Board not found";
            }
            
            if (!board.getListNames().contains(listName)) {
            	return "List not found in board " + board.getBoardName();
            }
            
			ListModel newList = new ListModel();
			newList.setListName(listName);        
			
			Set<String> cards;
			cards = newList.getCardNames();
			cards.add(cardName);
			newList.setCardNames(cards);
			
			CardModel card = new CardModel();
			card.setCardName(cardName);
			EM.persist(card);

			return "Added Card " + cardName + " to list " + listName + " Successfully";
            
    		
    		
    	} catch (Exception e) {
    		return "Something went wrong";
    	}
    	
    }
    
	
	
}
