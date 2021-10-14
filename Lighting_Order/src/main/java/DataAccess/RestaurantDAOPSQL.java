package DataAccess;


import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Repository("psqlRestaurant")
public class RestaurantDAOPSQL implements RestaurantDAO {

	private JdbcTemplate database;
	
	public static final String DRIVER = "org.postgresql.Driver";
    public static final String JDBC_URL = "jdbc:postgresql://localhost:5432/lightningOrder";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "postgre";
    
    public RestaurantDAOPSQL() {
    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(UserDAOPSQL.DRIVER);
        dataSource.setUrl(UserDAOPSQL.JDBC_URL);
        dataSource.setUsername(UserDAOPSQL.USERNAME);
        dataSource.setPassword(UserDAOPSQL.PASSWORD);
		database = new JdbcTemplate(dataSource);
	}
	

    /**
	 * 
	 * @JSONRepresentation 
	 *	tableID= "tableIDvalue"
	 *	tableRoomNumber="roomNumberValue"
	 *	tableState="Valueofthestate"
	 *	orders=[]
	 */
	@Override
	public String getAllTablesJSON() {
		JSONArray to_return = new JSONArray();
		String query = "SELECT * FROM \"Restaurant\".\"Tavolo\"";
		List<Map<String,Object>> results = database.queryForList(query);
		for(Map<String, Object> record : results) {
			JSONObject temp = new JSONObject();
			JSONArray arrayOfOrders;
			try {
				arrayOfOrders=new JSONArray(this.findOrdersInTable(record.get("id").toString(),
						(Integer)record.get("room")));
			
				temp.put("tableID", record.get("id"));
				temp.put("tableRoomNumber", record.get("room"));
				temp.put("tableState", record.get("state"));
				arrayOfOrders=new JSONArray(this.findOrdersInTable(record.get("id").toString(),
						Integer.valueOf(record.get("room").toString())));
				temp.put("orders", (Object)arrayOfOrders);
				to_return.put((Object) temp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
		return to_return.toString();
	}
	
	/**
	 * 
	 * @JsonRepresenation
	 * 	item:"AssociatedMenuItemName"
	 * 	itemArea:"AssociatedMenuItemArea"
	 * 	additive:[ "additiveID1"...]
	 * 	sub:["subID1"..]
	 *	actualState:"StateName"
	 *	lineNumber="LineNumberValue"
	 *	priority="priorityValue"
	 */
	public String getOrderedItemJSON(int order_id, int line_number) {
		String to_return,state = null;
		Integer ln,priority;
		String query = "SELECT * FROM \"Restaurant\".\"ProdottoOrdinato\""
				+ "WHERE ordine_id = '"+order_id+"' AND linea_ordine = '"+line_number+"'";
		List<Map<String,Object>> results = database.queryForList(query);
		
		JsonObject ordered_item_json = new JsonObject();
		to_return = ordered_item_json.toString();
		if(results.size() > 0) {
			//try {
				String name=results.get(0).get("prodotto_name").toString();
				query="SELECT area FROM \"Restaurant\".\"Prodotto\"" + "WHERE name='"+  name+"'" ;
				ordered_item_json.addProperty("item", results.get(0).get("prodotto_name").toString());
				ordered_item_json.addProperty("itemArea", database.queryForObject(query,String.class));
				state= results.get(0).get("state").toString();
				ln=(Integer)results.get(0).get("linea_ordine");
				priority=(Integer)results.get(0).get("priority");
				query = "SELECT * FROM \"Restaurant\".\"Merce_ProdottoOrdinato\""
						+ "WHERE prodotto_ordinato_ordine_id = '"+order_id+"' AND prodotto_ordinato_linea_ordine = '"+line_number+"'"
						+ " AND type = true";
				JsonArray additive_goods = new JsonArray();
				results = database.queryForList(query);
				for(Map<String,Object> record : results) {
					additive_goods.add(record.get("merce_id").toString());
				}
				ordered_item_json.add("additive",  additive_goods);
				
				
				query = "SELECT * FROM \"Restaurant\".\"Merce_ProdottoOrdinato\""
						+ "WHERE prodotto_ordinato_ordine_id = '"+order_id+"' AND prodotto_ordinato_linea_ordine = '"+line_number+"'"
						+ " AND type = false";
				JsonArray sub_goods = new JsonArray();
				results = database.queryForList(query);
				for(Map<String,Object> record : results) {
					sub_goods.add(record.get("merce_id").toString());
				}
				ordered_item_json.add("sub",  sub_goods);
				ordered_item_json.addProperty("actualState",state);
				ordered_item_json.addProperty("lineNumber", ln);
				ordered_item_json.addProperty("priority",priority );
				to_return = ordered_item_json.toString();
				
		//	} catch (JSONException e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
			//}
		}
		return to_return;
	}

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
	@Override
	public String getAllOrdersJSON() {
		JSONArray to_return = new JSONArray();
		String query = "SELECT * FROM \"Restaurant\".\"Ordine\"";
		List<Map<String,Object>> results = database.queryForList(query);
		for(Map<String,Object> record : results) {
			JSONObject temp = new JSONObject();
			try {
				temp.put("orderID", record.get("id").toString());
				temp.put("orderState", record.get("state").toString());
				temp.put("completedItemNumber", (Integer)record.get("item_completed"));
				temp.put("tableRoomNumber", record.get("tavolo_room").toString());
				temp.put("tableID", record.get("tavolo_id").toString());
				JSONArray ordered_item_json = new JSONArray();
				query = "SELECT * FROM \"Restaurant\".\"ProdottoOrdinato\" WHERE ordine_id = '"+record.get("id").toString()+"'";
				List<Map<String,Object>> results_orderedItem = database.queryForList(query);
				for(Map<String,Object> record_orderedItem : results_orderedItem) {
					ordered_item_json.put(
							new JSONObject(getOrderedItemJSON(Integer.valueOf(record.get("id").toString()), Integer.valueOf(record_orderedItem.get("linea_ordine").toString()))
									));
				}
				temp.put("orderedItems", (Object) ordered_item_json);
				temp.put("userID", record.get("dipendente_id").toString());
				to_return.put(temp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return to_return.toString();
	}

	/**
	 * @JSONRepresentation 
	 *	tableID= "tableIDvalue"
	 *	tableRoomNumber="roomNumberValue"
	 *	tableState="Valueofthestate"
	 *	orders=[]
	 */
	@Override
	public boolean updateTableByJSON(String tableJsonRepresentation) {
		try {
			JSONObject table_json = new JSONObject(tableJsonRepresentation);
			String update_query = "UPDATE \"Restaurant\".\"Tavolo\""
					+ "	SET state='"+table_json.getString("tableState")+"'"
					+ "	WHERE id='"+table_json.getString("tableID") +"'"
					+ " AND room='"+table_json.getInt("tableRoomNumber") +"'"; 
			if(database.update(update_query)>0)
				return true;
			else 
				return false;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

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
	@Override
	public boolean updateOrderByJSON(String orderJsonRepresentation) {
		JSONObject order_json;
		try {
			order_json = new JSONObject(orderJsonRepresentation);
			String update_query = "UPDATE \"Restaurant\".\"Ordine\""
					+ "	SET state='"+order_json.getString("orderState")+"'"
					+", item_completed=" +order_json.getInt("completedItemNumber")+"	WHERE id="+order_json.getInt("orderID") +"";
			if(database.update(update_query)>0)
				return true;
			else
				return false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @JsonRepresenation
	 * 	item:"AssociatedMenuItemName"
	 * 	additive:[ "additiveID1"...]
	 * 	sub:["subID1"..]
	 *	actualState:"StateName"
	 *	lineNumber="LineNumberValue"
	 */
	@Override
	public boolean updateOrderedItem(String orderedItemJsonRepresentation, int order_id) {
		JSONObject orderitem_json;
		try {
			orderitem_json = new JSONObject(orderedItemJsonRepresentation);
			String update_query = "UPDATE \"Restaurant\".\"ProdottoOrdinato\""
					+ "	SET state='"+orderitem_json.getString("actualState")+"'"
					+ "	WHERE ordine_id='"+order_id +"'"
					+ " AND prodotto_name='"+orderitem_json.getString("item")+"'";
			if(database.update(update_query)>0)
				return true;
			else
				return false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	
	@Override
	public boolean addOrderedItem(String orderedItemJsonRepresentation, int order_id) {
		boolean to_return = false;
		try {
			JSONObject temp = new JSONObject(orderedItemJsonRepresentation);
			String insert_query = "INSERT INTO \"Restaurant\".\"ProdottoOrdinato\"("
					+ "ordine_id, prodotto_name, linea_ordine, state, priority)"
					+ "	VALUES ('"+order_id+"', '"+temp.getString("item")+"', '"+temp.getInt("lineNumber")+"', '"+temp.getString("actualState")+"', '"+temp.getInt("priority")+"')";
			if(database.update(insert_query)>0) {
				to_return = true;
				JSONArray additive = temp.getJSONArray("additive");
				for(int i=0; i<additive.length(); i++)
					to_return &= addAdditiveGoodsForOrderedItem(additive.getString(i),order_id, temp.getInt("lineNumber"));
				
				JSONArray subs = temp.getJSONArray("sub");
				for(int i=0; i<subs.length(); i++)
					to_return &= addSubGoodsForOrderedItem(subs.getString(i),order_id, temp.getInt("lineNumber"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return to_return;
	}
	
	private boolean addAdditiveGoodsForOrderedItem(String merce_id, int order_id, int linea_ordine) {
		String insert_query = "INSERT INTO \"Restaurant\".\"Merce_ProdottoOrdinato\"("
				+ "	type, prodotto_ordinato_ordine_id, prodotto_ordinato_linea_ordine, merce_id)"
				+ "	VALUES ('true', '"+order_id+"', '"+linea_ordine+"', '"+merce_id+"')";
		if(database.update(insert_query)>0)
			return true;
		else
			return false;
	}
	
	private boolean addSubGoodsForOrderedItem(String merce_id, int order_id, int linea_ordine) {
		String insert_query = "INSERT INTO \"Restaurant\".\"Merce_ProdottoOrdinato\"("
				+ "	type, prodotto_ordinato_ordine_id, prodotto_ordinato_linea_ordine, merce_id)"
				+ "	VALUES ('false', '"+order_id+"', '"+linea_ordine+"', '"+merce_id+"')";
		if(database.update(insert_query)>0)
			return true;
		else
			return false;
	}

	@Override
	public boolean removeOrderedItem(int orderedItemID, int orderedItemLineNumber) {
		String delete_query = "DELETE FROM \"Restaurant\".\"ProdottoOrdinato\""
				+ "	WHERE + ordine_id="+orderedItemID+" AND linea_ordine="+orderedItemLineNumber+"";
		database.update(delete_query);
		delete_query = "DELETE FROM \"Restaurant\".\"Merce_ProdottoOrdinato\""
				+ "	WHERE + prodotto_ordinato_ordine_id="+orderedItemID+" AND prodotto_ordinato_linea_ordine="+orderedItemLineNumber+"";
		database.update(delete_query);
		/*String query = "SELECT * FROM \"Restaurant\".\"ProdottoOrdinato\" "
				+ "WHERE + 'prodotto_ordinato_ordine_id="+orderedItemID+"' AND 'prodotto_ordinato_linea_ordine="+orderedItemLineNumber+"'";
		List<Map<String,Object>> results = database.queryForList(query);
		if(results.size() == 0) {
			delete_query = "DELETE FROM \"Restaurant\".\"Ordine\""
					+ "	WHERE + 'id="+orderedItemID+"'";
			database.update(delete_query);
		}*/
		return true;
	}

	@Override
	public int findNextOrderID() {
		String query="SELECT MAX(id) FROM \"Restaurant\".\"Ordine\" ";
		int max_value=database.queryForObject(query, Integer.class);
		return max_value+1;
	}

	
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
	@Override
	public boolean insertOrderByJSON(String orderJsonRepresetation, int dipendente_id) {
		try {
			JSONObject order_json = new JSONObject(orderJsonRepresetation);
			String insert_query = "INSERT INTO \"Restaurant\".\"Ordine\"("
					+ "	id, date, dipendente_id, tavolo_id, state, tavolo_room, item_completed)"
					+ "	VALUES ('"+order_json.getInt("orderID")+"', '2021-10-10', '"+dipendente_id+"', '"
							+ order_json.getString("tableID")+"', '"+order_json.getString("orderState")+"', '"+order_json.getInt("tableRoomNumber")+"', '"+order_json.getInt("completedItemNumber")+"')";
			if(database.update(insert_query)>0) {
			//	System.out.println("ORDINE INSERITO");
				JSONArray temp = order_json.getJSONArray("orderedItems");
				for(int i=0; i<temp.length(); i++) {
					addOrderedItem(temp.getJSONObject(i).toString(), order_json.getInt("orderID"));
				}
				return true;
			} else
				return false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public boolean removeOrderById(int order_id) {
		String delete_query = "DELETE FROM \"Restaurant\".\"Ordine\""
				+ "	WHERE + id='"+order_id+"'";
		if(database.update(delete_query)>0)
			return true;
		else
			return false;
	}
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
	@Override
	public String getOrderById(int orderID) {
		String toReturn =null;
		String query="SELECT * FROM \"Restaurant\".\"Ordine\""+ " WHERE + id='"+orderID+"'";
		List<Map<String,Object>> results = database.queryForList(query);
		JsonObject order = new JsonObject();
		toReturn = order.toString();
		JsonArray orderedItems;
		if (results.size()>0) {
			orderedItems=new JsonArray();
			order.addProperty("orderID",(Integer) results.get(0).get("id"));
			order.addProperty("orderState",results.get(0).get("state").toString() );
			order.addProperty("completedItemNumber", (Integer)results.get(0).get("item_completed"));
			order.addProperty("tableRoomNumber",(Integer)results.get(0).get("tavolo_room") );
			order.addProperty("tableID",results.get(0).get("tavolo_id").toString() );
			order.addProperty("userID", results.get(0).get("dipendente_id").toString());
			//temp.put("userID", record.get("dipendente_id").toString());
			//Generiamo gli ordered items
			//Prendo tutti gli item
			query = "SELECT * FROM \"Restaurant\".\"ProdottoOrdinato\" WHERE ordine_id = '"+results.get(0).get("id").toString()+"'";
			//mi trovo la lista degli item
			List<Map<String,Object>> results_orderedItem = database.queryForList(query);
			
			for(Map<String,Object> record_orderedItem : results_orderedItem) {
					//me li vado ad inizializzare
					orderedItems.add(	JsonParser.parseString(getOrderedItemJSON(Integer.valueOf(results.get(0).get("id").toString()), 
								Integer.valueOf(record_orderedItem.get("linea_ordine").toString()))
								).getAsJsonObject());
			}
			order.add("orderedItems", orderedItems);
			toReturn=order.toString();
		}
		return toReturn;
	}
	
	/**
	 * @info: find the orders associated to a table
	 * @param tableID
	 * @param tableRoomNumber
	 * @return the json array of the orders associated to a table
	 */
	public String findOrdersInTable(String tableID,int tableRoomNumber) {
		String query="SELECT * FROM \"Restaurant\".\"Ordine\" "+ "WHERE tavolo_room = " +tableRoomNumber+" "+
															" AND tavolo_id = '" + tableID + "'"
																				;
		
		List<Map<String,Object>> results = database.queryForList(query);
		JsonArray toRet=new JsonArray();
		String helper;
		for(Map<String,Object> o:results) {
			helper=getOrderById(Integer.valueOf(o.get("id").toString()));
			toRet.add(JsonParser.parseString(helper).getAsJsonObject());
	 
		}
		
		
		return toRet.toString();
		
	}
	


}
