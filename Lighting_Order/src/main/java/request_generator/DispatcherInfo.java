package request_generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	
	private controllerIface controllerFunctions;
	@Autowired
	public DispatcherInfo(@Qualifier("controllerIfaceImpl") controllerIface input) {
		this.controllerFunctions=input;
	}
    public void callerFactory(String mex) {
    	
    	Gson gson=new Gson();
    	baseMessage rec=gson.fromJson(mex, baseMessage.class);	
    	
    	if(rec.request.equals(controllerIface.requests.tableRequest.name()))
    		controllerFunctions.tableRequest(gson.toJson(mex,tableRequest.class));
    	
    	else if(rec.request.equals(controllerIface.requests.userWaitingForOrderRequest.name()))
    		controllerFunctions.userWaitingForOrderRequest(gson.toJson(mex,tableOperation.class));
    	
    	else if(rec.request.equals(controllerIface.requests.freeTableRequest.name()))
    		controllerFunctions.freeTableRequest(gson.toJson(mex,tableOperation.class));
    	
    	else if(rec.request.equals(controllerIface.requests.itemCompleteRequest.name()))
    		controllerFunctions.itemCompleteRequest(gson.toJson(mex,itemOpRequest.class));
    	
    	else if(rec.request.equals(controllerIface.requests.itemWorkingRequest.name()))
    		controllerFunctions.itemWorkingRequest(gson.toJson(mex,itemOpRequest.class));
    	
    	else if(rec.request.equals(controllerIface.requests.orderRequest.name()))
    		controllerFunctions.orderRequest(gson.toJson(mex,orderRequest.class));
       	
    	else if(rec.request.equals(controllerIface.requests.menuRequest.name()))
    		controllerFunctions.menuRequest(gson.toJson(mex,menuRequest.class));
       
    	else if(rec.request.equals(controllerIface.requests.orderToTableGenerationRequest.name()))
    		controllerFunctions.orderToTableGenerationRequest(gson.toJson(mex,orderToTableGenerationRequest.class));
    	
    	else if(rec.request.equals(controllerIface.requests.cancelOrderRequest.name()))
    		controllerFunctions.cancelOrderRequest(gson.toJson(mex,cancelOrderRequest.class));
    	
    	else if(rec.request.equals(controllerIface.requests.cancelOrderedItemRequest.name()))
    		controllerFunctions.cancelOrderedItemRequest(gson.toJson(mex,itemOpRequest.class));
    	
    	else if(rec.request.equals(controllerIface.requests.loginRequest.name()))
    		controllerFunctions.loginRequest(gson.toJson(mex,loginRequest.class));  
    }
}
