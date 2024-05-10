package Messaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import Model.BoardModel;
import Model.UserModel;

@Startup
@Singleton
public class JMSClient {


    @PersistenceContext(unitName = "hello")
    private EntityManager EM;


    @Resource(mappedName = "java:/jms/queue/DLQ")
    private Queue queue;

    @Inject
    private JMSContext context;


    @Transactional
    public void sendMessage(String message) {
        Message messages = new Message();
        JMSProducer producer = context.createProducer();
        producer.send(queue, message);
        System.out.println("Message sent: " + message);

        // Set the updated messages back to the Message class
        messages.setMessages(message);

        EM.persist(messages);
    }

    @Transactional
    public List<Message> getAllMessages() {
        return EM.createQuery("SELECT m FROM Message m", Message.class).getResultList();
    }


//    public String receiveMessage() {
//        JMSConsumer consumer = context.createConsumer(queue);
//        String message = consumer.receiveBody(String.class);
//        System.out.println("Message received: " + message);
//        return message;
//    }

}