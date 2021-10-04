package controller;




public interface BrokerInterface {
	/**
	 * @info publish a response to the broker
	 */
	public void publishResponse(String response);
}
