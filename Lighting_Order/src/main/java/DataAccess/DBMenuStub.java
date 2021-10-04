package DataAccess;

import java.util.List;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;


public class DBMenuStub implements MenuAndWarehouseDAO {
	
	
	
	//returns an array list of json
	public String findAllMenuItemsJSON() {
		try {
			JSONArray to_ret=new JSONArray();
			//Pizza
		  	JSONObject obj1=new JSONObject();
	    	obj1.put("name", "pizza");
	    	obj1.put("description","Pizza margherita");
	    	obj1.put("price", 3.5);
	    	obj1.put("inStock", false);
	    	obj1.put("area","forno");
	    	JSONArray temp=new JSONArray();
	    	temp.put("1");
	    	temp.put("2");
	    	obj1.put("goodsID",(Object)temp);
	    	
		  	JSONObject obj2=new JSONObject();
	    	obj2.put("name", "Pasta");
	    	obj2.put("description","PastaSalsa");
	    	obj2.put("price", 5);
	    	obj2.put("inStock", false);
	    	obj2.put("area","cucina");
	    	JSONArray temp2=new JSONArray();
	    	temp2.put("1");
	    	temp2.put("3");
	    	obj2.put("goodsID",(Object)temp2);
			
			
	     	JSONObject obj3=new JSONObject();
	    	obj3.put("name", "PastaVuota");
	    	obj3.put("description","Pasta no salsa");
	    	obj3.put("price", 5);
	    	obj3.put("inStock", false);
	    	obj3.put("area","cucina");
	    	JSONArray temp3=new JSONArray();
	    	temp3.put("3");
	    	obj3.put("goodsID",(Object)temp3);
	    	
	    	return to_ret.
	    			put(obj1).
	    			put(obj2).put(obj3).toString();
	    	
		}
		catch(JSONException e) {
			return "";
		}	
	}
	
	
	//returns a single json
	public String findGoodsByIdJSON(String id) {
		try {
			JSONObject obj=new JSONObject();
			JSONArray temp=new JSONArray();
			if (id.equals("1")) {
				obj.put("id", id);
	    		obj.put("quantity", 10);
	    		obj.put("priceAsAdditive", 15);
	    		obj.put("quantityLowerBound", 5);
	    		obj.put("name", "salsa");
	    		obj.put("unitPrice",30);
	    		obj.put("inStock", true);
	    		temp.put("pizza");
	    		temp.put("Pasta");
	    		obj.put("menuItems",(Object)temp);
	    		//obj.put("menuItems", temp.toString());
	    		return obj.toString();
	        	
			}
			else if (id.equals("2")) {
				JSONObject obj2=new JSONObject();
				JSONArray temp2=new JSONArray();
				obj2.put("id", "2");
				obj2.put("quantity", 3);
				obj2.put("priceAsAdditive", 7);
				obj2.put("quantityLowerBound", 1);
				obj2.put("name", "resto pizza");
				obj2.put("unitPrice",30);
				obj2.put("inStock", true);
				temp2.put("pizza");
				obj2.put("menuItems",(Object)temp);
				return obj2.toString();
				
			}
			
			else if (id.equals("3")) {
				obj.put("id", id);
	    		obj.put("quantity", 20);
	    		obj.put("priceAsAdditive", 23.9);
	    		obj.put("quantityLowerBound", 20);
	    		obj.put("name", "pasta vuota");
	    		obj.put("unitPrice",21);
	    		obj.put("inStock", true);
	    		temp.put("PastaVuota");
	    		temp.put("Pasta");
	    		obj.put("menuItems",(Object)temp);
	    		//obj.put("menuItems", temp.toString());
	    		return obj.toString();
				
			}
			return "";
			
		}
		
		catch(JSONException e){
			return "";
		}
		
	}
	
	//Returns a single menuItem
	public String findMenuItemByNameJSON(String menuItemName) {
		try {
			if (menuItemName.equals("pizza")) {
				//JSONArray to_ret=new JSONArray();
				//Pizza
				JSONObject obj2=new JSONObject();
		    	obj2.put("name", "pizza");
		    	obj2.put("description","pizza salsa");
		    	obj2.put("price", 5);
		    	obj2.put("inStock", false);
		    	obj2.put("area","forno");
		    	JSONArray temp2=new JSONArray();
		    	temp2.put("1");
		    	temp2.put("2");
		    	obj2.put("goodsID",(Object)temp2);
		    	return obj2.toString();
				
			}
			else if(menuItemName.equals("Pasta") ){
				
		
				JSONObject obj2=new JSONObject();
		    	obj2.put("name", "Pasta");
		    	obj2.put("description","pizza salsa");
		    	obj2.put("price", 5);
		    	obj2.put("inStock", false);
		    	obj2.put("area","cucina");
		    	JSONArray temp2=new JSONArray();
		    	temp2.put("1");
		    	temp2.put("3");
		    	obj2.put("goodsID",(Object)temp2);
		    	return obj2.toString();
				
			}
			else if(menuItemName.equals("PastaVuota") ){
				JSONObject obj3=new JSONObject();
		    	obj3.put("name", "PastaVuota");
		    	obj3.put("description","Pasta no salsa");
		    	obj3.put("price", 5);
		    	obj3.put("inStock", false);
		    	obj3.put("area","cucina");
		    	JSONArray temp3=new JSONArray();
		    	temp3.put("3");
		    	obj3.put("goodsID",(Object)temp3);
				return obj3.toString();
			}
			return "";
		}
		catch(JSONException e){
			return "";
		}
	}
	
	//Returns the number of menuItems 
	public Integer numberOfMenuItems() {
		return 3;
	}
	
	//returns the number of goods.
	public Integer numberOfGoods() {
		return 3;
	}
	//returns the list of menuItemsNames
	public List<String> menuItemsNames(){
		List<String> names=new ArrayList<String>();
		names.add("pizza");
		names.add("Pasta");
		names.add("PastaVuota");
		return names;
	}
}
