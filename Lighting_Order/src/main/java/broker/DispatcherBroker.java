package broker;
/****
 * There are two typed of messages : requests and messages.
 * Requests are both the request of an operation from an user and the response for this operation.
 * Notifications are events generated from the business logic after a processing event.
 * Eventually receivers on jms must wait for both request(Info functions ) and for Notifications.
 * Basically in the actual architecture with proxies a proxy will always send requests and wait for 
 * notification.
 * A request and a notification can be generated from the same message.
 * As an example a userWaitingForOrdersRequest( the response of the business logic for this operation)
 * and the notification to waiters(tableNotification) are generated from:tableOperation
 * As contrary, when you generate an order, the waiter has the confirmation with the message:
 * orderToTableGenerationRequest.
 * Makers will be notified(orderNotification) using the message orderNotifications.
 */

/****
 * List of requests:
 *		1-tableRequest,
 *		2-userWaitingForOrderRequest,
 *		3-freeTableRequest,
 *		4-orderRequest,
 *		5-itemCompleteRequest,
 *		6-itemWorkingRequest,
 *		7-menuRequest,
 *		8-orderToTableGenerationRequest,	
 *		9-cancelOrderRequest,
 *		10-cancelOrderedItemRequest,
 *		11-loginRequest
 */

/***
 * List of notification:
 * 	1-orderNotification
 * 	2-userWaitingNotification
 * 	3-registerNotification
 */
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
		switch (helper.messageName){
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
			/*//Lo possono richiedere enntrambi, saranno poi i proxy a distinguere in base all'userID
			sender.sendAcceptanceInfo(response);
			sender.sendWaitersInfo(response);*/
			sender.handleTableRequest(response);
			break;
		//7
		case "freeTableRequest":
			sender.sendAcceptanceInfo(response);
			break;
			
		//8
		case "userWaitingForOrderRequest":
			sender.sendAcceptanceInfo(response);
			break;
			
		//9	10 11 
		case "itemCompleteRequest":case "itemWorkingRequest":case "orderRequest":
			sender.sendMakerInfo(response);
			break;
		//12
		case "userWaitingNotification":
			//I camerieri devono essere informati che un utente voglia ordinare
			sender.sendWaitersNotification(response);
			break;
		
		//13
		case "loginRequest":
			sender.sendLoginInfo(response);
			break;
			
		//14
		case "registerNotification":
			sender.sendRegisterNotification(response);
			break;
		
		default: //Invalid request
			break;
		
		}
	}
}
