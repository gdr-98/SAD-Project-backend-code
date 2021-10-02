package controller;

import MenuAndWareHouseArea.MenuAndGoodsController;
import RestaurantArea.RestaurantController;
import RestaurantArea.TableState;
import UsersData.UsersController;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;



import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;



/**
 * @info: the controller that the system uses
 */

@Service
public class SystemController  extends GeneralController{
	
	private BrokerInterface brokerIface;
	/**
	 * 
	 * @param iface sets the broker interface object for the broker callback methods
	 */
	public void setBrokerListener(BrokerInterface iface) {
		this.brokerIface=iface;
	}
	@Autowired
	public SystemController(MenuAndGoodsController controllerMenu,
			RestaurantController controllerRestaurant,UsersController usersController) {
		super(controllerMenu,controllerRestaurant,usersController);
	}
	
	private enum results{
		roleFailed,
		roleOk,
		operationCompleted,
		operationAborted;
	}
	
	private class OrderParameters{
		@Expose(serialize=true,   deserialize=true)
		public List<String> itemNames;
		@Expose(serialize=true, deserialize=true)
		public List<List<String>> addGoods;
		@Expose(serialize=true,deserialize=true)
		public List<List<String>>subGoods;
		@Expose(serialize=true,deserialize=true)
		public List<Integer>  priority;
	}

	/**
	 * @info Satisfy a menuRequest and publish the response to the broker
	 * 	request:	{
	 * 					user:"userID"
	 * 					proxySource:"NameOfTheProxySource"
	 * 					request:"showMenu"
	 * 					areaVisualization:	"false/true"
	 * 					areaMenu:"RequestedArea"(nameOfArea1/...)
	 * 				}
	 * response:	{
	 * 					request
	 * 					result: "roleFailed/roleOk"
	 * 					response: requestedMenuItems
	 * 				}
	 */
	public void menuRequest(String request) {
			JsonObject obj=JsonParser.parseString(request).getAsJsonObject();
			//checking the role of the request
		
			if(this.usersController.checkRole(
					obj.get("user").getAsString()
					,UsersData.User.userRoles.Cameriere.name() ))
			{
				String response=this.controllerMenu.getMenuJSON(obj.get("areaVisualization").getAsBoolean(), obj.get("areaMenu").getAsString());
				obj.addProperty("result", results.roleOk.name());
				obj.add("response", JsonParser.parseString(response).getAsJsonArray());
			}
			else {
				obj.addProperty("result", results.roleFailed.name());
				obj.addProperty("response", "[]");
			}
			this.brokerIface.publishResponse(obj.getAsString());
	}
	
	
	/**
	 * 	@info Satisfy a table request and publish the response to the broker
	 * 		request:	{
	 * 					user:"userID"
	 * 					proxySource:"NameOfTheProxySource"
	 * 					request:"showTables"
	 * 					forRoom:"yes/no"
	 * 					roomNumber:"roomNumber"
	 * 					showItemsInArea:"yes/no"
	 * 					orderArea:"areaOfTheOrders"
	 * 					}
	 * 		response:	{
	 * 					request
	 * 					result: "roleFailed/roleOk"
	 * 					response: requestedTables
	 * 					}
	 */
	public void tableRequest(String request) {
		
		JsonObject obj=JsonParser.parseString(request).getAsJsonObject();
		//checking the role of the request
	
		if(this.usersController.checkRole(
				obj.get("user").getAsString()
				,UsersData.User.userRoles.Accoglienza.name() )||
				this.usersController.checkRole(
						obj.get("user").getAsString()
						,UsersData.User.userRoles.Cameriere.name() ))
		{
			String response;
			Optional<String> area=Optional.empty();
			Optional<Integer> room=Optional.empty();
			
			if(obj.get("forRoom").getAsBoolean())
				room=Optional.of(obj.get("roomNumber").getAsInt());
			if(obj.get("showItemsInArea").getAsBoolean())
				area=Optional.of(obj.get("orderArea").getAsString());
			
			response=this.controllerRestaurant.getAllTablesJSON(room, area);
			
			obj.addProperty("result", results.roleOk.name());
			obj.add("response", JsonParser.parseString(response).getAsJsonArray());
		}
		else {
			obj.addProperty("result", results.roleFailed.name());
			obj.addProperty("response", "[]");
		}
		this.brokerIface.publishResponse(obj.getAsString());
	}
	
	
	/**
	 * @info a user sits in a table waiting to order
	 * @param Request, alter a table state and publish the response to the broker
	 * request:		{
	 * 					user:"userID"
	 * 					proxySource:"NameOfTheProxySource"
	 * 					request:"userWaitingForOrderRequest"
	 * 					tableID:"tableIDValue"
	 * 					tableRoomNumber:"tableRoomNumber"
	 * 					
	 * 				}
	 * 	response:	{
	 * 					request
	 * 					result: "roleFailed/roleOk/operationCompleted/operationAborted"
	 * 					response:tableJsonRepresentation
	 * 				}
	 */
	public void userWaitingForOrderRequest(String Request) {
		JsonObject obj=JsonParser.parseString(Request).getAsJsonObject();
		String tableID=obj.get("tableID").getAsString();
		int tableRoomNumber=obj.get("tableRoomNumber").getAsInt();
		if(this.usersController.checkRole(
				obj.get("user").getAsString()
				,UsersData.User.userRoles.Accoglienza.name()))
		{
			String response=this.controllerRestaurant.setTableWaiting(
							tableID,
							tableRoomNumber);
			
			if(response.equals(TableState.StatesList.waitingForOrders.name())) 
				obj.addProperty("result",results.operationCompleted.name());
			else 
				obj.addProperty("result", results.operationAborted.name());
			
			obj.addProperty("response", 
					this.controllerRestaurant.getTableJSON(tableID, tableRoomNumber,Optional.empty())
					);
		
		}
		else 
		{
			obj.addProperty("result", results.roleFailed.name());
			obj.addProperty("response", "{}");
		}
		this.brokerIface.publishResponse(obj.getAsString());
	}
	
	/**
	 * @info a table must be freed
	 * @param Request, alter a table state and publish the response to the broker
	 * request:		{
	 * 					user:"userID"
	 * 					proxySource:"NameOfTheProxySource"
	 * 					request:"freeTable"
	 * 					tableID:"tableIDValue"
	 * 					tableRoomNumber:"tableRoomNumber"
	 * 					
	 * 				}
	 * 	response:	{
	 * 					request
	 * 					result: "roleFailed/roleOk/operationCompleted/operationAborted"
	 * 					response:tableJsonRepresentation
	 * 				}
	 */
	public void freeTableRequest(String Request) {
		JsonObject obj=JsonParser.parseString(Request).getAsJsonObject();
		String tableID=obj.get("tableID").getAsString();
		int tableRoomNumber=obj.get("tableRoomNumber").getAsInt();
		if(this.usersController.checkRole(
				obj.get("user").getAsString()
				,UsersData.User.userRoles.Accoglienza.name() ))
		{
			String response=this.controllerRestaurant.setTableFree(
							tableID,
							tableRoomNumber);
			
			if(response.equals(TableState.StatesList.free.name())) 
				obj.addProperty("result", results.operationCompleted.name());
			else 
				obj.addProperty("result",results.operationAborted.name());
			obj.addProperty("response", 
					this.controllerRestaurant.getTableJSON(tableID, tableRoomNumber,Optional.empty())
					);
		
		}
		else 
		{
			obj.addProperty("result", results.roleFailed.name());
			obj.addProperty("response", "{}");
		}
		this.brokerIface.publishResponse(obj.getAsString());
	}
	
	/**
	 * @info a waiter generates an order
	 * @param Request, alter a table state and publish the response to the broker
	 * request:		{
	 * 					user:"userID"
	 * 					proxySource:"NameOfTheProxySource"
	 * 					request:"orderToTableGeneration"
	 * 					tableID:"tableIDValue"
	 * 					tableRoomNumber:"tableRoomNumber"
	 * 					orderParams{
	 * 							itemNames:	[itemname1...]
	 * 							addGoods:	[ 	[addGoods1ForItem1 ...addGoodsNforItem1]
	 * 											..]
	 * 							subGoods:	[ 	[subGoods1ForItem1 ...subGoodsNforItem1]
	 * 											..]
	 * 							priority:	[priorityForItem1....]
	 * 					}
	 * 				}
	 * 	response:	{
	 * 					request
	 * 					result: "roleFailed/roleOk/operationReturnCode"
	 * 					response: empty
	 * 				}
	 */
	public void orderToTableGenerationRequest(String request) {
		JsonObject obj=JsonParser.parseString(request).getAsJsonObject();
		String tableID=obj.get("tableID").getAsString();
		int tableRoomNumber=obj.get("tableRoomNumber").getAsInt();
		String userID=obj.get("user").getAsString();
		OrderParameters param=getOrderParametersFromRequest(request);
		if(this.usersController.checkRole(
				userID
				,UsersData.User.userRoles.Accoglienza.name() ))
		{
			obj.addProperty("result", 
					this.controllerRestaurant.generateOrderForTable
					(		param.itemNames, 
							param.addGoods,
							param.subGoods, 
							param.priority,
							tableID, tableRoomNumber, Integer.valueOf(userID))
					);
			//obj.addProperty("response", this.controllerRestaurant.getLastOrderJSON());
		}
		else {
			obj.addProperty("result",results.roleFailed.name());
			//obj.addProperty("result", "{}");
		}
		this.brokerIface.publishResponse(obj.getAsString());
	}
	
	public void cancelOrder(String request) {
		
		
		//this.brokerIface.publishResponse(obj.getAsString());
	}
	
	
	/**
	 * 
	 * @param request
	 * @return the order parameters
	 */
	private static OrderParameters getOrderParametersFromRequest(String request){
	/*	JsonObject obj=JsonParser.parseString(request).getAsJsonObject();
		JsonArray orderLines=obj.get("orderLines").getAsJsonArray();
		List<String> names=new ArrayList<>();
		for(int i=0;i<orderLines.size();i++) {
			names.add(
					orderLines.get(i).getAsJsonObject().get("menuItem").getAsString()
					);
		}
		return names;*/
		OrderParameters toRet;
		Gson gson=new Gson();
		toRet=gson.fromJson(request, OrderParameters.class);
		return toRet;
	}
}
