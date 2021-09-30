package controller;
import MenuAndWareHouseArea.MenuAndGoodsController;
import RestaurantArea.RestaurantController;
import UsersData.UsersController;
public class GeneralController {
	//@SuppressWarnings("notUsed")
	protected MenuAndGoodsController controllerMenu;
	protected RestaurantController controllerRestaurant;
	protected UsersController usersController;
	public GeneralController(MenuAndGoodsController controllerMenu,
			RestaurantController controllerRestaurant,UsersController usersController) {
		this.controllerMenu=controllerMenu;
		this.controllerRestaurant=controllerRestaurant;
		this.usersController=usersController;
	}

	
}
