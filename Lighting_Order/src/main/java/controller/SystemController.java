package controller;

import MenuAndWareHouseArea.MenuAndGoodsController;
import RestaurantArea.RestaurantController;
import RestaurantArea.TableState;
import UsersData.UsersController;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import UsersData.User;
import com.google.gson.JsonParser;
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
				obj.addProperty("result", "roleOk");
				obj.add("response", JsonParser.parseString(response).getAsJsonArray());
			}
			else {
				obj.addProperty("result", "roleFailed");
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
			if(!obj.get("forRoom").getAsBoolean()) 
				response=this.controllerRestaurant.getAllTablesJSON(Optional.empty());
			else
				response=this.controllerRestaurant.getAllTablesJSON(Optional.of(
							obj.get("roomNumber").getAsInt())
						);
			obj.addProperty("result", "roleOk");
			obj.add("response", JsonParser.parseString(response).getAsJsonArray());
		}
		else {
			obj.addProperty("result", "roleFailed");
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
				,UsersData.User.userRoles.Accoglienza.name() ))
		{
			String response=this.controllerRestaurant.tableInWaitingForOrders(
							tableID,
							tableRoomNumber);
			if(response.equals(TableState.StatesList.waitingForOrders.name())) 
				obj.addProperty("result", "operationCompleted");
			
			else 
				obj.addProperty("result", "operationAborted");
			obj.addProperty("response", 
					this.controllerRestaurant.getTableJSON(tableID, tableRoomNumber)
					);
		
		}
		else 
		{
			obj.addProperty("result", "roleFailed");
			obj.addProperty("response", "{}");
		}
	}
	
	/**
	 * @info a user sits in a table waiting to order
	 * @param Request, alter a table state and publish the response to the broker
	 * request:		{
	 * 					user:"userID"
	 * 					proxySource:"NameOfTheProxySource"
	 * 					request:"freeATable"
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
	public void freeATableRequest(String Request) {
		JsonObject obj=JsonParser.parseString(Request).getAsJsonObject();
		String tableID=obj.get("tableID").getAsString();
		int tableRoomNumber=obj.get("tableRoomNumber").getAsInt();
		if(this.usersController.checkRole(
				obj.get("user").getAsString()
				,UsersData.User.userRoles.Accoglienza.name() ))
		{
			String response=this.controllerRestaurant.freeTable(
							tableID,
							tableRoomNumber);
			if(response.equals(TableState.StatesList.free.name())) 
				obj.addProperty("result", "operationCompleted");
			
			else 
				obj.addProperty("result", "operationAborted");
			obj.addProperty("response", 
					this.controllerRestaurant.getTableJSON(tableID, tableRoomNumber)
					);
		
		}
		else 
		{
			obj.addProperty("result", "roleFailed");
			obj.addProperty("response", "{}");
		}
	}
	
	/**
	 * @info a user sits in a table waiting to order
	 * @param Request, alter a table state and publish the response to the broker
	 * request:		{
	 * 					user:"userID"
	 * 					proxySource:"NameOfTheProxySource"
	 * 					request:"orderToTableGeneration"
	 * 					tableID:"tableIDValue"
	 * 					tableRoomNumber:"tableRoomNumber"
	 * 					orderLines: [{
	 * 									menuItem:"menuItemName"
	 * 									priority:"priority"
	 * 									additiveGoodsIds:[good1...]
	 * 									subGoodsIds:[good1...]
	 * 
	 * 									....]
	 * 					
	 * 				}
	 * 	response:	{
	 * 					request
	 * 					result: "roleFailed/roleOk/operationReport"
	 * 					response:orderJsonRepresentation
	 * 				}
	 */
	public void orderToTableGenerationRequest(String Request) {
		JsonObject obj=JsonParser.parseString(Request).getAsJsonObject();
		String tableID=obj.get("tableID").getAsString();
		int tableRoomNumber=obj.get("tableRoomNumber").getAsInt();
		String userID=obj.get("user").getAsString();
		if(this.usersController.checkRole(
				userID
				,UsersData.User.userRoles.Accoglienza.name() ))
		{
			obj.addProperty("result", 
					this.controllerRestaurant.generateOrderAndAddToTable(
							getGoodsFromOrderRequest(Request,"additiveGoodsIds"), 
							getGoodsFromOrderRequest(Request,"subGoodsIds"),
							getPriorityFromOrderRequest(Request), 
							tableID,
							tableRoomNumber,
							userID)
					);
			obj.addProperty("response", this.controllerRestaurant.getLastOrderJSON());
		}
		else {
			obj.addProperty("result", "roleFailed");
			obj.addProperty("result", "{}");
		}		
	}
	
	//public void canceRequest
	
	
	/**
	 * @info utility function
	 * @param Request
	 * @return the map of ordered item names and corresponent priority
	 */
	private static Map<String,Integer> getPriorityFromOrderRequest(String Request){
		JsonObject obj=JsonParser.parseString(Request).getAsJsonObject();
		JsonArray orderLines=obj.get("ordersLines").getAsJsonArray();
		Map<String,Integer>toRet=new HashMap<>();
		JsonObject helper;
		for (int i=0;i<orderLines.size() ;i++) {
			helper=orderLines.get(i).getAsJsonObject();
			toRet.put(helper.get("menuItem").getAsString(),
					helper.get("priority").getAsInt());
		}
		return toRet;
	}
	
	/**
	 * @info utility function
	 * @param Request
	 * @return the map of ordered item names and correspondent additive/subGoods
	 */
	private static Map<String,List<String>> getGoodsFromOrderRequest(String Request,
				String field){
		JsonObject obj=JsonParser.parseString(Request).getAsJsonObject();
		JsonArray orderLines=obj.get("ordersLines").getAsJsonArray();
		Map<String,List<String>>toRet=new HashMap<>();
		List<String>goods=new ArrayList<>();
		JsonObject helper;
		JsonArray helper1;
		for (int i=0;i<orderLines.size() ;i++) {
			helper=orderLines.get(i).getAsJsonObject();
			helper1=helper.get("subGoodsIds").getAsJsonArray();
			for(int j=0;j<helper1.size();j++)
				goods.add(helper1.get(j).getAsString());
			toRet.put(helper.get("menuItem").getAsString(),
					goods);
		}
		return toRet;
	}
	
}
