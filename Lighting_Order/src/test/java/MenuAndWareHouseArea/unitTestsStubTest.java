package MenuAndWareHouseArea;

import DataAccess.DBMenuStub;
import DataAccess.MenuAndWarehouseDAO;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.AfterEach;
/*
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Disabled;*/

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

//import java.lang.ModuleLayer.Controller;
import java.util.List;
import java.util.ArrayList;
class unitTestsStubTest {
	private MenuAndWarehouseDAO db; 
	private MenuAndGoodsController controller;

	@BeforeEach
	void setup() {
		db=new DBMenuStub();
		controller=new MenuAndGoodsController(db);
	}
	@AfterEach
	void cleanup() {
		//controller
	}
	
	
	@Test
	@SuppressWarnings("deprecation")
	void itemsTestInit() {
		Optional<MenuItem> item= controller.getMenuItemByName("pizza");
		assertTrue(item.isPresent());
		assertEquals("pizza",item.get().getName());
		item= controller.getMenuItemByName("Pasta");
		assertTrue(item.isPresent());
		assertEquals("Pasta",item.get().getName());
		item= controller.getMenuItemByName("PastaVuota");
		assertTrue(item.isPresent());
		assertEquals("PastaVuota",item.get().getName());
		assertEquals(3,controller.getAllItems().size());
	}
	@Test
	@SuppressWarnings("deprecation")
	void itemsTestInit2() {
		Optional<MenuItem> item= controller.getMenuItemByName("pizza");
		assertTrue(item.isPresent());
		assertEquals("pizza",item.get().getName());
		item= controller.getMenuItemByName("PastaVuota");
		assertTrue(item.isPresent());
		assertEquals("PastaVuota",item.get().getName());
		item= controller.getMenuItemByName("Pasta");
		assertTrue(item.isPresent());
		assertEquals("Pasta",item.get().getName());
		assertEquals(3,controller.getAllItems().size());
	}
	@Test
	@SuppressWarnings("deprecation")
	void itemsTestInit3() {
		Optional<MenuItem> item= controller.getMenuItemByName("PastaVuota");
		assertTrue(item.isPresent());
		assertEquals("PastaVuota",item.get().getName());
		item= controller.getMenuItemByName("pizza");
		assertTrue(item.isPresent());
		assertEquals("pizza",item.get().getName());
		item= controller.getMenuItemByName("Pasta");
		assertTrue(item.isPresent());
		assertEquals("Pasta",item.get().getName());
		assertEquals(3,controller.getAllItems().size());
	}
	
	@Test
	@SuppressWarnings("deprecation")
	void itemsTestInit4() {
		Optional<MenuItem> item= controller.getMenuItemByName("PastaVuota");
		assertTrue(item.isPresent());
		assertEquals("PastaVuota",item.get().getName());
		item= controller.getMenuItemByName("Pasta");
		assertTrue(item.isPresent());
		assertEquals("Pasta",item.get().getName());
		item= controller.getMenuItemByName("pizza");
		assertTrue(item.isPresent());
		assertEquals("pizza",item.get().getName());
		assertEquals(3,controller.getAllItems().size());
	}
	
	@Test
	@SuppressWarnings("deprecation")
	void itemsTestInit5() {
		Optional<MenuItem> item= controller.getMenuItemByName("Pasta");
		assertTrue(item.isPresent());
		assertEquals("Pasta",item.get().getName());
		item= controller.getMenuItemByName("pizza");
		assertTrue(item.isPresent());
		assertEquals("pizza",item.get().getName());
		item= controller.getMenuItemByName("PastaVuota");
		assertTrue(item.isPresent());
		assertEquals("PastaVuota",item.get().getName());
		assertEquals(3,controller.getAllItems().size());
	}
	
	@Test
	@SuppressWarnings("deprecation")
	void itemsTestInit6() {
		Optional<MenuItem> item= controller.getMenuItemByName("Pasta");
		assertTrue(item.isPresent());
		assertEquals("Pasta",item.get().getName());
		item= controller.getMenuItemByName("PastaVuota");
		assertTrue(item.isPresent());
		assertEquals("PastaVuota",item.get().getName());
		item= controller.getMenuItemByName("pizza");
		assertTrue(item.isPresent());
		assertEquals("pizza",item.get().getName());
		assertEquals(3,controller.getAllItems().size());
	}
	
	@Test
	//@SuppressWarnings("deprecation")
	void goodsTestInit() {
		Optional<Goods> item= controller.getGoodsById("2");
		assertTrue(item.isPresent());
		assertEquals("2",item.get().getId());
		item= controller.getGoodsById("1");
		assertTrue(item.isPresent());
		assertEquals("1",item.get().getId());
		item= controller.getGoodsById("3");
		assertTrue(item.isPresent());
		assertEquals("3",item.get().getId());
	}
	@Test
	//@SuppressWarnings("deprecation")
	void goodsTestInit2() {
		Optional<Goods> item= controller.getGoodsById("3");
		assertTrue(item.isPresent());
		assertEquals("3",item.get().getId());
		item= controller.getGoodsById("1");
		assertTrue(item.isPresent());
		assertEquals("1",item.get().getId());
		item= controller.getGoodsById("2");
		assertTrue(item.isPresent());
		assertEquals("2",item.get().getId());
	}
	@Test
	//@SuppressWarnings("deprecation")
	void goodsTestInit3() {
		Optional<Goods> item= controller.getGoodsById("1");
		assertTrue(item.isPresent());
		assertEquals("1",item.get().getId());
		item= controller.getGoodsById("2");
		assertTrue(item.isPresent());
		assertEquals("2",item.get().getId());
		item= controller.getGoodsById("3");
		assertTrue(item.isPresent());
		assertEquals("3",item.get().getId());
	}
	@Test
	//@SuppressWarnings("deprecation")
	void goodsTestInit4() {
		Optional<Goods> item= controller.getGoodsById("1");
		assertTrue(item.isPresent());
		assertEquals("1",item.get().getId());
		item= controller.getGoodsById("3");
		assertTrue(item.isPresent());
		assertEquals("3",item.get().getId());
		item= controller.getGoodsById("2");
		assertTrue(item.isPresent());
		assertEquals("2",item.get().getId());
	}
	@Test
	//@SuppressWarnings("deprecation")
	void goodsTestInit5() {
		Optional<Goods> item= controller.getGoodsById("2");
		assertTrue(item.isPresent());
		assertEquals("2",item.get().getId());
		item= controller.getGoodsById("3");
		assertTrue(item.isPresent());
		assertEquals("3",item.get().getId());
		item= controller.getGoodsById("1");
		assertTrue(item.isPresent());
		assertEquals("1",item.get().getId());
	}
	@Test
	//@SuppressWarnings("deprecation")
	void goodsTestInit6() {
		Optional<Goods> item= controller.getGoodsById("3");
		assertTrue(item.isPresent());
		assertEquals("3",item.get().getId());
		item= controller.getGoodsById("2");
		assertTrue(item.isPresent());
		assertEquals("2",item.get().getId());
		item= controller.getGoodsById("1");
		assertTrue(item.isPresent());
		assertEquals("1",item.get().getId());
	}
	
	
	
	@Test
	@SuppressWarnings("deprecation")
	void cooperationTest1() {
	
		controller.getMenuItemByName("PastaVuota");
		List<Goods> goods=controller.getAllGoods();
		List<MenuItem> items=controller.getAllItems();
		assertEquals(3,goods.size());
		assertEquals(3,items.size());
		//La pasta vuota ha portato a caricare la merce "3" che ha portato poi a caricare la merce "1" facendo
		//parte della pasta ed infine la " 2"
		assertEquals("3",goods.get(0).getId());
		assertEquals("1",goods.get(1).getId());
		assertEquals("2",goods.get(2).getId());
		
		assertEquals("PastaVuota",items.get(0).getName());
		assertEquals("Pasta",items.get(1).getName());
		assertEquals("pizza",items.get(2).getName());
	}
	
	@Test
	@SuppressWarnings("deprecation")
	void cooperationTest2() {
		controller.getMenuItemByName("pizza"); 
		List<Goods> goods=controller.getAllGoods();
		List<MenuItem> items=controller.getAllItems();
		assertEquals(3,goods.size());
		assertEquals(3,items.size());
		//La pizza porta come elementi 1 che automaticamente porta 3,l'ultimo sarà sicuramente 2
		assertEquals("1",goods.get(0).getId());
		assertEquals("3",goods.get(1).getId());
		assertEquals("2",goods.get(2).getId());
		
		assertEquals("pizza",items.get(0).getName());
		assertEquals("Pasta",items.get(1).getName());
		assertEquals("PastaVuota",items.get(2).getName());
		
	}
	
	@Test
	@SuppressWarnings("deprecation")
	void cooperationTest3() {
		controller.getMenuItemByName("Pasta"); 
		List<Goods> goods=controller.getAllGoods();
		List<MenuItem> items=controller.getAllItems();
		assertEquals(3,goods.size());
		assertEquals(3,items.size());
		assertEquals("Pasta",items.get(0).getName());
		assertEquals("pizza",items.get(1).getName());
		assertEquals("PastaVuota",items.get(2).getName());
		assertEquals("1",goods.get(0).getId());
		assertEquals("2",goods.get(1).getId());
		assertEquals("3",goods.get(2).getId());
		
	}
	@Test
	@SuppressWarnings("deprecation")
	void  duplicateTest() {
		controller.getMenuItemByName("Pasta");
		controller.getMenuItemByName("Pasta");
		List<MenuItem> list=controller.getAllItems();
		assertEquals(3,list.size());
		controller.getMenuItemByName("PastaVuota");
		controller.getMenuItemByName("PastaVuota");
		List<MenuItem> list2=controller.getAllItems();
		assertEquals(3,list2.size());
		List<Goods> list3 =controller.getAllGoods();
		assertEquals(3,list3.size());
	}
	
	@Test
	@SuppressWarnings("deprecation")
	void  duplicateTest3() {
		controller.getGoodsById("1");
		controller.getGoodsById("1");
		List<Goods> list=controller.getAllGoods();
		List<MenuItem>  item_list=controller.getAllItems();
		assertEquals(3,list.size());
		assertEquals(3,item_list.size());
		controller.getGoodsById("3");
		List<Goods> list2=controller.getAllGoods();
		List<MenuItem>  item_list2=controller.getAllItems();
		assertEquals(3,list2.size());
		assertEquals(3,item_list2.size());
		controller.getGoodsById("2");
		List<Goods> list3=controller.getAllGoods();
		List<MenuItem>  item_list3=controller.getAllItems();
		assertEquals(3,list3.size());
		assertEquals(3,item_list3.size());
	
	}
	
	@Test
	@SuppressWarnings("deprecation")
	void getMenuTest() {
		String k=controller.getMenuJSON(false,"");
		List  <MenuItem> item=controller.getAllItems();
		List<Goods> goods=controller.getAllGoods();
		assertEquals(3,item.size());
		assertEquals(3,goods.size());
		assertEquals("pizza",item.get(0).getName());
		assertEquals("Pasta",item.get(1).getName());
		assertEquals("PastaVuota",item.get(2).getName());
		try {
			JSONArray h=new JSONArray(k);
			JSONObject ob;
			assertEquals(3,h.length());
			String[] names= {"pizza","Pasta","PastaVuota"};
			
			for(int i=0;i<h.length();i++) {
				ob=h.getJSONObject(i);
				assertEquals(names[i],ob.getString("name"));
			}
		}
		catch(JSONException e) {
			e.printStackTrace();
		}
		
		controller.getMenuJSON(false,"");	
		List  <MenuItem> item2=controller.getAllItems();
		List<Goods> goods2=controller.getAllGoods();
		assertEquals(3,item2.size());
		assertEquals(3,goods2.size());
	}
	
	@Test
	@SuppressWarnings("deprecation")
	void getMenuArea() {
		try {
			JSONArray h=new JSONArray(controller.getMenuJSON(true, "forno"));
			JSONArray h2= new JSONArray(controller.getMenuJSON(true, "cucina")) ;
			JSONArray h3=new JSONArray(controller.getMenuJSON(true,"bar"));
			assertEquals(1,h.length());
			assertEquals(2,h2.length());
			assertEquals(0,h3.length());
			assertEquals("pizza",h.getJSONObject(0).getString("name"));
			assertEquals("Pasta",h2.getJSONObject(0).getString("name"));
			assertEquals("PastaVuota",h2.getJSONObject(1).getString("name"));
		}
		catch(JSONException e) {
			e.printStackTrace();
		}
		assertEquals(3,controller.getAllItems().size());
		assertEquals(3,controller.getAllGoods().size());
	}
	@Test
	void generateOrderedItemTest() {
		Optional<OrderedItem> item=controller.generateOrderedItem("pizza");
		assertTrue(item.isPresent());
		assertEquals("pizza",item.get().getItem().getName());
		assertEquals(controller.getMenuItemByName("pizza").get().getPrice(),item.get().getItem().getPrice());
		assertEquals("WaitingForWorking",item.get().getState());
		Optional<OrderedItem>item2=controller.generateOrderedItem("Pasta pasta");
		assertTrue(item2.isEmpty());
	}
	
	@Test
	void generateOrderedItemTest2() {
		Optional<OrderedItem> item=controller.generateOrderedItem("Pasta");
		assertTrue(item.isPresent());
		assertEquals("Pasta",item.get().getItem().getName());
		assertEquals(controller.getMenuItemByName("pizza").get().getPrice(),item.get().getItem().getPrice());
		assertEquals("WaitingForWorking",item.get().getState());
		assertEquals("NoOperation",item.get().getStatusCode());
	}
	
	@Test
	void changeStateTest() {
		Optional<OrderedItem> item=controller.generateOrderedItem("pizza");
		assertTrue(item.isPresent());
		item.get().complete();
		assertEquals("Completed",item.get().getState());
		item.get().setWorking();
		assertEquals("Completed",item.get().getState()); //Can't put the state in working if it's completed
	}
	
	@Test
	void changeStateTest2() {
		Optional<OrderedItem> item=controller.generateOrderedItem("PastaVuota");
		assertTrue(item.isPresent());
		item.get().setWorking();
		assertEquals("Working",item.get().getState());
		item.get().complete();
		assertEquals("Completed",item.get().getState()); 
		
	}
	
	@Test
	void goodsSubTest() {
		List<String> list=new ArrayList<String>();
		list.add("3");
		Optional<OrderedItem> item=controller.generateOrderedItem("PastaVuota");
		assertTrue(item.isPresent());
		OrderedItem ord=item.get();
		MenuItem correspondent_item=ord.getItem();
		assertEquals("PastaVuota",correspondent_item.getName());
		assertEquals(1,correspondent_item.getGoods().size());
		assertEquals("3",correspondent_item.getGoods().get(0).getId());
		OrderedItemState.StatusCodes sc= ord.changeSubGoods(list);
		assertEquals("GoodsSubbed",sc.name());
	}
	
	@Test
	void goodsSubTest2() {
		List<String> list=new ArrayList<String>();
		list.add("1");
		list.add("3");
		Optional<OrderedItem> item=controller.generateOrderedItem("pizza");
		assertTrue(item.isPresent());	
		OrderedItem ord=item.get();
		OrderedItemState.StatusCodes sc= ord.changeSubGoods(list);
		assertEquals("GoodsSubbedNotAll",sc.name());
	}
	
	@Test
	void goodsSubTest3() {
		List<String> list=new ArrayList<String>();
		list.add("1");
		list.add("3");
		Optional<OrderedItem> item=controller.generateOrderedItem("pizza");
		OrderedItem ord=item.get();
		ord.setWorking();
		assertEquals("Working",ord.getState());
		String k= ord.changeSubGoods(list).name();
		assertEquals("GoodsNotSubbedInWorking",k);
		ord.complete();
		k= ord.changeSubGoods(list).name();
		assertEquals("GoodsNotSubbedCompleted",k);
	}
	
	@Test
	void goodsAddTest() {
		List<String> list=new ArrayList<String>();
		list.add("1");
		OrderedItem  item=controller.generateOrderedItem("PastaVuota").get();
		OrderedItemState.StatusCodes sc= item.changeAddGoods(list);
		assertEquals("GoodsAdded",sc.name());
		double exstimated_price=controller.getGoodsById("1").get().getPriceAsAdditive()+
				controller.getMenuItemByName("PastaVuota").get().getPrice();
		assertEquals(exstimated_price,item.getPrice());
		list.add("3");
		sc= item.changeAddGoods(list);
		assertEquals("GoodsAddedNotAll",sc.name());
		list.clear();
		list.add("10");
		sc= item.changeAddGoods(list);
		assertEquals("GoodsNotAddedZeroMatches",sc.name());
	}
	@Test
	void goodsAddTest2() {
		List<String> list=new ArrayList<String>();
		list.add("1");
		OrderedItem  item=controller.generateOrderedItem("PastaVuota").get();
		assertTrue(item.isCancellable());
		item.setWorking();
		assertFalse(item.isCancellable());
		OrderedItemState.StatusCodes sc=item.changeAddGoods(list);
		assertEquals("GoodsNotAddedInWorking",sc.name());
		item.complete();
		sc=item.changeAddGoods(list);
		assertEquals("GoodsNotAddedCompleted",sc.name());
		assertFalse(item.isCancellable());
		
	}
	
	
}
