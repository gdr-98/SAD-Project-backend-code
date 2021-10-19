package TableAndOrdersArea;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

/**
 * 
 * @JSONRepresentation 
 *	tableID= "tableIDvalue"
 *	tableRoomNumber="roomNumberValue"
 *	tableState="Valueofthestate"
 *	orders=[orderJsonRepresentation1...]
 */
public class Table {
	
	/**
	 * @info	A table is univocally identified by his id and the room number.
	 */
	@Expose (serialize=true,deserialize=true)
	private String tableID;
	
	@Expose(serialize=true,deserialize=true)
	private int tableRoomNumber;
	
	/**
	 * @info	List of orders
	 */
	@Expose(serialize=false,deserialize=false)
	private List<Order> orders=new ArrayList<>();
	
	/**
	 * @info	Actual state of the table
	 */
	@Expose(serialize=false,deserialize=false)
	private TableState actualState;
	
	@Expose(serialize=false,deserialize=false)
	private RestaurantController controller=null;
	
	/**
	 * 
	 * @param id of the table
	 * @param roomNumber of the table
	 * @param isReserved reserved status
	 * @param controller reference to the controller
	 */
	public Table(String id,int roomNumber,boolean isReserved,RestaurantController controller) {
		this.tableID=id;
		this.tableRoomNumber=roomNumber;
		this.actualState=TableState.init(this,isReserved);
		this.controller=controller;
	}
	
	/**
	 * 
	 * @return	table id
	 */
	public String getId() { return this.tableID ;}
	
	/**
	 * 
	 * @return	table room number
	 */
	public int getRoomNumber() { return this.tableRoomNumber;}
	
	/**
	 * 
	 * @param	id of the table to compare
	 * @param	roomNumber of the table to compare
	 * @return	true if both value matches.
	 */
	public boolean isMe(String id,int roomNumber) { return (this.tableID.equals(id))&&(this.tableRoomNumber==roomNumber);}
	
	/**
	 * 
	 * @return	the json representation of the object
	 */
	public String getJSONRepresentation(Optional<String>orderArea){
		Gson gson = new GsonBuilder()
				  .excludeFieldsWithoutExposeAnnotation()
				  .create();
		 JsonObject to_ret=gson.toJsonTree((Object)this).getAsJsonObject();
		 JsonArray helper=new JsonArray();
		 String  helper1;
		 for(Order order:this.orders) {
			helper1=order.getJSONRepresentation(orderArea);
			if(!helper1.isBlank()) //add the order only if it is not null
				helper.add(JsonParser.parseString(helper1).getAsJsonObject());
		 }
		 
		 to_ret.getAsJsonObject().add("orders", helper);
		 to_ret.addProperty("tableState", this.actualState.getState().name());
		 return gson.toJson(to_ret);
	}
	
	/**
	 * @info	Add Order to the table, the function is meant to be called only from the order state or in the controller
	 * @param	Order to be added
	 * @return	true if the order is added else false
	 */
	public boolean addOrderRaw(Order o) {
		
		if(o==null) {
			return false;
		}
		return orders.add(o);
	}
	
	/**
	 * @info	Add Order to the table
	 * @param	Order to be added
	 * @return	true if the order is added else false
	 */
	
	public Optional<Order> addOrder(List<String>itemNames,List<List<String>> additive,List<List<String>>toSub,
			
			List<Integer> priority,Integer userID) {
		return actualState.makeOrder(itemNames,additive,toSub,priority,userID); 
	}
	
	/**
	 * @info	Cancel of orders of the table
	 */
	public void clearOrders() {
		
		for( int i=0;i<this.orders.size();i++) 
			this.orders.get(i).cancel();
		
	}
	
	/**
	 * @info this function is meant to be called ony from the states of the table and not outsine
	 * @param newState
	 */
	public void changeState(TableState newState) { 
		
		this.actualState=newState;
		this.controller.getDB().updateTableByJSON(this.getJSONRepresentation(Optional.empty()));
	}
	
	/**
	 * @return gets the actual state
	 */
	public TableState.StatesList getState() { return this.actualState.getState();}
	
	/**
	 * @return get the state name in form of a string
	 */
	public String getStateString() { return this.getState().name();}
	
	/**
	 * @return get the status code for the last operation of the table
	 */
	public TableState.StatusCodes getStatusCode() { return this.actualState.getStatusCode();}
	
	/**
	 * @return get the status code for the last operation in a string form
	 */
	public String getStatusCodeString() { return this.getStatusCode().name();}
	
	/**
	 * @info set the table in waiting for orders state
	 */
	public void setInWaitingForOrders() { this.actualState.putInWaiting();}
	
	/**
	 * @info free the table 
	 */
	public void free() { this.actualState.free();}
	
	/**
	 * @info free the table 
	 */
	public void reserve() { this.actualState.reserve();}
	
	/**
	 * @info unlock from reservation
	 */
	public void unlockFromReservation(boolean reservationCancelled) { this.actualState.unlockFromReserve(reservationCancelled);}

	/**
	 * @info : Just returns the order list
	 * @return the list of orders
	 */
	public List<Order> getOrdersList(){ return this.orders;}
	
	/**
	 * @info	sets the state of the table from a string
	 * @param newState
	 */
	public void setStateFromString(String newState) {
		
		if(newState.equals(TableState.StatesList.free.name())) {
			this.actualState=new TableStateFree(this);
		}
		else if(newState.equals(TableState.StatesList.Occupied.name())) {
			this.actualState=new TableStateOccupied(this);
		}
		else if(newState.equals(TableState.StatesList.waitingForOrders.name())) {
			this.actualState=new TableStateWaitingForOrders(this);
		}
		else if(newState.equals(TableState.StatesList.reserved.name())) {
			this.actualState=new TableStateReserved(this);
		}
		else //default, set as free
			this.actualState=new TableStateFree(this);
	}
	
	/**
	 * @return true if the table is in the input room 
	 */
	public boolean isInRoom(int roomNumber) {return this.tableRoomNumber==roomNumber;}
	
		
	/**
	 * 
	 * @param json representation of the table
	 * @return the table generated from the json input
	 */
	public static Table getTableFromJSON(String json,RestaurantController c) {
		Gson gson = new GsonBuilder()
				  .excludeFieldsWithoutExposeAnnotation()
				  .create();
		
		JsonObject to_init=JsonParser.parseString(json).getAsJsonObject();
		Table toRet=gson.fromJson(to_init, Table.class);
		toRet.initOrders();
		toRet.setController(c);
		
		//init the state
		toRet.setStateFromString(to_init.get("tableState").getAsString());
		JsonArray arrayOfOrders=to_init.get("orders").getAsJsonArray();
		for(int i=0;i<arrayOfOrders.size();i++) {
			toRet.addOrderRaw(new Order(arrayOfOrders.get(i).toString(),Optional.of(toRet),c));
		}
		return toRet;
	}
	
	public void setController(RestaurantController c) { this.controller=c;}
	
	public RestaurantController getController() { return this.controller;}
	
	/**
 	 * @info when obtaining the object  from json we need to re-init the orderslist
 	 */
 	public void initOrders() { this.orders=new ArrayList<>();}
 	
 	/**
 	 * @info : 	utility function that removes an order from the table.
 	 * 			No need to check the state since it could always be done.
 	 * 			This function is meant to be called only from the order
 	 * @return: true if the element is present
 	 */
 	public  boolean unregisterOrder(int orderID) {
 	
 		for(int i=0;i<this.orders.size();i++) {
 			if(this.orders.get(i).getId()==orderID) {
 				this.orders.remove(i);
 				return true;
 			}
 		}
 		
 		return false;
 	}
}
