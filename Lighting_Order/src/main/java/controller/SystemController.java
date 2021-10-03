package controller;

import MenuAndWareHouseArea.MenuAndGoodsController;
import RestaurantArea.RestaurantController;
import RestaurantArea.RestaurantController.returnCodes;
import UsersData.UsersController;
import ch.qos.logback.core.joran.action.IADataForComplexProperty;
import messages.cancelOrder;
import messages.itemOpRequest;
import messages.menuRequest;
import messages.tableOperation;
import messages.tableRequest;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

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
	public enum responses{
		orderCanceled,
		orderNotCanceled,
		orderNotFound;
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
			Gson gson=new Gson();
			menuRequest obj=gson.fromJson(request, menuRequest.class);
			//checking the role of the request
		
			if(this.usersController.checkRole(
					obj.user
					,UsersData.User.userRoles.Cameriere.name() ))
			{
				obj.response=this.controllerMenu.getMenuJSON(obj.areaVisualization,
						obj.areaMenu);
				obj.result= results.roleOk.name();
				
			}
			else {
				obj.result=results.roleFailed.name();
			}
			this.brokerIface.publishResponse(gson.toJson(obj,menuRequest.class));
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
		Gson gson=new Gson();
		tableRequest obj=gson.fromJson(request,tableRequest.class);
		//checking the role of the request
	
		if(this.usersController.checkRole(
				obj.user
				,UsersData.User.userRoles.Accoglienza.name() )||
				this.usersController.checkRole(
						obj.user
						,UsersData.User.userRoles.Cameriere.name() ))
		{
			Optional<String> area=Optional.empty();
			Optional<Integer> room=Optional.empty();
			
			if(obj.forRoom)
				room=Optional.of(obj.roomNumber);
			if(obj.showItemsInArea)
				area=Optional.of(obj.orderArea);
			obj.response=this.controllerRestaurant.getAllTablesJSON(room, area);
			obj.result=results.roleOk.name();
		
		}
		else {
			obj.result=results.roleFailed.name();
		}
		this.brokerIface.publishResponse(gson.toJson(obj,tableRequest.class));
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
	 * 					result: "roleFailed/roleOk"
	 * 					response:tableJsonRepresentation
	 * 				}
	 */
	public void userWaitingForOrderRequest(String request) {
		Gson gson=new Gson();
		tableOperation obj=gson.fromJson(request, tableOperation.class);
		if(this.usersController.checkRole(
				obj.user
				,UsersData.User.userRoles.Accoglienza.name()))
		{
			obj.response=this.controllerRestaurant.setTableWaiting(
							obj.tableID,
							obj.tableRoomNumber);
			
			obj.result=results.roleOk.name();
		}
		else 
		{
			obj.result= results.roleFailed.name();
		}
		this.brokerIface.publishResponse(gson.toJson(obj,tableOperation.class));
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
	 * 					result: "roleFailed/roleOk
	 * 					response:tableJsonRepresentation
	 * 				}
	 */
	public void freeTableRequest(String request) {
		Gson gson=new Gson();
		tableOperation obj=gson.fromJson(request, tableOperation.class);
		if(this.usersController.checkRole(
				obj.user
				,UsersData.User.userRoles.Accoglienza.name()))
		{
			obj.response=this.controllerRestaurant.setTableFree(
							obj.tableID,
							obj.tableRoomNumber);
			
			obj.result=results.roleOk.name();
		}
		else 
		{
			obj.result= results.roleFailed.name();
		}
		this.brokerIface.publishResponse(gson.toJson(obj,tableOperation.class));
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
		Gson gson=new Gson();
		messages.orderToTableGenerationRequest obj=gson.fromJson(request,
				messages.orderToTableGenerationRequest.class);
		if(this.usersController.checkRole(
				obj.user
				,UsersData.User.userRoles.Cameriere.name() ))
		{
			obj.result=results.roleOk.name();
			obj.response=
					this.controllerRestaurant.generateOrderForTable
					(		obj.orderParams.itemNames, 
							obj.orderParams.addGoods,
							obj.orderParams.subGoods, 
							obj.orderParams.priority,
							obj.tableID, obj.tableRoomNumber, Integer.valueOf(obj.user))
					;
			
		}
		else {
			obj.result=results.roleFailed.name();
			
		}
		this.brokerIface.publishResponse(gson.toJson(obj,messages.
															orderToTableGenerationRequest.class));
	}
	
	/**
	 * 
	 * @param request
	 *  request:	{
	 * 					user:"userID"
	 * 					proxySource:"NameOfTheProxySource"
	 * 					request:"cancelOrder"
	 * 					orderID: "idOfTheOrder"
	 * 				}
	 * response		{
	 * 					result:	roleOk/roleFailed/orderFound/orderNotFounnd
	 * 					response:true/false
	 *				}
	 */
	public void cancelOrder(String request) {
		Gson gson=new Gson();
		cancelOrder obj=gson.fromJson(request, cancelOrder.class);
		
		if(this.usersController.checkRole(
				obj.user
				,UsersData.User.userRoles.Cameriere.name() ))
		{
			obj.result=this.controllerRestaurant.harOrder(obj.orderID);
			if(obj.result.equals(returnCodes.orderFound.name()))
					obj.response=controllerRestaurant.cancelOrder(obj.orderID)
																		.get().toString();
		}
		else 
			obj.result=results.roleFailed.name();
		
		this.brokerIface.publishResponse(gson.toJson(obj,messages.
															cancelOrder.class));
	}
	
	/**
	 * 
	 * @param request
	 *  request:	{
	 * 					user:"userID"
	 * 					proxySource:"NameOfTheProxySource"
	 * 					request:"cancelOrder"
	 * 					orderID: "idOfTheOrder"
	 * 					itemLineNumber:"itemLineNumber"
	 * 				}
	 * response		{
	 * 					result:	roleOk/roleFailed/orderNotFound/itemFound/itemNotFound
	 * 					response:true/false
	 *				}
	 */
	public void cancelOrderedItem(String request) {
		Gson gson=new Gson();
		itemOpRequest obj=gson.fromJson(request, itemOpRequest.class);
		
		if(this.usersController.checkRole(
				obj.user
				,UsersData.User.userRoles.Cameriere.name() ))
		{
			obj.result=this.controllerRestaurant.hasItem(obj.orderID,obj.itemLineNumber);
			if(obj.result.equals(returnCodes.itemFound.name()))
					obj.response=controllerRestaurant.deleteItemFromOrder(obj.orderID,
														obj.itemLineNumber);
																	
		}
		else 
			obj.result=results.roleFailed.name();
		
		this.brokerIface.publishResponse(gson.toJson(obj,messages.
															itemOpRequest.class));
	}
	
	public void itemWorkingRequest(String request) {
		
		Gson gson=new Gson();
		itemOpRequest obj=gson.fromJson(request, itemOpRequest.class);
		
		if(this.usersController.checkRole(
				obj.user
				,UsersData.User.userRoles.Cameriere.name() ))
		{
			obj.result=this.controllerRestaurant.hasItem(obj.orderID,obj.itemLineNumber);
			if(obj.result.equals(returnCodes.itemFound.name()))
					obj.response=controllerRestaurant.itemInWorking(obj.orderID,
														obj.itemLineNumber).get().toString();
																	
		}
		else 
			obj.result=results.roleFailed.name();
		
		this.brokerIface.publishResponse(gson.toJson(obj,messages.
															itemOpRequest.class));
		
	}
	
	public void itemComplete(String request) {
		
		Gson gson=new Gson();
		itemOpRequest obj=gson.fromJson(request, itemOpRequest.class);
		
		if(this.usersController.checkRole(
				obj.user
				,UsersData.User.userRoles.Cameriere.name() ))
		{
			obj.result=this.controllerRestaurant.hasItem(obj.orderID,obj.itemLineNumber);
			if(obj.result.equals(returnCodes.itemFound.name()))
					obj.response=controllerRestaurant.itemComplete(obj.orderID,
														obj.itemLineNumber).get().toString();
																	
		}
		else 
			obj.result=results.roleFailed.name();
		
		this.brokerIface.publishResponse(gson.toJson(obj,messages.
															itemOpRequest.class));
		
	}
}