package request_generator;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import messages.baseMessage;
import messages.cancelOrderRequest;
import messages.itemOpRequest;
import messages.loginRequest;
import messages.menuRequest;
import messages.orderRequest;
import messages.orderToTableGenerationRequest;
import messages.tableOperation;
import messages.tableRequest;

@Controller
public class DispatcherInfo {
	
	private controllerIface acceptance;
	
    public void callerFactory(String mex) {
    	
    	Gson gson=new Gson();
    	baseMessage rec=gson.fromJson(mex, baseMessage.class);	
    	
    	if(rec.request.equals(controllerIface.requests.tableRequest.name()))
    		acceptance.tableRequest(gson.toJson(mex,tableRequest.class));
    	
    	else if(rec.request.equals(controllerIface.requests.userWaitingForOrderRequest.name()))
    		acceptance.userWaitingForOrderRequest(gson.toJson(mex,tableOperation.class));
    	
    	else if(rec.request.equals(controllerIface.requests.freeTableRequest.name()))
    		acceptance.freeTableRequest(gson.toJson(mex,tableOperation.class));
    	
    	else if(rec.request.equals(controllerIface.requests.itemCompleteRequest.name()))
    		acceptance.itemCompleteRequest(gson.toJson(mex,itemOpRequest.class));
    	
    	else if(rec.request.equals(controllerIface.requests.itemWorkingRequest.name()))
    		acceptance.itemWorkingRequest(gson.toJson(mex,itemOpRequest.class));
    	
    	else if(rec.request.equals(controllerIface.requests.orderRequest.name()))
    		acceptance.orderRequest(gson.toJson(mex,orderRequest.class));
       	
    	else if(rec.request.equals(controllerIface.requests.menuRequest.name()))
    		acceptance.menuRequest(gson.toJson(mex,menuRequest.class));
       
    	else if(rec.request.equals(controllerIface.requests.orderToTableGenerationRequest.name()))
    		acceptance.orderToTableGenerationRequest(gson.toJson(mex,orderToTableGenerationRequest.class));
    	
    	else if(rec.request.equals(controllerIface.requests.cancelOrderRequest.name()))
    		acceptance.cancelOrderRequest(gson.toJson(mex,cancelOrderRequest.class));
    	
    	else if(rec.request.equals(controllerIface.requests.cancelOrderedItemRequest.name()))
    		acceptance.cancelOrderedItemRequest(gson.toJson(mex,itemOpRequest.class));
    	
    	else if(rec.request.equals(controllerIface.requests.loginRequest.name()))
    		acceptance.loginRequest(gson.toJson(mex,loginRequest.class));
    	
   
    }
}
