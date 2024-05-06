package Model;

import javax.persistence.*;

import java.util.Set;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Stateless
@Entity
public class ListModel {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long listId;
	
	@NotNull
	@Column
	private String listName;
	
	@Column
	private String boardName;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> cardNames;
	
	@ManyToOne
	@JoinColumn(name="boardId")
	private BoardModel board;	
	
	@OneToMany(mappedBy="list", fetch=FetchType.EAGER)
    @JsonIgnore
    private Set<CardModel> cards;
	
	
	
	public ListModel() {}

	public ListModel(String name) {
		this.listName = name;
	}
	
	public void setListName(String name) {
		this.listName = name;
	}
	
	public String getListName() {
		return this.listName;
	}
	
	public void setBoardName(String name) {
		this.boardName = name;
	}
	
	public String getBoardName() {
		return this.boardName;
	}
	
    public void setCardNames(Set<String> names) {
    	this.cardNames = names;
    }
    
    public Set<String> getCardNames(){
    	return this.cardNames;
    }
	
}
