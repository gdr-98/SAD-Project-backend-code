package broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service("SenderBrokerJMS")
public class SenderBrokerJMS {

        @Autowired
        private JmsTemplate JmsTemp;
        
        @Value("CodaBarBroker") //Da aggiungere
        private String realizzatorProxyQueue;
        
        @Value("CodaCamerieriBroker")
        private String waitersProxyQueue;
        
        @Value("CodaAccoglienzaBroker")
        private String acceptaceProxyQueue;
        
        
        public enum queuesBroker{
        	realizzatorProxyQueue,
        	acceptanceProxyQueue,
        	waitersProxyQueue;
        }
        
        public void send(String nomeCoda,String message) {
        	JmsTemp.convertAndSend(nomeCoda,message);
        }       
}
