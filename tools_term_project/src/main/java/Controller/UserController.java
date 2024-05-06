package Controller;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Model.UserModel;
import Service.UserService;

@Path("")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

	
	@Inject
	private UserService UserService;

	@POST
	@Path("/register")
	public Response register(UserModel account) {		
	    try {
	        String response = UserService.register(account);
	        if (response.equals("invalid email!") || response.equals("username or email can not be empty!") || 
	            response.equals("Invalid password, the password must have at least one uppercase letter and at least 8 characters") ||
	            response.equals("Password can not be empty!")) {
	            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
	        } else if (response.equals("username or email already existed!")) {
	            return Response.status(Response.Status.CONFLICT).entity(response).build(); 
	        }
	        return Response.ok(response).build();  
	    } catch (Exception e) {
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred!").build();
	    }

	}

	@GET
    @Path("/accounts")
    public List<UserModel> getAllAccounts() {
    	 try {
    	        return UserService.getAllAccounts();
    	    } catch (Exception e) {
    	            return null;
    	      }
    }
    	
   

    @GET
    @Path("/loginWithEmail")
    public Response loginWithEmail(@QueryParam("email") String email, @QueryParam("password") String password) {
        
            String response = UserService.loginWithEmail(email, password);
            if(response.equals("Valid login!")) {
                return Response.ok(response).build();
            } else if (response.equals("Invalid email or password")) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
            } 
            else if(response.equals("email or password cannot be empty"))
            {
            	return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }
            else if (response.equals("Not found, Invalid email or password")) {
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred!").build();
            }
    }

    //done
    @GET
    @Path("/loginWithUserName")
    public Response loginWithUserName(@QueryParam("userName") String userName, @QueryParam("password") String password) {
            String response = UserService.loginWithUserName(userName, password);
            
            if(response.equals("Valid login!")) {
                return Response.ok(response).build();
            } else if (response.equals("Invalid username or password")) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
            } 
            else if(response.equals("Username or password cannot be empty"))
            {
            	return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }
            else if (response.equals("Not found ,Invalid username or password")) {
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred!").build();
            }
            
 
    }
    
    
    
//    @PUT
//    @Path("/update")
//    public Response update(
//    		@QueryParam String email , @QueryParam String newEmail,
//    		@QueryParam String userName , @QueryParam String newUserName,
//    		@QueryParam String password , @QueryParam String newPassword
//    		)
//    	{
//    	
//    	}


   

	@PUT
    @Path("/updateEmail")
    public Response updateEmail(@QueryParam("email") String email, @QueryParam("updatedEmail") String updatedEmail,
            @QueryParam("password") String password) {
        try {
            String response = UserService.updateEmail(email, updatedEmail, password);
            
            if(response.equals("email or password cannot be empty") || response.equals("Invalid email or password, email can't be changed") )
             	{return  Response.status(Response.Status.UNAUTHORIZED).entity(response).build();}
            else if(response.equals("Valid login, updated email is Invalid, Try entering another email.."))
            	{return Response.status(Response.Status.BAD_REQUEST).entity(response).build();}
            else if(response.equals("Valid login, updated email is already existed, Try entering another email.."))
            	{return Response.status(Response.Status.CONFLICT).entity(response).build();}
            else if(response.equals("Valid login, Your email has been updated to : " + updatedEmail))
            	{return Response.ok(response).build();}
            else
            	return Response.status(Response.Status.NOT_FOUND).entity(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred!").build();
        }
    }

    @PUT
    @Path("/updateUserName")
    public Response updateUserName(@QueryParam("userName") String userName,
            @QueryParam("updatedUserName") String updatedUserName, @QueryParam("password") String password) {
        try {
            String response = UserService.updateUserName(userName, updatedUserName, password);
            if(response.equals("updated username can not be null"))
            	return  Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            if(response.equals("userName or password cannot be empty") || response.equals("Invalid username or password , username can't be changed") )
         		{return  Response.status(Response.Status.UNAUTHORIZED).entity(response).build();}
      
            else if(response.equals("Valid login, updated username is already existed, Try entering another username.."))
        		{return Response.status(Response.Status.CONFLICT).entity(response).build();}
            else if(response.equals("Valid login, your username is updated to : " + updatedUserName))
        		{return Response.ok(response).build();}
            else
            	return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred!").build();
        }
    }

    @PUT
    @Path("/updatePassword")
    public Response updatePassword(@QueryParam("email") String email, @QueryParam("password") String password,
            @QueryParam("newPassword") String newPassword) {
        try {
            String response = UserService.updatePassword(email, password, newPassword);
            
            if(response.equals("email or password cannot be empty") || response.equals("new password can not be empty") ||
               response.equals("Valid login! The new password is invalid , Try entering another password.."))
            	{return  Response.status(Response.Status.BAD_REQUEST).entity(response).build();}
           
            else if(response.equals("Valid login, your new password is :" + newPassword))
            	 {return Response.ok(response).build();}
             
            else if(response.equals("Invalid email or password"))
              {return  Response.status(Response.Status.UNAUTHORIZED).entity(response).build();} 
            else return Response.status(Response.Status.NOT_FOUND).entity(response).build();
     
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred!").build();
        }
    }

    @DELETE
    @Path("/deleteAccount")
    public Response deleteAccount(@QueryParam("email") String email, @QueryParam("password") String password) {
        try {
            String response = UserService.deleteAccount(email, password);
            if(response.equals("email or password cannot be empty"))
            	{return  Response.status(Response.Status.BAD_REQUEST).entity(response).build();} 
            else if(response.equals("Account deleted successfully."))
            	return Response.ok(response).build();
            else if(response.equals("Invalid email or password."))
            {return  Response.status(Response.Status.UNAUTHORIZED).entity(response).build();} 
            else return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred!").build();
        }
    }
}












