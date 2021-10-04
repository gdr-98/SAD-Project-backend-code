package DataAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

@Repository("psqlMenu")
public class MenuDAOPSQL implements MenuAndWarehouseDAO {

	@Autowired
	private ApplicationContext context;
	private JdbcTemplate database;
	
	public static final String DRIVER = "org.postgresql.Driver";
    public static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "porcodio";
	
	/**JSON Format:
	 * {
	 *      name : "name_value"
	 *      description : "description_value"
	 *      price : "price_value"
	 *      inStock : "inStock_value"
	 *      area : "area_value"
	 *      goodsID : ["goodsID1", "goodsID2", ... , "goodsIDn"]
	 * }
	 * */
    
    public MenuDAOPSQL() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(UserDAOPSQL.DRIVER);
        dataSource.setUrl(UserDAOPSQL.JDBC_URL);
        dataSource.setUsername(UserDAOPSQL.USERNAME);
        dataSource.setPassword(UserDAOPSQL.PASSWORD);
		database = new JdbcTemplate(dataSource);
	}
	
	@Override
	public String findAllMenuItemsJSON() {
		List<String> menu_items = new ArrayList<String>();
		
		String query = "SELECT name FROM \"Restaurant\".\"Prodotto\"";
		List<Map<String, Object>> results = database.queryForList(query);
		for(Map<String, Object> record : results) {
			menu_items.add(findMenuItemByNameJSON(record.get("name").toString()));
			
		}
		JSONArray temp = new JSONArray();
		
		try {
			for(int i=0; i<menu_items.size(); i++) {
				temp.put(new JSONObject(menu_items.get(i)));
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		return temp.toString();
	}

	
	/**JSON Format:
	 * {
	 *      id : "goods_id"
	 *      quantity : "goods_quantity"
	 *      priceAsAdditive : "price_as_additive_value"
	 *      quantityLowerBound:"quantityLowerBound_value"
	 *      name:"goods_name"
	 *      unitPrice:"unitPriceName"
	 *      inStock : "inStock_value"
	 *      menuItems : ["menuItemName1", "MenuItemName2", ... , "MenuItemNameN"]
	 * }
	 * */
	@Override
	public String findGoodsByIdJSON(String id) {
		String to_return;
		
		String query = "SELECT * "
				+ "FROM \"Restaurant\".\"Merce\" "
				+ "WHERE id = '"+id+"'";
		JSONObject good = new JSONObject();
		List<Map<String,Object>> results = database.queryForList(query);
		List<String> menu_items = new ArrayList<String>();
		
		to_return = good.toString();
		
		if(results.size() > 0) {
			try {
				good.put("id", results.get(0).get("id"));
				good.put("quantity", results.get(0).get("quantity"));
				Double price=(Double) results.get(0).get("priceAsAdditive");
				if(price==null)
					good.put("priceAsAdditive", 0);
				else
					good.put("priceAsAdditive", price);
				good.put("quantityLowerBound", results.get(0).get("quantityLowerBound"));
				good.put("name", results.get(0).get("name"));
				good.put("unitPrice", results.get(0).get("price"));
				good.put("inStock", results.get(0).get("inStock"));
				
				query = "SELECT * "
						+ "FROM \"Restaurant\".\"Merce_Prodotto\" "
						+ "WHERE merce_id = '"+id+"'";
				results = database.queryForList(query);
				//if(results.size() > 0) {
					for(Map<String,Object> record : results)
						menu_items.add(record.get("prodotto_name").toString());
					JSONArray temp = new JSONArray();
					for(String r : menu_items)
						temp.put(r);
					good.put("menuItems", (Object) temp);
					
					to_return = good.toString();
				//}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return to_return;
	}
	
	/**JSON Format:
	 * {
	 *      name : "name_value"
	 *      description : "description_value"
	 *      price : "price_value"
	 *      inStock : "inStock_value"
	 *      area : "area_value"
	 *      goodsID : ["goodsID1", "goodsID2", ... , "goodsIDn"]
	 * }
	 * */

	@Override
	public String findMenuItemByNameJSON(String menuItemName) {
		String to_return = null;
		
		String query = "SELECT * "
				+ "FROM \"Restaurant\".\"Prodotto\" "
				+ "WHERE name = '"+menuItemName+"'";
		JSONObject menu_item = new JSONObject();
		List<Map<String,Object>> results = database.queryForList(query);
		List<String> goods = new ArrayList<String>();
		
		if(results.size() > 0) {
			try {
				menu_item.put("name", results.get(0).get("name"));
				menu_item.put("description", results.get(0).get("description"));
				menu_item.put("price", results.get(0).get("price"));
				menu_item.put("inStock", results.get(0).get("inStock"));
				menu_item.put("area", results.get(0).get("area"));
				
				query = "SELECT * "
						+ "FROM \"Restaurant\".\"Merce_Prodotto\" "
						+ "WHERE prodotto_name = '"+menuItemName+"'";
				results = database.queryForList(query);
				
					for(Map<String,Object> record : results)
						goods.add(record.get("merce_id").toString());
					JSONArray temp = new JSONArray();
					for(String r : goods)
						temp.put(r);
					menu_item.put("goodsID", (Object) temp);
					
					to_return =menu_item.toString();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return to_return;
	}

	@Override
	public Integer numberOfMenuItems() {
		int to_return = 0;
		String query = "SELECT COUNT(*) FROM \"Restaurant\".\"Prodotto\"";
		to_return = database.queryForObject(query, Integer.class);
		return to_return;
	}

	@Override
	public Integer numberOfGoods() {
		int to_return = 0;
		String query = "SELECT COUNT(*) FROM \"Restaurant\".\"Merce\"";
		to_return = database.queryForObject(query, Integer.class);
		return to_return;
	}

	@Override
	public List<String> menuItemsNames() {
		List<String> menu_items_name = new ArrayList<String>();
		String query = "SELECT name FROM \"Restaurant\".\"Prodotto\"";
		List<Map<String,Object>> results = database.queryForList(query);
		for(Map<String,Object> record : results)
			menu_items_name.add(record.get("name").toString());
		return menu_items_name;
	}
	
	@PostConstruct
	public void check() {
		if(context==null) {
			System.out.println("true");
		}
		else {
			System.out.println("nooo");
		}
		//MenuDAOPSQL list =context.getBeanNamesForType(MenuDAOPSQL.class);
		String[]list=context.getBeanNamesForType(MenuDAOPSQL.class);
		System.out.println("The list of orders is"+list[0]);
	}
}
