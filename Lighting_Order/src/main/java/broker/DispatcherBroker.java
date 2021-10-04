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
	
	@Override
	public void publishResponse(String response) {
		Gson gson=new Gson();
		baseMessage helper=gson.fromJson(response,baseMessage.class);
		switch (helper.request){
		
		case "menuRequest":
			sender.sendMenuRequest(response);
			break;
		case "orderToTableGenerationRequest":
			sender.sendOrderGenerationConfirmation(response);
			sender.sendOrderToKitchen(response);
			sender.sendOrderToBakery(response);
			sender.sendOrderToBar(response);
			break;
		default: //Invalid request
			break;
		
		}
	}
}
