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
		//1 2 3 4
		case "menuRequest" :case "cancelOrderRequest" :case "cancelOrderedItemRequest": case "orderToTableGenerationRequest": 
			sender.sendWaitersInfo(response);
			break;
		//5 
		case "orderNotification":
			sender.sendMakerNotification(response);
			break;
		//6
		case "tableRequest":
			//Lo possono richiedere enntrambi, saranno poi i proxy a distinguere in base all'userID
			sender.sendAcceptanceInfo(response);
			sender.sendWaitersInfo(response);
			break;
		//7
		case "freeTableRequest":
			sender.sendAcceptanceInfo(response);
			break;
		//8
		case "userWaitingForOrderRequest":
			sender.sendAcceptanceInfo(response);
			//I camerieri devono essere informati che un utente voglia ordinare
			sender.notifyWaitersForTable(response);
			break;
		//9	10 11 
		case "itemCompleteRequest":case "itemWorkingRequest":case "orderRequest":
			sender.sendMakerInfo(response);
			break;
		
		default: //Invalid request
			break;
		
		}
	}
}
