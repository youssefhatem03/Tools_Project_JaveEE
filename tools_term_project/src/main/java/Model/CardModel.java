package Model;
import javax.persistence.*;

import java.util.Set;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Stateless
@Entity
public class CardModel {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cardId;
	
	@NotNull
	@Column(unique=true)
	private String cardName;
	
	@Column
	private String cardDescription;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> comments;
	
	@Column
	private String listName;
	
	@Column
	private String assignee;
	
	@ManyToOne
	@JoinColumn(name="listId")
	@JsonIgnore
	private ListModel list;	
	
	
    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public Set<String> getComments() {
        return comments;
    }

    public void setComments(Set<String> comments) {
        this.comments = comments;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public ListModel getList() {
        return list;
    }

    public void setList(ListModel list) {
        this.list = list;
    }
	
    
    public void setAssignee(String name) {
    	this.assignee = name;
    }
    
    public String getAssignee() {
    	return this.assignee;
    }
	
}
