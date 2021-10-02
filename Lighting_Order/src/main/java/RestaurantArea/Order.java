package RestaurantArea;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import MenuAndWareHouseArea.OrderedItem;
import MenuAndWareHouseArea.OrderedItemState;

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
 */
public class Order {

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

	/**
	 * 
	 * @param id of the order
	 * @param t table
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
		
		//Insert ordered items
		for(OrderedItem item:this.orderedItems)
			item.setLineNumber(this.getGreatestLineNumber()+1);
		
		//Insert the order into the database
		this.controller.getDB().insertOrderByJSON
			(this.getJSONRepresentation(Optional.empty()), userID);
		
		//register the order to the controller
		this.controller.registerOrder(this);
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
	}
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
			this.controller.getDB().addOrderedItem(to_add.getJSONRepresentation(), 
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
		for(OrderedItem item :orderedItems) {
			if(item.isMe(lineNumber)) { //If the item is present
				if(item.isCancellable()) { //if it can be cancelled
					this.orderedItems.remove(item);
					if(this.orderedItems.size()==this.completedItemNumber) { //If it was the last item then the order is completed
						this.orderState=OrderStates.Completed; 
						//Update the db..
						this.controller.getDB(). //remove the item from the database
						removeOrderedItem(this.orderID, lineNumber);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param lineNumber of the item to be taken in work
	 * @return true if the item is inn working state, false if the item is not in working state or it isn't in list 
	 */
	public boolean takeItemInWorking(int lineNumber) {
		for(OrderedItem item :orderedItems) {
			if(item.isMe(lineNumber)) {
				item.setWorking();
				if(item.getState().equals("Working")) {
					//Update the item
					this.controller.getDB().updateOrderedItem(item.getJSONRepresentation(), this.orderID);
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
			else
				System.out.println("Item not found");
		}
		return false;
	}
	
	/**
	 * 
	 * @param lineNumber of the item to be completed
	 * @return true if the item is completed, false if the item is not in completed state or it isn't in list 
	 */
	public boolean completeItem(int lineNumber) {
		for(OrderedItem item :orderedItems) {
			if(item.isMe(lineNumber)) {
				item.complete();
				if(item.getState().equals("Completed")) {
					completedItemNumber++;
					//once an order enters in working it can only be completed
					if(this.completedItemNumber==this.orderedItems.size())
						this.orderState=OrderStates.Completed;
					this.controller.getDB().
						updateOrderByJSON(this.getJSONRepresentation(Optional.empty()));
					this.controller.getDB().updateOrderedItem(item.getJSONRepresentation(), this.orderID);
					return true;
				}
				else
					return false;
			}
		}
		return false;
		
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
		for(OrderedItem item:orderedItems) {
			if(item.isMe(lineNumber))
				return Optional.of(item.getAdditiveIDs());
		}
		return Optional.empty();
	}
	
	/**
	 * @return: get sub goods for a specific item if the item exists.
	 * Else returns empty
	 */
	public Optional<List<String>> getSubForItem(int lineNumber){
		for(OrderedItem item:orderedItems) {
			if(item.isMe(lineNumber))
				return Optional.of(item.getSubIDs());
		}
		return Optional.empty();
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
		for(OrderedItem item:orderedItems) {
			if(item.isMe(lineNumber)) {
				toRet=item.changeAddGoods(additiveGoods);
				this.controller.getDB().updateOrderedItem(item.getJSONRepresentation(), this.orderID);
				return Optional.of(toRet);
			}	
		}
		return Optional.empty();
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
		for(OrderedItem item:orderedItems) {
			if(item.isMe(lineNumber)) {
				toRet=item.changeSubGoods(subGoods);
				this.controller.getDB().updateOrderedItem(item.getJSONRepresentation(), this.orderID);
				return Optional.of(toRet);
			}	
		}
		return Optional.empty();
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
		for(OrderedItem items :this.orderedItems) {
			if (items.isMe(lineNumber))
				return items.getJSONRepresentation();
		}
		return toRet;
	}
	
	/**
	 * 
	 * @param newPriority of the ordered item
	 * @param lineNumber of the item
	 * @return the status code of th eoperation
	 */
	public Optional<OrderedItemState.StatusCodes> setPriorityForItem(int newPriority,int lineNumber){
		OrderedItemState.StatusCodes toRet;
		for(OrderedItem items :this.orderedItems) {
			if (items.isMe(lineNumber)) {
				toRet=items.changePriority(newPriority);
				//The update of an ordered  item should be done from the item,
				//we should modify the dao but i'm  actually lazy..
				this.controller.getDB().updateOrderedItem(items.getJSONRepresentation(), this.orderID);
				return Optional.of(toRet);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * 
	 * @param lineNumber of the item
	 * @return  empty if  the item doesn't  exist else his priority
	 */
	public Optional<Integer> getPriorityForItem(int lineNumber){
		Optional<Integer> toRet=Optional.empty();
		for(OrderedItem items:this.orderedItems) {
			if(items.isMe(lineNumber))
				return Optional.of(items.getPriority());
		}
		return toRet;
	}
}
