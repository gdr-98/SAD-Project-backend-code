package DataAccess;
import  java.util.List;
public interface MenuAndWarehouseDAO {
	
	/**
	 * 
	 * @return  All the  menu in a JSON Array form(each element is the JSON representation of a menuitem)
	 */
	String findAllMenuItemsJSON();
	
	/**
	 * 
	 * @param id of the Goods to  search
	 * @return the JSON representation of  the goods or an empty string if  the element doesn't exists
	 */
	String findGoodsByIdJSON(String id);
	
	/**
	 * 
	 * @param name of the menuItem to search
	 * @return the JSON representation of the menu item or an empty string (if  the element
	 * 			doesn't exist)
	 */
	String findMenuItemByNameJSON(String menuItemName);
	
	/**
	 * 
	 * @return number of  menu items
	 */
	Integer numberOfMenuItems();
	
	/**
	 * 
	 * @return Number  of Goods
	 */
	Integer numberOfGoods();
	
	/**
	 * 
	 * @return List of all menu Items names
	 */
	List<String> menuItemsNames();

}
