package broker;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import messages.baseMessage;

import controller.BrokerInterface;

public class Dispatcher implements BrokerInterface{
	
	@Autowired
	public SenderBrokerJMS sender;
	/**
	 * Le code del broker devono avere lo stesso nome delle code del generator solo 
	 * devono possedere in più la parola Broker
	 * La proxy source del messaggio non sarà altro che il
	 *  nome della coda nel request generator.
	 */
	@Override
	public void publishResponse(String response) {
		Gson gson=new Gson();
		baseMessage helper=gson.fromJson(response,baseMessage.class);
		sender.send(helper.proxySource+"Broker",response);
	}
}
