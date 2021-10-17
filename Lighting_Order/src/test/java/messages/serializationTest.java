package messages;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;



class serializationTest {

	@Test
	void test() {
		JsonObject obj=new JsonObject();
		JsonObject obj2=new JsonObject();
		JsonArray helper=new JsonArray();
		JsonArray helper1=new JsonArray();
		JsonArray helper2=new JsonArray();
		JsonArray helper3=new JsonArray();
		JsonArray helper4=new JsonArray();
		obj.addProperty("user", "1");
		obj.addProperty("proxySource", "proxy1");
		obj.addProperty("request", "esempio");
		Gson gson=new Gson();
	
		obj.addProperty("tableID", "27");
		obj.addProperty("tableRoomNumber", 2);
		helper.add(1);
		helper.add(2);
		helper.add(1);
		obj2.add("priority", helper);
		
		
		helper1.add("Margherita");
		helper1.add("Salsiccia e patate");
		helper1.add("Marinara");
		obj2.add("itemNames", helper1);
		helper2.add("Prosciutto");
		helper2.add("patate");
		
		
		helper3.add(helper2);
		helper3.add(new JsonArray());
		helper3.add(new JsonArray());
		
		obj2.add("addGoods", helper3);
		
	
		for(int i=0;i<3;i++)
			helper4.add(new JsonArray());
		obj2.add("subGoods", helper4);
		obj.add("orderParams", obj2);
		
		orderToTableGenerationRequest obj3=gson.fromJson(obj.toString(), messages.orderToTableGenerationRequest.class);
		
		assertEquals(3,obj3.orderParams.itemNames.size());
		assertEquals("Margherita",obj3.orderParams.itemNames.get(0));
		assertEquals("Salsiccia e patate",obj3.orderParams.itemNames.get(1));
		assertEquals("Marinara",obj3.orderParams.itemNames.get(2));
		assertEquals(1,obj3.orderParams.priority.get(0));
		assertEquals(2,obj3.orderParams.priority.get(1));
		assertEquals(1,obj3.orderParams.priority.get(2));
		assertEquals(3,obj3.orderParams.addGoods.size());
		assertEquals(3,obj3.orderParams.subGoods.size());
		assertEquals(2,obj3.orderParams.addGoods.get(0).size());
		assertEquals("Prosciutto",obj3.orderParams.addGoods.get(0).get(0));
		assertEquals("patate",obj3.orderParams.addGoods.get(0).get(1));
		assertEquals(0,obj3.orderParams.addGoods.get(1).size());
		assertEquals(0,obj3.orderParams.addGoods.get(2).size());
		assertEquals(0,obj3.orderParams.subGoods.get(1).size());
		assertEquals(0,obj3.orderParams.subGoods.get(2).size());
		assertEquals(0,obj3.orderParams.subGoods.get(0).size());
	}

}
