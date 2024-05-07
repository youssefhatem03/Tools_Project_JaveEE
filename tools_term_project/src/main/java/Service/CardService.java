package Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import Model.BoardModel;
import Model.CardModel;
import Model.ListModel;
import Model.UserModel;

@Stateless
public class CardService {

	
    @PersistenceContext(unitName = "hello")
    private EntityManager EM;
	
    
    
    public List<CardModel> getAll() {
        return EM.createQuery("SELECT c FROM CardModel c", CardModel.class).getResultList();
    }
    
       
    
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
    		

            ListModel list =  EM.createQuery(                    
            		"SELECT l FROM ListModel l " +
                            "WHERE l.listName = :listName", ListModel.class)
                     .setParameter("listName", listName)
                     .getSingleResult();
			
            
            
            if (list.getBoardName() == boardName) {
            	return "Board is not found";
            }

			
			Set<String> cards = new HashSet<>();
			cards = list.getCardNames();
			cards.add(cardName);
			list.setCardNames(cards);
			  
			
			
			CardModel card = new CardModel();
			card.setCardName(cardName);
			card.setListName(listName);
		
			
			EM.persist(card);
			EM.merge(list);

			return "Added Card " + cardName + " to list " + listName + " Successfully";
       
    		
    	} catch (Exception e) {
    	    e.printStackTrace();
    	    
    	    return "Something went wrong: " + e.getMessage();
    	    }
    	
    }
    
    
    public String assignUser(String boardName, String listName, String cardName, String userName) {
    	
    	try {
    		
      		if (listName.isEmpty()) {
    			return "Please enter the list name";
    		}
      		if (cardName.isEmpty()) {
    			return "Please enter the card name";
    		}      		

      		
			BoardModel board = EM
					.createQuery("SELECT b FROM BoardModel b WHERE b.boardName = :boardName", BoardModel.class)
					.setParameter("boardName", boardName).getSingleResult();
      		
            ListModel list =  EM.createQuery(                    
            		"SELECT l FROM ListModel l " +
                            "WHERE l.listName = :listName", ListModel.class)
                     .setParameter("listName", listName)
                     .getSingleResult();
      		
            CardModel card =  EM.createQuery(                    
            		"SELECT c FROM CardModel c " +
                            "WHERE c.cardName = :cardName", CardModel.class)
                     .setParameter("cardName", cardName)
                     .getSingleResult();
      		
        	UserModel user = EM.createQuery("SELECT a FROM UserModel a WHERE a.userName = :userName", UserModel.class)
                    .setParameter("userName", userName)
                    .getSingleResult();
        	
      		if (card == null) {
      			return "card does not exist";
      		}
      		
      		if (user == null) {
      			return "no such user found";
      		}
      		
      		if (!board.getListNames().contains(listName)) {
      			return "list not found in board " + board.getBoardName();
      		}
      		
      		if (!board.getCollaboratorsUsernames().contains(userName)) {
      			return "assignee is not a collaborator is board " + board.getBoardName(); 
      		}
      		
      		card.setAssignee(user.getUserName());

      		EM.merge(card);
            
            return "Added " + userName +" successfully";
  
    		
    	} catch (Exception e) {
    	    e.printStackTrace(); 
    	    return "Something went wrong: " + e.getMessage();
    	    } 
    	
    }
    
	
	
}











