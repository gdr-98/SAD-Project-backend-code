package TableAndOrdersArea;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import MenuAndWareHouseArea.OrderedItem;
import MenuAndWareHouseArea.OrderedItemState;
import UsersData.User;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
/**
 * 
 * @JSONRepresentation:
 * 	"orderID":"orderIDValue"
 *	"orderState":"orderStateValue"
 *	"completedItemNumber":"CompletedItemNumberValue"
 *	"tableRoomNumber":"tableRoomValue"
 *	"tableID":"tableIDValue"
 *	"orderedItems" :[OrderedItemJsonRappresentation1,...]
 *	"userID":	"userID"
 */
public class Order {

	/**
	 * @info: flag used to show only item with max priority
	 */
	public final static  Integer maxPrioFlag=-1;
	
	/**
	 * 
	 * @info List of states for the order
	 *
	 */
	public enum OrderStates{
		WaitingForWorking,
		Working,
		Completed;
	}
	
	@Expose
	private int orderID;
	
	@Expose
	private OrderStates orderState;
	
	@Expose(serialize=false,deserialize=false)
	List<OrderedItem> orderedItems=new ArrayList<>();

	@Expose
	private int completedItemNumber=0;
	
	/**
	 * @info null can be a valid value
	 */
	@Expose(serialize=false,deserialize=false)
	private Optional<Table> associatedTable=Optional.empty();

	@Expose(serialize=false,deserialize=false)
	private RestaurantController controller;
	
	private Optional<User> generator=null;
	/**
	 * 
	 * @param id of the order
	 * @param it table
	 * @param itemsNames names of the items
	 * @param additiveGoods additive goods of the items
	 * @param subGoods sub goods of the items
	 * @param priority of the items
	 */
	public Order(int id,Optional<Table>t,List<String>itemsNames,List<List<String>>additiveGoods,
			List<List<String>>subGoods,List<Integer>priority,Integer userID,
			RestaurantController  controller) {
		this.orderID=id;
		this.associatedTable=t;
		this.orderState=OrderStates.WaitingForWorking;
		this.controller=controller;
		this.orderedItems=generateItemRaw(itemsNames,additiveGoods,subGoods,priority);
		this.generator=Optional.empty();
		//Insert ordered items
		for(OrderedItem item:this.orderedItems)
			item.setLineNumber(this.getGreatestLineNumber()+1);
		
		//Insert the order into the database
		this.controller.getDB().insertOrderByJSON
			(this.getJSONRepresentation(Optional.empty()), userID);
		
		//register the order to the controller
		this.controller.registerOrder(this);
		
		//register the order
		this.generator=this.controller.askForOrderRegistration(userID.toString(), this);
	}
	
	public Order(String jsonRepresentation,Optional<Table> t,RestaurantController controller) {
		JsonObject obj=JsonParser.parseString(jsonRepresentation).getAsJsonObject();
		JsonArray arrayOfItems;
		JsonObject singleItem;
		List<String> helperList=new ArrayList<>();
		this.associatedTable=t;
		this.orderID=obj.get("orderID").getAsInt();
		this.associatedTable=t;
		this.controller=controller;
		this.generator=Optional.empty();
		this.completedItemNumber=obj.get("completedItemNumber").getAsInt();
		//System.out.println(obj.get("orderState").getAsString());
		this.orderState=OrderStates.valueOf(obj.get("orderState").getAsString());
		arrayOfItems=obj.get("orderedItems").getAsJsonArray();
		//Init items with a raw initialization
		JsonArray helper1;
		for(int i=0;i<arrayOfItems.size();i++) {
			
			singleItem=arrayOfItems.get(i).getAsJsonObject();
			OrderedItem item=this.controller.
					askForOrderedItem(singleItem.get("item").getAsString()).get();
			helper1=singleItem.get("sub").getAsJsonArray();
			for(int j=0;j<helper1.size();j++)
				helperList.add(helper1.get(j).getAsString());
			
			item.setPriority(singleItem.get("priority").getAsInt());
			item.changeSubGoods(helperList);
			item.setLineNumber(singleItem.get("lineNumber").getAsInt());
			//Ora lo stesso per le additive
			helperList.clear();
			helper1=singleItem.get("additive").getAsJsonArray();
			for(int j=0;j<helper1.size();j++)
				helperList.add(helper1.get(j).getAsString());
			item.changeAddGoods(helperList);
			this.orderedItems.add(item);
		}
		
		//register the order to the controller
		this.controller.registerOrder(this);
		
		//Register the order to the user.
		obj.get("userID").getAsString();
		this.generator=this.controller.askForOrderRegistration(obj.get("userID").getAsString(), this);
	}
	
	public void setUser(User u) {this.generator=Optional.of(u);}
	public Optional<User> getUser() { return this.generator;}
	/**
	 * 
	 * @param names of the items
	 * @param additive goods for the items
	 * @param toSub sub goods for the items
	 * @param priority list
	 * @return the list of raw ordered items
	 */
	public  boolean addOrderedItems(List<String>names,List<List<String>> additive,List<List<String>>toSub,
				List<Integer> priority) {
		
		if(this.orderState.equals(OrderStates.WaitingForWorking)) { //Only in this status
			List<OrderedItem> toAdd=generateItemRaw(names, additive, toSub, priority);
			//Should check the db ..
			for(OrderedItem item:toAdd) 
				addOrdedItemSingle(item);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param 	to_add, the orderedItem to be added 
	 * @return	true if the item is correctly added else false.
	 */
	private void addOrdedItemSingle(OrderedItem to_add) { 
			to_add.setLineNumber(this.getGreatestLineNumber()+1); 
			orderedItems.add(to_add);
			//Add the item in the database
			this.controller.getDB().addOrderedItemByJSON(to_add.getJSONRepresentation(), 
					this.orderID);	
	}
	/**
	 * 
	 * @return the number of ordered items
	 */
	public int numOfItems() { return this.orderedItems.size();}
	
	/**
	 * 
	 * @param names of the items
	 * @param additive goods for the items
	 * @param toSub sub goods for the items
	 * @param priority list
	 * @return the list of raw ordered items
	 */
	public List<OrderedItem> generateItemRaw(List<String>names,List<List<String>> additive,List<List<String>>toSub,
				List<Integer> priority){ //generate ordered items to add
	
			
				List<OrderedItem> toRet=new ArrayList<>();
				Optional<OrderedItem> helper;
				OrderedItem helper2;
				for(int i=0;i<names.size();i++){
					helper=this.controller.askForOrderedItem(names.get(i));
					if(helper.isPresent()) {
						helper2=helper.get();
						helper2.changeAddGoods(additive.get(i));
						helper2.changeSubGoods(toSub.get(i));
						helper2.setPriority(priority.get(i));
						toRet.add(helper2);
					}
				}
				return toRet;
	}
	
	
	
	/**
	 * @param ID to set
	 */
	public void setId(int ID) { this.orderID=ID;}
	
	/**
	 * @return order id
	 */
	public int getId() {return this.orderID;}
	
	/**
	 * @return the actual state of the order
	 */
	public OrderStates getState() {return this.orderState;}
	
	/**
	 * @return the string of the actual state
	 */
	public String getStateString() {return getState().name();}
	
	/**
	 * old
	 * @area	returns ordered items with just a specific area
	 * @return the json representation of the object
	 */
	public String getJSONRepresentation(Optional<String> area) {
		
		JsonArray helper=new JsonArray();
		String helperArea;
		JsonObject objectHelper;
		for(OrderedItem items:this.orderedItems) {
			if(area.isEmpty())
				helper.add(JsonParser.parseString(items.getJSONRepresentation()).getAsJsonObject());
			else {
				helperArea=area.get();
				objectHelper=JsonParser.parseString(items.getJSONRepresentation()).getAsJsonObject();
				if(objectHelper.get("itemArea").getAsString().equals(helperArea)) //If it matches the area
					helper.add(objectHelper);
				
			}
		}
		
		if(helper.isEmpty()&&area.isPresent()) //if no items in the area with an area request
			return  "";
		
		Gson gson = new GsonBuilder()
				  .excludeFieldsWithoutExposeAnnotation()
				  .create();
		JsonObject to_ret=gson.toJsonTree(this).getAsJsonObject();
		if(this.associatedTable.isEmpty()) {
			to_ret.addProperty("tableRoomNumber", "-1");
			to_ret.addProperty("tableID","");
		}
		else {
			to_ret.getAsJsonObject().addProperty("tableRoomNumber", this.associatedTable.get().getRoomNumber());
			to_ret.getAsJsonObject().addProperty("tableID",this.associatedTable.get().getId());
		}
		if(this.generator.isEmpty()) 
			to_ret.getAsJsonObject().addProperty("userID", "");
		else
			to_ret.getAsJsonObject().addProperty("userID", this.generator.get().getId());
		
		to_ret.add("orderedItems", helper);
		
		return gson.toJson(to_ret);
	}
	
	/**
	 * @return returns the associated table
	 */
	public Optional<Table> getTable() { return this.associatedTable;}
	

	
	/**
	 * @param:	line Number of the item to be removed
	 * @info	remove an ordered item if it is in list and it is cancellable
	 * @ret		true if the item was removed
	 */
	public boolean cancelOrderedItem(int lineNumber) {
		Optional<OrderedItem> item=this.getItemByLineNumber(lineNumber);
		if(item.isEmpty())
			return false;
		else {
			this.orderedItems.remove(item.get()); //remove from list
			this.controller.getDB().removeOrderedItem(this.orderID, lineNumber);
			if(this.orderedItems.size()==this.completedItemNumber) { //If it was the last item then the order is completed
				this.orderState=OrderStates.Completed; 
				//Update the db..
				this.controller.getDB().
				updateOrderByJSON(this.getJSONRepresentation(Optional.empty()));//remove the item from the database	
			}
			return true;
		}
		
	}
	/**
	 * 
	 * @param lineNumber
	 * @return the ordered item if present, else empty
	 */
	public Optional<OrderedItem> getItemByLineNumber(int lineNumber){
		for(OrderedItem item :orderedItems) {
			if(item.isMe(lineNumber)) { //If the item is present
				return Optional.of(item);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * 
	 * @param lineNumber of the item to be taken in work
	 * @return true if the item is inn working state, false if the item is not in working state or it isn't in list 
	 */
	public boolean takeItemInWorking(int lineNumber) {
		Optional<OrderedItem>helper=this.getItemByLineNumber(lineNumber);
		OrderedItem item;
		if(helper.isEmpty()) 
			return false;
		else {
			//Get the item
			item=helper.get();
			//change the state
			item.setWorking();
			//If the state is working, so it was really updated
			if(item.getState().equals("Working")) {
				//Update the item in the database
				this.controller.getDB().updateOrderedItemByJSON(item.getJSONRepresentation(), this.orderID);
				//if it was the first update the order status
				if(this.orderState.name().equals("WaitingForWorking")) 
					//if it was the first then set completed
				{
					this.orderState=OrderStates.Working;
					this.controller.getDB().
					updateOrderByJSON(this.getJSONRepresentation(Optional.empty()));
				}
				return true;
			}
			else 
				return false;
		}
	}
	
	/**
	 * 
	 * @param lineNumber of the item to be completed
	 * @return true if the item is completed, false if the item is not in completed state or it isn't in list 
	 */
	public boolean completeItem(int lineNumber) {
		Optional<OrderedItem>helper=this.getItemByLineNumber(lineNumber);
		OrderedItem item;
		if(helper.isEmpty()) 
			return false;
		else {
			//Get the item
			item=helper.get();
			//change the state
			item.complete();
			//If the state is working, so it was really updated
			if(item.getState().equals("Completed")) {
				//Increment the number
				completedItemNumber++;
				//once an order enters in working it can only be completed
				if(this.completedItemNumber==this.orderedItems.size())
					this.orderState=OrderStates.Completed;
				//update the database
				this.controller.getDB().
					updateOrderByJSON(this.getJSONRepresentation(Optional.empty()));
				this.controller.getDB().updateOrderedItemByJSON(item.getJSONRepresentation(), this.orderID);
				return true;
			}
			else 
				return false;
		}
		
	}
	
	/**
	 * @return number of completed items
	 */
	public int getCompleteItemNumber() { return this.completedItemNumber;}
	
	/**
	 * @return number of remaining items to complete
	 */
	public int getRemainingItemNumber() { return orderedItems.size()-this.completedItemNumber;}
	
	/**
	 * @return: get additive goods for a specific item if the item exists.
	 * Else returns empty
	 */
	public Optional<List<String>> getAdditiveForItem(int lineNumber){
		Optional<OrderedItem> item=this.getItemByLineNumber(lineNumber);
		if(item.isEmpty())
			return Optional.empty();
		else 
			return Optional.of(item.get().getAdditiveIDs());
		
	}
	
	/**
	 * @return: get sub goods for a specific item if the item exists.
	 * Else returns empty
	 */
	public Optional<List<String>> getSubForItem(int lineNumber){
		Optional<OrderedItem> item=this.getItemByLineNumber(lineNumber);
		if(item.isEmpty())
			return Optional.empty();
		else 
			return Optional.of(item.get().getSubIDs());
	}
	
	/**
	 * 
	 * @info	return the optional of the status code for the last operation with the item.
	 * 			If the item isn't in list then return empty
	 * @param	additiveGoods, list of additive goods to be changed
	 * @param	lineNumber of the ordered item to modify
	 * @return	status code of the operation
	 */
	public Optional<OrderedItemState.StatusCodes> setAdditiveGoodsForItem(List<String> additiveGoods,int lineNumber) {
		OrderedItemState.StatusCodes toRet;
		Optional<OrderedItem> helper=this.getItemByLineNumber(lineNumber);
		OrderedItem item;
		if(helper.isEmpty())
			return Optional.empty();
		else {
			item=helper.get();
			toRet=item.changeAddGoods(additiveGoods);
			this.controller.getDB().updateOrderedItemByJSON(item.getJSONRepresentation(), this.orderID);
			return Optional.of(toRet);
		}
	}
	
	/**
	 * 
	 * @info	return the optional of the status code for the last operation with the item.
	 * 			If the item isn't in list then return empty
	 * @param	subGoods, list of sub goods to be changed
	 * @param	lineNumber of the ordered item to modify
	 * @return	status code of the operation
	 */
	public Optional<OrderedItemState.StatusCodes> setSubGoodsForItem(List<String> subGoods,int lineNumber) {	
		OrderedItemState.StatusCodes toRet;
		Optional<OrderedItem> helper=this.getItemByLineNumber(lineNumber);
		OrderedItem item;
		if(helper.isEmpty())
			return Optional.empty();
		else {
			item=helper.get();
			toRet=item.changeSubGoods(subGoods);
			this.controller.getDB().updateOrderedItemByJSON(item.getJSONRepresentation(), this.orderID);
			return Optional.of(toRet);
		}
	}
	
	/**
	 * 
	 * @param newPriority of the ordered item
	 * @param lineNumber of the item
	 * @return the status code of th eoperation
	 */
	public Optional<OrderedItemState.StatusCodes> setPriorityForItem(int newPriority,int lineNumber){
		OrderedItemState.StatusCodes toRet;
		Optional<OrderedItem> helper=this.getItemByLineNumber(lineNumber);
		OrderedItem item;
		if(helper.isEmpty())
			return Optional.empty();
		else {
			item=helper.get();
			toRet=item.changePriority(newPriority);
			this.controller.getDB().updateOrderedItemByJSON(item.getJSONRepresentation(), this.orderID);
			return Optional.of(toRet);
		}
	}
	
	/**
	 * 
	 * @param lineNumber of the item
	 * @return  empty if  the item doesn't  exist else his priority
	 */
	public Optional<Integer> getPriorityForItem(int lineNumber){
		Optional<OrderedItem> item=this.getItemByLineNumber(lineNumber);
		if(item.isEmpty())
			return Optional.empty();
		else 
			return Optional.of(item.get().getPriority());
	}
	
	/**
	 * @info		Find the greater line number 
	 * @return		greater line number of the order
	 */
	public int getGreatestLineNumber() {
		
		int to_ret=0;
		for(OrderedItem item:this.orderedItems) {
			int helper=item.getLineNumber();
			if(helper>to_ret)
				to_ret=helper;
		}
		return to_ret;
	}
	
	/**
	 * 
	 * @param orderID
	 * @return returns true if it has the order has the same input id
	 */
	public boolean isMe(int orderID) {
			return this.orderID==orderID;
	}
	
	/**
	 * @return the list of ordered items
	 */
	public List<OrderedItem> getOrderedItems(){return this.orderedItems;}
	
	/**
	 * @info	sets the optional table
	 */
	public void setTable(Optional<Table> t) {this.associatedTable=t;}
	
	/**
	 * @returns true if an order is cancelled
	 */
	public boolean isCancellable() { //Ideally you can always cancel an order if it's not in the working state
		return !this.orderState.equals(OrderStates.Working);
	}
	

	/**
	 * 
	 * @param lineNumber of the item
	 * @return the json representation of the specific ordered item
	 */
	public String getOrderedItemJSON(int lineNumber){
		String toRet="{}";
		Optional<OrderedItem> item=this.getItemByLineNumber(lineNumber);
		if(item.isPresent())
			return item.get().getJSONRepresentation();
		return toRet;
	}	
	
	/**
	 * 
	 * @param lineNumber
	 * @return true if the item exists
	 */
	public boolean hasItem(int lineNumber) {
		Optional<OrderedItem> item=this.getItemByLineNumber(lineNumber);
		if(item.isPresent())
			return true;
		return false;
	}
	
	/**
	 * @area	returns ordered items with just a specific area
	 * @return the json representation of the object
	 */
	public String getJSONRepresentationAsCommande(String area) {
		
		if(area==null)
			return "";
		//all items in waiting for working
		List <OrderedItem> itemsToAdd=itemsInState(this.orderedItems,OrderedItemState.States.WaitingForWorking.name());
		JsonArray helper=new JsonArray();
		JsonObject objectHelper;
		//return the highest priority of the item inn waiting for order
		Integer prioToAdd=findItemsHighestPriority(itemsToAdd);
		//Now isolate the items with this specific priority
		itemsToAdd=itemsWithPriority(itemsToAdd,prioToAdd);
		//now select the items of the specific area
		itemsToAdd=itemsInArea(itemsToAdd,area);
		if(itemsToAdd.isEmpty()) //if no items in the area with an area request
			return  "";
		
		for(OrderedItem item:itemsToAdd) { //if the item is present the add his json representation
			objectHelper=JsonParser.parseString(item.getJSONRepresentation()).getAsJsonObject();
			helper.add(objectHelper.toString());
		}
		
		Gson gson = new GsonBuilder()
				  .excludeFieldsWithoutExposeAnnotation()
				  .create();
		JsonObject to_ret=gson.toJsonTree(this).getAsJsonObject();
		if(this.associatedTable.isEmpty()) {
			to_ret.addProperty("tableRoomNumber", "-1");
			to_ret.addProperty("tableID","");
		}
		else {
			to_ret.getAsJsonObject().addProperty("tableRoomNumber", this.associatedTable.get().getRoomNumber());
			to_ret.getAsJsonObject().addProperty("tableID",this.associatedTable.get().getId());
		}
		
		
		to_ret.add("orderedItems", helper);
		
		return gson.toJson(to_ret);
	}
	
	/**
	 * @info check if there are items in an input status having the same priority of the itemLineNumber
	 * @param itemLineNumber  ordered item from which we want extract priority
	 * @param state the specific status of the items we are looking for.
	 * @return
	 */
	public Optional<Boolean>hasItemsInStatus(int itemLineNumber,String state) {
		
		Optional<Boolean> toRet=Optional.empty();
		List<OrderedItem> helper;
		int priority=Integer.MAX_VALUE;
		//retrieve the priority of the item
		Optional<OrderedItem> item=this.getItemByLineNumber(itemLineNumber);
		if(item.isPresent())
			priority=item.get().getPriority();
		if(priority==Integer.MAX_VALUE) //No item..
			return toRet;
		
		toRet=Optional.of(false);
		//Get all items with this priority
		helper=itemsWithPriority(this.orderedItems,priority);
		//Isolate the state
		helper=itemsInState(helper,state);
		if(helper.size()!=0) // if there is at least one
			return Optional.of(true);
		return toRet;
	}
	
	
	/**
	 * @info  higher is the priority, then smaller is the number value (1 max priority ) 
	 * @param list of ordered items
	 * @return
	 */
	
	private static Integer findItemsHighestPriority(List<OrderedItem> items) {
		Integer highestPriority=Integer.MAX_VALUE;
		Integer helper;
		for(OrderedItem item:items) {
			helper=item.getPriority();
			if(helper<highestPriority) //smallest number then highest priority
				highestPriority=helper;
		}
		return highestPriority;
	}
	
	public boolean cancel() {
		boolean check=true;
		if(this.isCancellable()) {
			
			if(associatedTable.isPresent())
				check=check && associatedTable.get().unregisterOrder(orderID); //unreg from table
			
			if(generator.isPresent())
				check= check && generator.get().unregisterOrder(this); //unreg from user
			
			check=check && this.controller.unregisterOrder(this) //unreg from controller
					&&this.controller.getDB().removeOrderById(orderID); //remove from db
			return  check;
		}
		else
			return false;
	}
	
	
	/**
	 * 
	 * @param items
	 * @return the ordered items with max priority
	 */

	private static List<OrderedItem> itemsWithPriority(List<OrderedItem>items,Integer priority){
		List<OrderedItem>toRet=new ArrayList<>();
		for(OrderedItem item:items) {
			if(priority.equals(item.getPriority()))
				toRet.add(item);
	
		}
		return toRet;
	}
	
	/**
	 * 
	 * @param items
	 * @return the ordered items in a specific state
	 */
	private static List<OrderedItem> itemsInState(List<OrderedItem>items,String state){
		List<OrderedItem>toRet=new ArrayList<>();	
		for(OrderedItem item:items) {
			if(item.getState().equals(state))
				toRet.add(item);
		}
		return toRet;
	}
	
	/**
	 * 
	 * @param items
	 * @return the ordered items in a specific area
	 */
	private static List<OrderedItem> itemsInArea(List<OrderedItem>items,String area){
		List<OrderedItem>toRet=new ArrayList<>();	
		for(OrderedItem item:items) {
			if(item.getItem().getArea().equals(area))
				toRet.add(item);
		}
		return toRet;
	}
	
}
