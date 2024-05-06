package Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import Controller.UserController;
import Model.BoardModel;
import Model.ListModel;
import Model.UserModel;

@Stateless
public class BoardService {

	
	
    @PersistenceContext(unitName = "hello")
    private EntityManager EM;

    
    
    public String createBoard(BoardModel board) {
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

            EM.persist(board);
            return "Board added successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to add board: " + e.getMessage();
        }
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
			
			EM.merge(board);
            return "Collaborator " + userName + " added successfully to board " + boardName;
			
						
		} catch (NoResultException e) {
			return "User or Board not found";
			
		}
				
	}
	
	
	
	public String addList(String boardName, String listName) {
		
		try {
			
			if (listName.isEmpty() || listName == null) {
				return "Enter the list name";
			}
			
        	
			BoardModel board = EM
					.createQuery("SELECT b FROM BoardModel b WHERE b.boardName = :boardName", BoardModel.class)
					.setParameter("boardName", boardName).getSingleResult();
			
			if (board.getBoardName() == null || board.getBoardName().isEmpty()) {
				return "Board does not exist";
			}
			
			if (!board.getTeamLeader().equals(UserService.username)) {
				return "You are not the Team Leader of this board, access denied";
			}
			
			if(board.getListNames().contains(listName)) {
				return "A list with this name already exists";
			}
			
			ListModel newList = new ListModel();
			newList.setListName(listName);
			EM.persist(newList);
			
			Set<ListModel> lists;
			lists = board.getLists();
			lists.add(newList);
			board.setLists(lists);
			
			Set<String> listNames;
			listNames = board.getListNames();
			listNames.add(newList.getListName());
			board.setListNames(listNames);
			
			EM.merge(board);
			return "List added successfully";
			
		} catch (Exception e) {
			
			return "Something went wrong";
		}
		
		
	}
	
	
	
	public String removeList(String boardName, String listName) {
		
		try {
			if (boardName.isEmpty() || listName.isEmpty()) {
				return "Please enter board name and list name";
			}
			
			BoardModel board = EM
					.createQuery("SELECT b FROM BoardModel b WHERE b.boardName = :boardName", BoardModel.class)
					.setParameter("boardName", boardName).getSingleResult();
			
			if (board.getBoardName() == null || board.getBoardName().isEmpty()) {
				return "Board does not exist";
			}
			
			if (!board.getTeamLeader().equals(UserService.username)) {
				return "You are not the Team Leader of this board, access denied";
			}
			
			if(!board.getListNames().contains(listName)) {
				return "No list with this name exists";
			}
			
			ListModel newList = new ListModel();
			newList.setListName(listName);
			EM.remove(newList);
			
			Set<ListModel> lists;
			lists = board.getLists();
			lists.remove(newList);
			board.setLists(lists);
			
			Set<String> listNames;
			listNames = board.getListNames();
			listNames.remove(newList.getListName());
			board.setListNames(listNames);
			
			EM.merge(board);
			return "List deleted successfully";
			
		} catch (Exception e) {
			return "An error occured";
		}
		

		
	}
	
	
    	
    	
    }











