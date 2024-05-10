package Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import Controller.UserController;
import Messaging.JMSClient;
import Model.BoardModel;
import Model.ListModel;
import Model.UserModel;

@Stateless
public class BoardService {

	@Resource
    EJBContext context;
	
    @PersistenceContext(unitName = "hello")
    private EntityManager EM;

    @Inject
    JMSClient jmsclient;
    
    
    public String createBoard(BoardModel board) {
    	
    	//if (context.isCallerInRole("TeamLeader")) {
    		    		
    	     String boardName = board.getBoardName();
    	     String teamLeader = board.getTeamLeader();

    	        try {
    	            if (boardName.isEmpty()) {
    	                return "Board Name cannot be empty";
    	            }

    	            if (teamLeader.isEmpty()) {
    	                return "You must be signed in to create a board!";
    	            }

    	            if (isBoardNameExists(boardName)) {
    	                return "Board Name already exists!";
    	            }
    	            board.setBoardName(boardName);
    	            board.setTeamLeader(teamLeader);
    	            
    	            
    	            String msg = "- Message for " + UserService.username + " : " + boardName + " Has Been Created By "+ teamLeader;
    	            
    	            jmsclient.sendMessage(msg);    	    
    	            
    	            EM.persist(board);
    	            
    	            return "Board added successfully!";
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	            return "Failed to add board: " + e.getMessage();
    	        } 
    		
//    	} else {
//    	            return "Unauthorized user.";
//    	        }
//    	
   
    }

    
    
    public boolean isBoardNameExists(String boardName) {
        List<BoardModel> similarBoardNames = EM.createQuery("SELECT b FROM BoardModel b WHERE b.boardName = :boardName", BoardModel.class)
                                          .setParameter("boardName", boardName)
                                          .getResultList();
        return !similarBoardNames.isEmpty();
    }

    
    
    public List<BoardModel> getAllBoards() {
        try {
            return EM.createQuery("SELECT b FROM BoardModel b", BoardModel.class)
                     .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    
    
    
    public List<BoardModel> getUserBoards(String username) {
        try {
            if (username == null || username.isEmpty()) {
                return Collections.emptyList();
            }

            List<BoardModel> board =  EM.createQuery(                    
            		"SELECT DISTINCT b " +
                    "FROM BoardModel b " +
                    "LEFT JOIN b.collaborators c " +
                    "WHERE b.teamLeader = :userName OR :userName MEMBER OF b.collaboratorsUsernames", BoardModel.class)
                     .setParameter("userName", username)
                     .getResultList();
            
            return board;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    
	public String deleteBoard(String boardName) {

		try {

			if (boardName == null || boardName.isEmpty()) {
				return "No Board with such name exists";
			}

			BoardModel board = EM
					.createQuery("SELECT b FROM BoardModel b WHERE b.boardName = :boardName", BoardModel.class)
					.setParameter("boardName", boardName).getSingleResult();

			if (board != null && board.getBoardName().equals(boardName)) {

				if (!board.getTeamLeader().equals(UserService.username)) {
					return "You are not the Team Leader of this board, access denied";
				}

				
	            String msg = "- Message for " + UserService.username + " : " + boardName + " Has Been Deleted";
;
	            
	            jmsclient.sendMessage(msg);    	    
			
				
				EM.remove(board);
				return "Board deleted succesfully";

			}
		} catch (NoResultException e) {
			return "Board not found";
		}
		return null;

	}
	
	
	@Transactional
	public String addCollaborator(String boardName, String userName) {
		
		try {
			
			
			if (userName == null || userName.isEmpty()) {
				return "Please enter the username of the user you want to invite";
			}
			
        	UserModel user = EM.createQuery("SELECT a FROM UserModel a WHERE a.userName = :userName", UserModel.class)
                    .setParameter("userName", userName)
                    .getSingleResult();
			
        	
        	
			BoardModel board = EM
					.createQuery("SELECT b FROM BoardModel b WHERE b.boardName = :boardName", BoardModel.class)
					.setParameter("boardName", boardName).getSingleResult();
						
			if (board.getBoardName() == null || board.getBoardName().isEmpty()) {
				return "Board does not exist";
			}
			
			if (!board.getTeamLeader().equals(UserService.username)) {
				return "You are not the Team Leader of this board, access denied";
			}
			
			if (user.getUserName() == board.getTeamLeader()) {
				return "Can't add yourself to the board";
			}
			
			if (board.getCollaboratorsUsernames().contains(user.getUserName())){
				return "user is already added in this board";
			}
			
			if (user.getUserName() == null || user.getUserName().isEmpty()) {
				return "user does not exist";
			}
			
			Set<UserModel> users;
			users = board.getCollaborators();
			users.add(user);
			board.setCollaborators(users);
			
			Set<String> usernames;
			usernames = board.getCollaboratorsUsernames();
			usernames.add(user.getUserName());
			board.setCollaboratorsUsernames(usernames);
			

            String msg = "- Message for " + UserService.username + " : " + "Collaborator " + userName + " added successfully to board " + boardName;
            
            jmsclient.sendMessage(msg);    	    
 		
			
			EM.merge(board);
            return "Collaborator " + userName + " added successfully to board " + boardName;
			
						
		} catch (NoResultException e) {
			return "User or Board not found";
			
		}
				
	}
	
	

	
	
    	
    	
    }











