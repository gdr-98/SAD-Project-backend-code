package request_generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@Service
public class SalaReceiver implements MessageListener {

    @Autowired
    private DispatcherInfo dispatcherInfo;

    private String received ;

    @JmsListener(destination = "CodaCamerieri")
    @Override
    public void onMessage(Message message) {
        try {
            received = (String) message.getBody(String.class);
           /* System.out.println(received);
        	Gson gson=new Gson();
        	
        	baseMessage rec=gson.fromJson(received, baseMessage.class);	*/
           dispatcherInfo.callerFactory(received);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
}
