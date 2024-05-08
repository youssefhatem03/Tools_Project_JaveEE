package Controller;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import Model.BoardModel;
import Service.BoardService;
import Service.UserService;
import java.util.List;

	
@Path("/board")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BoardController {

    @Inject
    private BoardService boardService;

    
    @POST
    @Path("/create")
    public Response createBoard(BoardModel board) {
        board.setTeamLeader(UserService.username);
        String res = boardService.createBoard(board);

        if (res.startsWith("Board added successfully!")) {
            return Response.ok(res).build();
        } else if (res.equals("Board Name cannot be empty")) {
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        } else if (res.equals("Board Name already exists!")) {
            return Response.status(Response.Status.CONFLICT).entity(res).build(); 
        } else if (res.equals("You must be signed in to create a board!")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(res).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }

    
    
    @GET
    @Path("/all")
    public List<BoardModel> getAllBoards() {
        return boardService.getAllBoards();
    }

    
    
    @GET
    @Path("/user")
    public List<BoardModel> getUserBoards() {
        String username = UserService.username;
        return boardService.getUserBoards(username);
    }
    
    


	@DELETE
	@Path("/delete")
    public Response deleteBoard(@QueryParam("boardName") String boardName) {
        String res = boardService.deleteBoard(boardName);

        if (res.startsWith("Board deleted successfully")) {
            return Response.ok(res).build();
        } else if (res.startsWith("No Board with such name exists")) {
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();        
        } else if (res.startsWith("You are not the Team Leader of this board, access denied")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(res).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
    }
	
	
	
	@PUT
	@Path("/addCollaborator")
    public Response addCollaborator(@QueryParam("boardName") String boardName, @QueryParam("userName") String userName) {
        String res = boardService.addCollaborator(boardName, userName);

        if (res.startsWith("Collaborator")) {
            return Response.ok(res).build();
        } else if (res.equals("Board does not exist") || res.equals("User or Board not found")) { 
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();        
        } else if (res.startsWith("user is already added in this board")) {
            return Response.status(Response.Status.CONFLICT).entity(res).build(); 
        } else if (res.equals("Please enter the username of the user you want to invite") || res.equals("Can't add yourself to the board")) {
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
    }
	
	
	}
