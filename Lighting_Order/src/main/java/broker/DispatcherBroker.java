package broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import messages.baseMessage;

import controller.BrokerInterface;

@Service("brokerDispatcher")
public class DispatcherBroker implements BrokerInterface{
	
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
		//System.out.println(response);
		Gson gson=new Gson();
		baseMessage helper=gson.fromJson(response,baseMessage.class);
		sender.send(helper.proxySource+"Broker",response);
	}
}
