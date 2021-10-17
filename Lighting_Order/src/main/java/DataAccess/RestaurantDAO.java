package DataAccess;

public interface RestaurantDAO {

	//return all the orders in the system
	public String getAllTablesJSON();
	
	//Returns all the orders in the system
	public String getAllOrdersJSON(); 
	
	//Updates a table row using the json representation of the table
	public boolean updateTableByJSON(String tableJsonRepresentation);
	
	//Updates an order by using his json representation.
	public boolean updateOrderByJSON(String orderJsonRepresentation);
	
	public boolean updateOrderedItemByJSON(String orderedItemJsonRepresentation, int order_id);
	
	public boolean addOrderedItemByJSON(String orderedItemJsonRepresentation, int order_id);
	
	public boolean removeOrderedItem(int orderedItemID, int orderedItemLineNumber);
	
	// returns the next orderID 
	public int findNextOrderID();
	
	//inserts an order by using his json representation
	public boolean insertOrderByJSON(String orderJsonRepresetation, int dipendente_id);
	
	public boolean removeOrderById(int order_id);
	
	public String getOrderByIdJSON(int orderID);
	
}
