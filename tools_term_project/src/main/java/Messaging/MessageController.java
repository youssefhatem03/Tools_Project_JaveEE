package Messaging;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Messaging.JMSClient;
import Messaging.Message;
import Model.BoardModel;
import Service.BoardService;

@Path("/message")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageController {

    @Inject
    JMSClient jmsclient;

    @GET
    @Path("/all")
    public List<Message> getAllMessage() {
        return jmsclient.getAllMessages();
    }

}