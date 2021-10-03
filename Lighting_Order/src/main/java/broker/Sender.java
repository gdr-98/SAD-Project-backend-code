package broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SenderJMS {

        @Autowired
        private JmsTemplate JmsTemp;

        @Value("")
        private String JmsQueue;
        
        @Value("CodaPizzaioli")
        private String JmsQueue2;
              
        @Value("Coda")
        private String JmsQueue3;
        
        public void sendMessage (String order) {
            JmsTemp.convertAndSend(JmsQueue, order);
        }
}
