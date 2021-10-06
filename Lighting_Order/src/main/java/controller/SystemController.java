package controller;

import MenuAndWareHouseArea.MenuAndGoodsController;
import MenuAndWareHouseArea.OrderedItemState;
import RestaurantArea.Order;
import RestaurantArea.RestaurantController;
import RestaurantArea.RestaurantController.returnCodes;
import UsersData.UsersController;
import messages.cancelOrderRequest;
import messages.itemOpRequest;
import messages.menuRequest;
import messages.orderNotification;
import messages.orderRequest;
import messages.tableOperation;
import messages.tableRequest;
import request_generator.controllerIface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;


/**
 * @info: the controller that the system uses
 */

@Service("SystemController")
@ComponentScan(basePackages= {"MenuAndWareHouseArea","UsersData","RestaurantArea","broker"})
public class SystemController  extends GeneralController implements controllerIface {
	@Autowired
	@Qualifier("brokerDispatcher")
	private BrokerInterface brokerIface;
	/**
	 * 
	 * @param iface sets the broker interface object for the broker callback methods
	 */
	
	@Autowired
	public SystemController(MenuAndGoodsController controllerMenu,
			RestaurantController controllerRestaurant,UsersController usersController
			) {
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
	 * 					request:"menuRequest"
	 * 					areaVisualization:	"false/true"
	 * 					areaMenu:"RequestedArea"(nameOfArea1/...)
	 * 				}
	 * response:	{
	 * 					request
	 * 					result: "roleFailed/roleOk"
	 * 					response: requestedMenuItems
	 * 				}
	 */
	@Override
	public void menuRequest( String request) {
			Gson gson=new Gson();
			menuRequest obj=gson.fromJson(request, menuRequest.class);
			usersController.login(obj.user);
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
	 * 					request:"tableRequest"
	 * 					forRoom:"true/false"
	 * 					roomNumber:"roomNumber"
	 * 					showItemsInArea:"true/false"
	 * 					orderArea:"areaOfTheOrders"
	 * 					}
	 * 		response:	{
	 * 					request
	 * 					result: "roleFailed/roleOk"
	 * 					response: requestedTables
	 * 					}
	 */
	@Override
	public void tableRequest(String request) {
		Gson gson=new Gson();
		tableRequest obj=gson.fromJson(request,tableRequest.class);
		//checking the role of the request
		usersController.login(obj.user);
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
	 * 				}
	 * 	response:	{
	 * 					request
	 * 					result: "roleFailed/roleOk"
	 * 					response:tableJsonRepresentation
	 * 				}
	 */
	@Override
	public void userWaitingForOrderRequest(String request) {
		Gson gson=new Gson();
		tableOperation obj=gson.fromJson(request, tableOperation.class);
		usersController.login(obj.user);
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
	 * 					request:"freeTableRequest"
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
	@Override
	public void freeTableRequest(String request) {
		Gson gson=new Gson();
		tableOperation obj=gson.fromJson(request, tableOperation.class);
		usersController.login(obj.user);
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
	 * @additiveInfo this function triggers more events, one for the operation confirmation and the others for sending the order
	 * @param Request, alter a table state and publish the response to the broker
	 * request:		{
	 * 					user:"userID"
	 * 					proxySource:"NameOfTheProxySource"
	 * 					request:"orderToTableGenerationRequest"
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
	 * 	responses:	{
	 * 					request
	 * 					result: "roleFailed/roleOk"
	 * 				}
	 *
	 */
	@Override
	public void orderToTableGenerationRequest(String request) {
		Gson gson=new Gson();
		messages.orderToTableGenerationRequest obj=gson.fromJson(request,
				messages.orderToTableGenerationRequest.class);
		usersController.login(obj.user);
		List<orderNotification> makerNotifications=new ArrayList<>();
		if(this.usersController.checkRole(
				obj.user
				,UsersData.User.userRoles.Cameriere.name() ))
		{
			obj.result=results.roleOk.name();
			obj.response=	this.controllerRestaurant.generateOrderForTableId
					(		obj.orderParams.itemNames, 
							obj.orderParams.addGoods,
							obj.orderParams.subGoods, 
							obj.orderParams.priority,
							obj.tableId, obj.tableRoomNumber, Integer.valueOf(obj.user))
					;
			if((!obj.response.equals(returnCodes.tableNotFound.name()))&&
					(!obj.response.equals(returnCodes.orderNotCreated.name()))	)  
				//if the order is created then generate events for makers
					makerNotifications=this.generateOrderNotifications(Integer.valueOf(obj.response));
					
		}
		else {
			obj.result=results.roleFailed.name();
		}
		
		//Publish the operation report
		this.brokerIface.publishResponse(gson.toJson(obj,messages.
															orderToTableGenerationRequest.class));
		for(orderNotification not:makerNotifications)
			this.brokerIface.publishResponse(gson.toJson(not,messages.
													orderNotification.class));
	}
	
	/**
	 * 
	 * @param request
	 *  request:	{
	 * 					user:"userID"
	 * 					proxySource:"NameOfTheProxySource"
	 * 					request:"cancelOrderRequest"
	 * 					orderID: "idOfTheOrder"
	 * 				}
	 * response		{
	 * 					result:	roleOk/roleFailed/orderFound/orderNotFounnd
	 * 					response:true/false
	 *				}
	 */
	@Override
	public void cancelOrderRequest(String request) {
		Gson gson=new Gson();
		cancelOrderRequest obj=gson.fromJson(request, cancelOrderRequest.class);
		usersController.login(obj.user);
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
															cancelOrderRequest.class));
	}
	
	/**
	 * 
	 * @param request
	 *  request:	{
	 * 					user:"userID"
	 * 					proxySource:"NameOfTheProxySource"
	 * 					request:"cancelOrderedItemRequest"
	 * 					orderID: "idOfTheOrder"
	 * 					itemLineNumber:"itemLineNumber"
	 * 				}
	 * response		{
	 * 					result:	roleOk/roleFailed/orderNotFound/itemFound/itemNotFound
	 * 					response:true/false
	 *				}
	 */
	@Override
	public void cancelOrderedItemRequest(String request) {
		Gson gson=new Gson();
		itemOpRequest obj=gson.fromJson(request, itemOpRequest.class);
		usersController.login(obj.user);
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
	/**
	 * 
	 * @param request
	 *  request:	{
	 * 					user:"userID"
	 * 					proxySource:"NameOfTheProxySource"
	 * 					request:"itemWorkingRequest"
	 * 					orderID: "idOfTheOrder"
	 * 					itemLineNumber:"itemLineNumber"
	 * 				}
	 * response		{
	 * 					result:	roleOk/roleFailed/orderNotFound/itemFound/itemNotFound
	 * 					response:true/false
	 *				}
	 */
	@Override
	public void itemWorkingRequest(String request) {
		
		Gson gson=new Gson();
		itemOpRequest obj=gson.fromJson(request, itemOpRequest.class);
		usersController.login(obj.user);
		if(	this.usersController.checkRole(
						obj.user
						,UsersData.User.userRoles.Bar.name() )
				||this.usersController.checkRole(
						obj.user
						,UsersData.User.userRoles.Cucina.name())
				||
						this.usersController.checkRole(
								obj.user
								,UsersData.User.userRoles.Forno.name())
			)
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
	
	/**
	 * 
	 * @param request
	 *  request:	{
	 * 					user:"userID"
	 * 					proxySource:"NameOfTheProxySource"
	 * 					request:"itemCompleteRequest"
	 * 					orderID: "idOfTheOrder"
	 * 					itemLineNumber:"itemLineNumber"
	 * 				}
	 * response		{
	 * 					result:	roleOk/roleFailed/orderNotFound/itemFound/itemNotFound
	 * 					response:true/false
	 *				}
	 */
	@Override
	public void itemCompleteRequest(String request) {
		
		Gson gson=new Gson();
		itemOpRequest obj=gson.fromJson(request, itemOpRequest.class);
		usersController.login(obj.user);
		List<orderNotification>makerNotifications=new ArrayList<>();
		if(		this.usersController.checkRole(
				obj.user
				,UsersData.User.userRoles.Bar.name() )
		||this.usersController.checkRole(
				obj.user
				,UsersData.User.userRoles.Cucina.name())
		||
				this.usersController.checkRole(
						obj.user
						,UsersData.User.userRoles.Forno.name())
		)
		{
			
			obj.result=this.controllerRestaurant.hasItem(obj.orderID,obj.itemLineNumber);
			if(obj.result.equals(returnCodes.itemFound.name())) {
					obj.response=controllerRestaurant.itemComplete(obj.orderID,
														obj.itemLineNumber).get().toString();	
					//now generate users notifications 
					 //sure to find the order
					if((!controllerRestaurant.orderHasItemsInStatus(
									obj.orderID,obj.itemLineNumber,
									OrderedItemState.States.WaitingForWorking.name())
									.get())&&
						(!controllerRestaurant.orderHasItemsInStatus(
									obj.orderID,obj.itemLineNumber,
									OrderedItemState.States.Working.name())
									.get()))
						makerNotifications=generateOrderNotifications(obj.orderID);
			}
		}
		else 
			obj.result=results.roleFailed.name();
		this.brokerIface.publishResponse(gson.toJson(obj,messages.
															itemOpRequest.class));
		for(orderNotification not:makerNotifications)
			this.brokerIface.publishResponse(gson.toJson(not,messages.
													orderNotification.class));
	}
	/**
	 * 
	 * @param a realizzator asks for the list of orders of his specific area
	 *  request:	{
	 * 					user:"userID"
	 * 					proxySource:""
	 * 					request:"orderRequest"
	 * 					areaVisualization: "areaToShow"
	 * 					area:"nameOfTheArwa"
	 * 				}
	 * response		{
	 * 					result:	roleOk/roleFailed
	 * 					response:true/false
	 *				}
	 */
	@Override
	public void orderRequest(String request) {
		Gson gson=new Gson();
		orderRequest obj=gson.fromJson(request, orderRequest.class);
		
		usersController.login(obj.user);
		if(this.usersController.checkRole(
				obj.user
				,UsersData.User.userRoles.Bar.name() )
				||this.usersController.checkRole(
						obj.user
						,UsersData.User.userRoles.Cucina.name() )
				|| this.usersController.checkRole(
						obj.user
						,UsersData.User.userRoles.Forno.name() 
				)
			)
		{
			if(obj.areaVisualization)
				obj.response=this.controllerRestaurant.getOrdersJSON(
						Optional.of(obj.area));
			else
				obj.response=this.controllerRestaurant.getOrdersJSON(
						Optional.empty());
			obj.result= results.roleOk.name();
		}
		else {
			obj.result=results.roleFailed.name();
		}
		this.brokerIface.publishResponse(gson.toJson(obj,orderRequest.class));
	
	}


	@Override
	public void loginRequest(String request) {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * @info generate notification for makers in case a new order is created or priority of the order changed
	 * @param orderID
	 * @return list of notifications
	 */
	private List<orderNotification> generateOrderNotifications(int orderID){
		
		//["Forno","Cucina","Bar"]
		List<String> areas=new ArrayList<>(
				Arrays.asList("Forno","Cucina","Bar")	
			);
		String helper;
		
		//find the order
		List<orderNotification> notifications=new ArrayList<>();
		Optional<Order>order=this.controllerRestaurant.getOrderById(orderID);
		if(order.isPresent()) { //if the order exists
			for(String area:areas) { //for each area
				//get orders with highest priority
				helper=order.get().getJSONRepresentationAsCommande(area);
				if(!helper.isBlank()) { //if there are items in that area
					orderNotification helper2=new orderNotification();
					helper2.request="orderNotification";
					helper2.area=area;
					helper2.order=helper;
					notifications.add(
							helper2
							);
				}
			}						
		}
		return notifications;
	}
}