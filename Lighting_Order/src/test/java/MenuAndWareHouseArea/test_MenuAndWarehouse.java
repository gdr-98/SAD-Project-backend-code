package MenuAndWareHouseArea;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import DataAccess.MenuDAOPSQL;



//@SpringBootTest(classes=MenuAndGoodsController.class)
class test_MenuAndWarehouse {
	
	MenuAndGoodsController c;
	MenuDAOPSQL db;

	@BeforeEach
	void setUp() throws Exception {
		db = new MenuDAOPSQL();
		c = new MenuAndGoodsController(db);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test_printAllMenuItems() {
		db.findAllMenuItemsJSON();
		
		JsonArray array=JsonParser.parseString(db.findAllMenuItemsJSON()).getAsJsonArray();
		assertTrue(array.size()>0);
		assertEquals(6,array.size());
		
		JsonObject good1= JsonParser.parseString(db.findGoodsByIdJSON("C001")).getAsJsonObject();
		c.initGoodsFromJSON(good1.toString());
		good1=JsonParser.parseString(db.findGoodsByIdJSON("B001")).getAsJsonObject();
		assertEquals(db.findGoodsByIdJSON("B001"),c.initGoodsFromJSON(good1.toString()).get().getJSONRepresentation());
		good1=JsonParser.parseString(db.findGoodsByIdJSON("B002")).getAsJsonObject();
		assertEquals(good1.toString(),c.initGoodsFromJSON(good1.toString()).get().getJSONRepresentation());
		good1=JsonParser.parseString(db.findGoodsByIdJSON("F001")).getAsJsonObject();
		c.initGoodsFromJSON(good1.toString());
		assertEquals(good1.toString(),c.initGoodsFromJSON(good1.toString()).get().getJSONRepresentation());
		good1=JsonParser.parseString(db.findGoodsByIdJSON("G001")).getAsJsonObject();
		c.initGoodsFromJSON(good1.toString());
		assertEquals(good1.toString(),c.initGoodsFromJSON(good1.toString()).get().getJSONRepresentation());
		good1=JsonParser.parseString(db.findGoodsByIdJSON("G002")).getAsJsonObject();
		c.initGoodsFromJSON(good1.toString());
		assertEquals(good1.toString(),c.initGoodsFromJSON(good1.toString()).get().getJSONRepresentation());
		good1=JsonParser.parseString(db.findGoodsByIdJSON("G003")).getAsJsonObject();
		c.initGoodsFromJSON(good1.toString());
		assertEquals(good1.toString(),c.initGoodsFromJSON(good1.toString()).get().getJSONRepresentation());
		good1=JsonParser.parseString(db.findGoodsByIdJSON("G004")).getAsJsonObject();
		c.initGoodsFromJSON(good1.toString());
		assertEquals(good1.toString(),c.initGoodsFromJSON(good1.toString()).get().getJSONRepresentation());
	}
	

	@Test
	@SuppressWarnings({ "deprecation", "unused" })
	
	void test_MenuControllerInit() {
		String menu=c.getMenuJSON(false, null);
		List<MenuItem> m = c.getAllItems();
		List<Goods> g=c.getAllGoods();
		assertEquals(6, m.size());
		/*The query is: SELECT COUNT(*)
		FROM(
				SELECT merce_id FROM "Restaurant"."Merce_Prodotto" 
					GROUP BY "merce_id")oldQuery;*/
		assertEquals(10,g.size());

		assertEquals("B001",g.get(0).getId());
		assertEquals("B002",g.get(1).getId());
		assertEquals("C001",g.get(2).getId());
		assertEquals("F001",g.get(3).getId());
		assertEquals("F002",g.get(4).getId());
		assertEquals("G001",g.get(5).getId());
		assertEquals("G002",g.get(6).getId());
		assertEquals("F003",g.get(7).getId());
		assertEquals("G003",g.get(8).getId());
		assertEquals("G004",g.get(9).getId());
		
		assertEquals("Acqua Natia",m.get(0).getName());
		assertEquals("Acqua Ferrarelle",m.get(1).getName());
		assertEquals("Patate Fritte",m.get(2).getName());
		assertEquals("Margherita",m.get(3).getName());
		assertEquals("Margherita Speciale",m.get(4).getName());
		assertEquals("Napoletana",m.get(5).getName());
	}
	

}
