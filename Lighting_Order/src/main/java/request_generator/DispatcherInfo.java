
package request_generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import messages.baseMessage;

@Controller
@ComponentScan(basePackages= {"controller"})
public class DispatcherInfo {
	private controllerIface controllerFunctions;
	
	@Autowired
	public DispatcherInfo(@Qualifier("SystemController") controllerIface input) {
		this.controllerFunctions=input;
	}
    public void callerFactory(String mex) {
    	
    	Gson gson=new Gson();
    	
    	baseMessage rec=gson.fromJson(mex, baseMessage.class);	
    	if(rec.messageName!=null) {
	    	if(rec.messageName.equals(controllerIface.requests.tableRequest.name()))
	    		controllerFunctions.tableRequest(mex);
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.userWaitingForOrderRequest.name()))
	    		controllerFunctions.userWaitingForOrderRequest(mex);
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.freeTableRequest.name()))
	    		controllerFunctions.freeTableRequest(mex);
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.itemCompleteRequest.name()))
	    		controllerFunctions.itemCompleteRequest(mex);
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.itemWorkingRequest.name()))
	    		controllerFunctions.itemWorkingRequest(mex);
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.orderRequest.name()))
	    		controllerFunctions.orderRequest(mex);
	       	
	    	else if(rec.messageName.equals(controllerIface.requests.menuRequest.name())) 
	    		controllerFunctions.menuRequest(mex);
	    	
	       
	    	else if(rec.messageName.equals(controllerIface.requests.orderToTableGenerationRequest.name()))
	    		controllerFunctions.orderToTableGenerationRequest(mex);
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.cancelOrderRequest.name()))
	    		controllerFunctions.cancelOrderRequest(mex);
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.cancelOrderedItemRequest.name()))
	    		controllerFunctions.cancelOrderedItemRequest(mex);
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.loginRequest.name()))
	    		controllerFunctions.loginRequest(mex); 
    	}
    		
    }
}
