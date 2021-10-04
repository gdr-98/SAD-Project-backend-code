package request_generator;

public interface controllerIface {
	
	public enum requests{
		tableRequest,
		userWaitingForOrderRequest,
		freeTableRequest,
		orderRequest,
		itemCompleteRequest,
		itemWorkingRequest,
		menuRequest,
		orderToTableGenerationRequest,
		cancelOrderRequest,
		cancelOrderedItemRequest,
		loginRequest
		;
	}
	
	public void tableRequest(String request);
	public void userWaitingForOrderRequest(String request);
	public void freeTableRequest(String request);
	public void orderRequest(String request);
	public void itemCompleteRequest(String request);
	public void itemWorkingRequest(String request);
	public void menuRequest(String request);
	public void orderToTableGenerationRequest(String request);
	public void cancelOrderRequest(String request);
	public void cancelOrderedItemRequest(String request);
	public void loginRequest(String request);
}
