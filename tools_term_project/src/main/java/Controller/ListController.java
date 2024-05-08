package Controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Model.ListModel;
import Service.BoardService;
import Service.ListService;

@Path("/list")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ListController {

    @Inject
    private ListService listService;

    
	
	@PUT
	@Path("/createList")
	public Response addList(@QueryParam("boardName") String boardName, @QueryParam("listName") String listName) {
		
		String res = listService.addList(boardName, listName);
		
		if(res.startsWith("List added successfully")) {
			return Response.ok(res).build();
		} else if (res.startsWith("Board does not exist")) {
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		} else if (res.startsWith("A list with this name already exists")) {
            return Response.status(Response.Status.CONFLICT).entity(res).build(); 
		}
		else {
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
		}
		
	}
	
	
	@DELETE
	@Path("/removeList")
	public Response removeList(@QueryParam("boardName") String boardName, @QueryParam("listName") String listName){
		
		String res = listService.removeList(boardName, listName);
		
		if (res.startsWith("List deleted successfully")) {
			return Response.ok(res).build();
		} else if (res.startsWith("Board does not exist")) {
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		} else if (res.startsWith("No list with this name exists")) {
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		} else {
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
		}
		
	}
	
	
	@GET
	@Path("/getAll")
	public List<ListModel> getAll() {
		try {
			return listService.getAllLists();
		} catch (Exception e){
			return null;
		}
	}
	
	
}


