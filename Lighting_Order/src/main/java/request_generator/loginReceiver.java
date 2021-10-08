package request_generator;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class loginReceiver implements MessageListener {
	@Autowired
    private DispatcherInfo dispatcherInfo;

    String received;
    
    @JmsListener(destination = "CodaLogin")
    @Override
    public void onMessage(Message message) {
        try {
        	
            received = (String) message.getBody(String.class);
            dispatcherInfo.callerFactory(received);
            
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
}
