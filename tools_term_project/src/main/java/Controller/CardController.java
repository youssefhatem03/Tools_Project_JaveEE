package Controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Model.CardModel;
import Service.CardService;

@Path("/card")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CardController {

	@Inject
	CardService cardService;
	
	@PUT
	@Path("/addCard")
	public Response addCard(@QueryParam("boardName") String boardName,
							@QueryParam("listName") String listName,
							@QueryParam("cardName") String cardName) {
		
		String res = cardService.addCard(boardName, listName, cardName);
		
		if (res.startsWith("Added Card")) {
			return Response.ok(res).build();
		} else if (res.startsWith("Board is not found")) {
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		} else if (res.startsWith("This card is already in this list")) {
            return Response.status(Response.Status.CONFLICT).entity(res).build(); 
		} else {
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
		}
		
		
	}
	
	
	@GET
	@Path("/getAllCards")
	public List<CardModel> getAll(){
		try {
			return cardService.getAll();
		} catch (Exception e){
			return null;
		}
		
	}
	
	
	@PUT
	@Path("/addAssignee")
	public Response addAssignee(@QueryParam("boardName") String boardName,
								@QueryParam("listName") String listName,
								@QueryParam("cardName") String cardName,
								@QueryParam("userName") String userName) {
		
		String res = cardService.assignUser(boardName, listName, cardName, userName);
		
		if (res.startsWith("Added")) {
			return Response.ok(res).build();
		} else if (res.startsWith("card does not exist") || res.startsWith("no such user found")
				|| res.startsWith("list not found in board")) {
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();	
		} else if (res.startsWith("this user is already assigned to this card")) {
            return Response.status(Response.Status.CONFLICT).entity(res).build(); 
		} else {
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
		}
	}
	
	
	@PUT
	@Path("/updateList")
	public Response updateList(@QueryParam("boardName") String boardName,
								@QueryParam("listName") String listName,
								@QueryParam("newListName") String newListName,
								@QueryParam("cardName") String cardName) {
		
		String res = cardService.changeList(boardName, listName, newListName, cardName);
		
		if (res.startsWith("Card moved from list")) {
			return Response.ok(res).build();
		} else if (res.contains("does not exist")) {
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();		
		} else if (res.startsWith("Card already exists in this list")) {
            return Response.status(Response.Status.CONFLICT).entity(res).build(); 
		} else {
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
		}
		
	}
	
	
	@PUT
	@Path("/addDescription")
	public Response addDescription(@QueryParam("boardName") String boardName,
									@QueryParam("listName") String listName,
									@QueryParam("cardName") String cardName,
									@QueryParam("description") String desc) {
		
		String res = cardService.addDescription(boardName, listName, cardName, desc);
		if (res.startsWith("Description added")) {
			return Response.ok(res).build();
		} else if (res.contains("does not exist")) {
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();		
		} else {
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
		}
		
	}
	
	@PUT
	@Path("/addComment")
	public Response addComment(@QueryParam("boardName") String boardName,
									@QueryParam("listName") String listName,
									@QueryParam("cardName") String cardName,
									@QueryParam("comment") String comment) {
		
		String res = cardService.addComments(boardName, listName, cardName, comment);
		if (res.startsWith("Comment added")) {
			return Response.ok(res).build();
		} else if (res.contains("does not exist")) {
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();		
		} else {
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
		}
		
	}
	
	
	
}
