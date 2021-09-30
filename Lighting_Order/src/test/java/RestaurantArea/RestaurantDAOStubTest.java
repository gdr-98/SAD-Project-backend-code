package RestaurantArea;

/*
package RestaurantArea;
import MenuAndWareHouseArea.*;
import DataAccess.*;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.Test;

import com.google.gson.JsonParser;

import com.google.gson.*;
class RestaurantDAOStubTest {
	private RestaurantController controller;
//	private RestaurantDAO stub;
	private MenuAndGoodsController menuController;
	@BeforeEach
	void init() {
		menuController=new MenuAndGoodsController(new DBMenuStub());
		controller=new RestaurantController(new RestaurantDAOStub(),menuController);
		
	}
	@AfterEach
	void cleanup() {
		
	}
	@Test
	void test1() {
		List<Table> t=controller.getTables(Optional.empty());
		assertEquals(3,t.size());
		assertEquals("id1", t.get(0).getId());
		assertEquals("id2", t.get(1).getId());
		assertEquals("id1", t.get(2).getId());
		assertEquals(1, t.get(0).getRoomNumber());
		assertEquals(1, t.get(1).getRoomNumber());
		assertEquals(2, t.get(2).getRoomNumber());
		assertEquals("free",t.get(0).getStateString());
		assertEquals("free",t.get(1).getStateString());
		assertEquals("free",t.get(2).getStateString());
		assertEquals(0,controller.getOrders().size());
	}
	
	@Test
	void test2() {
		String result=controller.freeTable("id4", 1);
		assertEquals("TableNotFound",result);
		assertEquals("id1", controller.getTables(Optional.empty()).get(0).getId());
		assertEquals(1,controller.getTables(Optional.empty()).get(0).getRoomNumber());
		assertTrue(controller.getTable("id1", 1).isPresent());
		result=controller.tableInWaitingForOrders("id1", 1);
		assertEquals("waitingForOrders",result);
		result=controller.freeTable("id1", 1);
		assertEquals("free",result);
	}
	
	//Adds an order to table...
	@Test
	void test3() {
		Map<String,List<String>>additive=new HashMap<>();
		List<String> toAdd=new ArrayList<>();
		List<String > toSub=new ArrayList<>();
		toSub.add("3");
		toAdd.add("1");
		toAdd.add("2");
		toAdd.add("3");
		additive.put("PastaVuota",toAdd);
		additive.put("pizza",toAdd);
		additive.put("banana",toAdd);
		
		
		Map<String,List<String>> sub=new HashMap<>();
		sub.put("PastaVuota",toSub);
		sub.put("pizza",toSub);
		sub.put("banana",toSub);
		
		Map<String,Integer> priority=new HashMap<>();
		priority.put("PastaVuota",1);
		priority.put("pizza",1);
		priority.put("banana",2);
		assertTrue(controller.getTable("id2", 1).isPresent());
		JsonObject obj=JsonParser.parseString(controller.generateOrderAndAddToTable
		(additive, sub, priority, "id2", 1, "random"
				)).getAsJsonObject(); //table not founnd but order created since the table isn't in list
		assertEquals("notFound",obj.get("table").getAsString());
		assertEquals(1,controller.getOrders().size());
		assertEquals(1,controller.getOrders().get(0).getId());
		assertTrue(controller.getOrderById(1).isPresent());
		assertEquals(2,controller.getOrderById(1).get().getRemainingItemNumber());
		//System.out.println(controller.getOrdersJSON());
		assertTrue(controller.cancelOrder(1).get());
		assertEquals(0, controller.getOrders().size());
	}
	
	@Test
	void test4() {
		Map<String,List<String>>additive=new HashMap<>();
		List<String> toAdd=new ArrayList<>();
		List<String > toSub=new ArrayList<>();
		List<String>toAdd2=new ArrayList<>();
		toAdd2.add("3");
		toSub.add("3");
		toAdd.add("1");
		toAdd.add("2");
		toAdd.add("3");
		additive.put("PastaVuota",toAdd);
		additive.put("pizza",toAdd2);
		additive.put("banana",toAdd);
		Map<String,List<String>> sub=new HashMap<>();
		sub.put("PastaVuota",toSub);
		sub.put("pizza",toSub);
		sub.put("banana",toSub);
		
		Map<String,Integer> priority=new HashMap<>();
		
		priority.put("PastaVuota",1);
		priority.put("pizza",1);
		priority.put("banana",2);
		
		controller.getTable("id2", 1).get().setInWaitingForOrders();
		JsonObject obj=JsonParser.parseString(controller.generateOrderAndAddToTable
		(additive, sub, priority, "id2", 1, "random"
				)).getAsJsonObject(); //table not founnd but order created since the table isn't in list
		//System.out.println(obj.toString());
		assertEquals(1,controller.getOrders().size());
		assertEquals(1,controller.getOrders().get(0).getId());
		assertTrue(controller.getOrderById(1).isPresent());
		assertEquals(2,controller.getOrderById(1).get().getRemainingItemNumber());
		controller.generateOrderAndAddToTable(additive, sub, priority, "id1", 2, "random");
		assertTrue(controller.cancelOrder(2).get());
		assertEquals(1, controller.getOrders().size());	
		
		//Check the report if is ok
		assertEquals("added",obj.get("table").getAsString());
		assertEquals("yes",obj.get("orderCreated").getAsString());
		JsonArray itemsReport=obj.get("itemsReport").getAsJsonArray();
		assertEquals(3,itemsReport.size());
		System.out.println(itemsReport.toString());
		
		assertEquals("banana",itemsReport.get(0).getAsJsonObject().get("name").getAsString());
		assertEquals("notFound",itemsReport.get(0).getAsJsonObject().get("status").getAsString());
		assertEquals("pizza",itemsReport.get(1).getAsJsonObject().get("name").getAsString());
		assertEquals("found",itemsReport.get(1).getAsJsonObject().get("status").getAsString());
		assertEquals("GoodsAdded",itemsReport.get(1).getAsJsonObject().get("additive").getAsString());
		assertEquals("GoodsNotSubbedZeroMatches",itemsReport.get(1).getAsJsonObject().get("sub").getAsString());
		
		assertEquals("PastaVuota",itemsReport.get(2).getAsJsonObject().get("name").getAsString());
		assertEquals("found",itemsReport.get(2).getAsJsonObject().get("status").getAsString());
		assertEquals("GoodsAddedNotAll",itemsReport.get(2).getAsJsonObject().get("additive").getAsString());
		assertEquals("GoodsSubbed",
				itemsReport.get(2).getAsJsonObject().get("sub").getAsString());
		
	}

	
	
}

*/
