package RestaurantArea;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import DataAccess.RestaurantDAO;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import MenuAndWareHouseArea.MenuAndGoodsController;
import MenuAndWareHouseArea.OrderedItem;
import MenuAndWareHouseArea.OrderedItemState;

@Service
@ComponentScan(basePackages= {"DataAccess"})
public class RestaurantController {
	
	public enum returnCodes{
		
		tableNotFound,
		orderNotCreated,
		orderNotFound,
		itemNotFound,
		itemNotCanceled,
		itemsNotAdded,
		orderFound,
		itemFound;
	}
	
	private List<Table> tables=new ArrayList<>();
	private List<Order> orders=new ArrayList<>();
	private RestaurantDAO db;
	private MenuAndGoodsController menuAndWarehouseController;
	@Autowired
	public RestaurantController(@Qualifier("psqlRestaurant")RestaurantDAO db,MenuAndGoodsController c) {
		this.db=db;
		this.menuAndWarehouseController=c;
		 //retrieves all tables, the tables must ALWAYS be in the system
		this.initTablesJSON(db.getAllTablesJSON());
		
	}
	
	/**
	 * @info: inits all tables from a json array
	 * @param tablesJSONArray
	 */
	private void initTablesJSON(String tablesJSONArray) {
		JsonArray array=JsonParser.parseString(tablesJSONArray).getAsJsonArray();
		Table toAdd;
		for(int i=0;i<array.size();i++) { //generate all the tables
			toAdd=Table.getTableFromJSON(array.get(i).toString(),this);
			//toAdd.setController(this);
			this.tables.add(
					toAdd
					);
		}
		
	}
		
	/**
	 * @info: utility function, the controller registers to the controller in his costructor
	 * @param o order to be registered
	 */
	public void registerOrder(Order o) { this.orders.add(o);}

	/**
	 * @return a reference to the DAO
	 */
	public RestaurantDAO getDB() { return this.db;}
	
	
	/**
	 * @info request an ordered item to the menuArea
	 * @param menuItemName
	 * @return
	 */
	public Optional<OrderedItem> askForOrderedItem(String menuItemName) {
		return this.menuAndWarehouseController.generateOrderedItem(menuItemName);
	}
	
	/**
	 *
	 * @param orderID to cancel
	 * @return empty if the order was found or Option.of(result)
	 */
	public Optional<Boolean> cancelOrder(int orderID) {

		for(Order o:this.orders) {
			if(o.isMe(orderID)) {
				if(o.isCancellable()) {
					this.orders.remove(o);
					db.removeOrderById(orderID);
					return Optional.of(true);
				}
				else
					return Optional.of(false);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * 
	 * @roomNumber the value of the room
	 * @return	the list of all tables in the system
	 */
	public List<Table> getTables(Optional<Integer> roomNumber){
		List<Table>toRet=new ArrayList<>();
		if(roomNumber.isEmpty())
			toRet= this.tables;
		else {
			for(Table t:this.tables) {
				if(t.isInRoom(roomNumber.get()))
					toRet.add(t);
			}
		}
		return toRet;
	}
	
	/**
	 * @param room number of the tables
	 * @param order Area area of the order to show
	 * @return all tables in jsonn rappresentation
	 */
	public String getAllTablesJSON(Optional<Integer>roomNumber,Optional<String>orderArea) {

		JsonArray to_ret=new JsonArray();
		List<Table> tableList=getTables(roomNumber);
		for(Table t:tableList) {
				to_ret.add(JsonParser.parseString(t.getJSONRepresentation(orderArea)).
						getAsJsonObject());
		}
		return to_ret.toString();
	}
	

	/**
	 * @info
	 * @param tableID		id of the table associated with orders
	 * @param roomNumber	roomNumber of the table associated with orders
	 * @param orderArea		area of the order to show
	 * @return	The jsonarray of the orders associated with the table (item of the array are the jsonn rappresentationn of the order)
	 */
	public String getOrdersByTableJSON(String tableID,int roomNumber,Optional<String>orderArea){
		JsonObject helper;
		JsonArray toRet=new JsonArray();
		for(Table t:this.tables) {
			if(t.isMe(tableID, roomNumber)) {
				 helper=JsonParser.parseString(t.getJSONRepresentation(orderArea)).getAsJsonObject();
				 toRet=helper.get("orders").getAsJsonArray();
			}
		}
		return toRet.toString();
	}
	

	/**
	 * @return the list of orders in the system
	 */
	public List<Order> getOrders(){	return this.orders;}

	/**
	 *
	 * @return the array list of orders inn json form
	 */
	public String getOrdersJSON(Optional<String> area) {
		JsonArray to_ret=new JsonArray();
		String helper;
		for(Order o:this.orders) {
			helper=o.getJSONRepresentation(area);
			if(!helper.isBlank())
				to_ret.add(JsonParser.parseString(helper).getAsJsonObject());
		}
		return to_ret.toString();
	}
	
	/**
	 *
	 * @param tableID id of the table to search
	 * @param roomNumber room nnumber of the table to search
	 * @param orderArea area of the orders to show
	 * @return Json Representation of the table
	 */
	public String getTableJSON(String tableID,int roomNumber,Optional<String>orderArea){
		for(Table t:this.tables) {
			if(t.isMe(tableID, roomNumber))
				return t.getJSONRepresentation(orderArea);
		}
		return "{}";
	}

	/**
	 *
	 * @param tableID id of the table to search
	 * @param roomNumber room number of the table to search
	 * @return Optional table if found else empty
	 */
	public Optional<Table> getTable(String tableID,int roomNumber){

		for(Table t:this.tables) {
			if(t.isMe(tableID, roomNumber))
				return Optional.of(t);
		}
		return Optional.empty();
	}
	
	/**
	 * @param order id to search
	 * @return optional of the order if found
	 */
	public Optional<Order> getOrderById(int orderID){
		for(Order o:this.orders) {
			if(o.isMe(orderID))
				return Optional.of(o);
		}
		return Optional.empty();
	}
	
	/**
	 *
	 * @param orderID to search
	 * @return  optional of the order if found else an empty object
	 */
	public String getOrderByIdJSON(int orderID) {
		for(Order o:this.orders) {
			if(o.isMe(orderID))
				return o.getJSONRepresentation(Optional.empty());
		}
		return "{}";
	}
	
	/**
	 *
	 * @info: free a specific table
	 * @param tableID
	 * @param roomNumber
	 * @return TableNotFound if the table doesn't exists else the new state of the table
	 */
	public String setTableFree(String tableID,int roomNumber) {
		Optional<Table> t=this.getTable(tableID, roomNumber);
		Table to_free;
		if(t.isEmpty())
			return returnCodes.tableNotFound.name();
		else {
			to_free=t.get();
			to_free.free();
			return to_free.getStateString();
		}

	}

	/**
	 *
	 * @info: set i waiting for orders a specific table
	 * @param tableID
	 * @param roomNumber
	 * @return tableNotFound if the table doesn't exists else the new state of the table
	 */
	public String setTableWaiting(String tableID,int roomNumber) {
		Optional<Table> t=this.getTable(tableID, roomNumber);
		Table table;
		if(t.isEmpty())
			return returnCodes.tableNotFound.name();
		else {
			table=t.get();
			table.setInWaitingForOrders();
			//db.updateTableByJSON(table.getJSONRepresentation(Optional.empty()));
			return table.getStateString();
		}

	}
	
	/**
	 * 
	 * @param itemNames of the order
	 * @param additive goods names
	 * @param toSub names
	 * @param priority of the item
	 * @param userID creator
	 * @return orderNotCreated or tableNotFound, else the JSONRepresenntationn of the order
	 */
	public String generateOrderForTable(List<String>itemNames,List<List<String>> additive,List<List<String>>toSub,
			
			List<Integer> priority,String tableID,int tableRoomNumber,Integer userID){
		String toRet =returnCodes.tableNotFound.name();
		Optional<Order> newOrder;
		for(Table t:this.tables) {
			if(t.isMe(tableID, tableRoomNumber)) {
				newOrder=t.addOrder(itemNames, additive, toSub, priority, userID);
				if(newOrder.isEmpty())
					toRet=returnCodes.orderNotCreated.name();
				else
					toRet=newOrder.get().getJSONRepresentation(Optional.empty());
			}
		}
		return toRet;
	}
	
	/**
	 * 
	 * @param itemNames of the order
	 * @param additive goods names
	 * @param toSub names
	 * @param priority of the item
	 * @param userID creator
	 * @return orderNotCreated or tableNotFound, else the order Id
	 */
	public String generateOrderForTableId(List<String>itemNames,List<List<String>> additive,List<List<String>>toSub,
			
			List<Integer> priority,String tableID,int tableRoomNumber,Integer userID){
		String toRet =returnCodes.tableNotFound.name();
		Optional<Order> newOrder;
		Integer id;
		for(Table t:this.tables) {
			if(t.isMe(tableID, tableRoomNumber)) {
				newOrder=t.addOrder(itemNames, additive, toSub, priority, userID);
				if(newOrder.isEmpty())
					toRet=returnCodes.orderNotCreated.name();
				else {
					id=newOrder.get().getId();
					toRet=id.toString();
				}
			}
		}
		return toRet;
	}
	
	/**
	 *
	 * @return the last order
	 */
	public Optional<Order> getLastOrder(){
		if(this.orders.size()==0)
			return Optional.empty();
		else
			return Optional.of(this.orders.get(this.orders.size()-1));
	}

	/**
	 *
	 * @return json representation of the last ordered item
	 */
	public String getLastOrderJSON() {
		if(this.orders.size()!=0)
			return this.orders.get(this.orders.size()-1).getJSONRepresentation(Optional.empty());
		else
			return "{}";
	}
	
	/**
	 * @info Bring an item in working state
	 * @param orderId id of the order containing the item
	 * @param lineNumber number of the item
	 * @return
	 */
	public Optional<Boolean> itemInWorking(int orderID,int lineNumber) {
		Optional<Order>helper=this.getOrderById(orderID);
		Order toModify;
		Optional <Boolean>toRet=Optional.empty();
		if(helper.isPresent()) {
			toModify=helper.get();
			toRet=Optional.of(toModify.takeItemInWorking(lineNumber));
		}
		return toRet;
	}

	/**
	 * @info complete an item for a specific order
	 * @param orderId id of the order containing the item
	 * @param lineNumber number of the item
	 * @return
	 */
	public Optional<Boolean> itemComplete(int orderID,int lineNumber) {
		Optional<Order>helper=this.getOrderById(orderID);
		Order toModify;
		Optional <Boolean>toRet=Optional.empty();
		if(helper.isPresent()) {
			toModify=helper.get();
			toRet=Optional.of(toModify.completeItem(lineNumber));
		}
		return toRet;
	}
	
	/**
	 *
	 * @info: Reserve a specific table
	 * @additiveinfo: not tested
	 * @param tableID
	 * @param roomNumber
	 * @return tableNotFound if the table doesn't exists else the new state of the table
	 */
	public String setTableReserved(String tableID,int roomNumber) {
		Optional<Table> t=this.getTable(tableID, roomNumber);
		Table table;
		if(t.isEmpty())
			return returnCodes.tableNotFound.name();
		else {
			table=t.get();
			table.reserve();
			return table.getStateString();
		}
	}
	
	/**
	 * 
	 * @info: adds an ordered item to an order
	 * @param orderID id of the order 
	 * @param names list of menuItem names
	 * @param additive goods for item
	 * @param toSub goods for item
	 * @param priority priority of items
	 * @return JSON representation of the order, else the specific error code
	 */
	public String addItemToOrder(int orderID,List<String>names,List<List<String>> additive,List<List<String>>toSub,
	List<Integer> priority) {
		Order toMod=null;
		boolean check=false;
		for(Order o:this.orders) {
			if (o.isMe(orderID))
				toMod=o;
		}
		if(toMod==null) {
			return returnCodes.orderNotFound.name();
		}
		check=toMod.addOrderedItems(names, additive, toSub, priority);
		if(!check)
			return	returnCodes.itemsNotAdded.name();
		return toMod.getJSONRepresentation(Optional.empty());
	}
	
	/**
	 * @info delete an item from an order
	 * @param orderID 
	 * @param lineNumber
	 * @return the json representation of the order else the specific error code
	 */
	public String deleteItemFromOrder(int orderID,int lineNumber) {
		Order toMod=null;
		boolean check=false;
		for(Order o:this.orders) {
			if (o.isMe(orderID))
				toMod=o;
		}
		if(toMod==null) {
			return returnCodes.orderNotFound.name();
		}
		check=toMod.cancelOrderedItem(lineNumber);
		if(!check)
			return	returnCodes.itemNotCanceled.name();
		return toMod.getJSONRepresentation(Optional.empty());
		
	}
	
	/**
	 * @info modify an item 
	 * @param orderID 
	 * @param lineNumber
	 * @return the specific status codes  string list, if the size is 1 then 
	 * 			the order or the item wasn't found.
	 * 			Else it is a 3 position array which contains  status codes  for  each single
	 * 			operation performed.
	 * 			Positions:
	 * 			0-> addGoods
	 * 			1->subGoods
	 * 			2->priority
	 */
	
	public List<String> modifyItemInOrder(int orderID,int lineNumber,List<String>  additiveGoods,List<String> subGoods,
			int priority) {
		Order toMod=null;		
		List<String> returnStatus=new ArrayList<>();
		for(Order o:this.orders) {
			if (o.isMe(orderID))
				toMod=o;
		}
		if(toMod==null) {
			returnStatus.add(returnCodes.orderNotFound.name());
		}
		else {
			Optional<OrderedItemState.StatusCodes> result1=toMod.setAdditiveGoodsForItem(additiveGoods, lineNumber);
			Optional<OrderedItemState.StatusCodes>result2=toMod.setSubGoodsForItem(subGoods, lineNumber);
			Optional<OrderedItemState.StatusCodes>result3=toMod.setPriorityForItem(priority, lineNumber);
			if(result1.isEmpty())
				 returnStatus.add(returnCodes.itemNotFound.name());
			else {
				returnStatus.add(result1.get().name());
				returnStatus.add(result2.get().name());
				returnStatus.add(result3.get().name());
			}
		}
		return returnStatus;
	}
	
	/**
	 * 
	 * @param orderID
	 * @param itemLineNumber
	 * @return orderNotFound if the order not exists,itemNotFound if the item Not exists
	 * 			itemFound if the item exists
	 */
	public String hasItem(int orderID,int itemLineNumber) {
		String toRet=returnCodes.orderNotFound.name();
		boolean check=false;
		int index=0;
		while(!check && index<this.orders.size()) {
			if(this.orders.get(index).isMe(orderID)) {
				check=true;
				toRet=returnCodes.itemNotFound.name();
				System.out.println(this.orders.get(index).getOrderedItems().get(0).getLineNumber());
				System.out.println(this.orders.get(index).getOrderedItems().get(0).getItem().getName());
				if(this.orders.get(index).hasItem(itemLineNumber))
					return returnCodes.itemFound.name();
			}
			index++;
		}
		
		return toRet;
	}
	/**
	 * 
	 * @param orderID
	 * @return true if the order is inn list
	 */
	public String harOrder(int orderID) {
		for(Order o:this.orders) {
			if(o.isMe(orderID))
				return returnCodes.orderFound.name();
		}
		return returnCodes.orderNotFound.name();
	}
	/**
	 * @param orderID
	 * @param itemLineNumber
	 * @return true if the order has items with  same priority of the input one in the specific status , else false.
	 * 			returns empty if the order or the item are not in list
	 */
	public Optional<Boolean> orderHasItemsInStatus(int orderID,int itemLineNumber,String status) {
		Optional<Boolean> toRet=Optional.empty();
		Optional<Order>order=this.getOrderById(orderID);
		if(order.isPresent()) {
			toRet=order.get().hasItemsInStatus(itemLineNumber,status);
		}
		return toRet;
	}
	
	
	/******************************************************************
	 * 	Every good project has a lot of dead code of previous versions that maybe in a 
	 * dystopian future will be useful :)
	 *********************************************************/
	
	/** @param additive map item name with additive goods
	 * @param sub	map item name with sub goods
	 * @param priority	map item name with his priority
	 * @return	 list of ordered item generated
	 */
	/*
	public List<OrderedItem> generateItemRaw(	Map<String,List<String>>additive,Map<String,List<String>>sub,
				Map<String,Integer> priority){ //generate ordered items to add

				List<String> additiveGoods;
				List<String> subGoods;
				int prioToSet;
				Optional<OrderedItem> helper;
				List<OrderedItem> toRet=new ArrayList<>();
				for(String menuItemName: additive.keySet()) {

					additiveGoods=additive.get(menuItemName);
					subGoods=sub.get(menuItemName);
					prioToSet=priority.get(menuItemName);

					if(subGoods!=null & priority!=null ) { //if the menuItem is present in all lists
						helper=this.menuAndWarehouseController.generateOrderedItem(menuItemName);
						if(helper.isPresent()) {  //if menuItem exists
							System.out.println(helper.get().changeAddGoods(additiveGoods).name());
							System.out.println(helper.get().changeSubGoods(subGoods).name());
							helper.get().setPriority(prioToSet);
							toRet.add(helper.get());
						}
						else {} //if menuItem doesn't exist, does nothing
					}
				}
				return toRet;
	}*/

	/**
	 * @info Generate an order and associates it with a table
	 * @return an empty json object if no item created else the json representation of the order.
	 */
	/*
	public String generateOrderToTable(	List<String>itemNames,List<List<String>> additive,List<List<String>>toSub,

			List<String> priority,String tableID,int tableNumber,Integer userID){

		Optional<Table>t=this.getTable(tableID,tableNumber);
		String toRet="tableNotFound";
		Optional<Order>generated;
		Order newOrder;
		if(t.isPresent()) {
			generated=t.get().addOrder(additive, toSub, priority, userID);
			if(generated.isEmpty())
				toRet="orderNotGenerated";
			else {
				newOrder=generated.get();
				toRet=newOrder.getJSONRepresentation(Optional.empty());
				this.orders.add(newOrder);
			}
		}
		return toRet;
	}*/
	/**
	 * @param json representation of the ordered item
	 * @return the ordered item associated
	 */
	/*
	private OrderedItem generateItemFromJSON(String json) {

		JsonObject objToInit =JsonParser.parseString(json).getAsJsonObject();
		JsonArray additive=objToInit.get("additive").getAsJsonArray();
		JsonArray sub=objToInit.get("sub").getAsJsonArray();
		List<String>toAdd=new ArrayList<>();
		List<String >toSub=new ArrayList<>();
		OrderedItem to_ret=this.menuAndWarehouseController.generateOrderedItem(
								objToInit.get("item").getAsString()).get();
		to_ret.setLineNumber(objToInit.get("lineNumber").getAsInt());
		for(int i=0;i<additive.size();i++) {
			toAdd.add(additive.get(i).getAsString());
		}
		for(int i=0;i<sub.size();i++) {
			toSub.add(sub.get(i).getAsString());
		}
		to_ret.setStateFromString(objToInit.get("actualState").getAsString());
		to_ret.changeAddGoods(toAdd);
		to_ret.changeSubGoods(toSub);
		return to_ret;
	}*/
	
	/**
	 *
	 * @info Generate an order from his json representation. THIS FUNCTION MUST BE CALLED WITH ALL
	 * 			TABLES IN THE SYSTEM
	 * @return Order created
	 */
	/*
	public Order createOrderFromJSON(String json) {
		Gson gson = new GsonBuilder()
				  .excludeFieldsWithoutExposeAnnotation()
				  .create();
		JsonObject toInit=JsonParser.parseString(json).getAsJsonObject();
		JsonArray array=toInit.get("orderedItems").getAsJsonArray();
		//create the order
		Order o=gson.fromJson(toInit,Order.class);//Inits the object
		//gets the associated table,WE ARE SUPPOSING THE TABLES ARE IN THE SYSTEM
		Optional<Table> associated=this.getTable(toInit.get("tableID").getAsString(),
				toInit.get("tableRoomNumber").getAsInt());
		o.setTable(associated);
		//associated.get().addOrder(o); //Register the order to the table
		associated.get().addOrderRaw(o); //Just in case of inconsistency in the db
		o.initItems();
		//Now add all the ordered items..
		for(int i=0;i<array.size();i++) {

			o.addOrdedItem(generateItemFromJSON(
							array.get(i).toString()
							));
		}
		return o;
	}*/
	
	/**
	 * @info : Private function , generate orders list.
	 *			Orders and tables must always be in the system.
	 */
	/*private  void initOrdersJSON(String ordersJsonArray){

		JsonArray array=JsonParser.parseString(db.getAllOrdersJSON()).getAsJsonArray();
		for(int i=0;i<array.size();i++) {
			this.orders.add(
					createOrderFromJSON(array.get(i).toString())
					);
		}
	}*/

}
