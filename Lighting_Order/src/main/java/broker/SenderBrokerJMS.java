package broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import messages.orderNotification;
@Service("SenderBrokerJMS")
public class SenderBrokerJMS {

        @Autowired
        private JmsTemplate JmsTemp;
        
        @Value("CodaBarBroker") 
        private String barQueueBroker;
        
        @Value("CodaPizzaioliBroker") 
        private String bakeryQueueBroker;
        
        @Value("CodaChefBroker") 
        private String kitchenQueueBroker;
        
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
        
        /*
         * In pratica è usata per tutti i messaggi di conferma ai camerieri come 
         * table requests, cancelOrder e canncelOrderedItems...
         * Solo messaggi di conferma operazione o di inoltro di dati
         */
        public void sendWaitersInfo(String message) {
        	this.send(waitersQueueBroker,message);
        }
        
        public void sendOrderGenerationInfo(String message) {
        	this.send(waitersQueueBroker,message);
        }
        public void sendOrderToBakery(String message) {
			this.send(bakeryQueueBroker,message);
        }
        public void sendOrderToKitchen(String message) {
        	this.send(kitchenQueueBroker,message);
        }
        public void sendOrderToBar(String message) {
        	this.send(barQueueBroker,message);
        }
       
        /**
         * Eventi di eseguita operazionne/conferma da inviare agli addetti all'accoglienza
         */
        public void sendAcceptanceInfo(String message) {
        	this.send(acceptanceQueueBroker, message);
        }
          
        /**
         * Un cliente si è seduto ad un tavolo, questa è la notifica che arriva ad un cameriere
         */
        public void notifyWaitersForTable(String message) {
        	this.send(waitersQueueBroker, message);
        }
        
        /**
         * Invia un messaggio di avvenuta operazione per un realizzatore
         */
    	public void sendMakerInfo(String message) {
    		this.send(bakeryQueueBroker,message);
    		this.send(barQueueBroker,message);
    		this.send(kitchenQueueBroker,message);
    	}
    	
    	/**
    	 *  Notificare ai maker che devono effettuare un ordine
    	 */
    	public void sendMakerNotification(String message) {
    		Gson gson=new Gson();		
			orderNotification not=gson.fromJson(message, orderNotification.class);
			switch (not.area) {
				case "Cucina":
					this.sendOrderToKitchen(message);
					break;
				case "Bar":
					this.sendOrderToBar(message);
					break;
				case "Forno":
					this.sendOrderToBakery(message);
					break;
			}
    		
    	}
}
