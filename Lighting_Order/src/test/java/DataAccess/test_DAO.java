package DataAccess;



import static org.junit.jupiter.api.Assertions.*;



import java.util.List;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;





class test_DAO {

	MenuDAOPSQL db;
	RestaurantDAOPSQL dbr;



	@BeforeEach
	void setUp() throws Exception {
		db = new MenuDAOPSQL();
		dbr = new RestaurantDAOPSQL();
	}



	@AfterEach
	void tearDown() throws Exception {
	}



	@Test
	void test_findAllMenuItemsJSON() {
		String res = db.findAllMenuItemsJSON();
		try {
			JSONArray menu_items = new JSONArray(res);
			assertEquals(menu_items.length(), 6);
		for(int i=0; i<menu_items.length(); i++) {
			JSONObject j = menu_items.getJSONObject(i);
			System.out.println("[TEST_1] : "+j.toString());
		}
		} catch (JSONException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}



	@Test
	void test_findGoodsByIdJSON() {
		String res = db.findGoodsByIdJSON("B001");
		try {
			JSONObject j = new JSONObject(res);
			assertEquals(j.getString("name"), "Acqua Natia");
		} catch (JSONException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

	@Test
	void test_findMenuItemByName() {
		String res = db.findMenuItemByNameJSON("Margherita");
		try {
			JSONObject j = new JSONObject(res);
			assertEquals(j.getString("area"), "Forno");
			assertEquals(j.getDouble("price"), 5);
		} catch (JSONException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

	@Test
	void test_numberOfMenuItems() {
		int res = db.numberOfMenuItems();
		assertEquals(res, 6);
	}

	@Test
	void test_numberOfGoods() {
		int res = db.numberOfGoods();
		assertEquals(res, 10);
	}

	@Test
	void test_menuItemsNames() {
		List<String> l = db.menuItemsNames();
		assertEquals(l.size(), 6);
		for(String k : l) {
			System.out.println("[TEST 2] : "+k);
		}
	}

	@Test
	void test_getAllTablesJSON() {
	try {
		JSONArray tables = new JSONArray(dbr.getAllTablesJSON());
		assertEquals(tables.length(), 4);
		for(int i=0; i<tables.length(); i++) {
			JSONObject j = tables.getJSONObject(i);
			System.out.println("[TEST 3] : "+j.toString());
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void test_getOrderedItemJSON() {
	
	}
}