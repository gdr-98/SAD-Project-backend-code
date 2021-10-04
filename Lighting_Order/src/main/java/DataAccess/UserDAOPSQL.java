package DataAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;
@Repository("psqlUsers")
public class UserDAOPSQL implements UsersDAO {
	
	
	private JdbcTemplate database;
	
	public static final String DRIVER = "org.postgresql.Driver";
    public static final String JDBC_URL = "jdbc:postgresql://localhost:5432/lightingOrder";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "porcodio";
	
	public UserDAOPSQL() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(UserDAOPSQL.DRIVER);
        dataSource.setUrl(UserDAOPSQL.JDBC_URL);
        dataSource.setUsername(UserDAOPSQL.USERNAME);
        dataSource.setPassword(UserDAOPSQL.PASSWORD);
		database = new JdbcTemplate(dataSource);
	}

	
	/**JSON Format:
	 * {
	 *      name : "name_value"
	 *      surname : "surname_value"
	 *      id: "id"
	 *      roles : ["rolename_1", "rolename_2", ... , "rolename_n"]
	 * }
	 * */
	@Override
	public String findUserByIdJSON(String id) {
		String to_return = null;
		
		//Query per ricavare un utente tramite id
		String query = "SELECT * FROM \"Restaurant\".\"Dipendente\" "
				+ "WHERE id = '"+ id + "'";
		JSONObject user = new JSONObject();
		List<String> roles = new ArrayList<String>();
		
		List<Map<String,Object>> results = database.queryForList(query);
		
		//Se l'utente viene trovato nel DB
		if(results.size() > 0) {
			try {
				user.put("name", results.get(0).get("name"));
				user.put("surname", results.get(0).get("surname"));
				user.put("id", results.get(0).get("id"));
				query = "SELECT * FROM \"Restaurant\".\"Dipendente_Ruolo\" "
						+ "WHERE dipendente_id = '"+ id + "'";
				results = database.queryForList(query);
				
				//Se ci sono ruoli associati all'utente allora può continuare
				if(results.size() > 0) {
					for(Map<String,Object> record : results)
						roles.add(record.get("ruolo_name").toString());
					
					JSONArray temp = new JSONArray();
					for(String r : roles)
						temp.put(r);
					user.put("roles", (Object)temp);
					
					to_return = user.toString();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return to_return;
	}

	@Override
	public int numberOfUsers() {
		int to_return = 0;
		//Query per ricavare un utente tramite id
		String query = "SELECT COUNT(*) FROM \"Restaurant\".\"Dipendente\" ";
		to_return = database.queryForObject(query, Integer.class);
		return to_return;
	}

	@Override
	public List<String> findAllUserJSON() {
		List<String> users_list = new ArrayList<String>();
		
		String query = "SELECT id FROM \"Restaurant\".\"Dipendente\"";
		List<Map<String, Object>> results = database.queryForList(query);
		for(Map<String, Object> record : results) {
			users_list.add(findUserByIdJSON(record.get("id").toString()));
		}
		return users_list;
	}


}
