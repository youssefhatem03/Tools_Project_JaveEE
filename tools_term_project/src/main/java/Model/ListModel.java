//package Model;
//
//import javax.persistence.*;
//
//import java.util.Set;
//import javax.ejb.Stateless;
//import javax.validation.constraints.NotNull;
//
//@Stateless
//@Entity
//public class ListModel {
//	
//	@Id 
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long listId;
//	
//	@NotNull
//	@Column(unique=true)
//	private String listName;
//	
//	
//	@ManyToOne
//	@JoinColumn(name="boardId")
//	private BoardModel board;	
//	
//	
//}
