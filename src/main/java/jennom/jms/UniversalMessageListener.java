package jennom.jms;

import com.google.gson.Gson;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import javax.swing.JOptionPane;
import jennom.iface.ISDTF;

@Component
public class UniversalMessageListener implements BeanNameAware {

    private String myName;
    @Inject
    private Gson gson;  
    @Inject
    private JmsTemplate jmsTemplate;   
    
    @Override
    public void setBeanName(String bname) {
       this.myName=bname; 
       //System.out.println("my name is = " + this.myName);
    }    
    
    @PostConstruct
    public void afterBirn() {
        System.out.println("my name is = " + this.myName);
    }      

    // ONLY 1-listener for 1-queue !!!
    @Async
    @JmsListener(destination = "harp07qq", containerFactory = "jmsListenerContainerFactory")
    public void onMessage(Object message) {
        System.out.println(" >>> Listener INFO: Received object = " + message.getClass().getName());
        System.out.println(" >>> Listener INFO: Received thread = " + Thread.currentThread().getName()+", run at: " + ISDTF.stf.format(new Date()));
        //if (message.getClass().getName().equals("jennom.jms.User")) {
        //if (message instanceof User) { 
        if (message.getClass() == ActiveMQObjectMessage.class) {
            try {
                ActiveMQObjectMessage activeMQObjectMessage = (ActiveMQObjectMessage) message;
                //User user = (User) ActiveMQObjectMessage.getObject();
                ObjectMessage receivedMessage=(ObjectMessage) jmsTemplate.receive();
                User user = (User) receivedMessage.getObject();
                //JOptionPane.showMessageDialog(null, " >>> Listener INFO: Received object user GSON = " + gson.toJson(user), "info", JOptionPane.ERROR_MESSAGE); 
                System.out.println(" >>> Listener INFO: Received object user GSON = " + gson.toJson(user));
                //System.out.println(" >>> Listener INFO: Received object user GSON = " + message.getClass().getName());
            } catch (JMSException | NullPointerException je) {
                System.out.println("Listener WARNING: JMS error = " + je.getMessage());                
            }
        } 
        //
        if (message.getClass() == ActiveMQTextMessage.class) {
            TextMessage textMessage = (TextMessage) message;
            try {
                User user = gson.fromJson(textMessage.getText(), User.class );
                //System.out.println("objListener INFO: >>> Received: " + textMessage.getText());
                //JOptionPane.showMessageDialog(null, " >>> Listener INFO: Received object user GSON = " + gson.toJson(user), "info", JOptionPane.ERROR_MESSAGE); 
                System.out.println(" >>> Listener INFO: Received text user GSON = " + gson.toJson(user));
                //System.out.println(" >>> Listener INFO: Received object user GSON = " + message.getClass().getName());
            } catch (JMSException ex) {
                System.out.println("Listener WARNING: JMS error = " + ex.getMessage());
            }                 
        }
    }
    
    //@JmsListener(destination = "harp07tt")
    public void receiveTopic(String message) {
        System.out.println("Отслеживать тему ============= Отслеживать тему");
        System.out.println(message);
 
    }    

}
