package jennom.jms;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import jennom.iface.ISDTF;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component("txtSender")
@DependsOn(value = {"universalMessageListener"})
public class MessageSenderTxt implements BeanNameAware { //implements MessageSender {
    
    private String myName;
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

    //@Override
    // send like this: {login:"zz", passw:"xx"}
    @Async
    public void sendMessage(String destinationNameQ, String message) {
        //jmsTemplate.setDeliveryDelay(500L);
        this.jmsTemplate.send(destinationNameQ, (Session session) -> {
            TextMessage jmsMessage = session.createTextMessage(message);
            jmsMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 9999);
            System.out.println(">>> Sending txt user: " + jmsMessage.getText());
            //System.out.println(">>> Sending txt user thread = " + Thread.currentThread().getName()+", run at: " + ISDTF.stf.format(new Date()));
            return jmsMessage;
        });
    }
   
}
