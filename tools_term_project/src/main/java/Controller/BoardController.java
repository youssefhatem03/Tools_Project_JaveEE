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
		
		if (res.startsWith("Board deleted succesfully")) {
			return Response.ok(res).build();
		} else {
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
		}
				
		}
	
	
	
	@PUT
	@Path("/addCollaborator")
	public Response addCollaborator(@QueryParam("boardName") String boardName, @QueryParam("userName") String userName) {
		
		String res = boardService.addCollaborator(boardName, userName);
		
		if (res.startsWith("Collaborator")) {
			return Response.ok(res).build();
		} else {
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
		}
		
	}
	
	

	
	
	
	
	}


































