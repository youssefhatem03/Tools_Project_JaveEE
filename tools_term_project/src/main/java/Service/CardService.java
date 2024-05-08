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
                            "WHERE l.listName = :listName AND l.boardName = :boardName", ListModel.class)
                     .setParameter("listName", listName).setParameter("boardName", boardName)
                     .getSingleResult();
			
            
            
            if (list.getBoardName() == boardName) {
            	return "Board is not found";
            }
            
      		if (list.getCardNames().contains(cardName)){
      			return "This card is already in this list";
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
      			return "list not found in board" + board.getBoardName();
      		}
      		
      		if (card.getAssignee() != null) {
      			if (card.getAssignee().equals(userName)) {
      			return "this user is already assigned to this card";
      		}
      		}
      		
      		
      		if(board.getTeamLeader() == userName) {
      			return "cannot assign cards to the team leader";
      		}
      		
      		if (!board.getCollaboratorsUsernames().contains(userName)) {
      			return "assignee is not a collaborator in board " + board.getBoardName(); 
      		}
      		
      		card.setAssignee(user.getUserName());

      		EM.merge(card);
            
            return "Added " + userName +" successfully";
  
    		
    	} catch (Exception e) {
    	    e.printStackTrace(); 
    	    return "Something went wrong: " + e.getMessage();
    	    } 
    	
    }
    
    
    public String changeList(String boardName, String listName, String newListName, String cardName) {
    	
    	try {
      		if (boardName.isEmpty()) {
    			return "Please enter the board name";
    		}
      		if (cardName.isEmpty()) {
    			return "Please enter the card name";
    		}      		

      		if (listName.isEmpty()) {
    			return "Please enter the list name";
    		}
      		if (newListName.isEmpty()) {
    			return "Please enter the new list name";
    		}      		
      		
			BoardModel board = EM
					.createQuery("SELECT b FROM BoardModel b WHERE b.boardName = :boardName", BoardModel.class)
					.setParameter("boardName", boardName).getSingleResult();
      		
            ListModel list =  EM.createQuery(                    
            		"SELECT l FROM ListModel l " +
                            "WHERE l.listName = :listName", ListModel.class)
                     .setParameter("listName", listName)
                     .getSingleResult();
            
            ListModel newList =  EM.createQuery(                    
            		"SELECT l FROM ListModel l " +
                            "WHERE l.listName = :newListName", ListModel.class)
                     .setParameter("newListName", newListName)
                     .getSingleResult();
      		
            CardModel card =  EM.createQuery(                    
            		"SELECT c FROM CardModel c " +
                            "WHERE c.cardName = :cardName", CardModel.class)
                     .setParameter("cardName", cardName)
                     .getSingleResult();
            
            
            if(board == null) {
            	return "board does not exist";
            }
            
            if (list == null) {
            	return "list does not exist";
            }
            
            if (newList == null) {
            	return "new list does not exist";
            }
            
            if (card == null) {
            	return "card does not exist";
            }
      		
            
            
            if(!board.getListNames().contains(listName)) {
            	return "list: " + listName + " does not exist in board: " + boardName; 
            }
            
            if(!board.getListNames().contains(newListName)) {
            	return "list: " + newListName + " does not exist in board: " + boardName; 
            }
            
            if(!list.getCardNames().contains(cardName)) {
            	return "card does not exist in this list: " + listName;
            }
            
            if(listName.equals(newListName)) {
            	return "Card already exists in this list";
            }
            
            card.setListName(newListName);
            card.setList(newList);
            list.getCardNames().remove(cardName);
            newList.getCardNames().add(cardName);
            
            EM.merge(card);
            EM.merge(newList);
            EM.merge(list);
            

            return "Card moved from list: " + listName + " to list: " + newListName;
      		
    		
    	} catch (Exception e) {
    	    e.printStackTrace(); 
    	    return "Something went wrong: " + e.getMessage();
    	    } 
    	
    	
    }
    
    
    public String addDescription(String boardName, String listName, String cardName, String desc) {
    	
    	try {
    		
      		if (boardName.isEmpty()) {
    			return "Please enter the board name";
    		}
      		if (cardName.isEmpty()) {
    			return "Please enter the card name";
    		}      		

      		if (listName.isEmpty()) {
    			return "Please enter the list name";
    		}
      		if (desc.isEmpty()) {
    			return "Please enter the description";
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
            
            
            if(!board.getListNames().contains(listName)) {
            	return "list: " + listName + " does not exist in board: " + boardName; 
            }
            
            if(!list.getCardNames().contains(cardName)) {
            	return "card does not exist in this list: " + listName;
            }
            
            
            card.setCardDescription(desc);
            EM.merge(card);
            
            return "Description added";
            
            
            
    		
    	} catch (Exception e) {
    	    e.printStackTrace(); 
    	    return "Something went wrong: " + e.getMessage();
    	    } 
    	
    	
    }
    
    
    public String addComments(String boardName, String listName, String cardName, String comment) {
    	
    	try {
    		
      		if (boardName.isEmpty()) {
    			return "Please enter the board name";
    		}
      		if (cardName.isEmpty()) {
    			return "Please enter the card name";
    		}      		

      		if (listName.isEmpty()) {
    			return "Please enter the list name";
    		}
      		if (comment.isEmpty()) {
    			return "Please enter the description";
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

            
            if (!board.getCollaboratorsUsernames().contains(UserService.username) && !board.getTeamLeader().equals(UserService.username)) {
            	return "You have no access to this board";
            }
            
            
            if(!board.getListNames().contains(listName)) {
            	return "list: " + listName + " does not exist in board: " + boardName; 
            }
            
            
            if(!list.getCardNames().contains(cardName)) {
            	return "card does not exist in this list: " + listName;
            }
            
            
            Set<String> comments = card.getComments();
            comments.add(comment);
            
            card.setComments(comments);
            EM.merge(card);
            
            return "Comment added";
            
                		
    	} catch (Exception e) {
    	    e.printStackTrace(); 
    	    return "Something went wrong: " + e.getMessage();
    	    } 
    	
    	
    }
    
	
	
}











