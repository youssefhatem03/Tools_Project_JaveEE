package Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Stateless
@Entity
public class BoardModel {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long boardId;
	
	@NotNull
	@Column(unique=true)
	private String boardName;
	

	@NotNull
	@Column
	private String teamLeader;
	
	
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> collaboratorsUsernames;
	
	
	@OneToMany(mappedBy="board", fetch=FetchType.EAGER)
    @JsonIgnore
    private Set<UserModel> collaborators;

    public Set<UserModel> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(Set<UserModel> users) {
        this.collaborators = users;
    }
	
    
	@OneToMany(mappedBy="board", fetch=FetchType.EAGER)
	@JsonIgnore
    private Set<ListModel> lists;
	
	
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> listNames;
	
	
	public BoardModel(){}
	
	public BoardModel(String boardName, String user) {
		this.boardName = boardName;
		this.teamLeader = user;
	}
	
    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(String owner) {
        this.teamLeader = owner;
    }


    
    public void addCollaborators(UserModel user) {
    	this.collaborators.add(user);
    }
    
    
    public void setCollaboratorsUsernames(Set<String> names) {
    	this.collaboratorsUsernames = names;
    }
    
    public Set<String> getCollaboratorsUsernames(){
    	return this.collaboratorsUsernames;
    }

    
    public void setLists(Set<ListModel> lists) {
    	this.lists = lists;
    }
    
    public Set<ListModel> getLists() {
    	return this.lists;
    }
    
    
    public void setListNames(Set<String> names) {
    	this.listNames = names;
    }
    
    public Set<String> getListNames(){
    	return this.listNames;
    }
    
}
