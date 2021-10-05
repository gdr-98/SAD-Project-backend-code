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
		//1 2 3
		case "menuRequest" :case "cancelOrderRequest" :case "cancelOrderedItemRequest":
			sender.sendWaitersConfirmation(response);
			break;
		//4 
		case "orderToTableGenerationRequest": 
			sender.sendOrderGenerationConfirmation(response);
			sender.sendOrderToKitchen(response);
			sender.sendOrderToBakery(response);
			sender.sendOrderToBar(response);
			break;
		//5
		case "tableRequest":
			//Lo possono richiedere enntrambi, saranno poi i proxy a distinguere in base all'userID
			sender.sendAcceptanceConfirmation(response);
			sender.sendWaitersConfirmation(response);
			break;
		//6
		case "freeTableRequest":
			sender.sendAcceptanceConfirmation(response);
			break;
		//7
		case "userWaitingForOrderRequest":
			sender.sendAcceptanceConfirmation(response);
			//I camerieri devono essere informati che un utente voglia ordinare
			sender.notifyWaitersForTable(response);
			break;
		//8	9 10 
		case "itemCompleteRequest":case "itemWorkingRequest":case "orderRequest":
			sender.sendRealizzatorConfirmation(response);
			break;
		
		default: //Invalid request
			break;
		
		}
	}
}
