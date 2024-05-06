package Service;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import Model.BoardModel;
import Model.ListModel;
import Model.UserModel;

@Stateless
public class ListService {

	
	
    @PersistenceContext(unitName = "hello")
    private EntityManager EM;

    
    public List<ListModel> getAllLists() {
        return EM.createQuery("SELECT l FROM ListModel l", ListModel.class).getResultList();
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
			newList.setBoardName(board.getBoardName());
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
