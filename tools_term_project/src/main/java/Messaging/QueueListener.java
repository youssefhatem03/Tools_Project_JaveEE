package Messaging;

import javax.jms.MessageListener;
import javax.jms.Message;
import javax.ejb.ActivationConfigProperty;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.ejb.MessageDriven;

@MessageDriven(name = "queue", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/DLQ"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")})
public class QueueListener implements MessageListener{

    public void onMessage(Message rcvMessage) {
        TextMessage msg = null;
        try {
            if (rcvMessage instanceof TextMessage) {
            msg = (TextMessage) rcvMessage;
                System.out.println("Received Message from helloWorldQueue ===> " + msg.getText());
            } else {
                System.out.println("Message of wrong type: " + rcvMessage.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}