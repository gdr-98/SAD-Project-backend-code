package configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import controller.SystemController;
import request_generator.controllerIface;

@Configuration
public class Config {
	
@Bean("controllerIfaceImpl")
@Autowired
public controllerIface setControllerCallBack(SystemController controller) {
	return new controllerIface() {
		@Override
		public void tableRequest(String request) {
			// TODO Auto-generated method stub
			controller.tableRequest(request);
		}

		@Override
		public void userWaitingForOrderRequest(String request) {
			// TODO Auto-generated method stub
			controller.userWaitingForOrderRequest(request);
		}

		@Override
		public void freeTableRequest(String request) {
			// TODO Auto-generated method stub
			controller.freeTableRequest(request);
		}

		@Override
		public void orderRequest(String request) {
			// TODO Auto-generated method stub
			controller.orderRequest(request);
		}

		@Override
		public void itemCompleteRequest(String request) {
			// TODO Auto-generated method stub
			controller.itemCompleteRequest(request);
		}

		@Override
		public void itemWorkingRequest(String request) {
			// TODO Auto-generated method stub
			controller.itemWorkingRequest(request);
		}

		@Override
		public void menuRequest(String request) {
			// TODO Auto-generated method stub
			controller.menuRequest(request);
		}

		@Override
		public void orderToTableGenerationRequest(String request) {
			// TODO Auto-generated method stub
			controller.orderToTableGenerationRequest(request);
		}

		@Override
		public void cancelOrderRequest(String request) {
			// TODO Auto-generated method stub
			controller.cancelOrderRequest(request);
		}

		@Override
		public void cancelOrderedItemRequest(String request) {
			// TODO Auto-generated method stub
			controller.cancelOrderedItemRequest(request);
		}

		@Override
		public void loginRequest(String request) {
			// TODO Auto-generated method stub
			//controller.loginRequest(request);
		}
	};
}
}
