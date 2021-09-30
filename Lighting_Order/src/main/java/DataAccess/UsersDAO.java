package DataAccess;

import java.util.List;

public interface UsersDAO {
	
	
	
	public String findUserByIdJSON(String id);
	public int numberOfUsers();
	public List<String> findAllUserJSON();
	

}
