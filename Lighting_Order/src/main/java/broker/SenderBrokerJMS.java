package broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service("SenderBrokerJMS")
public class SenderBrokerJMS {

        @Autowired
        private JmsTemplate JmsTemp;
        
        @Value("CodaBarBroker") 
        private String realizzatorProxyQueue;
        
        @Value("CodaCamerieriBroker")
        private String waitersQueueBroker;
        
        @Value("CodaAccoglienzaBroker")
        private String acceptanceQueueBroker;
        
        /**
         * 
         * @info : Base function to send something in a queue
         * @param queueName
         * @param message
         */
        public void send(String queueName,String message) {
        	JmsTemp.convertAndSend(queueName,message);
        }   
        
        public void sendMenuRequest(String message) {
        	this.send(waitersQueueBroker,message);
        }
        
        public void sendTableRequest(String message) {
        	this.send(waitersQueueBroker,message);
        }
}
