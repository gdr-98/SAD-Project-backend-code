package UsersData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import DataAccess.UserDAOPSQL;
import RestaurantArea.Order;
@Service
public class UsersController {
	
	public enum ErrorCode {
		UserNotFound,
		UserFound,
		UserNotFoundInDB,
		UserFoundInDB
	}
	
	public List<User> users;
	private ErrorCode ec;

	public UsersController() {
		users = new ArrayList<User>();
	}
	
	public List<String> login(String id) {

		//Caricamento preliminare se necessario
		if(!checkUser(id)) {
			UserDAOPSQL db = new UserDAOPSQL();
			String user = db.findUserByIdJSON(id);

			if(user == null) 
				this.ec = ErrorCode.UserNotFoundInDB;
			else {
				setUserByJSON(user);
				this.ec = ErrorCode.UserFoundInDB;
			}
		}
		
		Optional<User> u = getUserById(id);
		List<String> roles = null;
		if(u.isPresent())
			roles = u.get().getRoles();
		
		return roles;
	}
	
	public void loginAll() {
		users = new ArrayList<User>();
		UserDAOPSQL db = new UserDAOPSQL();
		List<String> list = db.findAllUserJSON();
		for(String single_user_json : list)
			setUserByJSON(single_user_json);
	}
	
	public boolean checkRole(String id, String role) {
		Optional<User> u = getUserById(id);
		boolean found = false;
		if(u.isPresent()) {
			for(String r : u.get().getRoles()) {
				if (r.equals(role))
					found = true;
			}
			return found;
		} else 
			return false;
	}
	
	public boolean checkUser(String id) {
		boolean found = false;
		for(User u : users) {
			if(u.isMe(id))
				found = true;
		}
		return found;
	}
	
	public String getUserByIdJSON(String id) {
		Optional<User> u = getUserById(id);
		if(u.isPresent())
			return u.get().getJSONReppresentation();
		else 
			return "";
	}
	
	public boolean setUserByJSON(String UserJSON) {
		User u = new User();
		boolean created = false;
		try {
			JSONObject user = new JSONObject(UserJSON);
			u.setName(user.getString("name"));
			u.setSurname(user.getString("surname"));
			u.setId(user.getString("id"));
			
			
			JSONArray roles_list = user.getJSONArray("roles");
			List<String> temp = new ArrayList<String>();
			for(int i=0; i<roles_list.length(); i++) 
				temp.add(roles_list.get(i).toString());
			
			u.setRoles(temp);
			if(checkUser(u.getId()))
				created = false;
			else {
				users.add(u);
				created = true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return created;
	}
	
	public UsersController.ErrorCode getErrorCode(){
		return this.ec;
	}
	
	public Optional<User> getUserById(String id){
		boolean found = false;
		this.ec = UsersController.ErrorCode.UserNotFound;
		int index = 0;
		
		while(!found && index<users.size()) {
			if(users.get(index).isMe(id))
				found = true;
			index++;
		}
		if(!found)
			return Optional.empty();
		else {
			this.ec = UsersController.ErrorCode.UserFound;
			return Optional.of(users.get(index-1));
		}
	}
	/**
	 * 
	 * @param id of the user
	 * @param o order to be inserted
	 * @return empty if the user doesn't exists else optional.of operationn result
	 */
	public Optional<User> registerOrderToUser(String id,Order o) {
		login(id);
		Optional<User> user=this.getUserById(id);
		boolean result;
		if(user.isPresent()) {
			result=user.get().registerOrder(o);
			if(result)
				return user;
		}
		return Optional.empty();
	}
}
