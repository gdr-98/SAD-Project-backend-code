package UsersData;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
 
import RestaurantArea.Order;


/**JSON Format:
 * {
 *      name : "name_value"
 *      surname : "surname_value"
 *      id: "id"
 *      roles : ["role_1", "role_2", ... , "role_n"]
 * }
 * */

@Service
public class User {
	private List<Order> orders;
	private String name;
	private String surname;
	private String id;
	private List<String> roles = null;
	
	public enum userRoles{
		Accoglienza,
		Bar,
		Cameriere,
		Cassa,
		Cucina,
		Forno;
	}
	public User() {
		this.roles = new ArrayList<String>();
	}
	
	public User(String name, String surname, String id) {
		this.name = name;
		this.surname = surname;
		this.id = id;
		this.roles = new ArrayList<String>();
	}
	
	public String getJSONReppresentation() {
		String to_return=null;
        try {
        	JSONObject UserJSON=new JSONObject();
        	UserJSON.put("name", this.name);
        	UserJSON.put("surname",this.surname);
        	UserJSON.put("id", this.id);
        	
        	JSONArray temp=new JSONArray();
        	for(String r:this.roles) {
        		temp.put(r);
        	}
        	UserJSON.put("roles", (Object)temp);
        	to_return=UserJSON.toString();
        }
        catch(JSONException e) {
        	//should never happen
        	to_return="";
        	e.printStackTrace();
        }
        return to_return;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = new ArrayList<String>();
		for(String role : roles) 
			this.roles.add(role);
	}
	public boolean isMe(String id) {
		if(id.equals(this.id))
			return true;
		else
			return false;
	}
	
	public boolean registerOrder(Order o) {
		
		if(this.orders==null)
			this.orders=new ArrayList<>();
		return orders.add(o);
		
	}
	/**
 	 * @info : 	utility function that removes an order from the user.
 	 * 			No need to check the state since it could always be done.
 	 * 			This function is meant to be called only from the order
 	 * @return: true if the element is present
 	 */
 	public  boolean unregisterOrder(Order o) {
 	
 		for(int i=0;i<this.orders.size();i++) {
 			if(this.orders.get(i).getId()==o.getId()) {
 				this.orders.remove(i);
 				return true;
 			}
 		}
 		
 		return false;
 	}
}
