package RestaurantArea;

import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;

import java.util.List;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import DataAccess.MenuDAOPSQL;
import DataAccess.RestaurantDAOPSQL;
import MenuAndWareHouseArea.MenuAndGoodsController;
import MenuAndWareHouseArea.OrderedItem;
import TableAndOrdersArea.Order;
import TableAndOrdersArea.RestaurantController;
import UsersData.UsersController;
@SuppressWarnings("deprecation")
class controllerTests {
	private MenuAndGoodsController controllerMenu;
	private RestaurantController controllerRestaurant;
	private MenuDAOPSQL dbMenu;
	private RestaurantDAOPSQL dbRestaurant;
	private UsersController uc;
	@BeforeEach
	void setup() {
		this.dbRestaurant=new RestaurantDAOPSQL();
		this.dbMenu=new  MenuDAOPSQL();
		this.controllerMenu=new MenuAndGoodsController(this.dbMenu);
		
		this.uc=new UsersController();
		this.controllerRestaurant=new RestaurantController(this.dbRestaurant,this.controllerMenu,this.uc);
		
	}
	
	@AfterEach
	void cleanup() {
		
	}
	
	
	
	@Test
	void  getAllTablesTest1() {
		String tables=controllerRestaurant.getAllTablesJSON(Optional.of(1),Optional.empty());
		JsonArray arrayOfTables=JsonParser.parseString(tables).getAsJsonArray();
		assertEquals(4,arrayOfTables.size());
		
		JsonObject []tablesObjJSON= {
				JsonParser.parseString(arrayOfTables.get(0).toString()).getAsJsonObject(),
				JsonParser.parseString(arrayOfTables.get(1).toString()).getAsJsonObject(),
				JsonParser.parseString(arrayOfTables.get(2).toString()).getAsJsonObject(),
				JsonParser.parseString(arrayOfTables.get(3).toString()).getAsJsonObject(),
				};
		
		assertEquals(1,tablesObjJSON[0].get("tableRoomNumber").getAsInt());
		assertEquals(1,tablesObjJSON[1].get("tableRoomNumber").getAsInt());
		assertEquals(1,tablesObjJSON[2].get("tableRoomNumber").getAsInt());
		assertEquals(1,tablesObjJSON[3].get("tableRoomNumber").getAsInt());
		
		//Don't know why this is this order
		//IMPORTANT: IF THIS TEST FAILS GIVE THE ORDER  1 2 3 4 to tables ids and table states, otherwise use this
		//order.
		/*assertEquals("1",tablesObjJSON[0].get("tableID").getAsString());
		assertEquals("2",tablesObjJSON[1].get("tableID").getAsString());
		assertEquals("3",tablesObjJSON[2].get("tableID").getAsString());
		assertEquals("4",tablesObjJSON[3].get("tableID").getAsString());
		
		assertEquals(TableState.StatesList.reserved.name(),
				tablesObjJSON[0].get("tableState").getAsString());
		
		assertEquals(TableState.StatesList.free.name(),
				tablesObjJSON[1].get("tableState").getAsString());
		assertEquals(TableState.StatesList.free.name(),
				tablesObjJSON[2].get("tableState").getAsString());
		assertEquals(TableState.StatesList.free.name(),
				tablesObjJSON[3].get("tableState").getAsString());*/
		

	}
	
	@Test
	void getAllTables2() {
		String tables=controllerRestaurant.getAllTablesJSON(Optional.of(2),Optional.empty());
		JsonArray arrayOfTables=JsonParser.parseString(tables).getAsJsonArray();
		List<JsonObject> tablesJSON=new ArrayList<>();
		assertEquals(1,arrayOfTables.size());
		for(int i=0;i<arrayOfTables.size();i++)
			tablesJSON.add(arrayOfTables.get(i).getAsJsonObject());
		assertEquals("1",tablesJSON.get(0).get("tableID").getAsString());
		assertEquals(2,tablesJSON.get(0).get("tableRoomNumber").getAsInt());
		String tables2=controllerRestaurant.getAllTablesJSON(Optional.of(10),Optional.empty());
		arrayOfTables=JsonParser.parseString(tables2).getAsJsonArray();
		assertTrue(arrayOfTables.isEmpty());
		
		
	}
	
	
	/*@Test
	void getOrderTableTest() {
		
		System.out.println(controllerRestaurant.getOrdersJSON(Optional.empty()));
		
		String orders=controllerRestaurant.getOrdersByTableJSON("1", 1,Optional.empty());
		JsonArray arrayOfOrders=JsonParser.parseString(orders).getAsJsonArray();
		JsonArray items;
		assertFalse(arrayOfOrders.isEmpty());
		
		orders=controllerRestaurant.getOrdersByTableJSON("1", 1,Optional.of("Cucina"));
		arrayOfOrders=JsonParser.parseString(orders).getAsJsonArray();
		assertEquals(1,arrayOfOrders.size());
		assertEquals(1,arrayOfOrders.get(0).getAsJsonObject().get("orderID").getAsInt());
		assertEquals(1,arrayOfOrders.get(0).getAsJsonObject().get("tableRoomNumber").getAsInt());
		assertEquals("1",arrayOfOrders.get(0).getAsJsonObject().get("tableID").getAsString());
		items=arrayOfOrders.get(0).getAsJsonObject().get("orderedItems").getAsJsonArray();
		assertEquals(1,items.size());  //Ho chiesto solo cucina quindi ora mi da solo patatine fritte
		assertEquals("Patate Fritte",items.get(0).getAsJsonObject().get("item").getAsString());
		
		orders=controllerRestaurant.getOrdersByTableJSON("1", 1,Optional.of("Forno"));
		arrayOfOrders=JsonParser.parseString(orders).getAsJsonArray();
		assertEquals(1,arrayOfOrders.size());
		assertEquals(1,arrayOfOrders.get(0).getAsJsonObject().get("orderID").getAsInt());
		assertEquals(1,arrayOfOrders.get(0).getAsJsonObject().get("tableRoomNumber").getAsInt());
		assertEquals("1",arrayOfOrders.get(0).getAsJsonObject().get("tableID").getAsString());
		items=arrayOfOrders.get(0).getAsJsonObject().get("orderedItems").getAsJsonArray();
		assertEquals(1,items.size());  //Ho chiesto solo Forno, mi darà solo margherita
		assertEquals("Margherita",items.get(0).getAsJsonObject().get("item").getAsString());
		
	}
	
	@Test
	void getOrderTableTest2() {
		
		String orders=controllerRestaurant.getOrdersByTableJSON("3", 1,Optional.of("Forno"));
		JsonArray arrayOfOrders=JsonParser.parseString(orders).getAsJsonArray();
		JsonArray items;
		assertTrue(arrayOfOrders.isEmpty()); //Non avendo item nel forno nonn ritorna nulla
	
		orders=controllerRestaurant.getOrdersByTableJSON("3", 1,Optional.of("Cucina")); //Ritornna l'unico ordine che ha un unico item in cucina
		arrayOfOrders=JsonParser.parseString(orders).getAsJsonArray();
		assertEquals(1,arrayOfOrders.size()); 
		items=arrayOfOrders.get(0).getAsJsonObject().get("orderedItems").getAsJsonArray();
		assertEquals(1,items.size());
		assertEquals("Patate Fritte",items.get(0).getAsJsonObject().get("item").getAsString());
		
		//Sarà la stessa cosa in questo specifico caso
		orders=controllerRestaurant.getOrdersByTableJSON("3", 1,Optional.empty());
		arrayOfOrders=JsonParser.parseString(orders).getAsJsonArray();
		assertEquals(1,arrayOfOrders.size()); 
		items=arrayOfOrders.get(0).getAsJsonObject().get("orderedItems").getAsJsonArray();
		assertEquals(1,items.size());
		assertEquals("Patate Fritte",items.get(0).getAsJsonObject().get("item").getAsString());
	}*/
	
	/*  
	 * //Ritorna gli array strani ma funziona
	//@Test //Visualizza tutti gli ordini area cucina
	void getOrders() {
		
		//Visualizza tutti gli ordini che hanno almeno un item in cucina, visualizzando solo l'item correlato
	
		
		String orders=controllerRestaurant.getOrdersJSON(Optional.of("Cucina"));
		JsonArray arrayOfOrders=JsonParser.parseString(orders).getAsJsonArray();
		JsonArray items;
		assertEquals(2,arrayOfOrders.size());
		//Sono solo gli ordini 1 e 2 che li contengono
		assertEquals(1,arrayOfOrders.get(1).getAsJsonObject().get("orderID").getAsInt());
		assertEquals(2,arrayOfOrders.get(0).getAsJsonObject().get("orderID").getAsInt());
		
		//Tutti hanno solo le patate fritte..
		items=arrayOfOrders.get(0).getAsJsonObject().get("orderedItems").getAsJsonArray();
		assertEquals(1,items.size());
		assertEquals("Cucina",items.get(0).getAsJsonObject().get("itemArea").getAsString());
		
		items=arrayOfOrders.get(1).getAsJsonObject().get("orderedItems").getAsJsonArray();
		assertEquals(1,items.size());
		assertEquals("Cucina",items.get(0).getAsJsonObject().get("itemArea").getAsString());
		
		//Ora ci ritorna tutti gli ordini
		orders=controllerRestaurant.getOrdersJSON(Optional.empty());
		arrayOfOrders=JsonParser.parseString(orders).getAsJsonArray();
		assertEquals(3,arrayOfOrders.size());
		
		//Controllo giusto gli ordini chi sono
		assertEquals(1,arrayOfOrders.get(2).getAsJsonObject().get("orderID").getAsInt());
		assertEquals(0,arrayOfOrders.get(1).getAsJsonObject().get("orderID").getAsInt());
		assertEquals(2,arrayOfOrders.get(0).getAsJsonObject().get("orderID").getAsInt());
		
		//Vedo se di fatto iil messaggio è come dico io
		items=arrayOfOrders.get(1).getAsJsonObject().get("orderedItems").getAsJsonArray();
		assertTrue(items.isEmpty());
		
		items=arrayOfOrders.get(2).getAsJsonObject().get("orderedItems").getAsJsonArray();
		assertEquals(2,items.size());
		assertEquals("Patate Fritte",items.get(0).getAsJsonObject().get("item").getAsString());
		assertEquals("Margherita",items.get(1).getAsJsonObject().get("item").getAsString());
		
		items=arrayOfOrders.get(0).getAsJsonObject().get("orderedItems").getAsJsonArray();
		assertEquals(1,items.size());
		assertEquals("Patate Fritte",items.get(0).getAsJsonObject().get("item").getAsString());
		
		orders=controllerRestaurant.getOrdersJSON(Optional.of("Forno")); //Ritornno solo order 1 con il solo item margherita
		arrayOfOrders=JsonParser.parseString(orders).getAsJsonArray();
		assertEquals(1,arrayOfOrders.size());
		assertEquals(1,arrayOfOrders.get(0).getAsJsonObject().get("orderID").getAsInt());
		items=arrayOfOrders.get(0).getAsJsonObject().get("orderedItems").getAsJsonArray();
		assertEquals(1,items.size());
		assertEquals("Forno",items.get(0).getAsJsonObject().get("itemArea").getAsString());
	}*/
	
	//Andiamo a liberare ed occupare i tavoli
	@Test
	void putATableInWaitingForOrders() {
		this.controllerRestaurant.setTableWaiting("1", 1);
		JsonArray array= JsonParser.parseString(dbRestaurant.getAllTablesJSON()).getAsJsonArray();
		JsonObject table;
		int found=0;
		for(int i=0;i<array.size();i++) {
			table=array.get(i).getAsJsonObject();
			if(table.get("tableID").getAsString().equals("1")&& 
					table.get("tableRoomNumber").getAsInt()==1) {
				assertEquals("reserved",table.get("tableState").getAsString());
				found++;
			}
		
		}
		if(found!=1)
			assertTrue(false);
				
		this.controllerRestaurant.setTableWaiting("1", 2); //Checked also in db 
		array= JsonParser.parseString(dbRestaurant.getAllTablesJSON()).getAsJsonArray();
		for(int i=0;i<array.size();i++) {
			table=array.get(i).getAsJsonObject();
			if(table.get("tableID").getAsString().equals("1")&& 
					table.get("tableRoomNumber").getAsInt()==2) {
				assertEquals("waitingForOrders",table.get("tableState").getAsString());
				found++;
			}
		
		}
		if(found!=2)
			assertTrue(false);
	
		
		
		
		//The table will now be freed
		this.controllerRestaurant.setTableFree("1", 2);
		array= JsonParser.parseString(dbRestaurant.getAllTablesJSON()).getAsJsonArray();
		for(int i=0;i<array.size();i++) {
			table=array.get(i).getAsJsonObject();
			if(table.get("tableID").getAsString().equals("1")&& 
					table.get("tableRoomNumber").getAsInt()==2) {
				assertEquals("free",table.get("tableState").getAsString());
				found++;
			}
		
		}
		if(found!=3)
			assertTrue(false);
	}
	
	 @Test
	void generateOrderTable() {
		List<String> names=new ArrayList<>();
		names.add("Margherita");
		List<List<String>>additive=new ArrayList<List<String>>();
		List<List<String>>toSub=new ArrayList<List<String>>();
		List<Integer>priority=new ArrayList<Integer>();
		List<String>additiveMargherita=new ArrayList<>();
		additiveMargherita.add("G003");
		additiveMargherita.add("G002");
		priority.add(1);
		additive.add(additiveMargherita);
		this.controllerRestaurant.setTableWaiting("3", 1) ; //Metti il tavolo in waiting
		toSub.add(new ArrayList<String>()); //Devono essere tutti della stessa grandezza
		String result=controllerRestaurant.generateOrderForTable(names,additive,toSub,priority,"3",1,1);
		
		
		String newOrder=dbRestaurant.getOrderByIdJSON(this.dbRestaurant.findNextOrderID()-1);
		//System.out.println(newOrder);
		assertEquals(newOrder,this.controllerRestaurant.getLastOrderJSON());
	
		assertEquals(result,this.controllerRestaurant.getLastOrderJSON());
		
		dbRestaurant.removeOrderById(dbRestaurant.findNextOrderID()-1);
		this.controllerRestaurant.setTableFree("3", 1);
		
		assertEquals("tableNotFound",
				controllerRestaurant.generateOrderForTable(names,additive,toSub,priority,"1",10,1));
	
	}
	
	@Test
	void generateOrderTable2() {
		List<String> names=new ArrayList<>();
		names.add("Margherita");
		List<List<String>>additive=new ArrayList<List<String>>();
		List<List<String>>toSub=new ArrayList<List<String>>();
		List<Integer>priority=new ArrayList<Integer>();
		List<String>additiveMargherita=new ArrayList<>();
		additiveMargherita.add("G003");
		additiveMargherita.add("G002");
		priority.add(1);
		List<String >subMargherita=new ArrayList<String>();
		subMargherita.add("F001");
		additive.add(additiveMargherita);
		this.controllerRestaurant.setTableWaiting("2", 1) ; //Metti il tavolo in waiting
		toSub.add(subMargherita); //Devono essere tutti della stessa grandezza
		String result=controllerRestaurant.generateOrderForTable(names,additive,toSub,priority,"2",1,1);
		
		System.out.println(result);
		String newOrder=dbRestaurant.getOrderByIdJSON(this.dbRestaurant.findNextOrderID()-1);
		
		assertEquals(newOrder,this.controllerRestaurant.getLastOrderJSON());
	
		assertEquals(result,this.controllerRestaurant.getLastOrderJSON());
		
		//dbRestaurant.removeOrderById(dbRestaurant.findNextOrderID()-1);
		
		controllerRestaurant.setTableFree("2",1);
	
		
	}
	
	@Test
	void generateOrderTable3() { //proviamo adesso a cambiare lo stato dell'ordereditem e di prennderlo inn lavoro
		List<String> names=new ArrayList<>();
		names.add("Margherita");
		List<List<String>>additive=new ArrayList<List<String>>();
		List<List<String>>toSub=new ArrayList<List<String>>();
		List<Integer>priority=new ArrayList<Integer>();
		List<String>additiveMargherita=new ArrayList<>();
		additiveMargherita.add("G003");
		additiveMargherita.add("G002");
		priority.add(1);
		List<String >subMargherita=new ArrayList<String>();
		subMargherita.add("F001");
		additive.add(additiveMargherita);
		this.controllerRestaurant.setTableWaiting("1", 2) ; //Metti il tavolo in waiting
		toSub.add(subMargherita); //Devono essere tutti della stessa grandezza
		String result=controllerRestaurant.generateOrderForTable(names,additive,toSub,priority,"1",2,1);
		
		System.out.println(result);
		String newOrder=dbRestaurant.getOrderByIdJSON(this.dbRestaurant.findNextOrderID()-1);
		
		assertEquals(newOrder,this.controllerRestaurant.getLastOrderJSON());
	
		assertEquals(result,this.controllerRestaurant.getLastOrderJSON());
		
		System.out.println(
				controllerRestaurant.itemInWorking(this.dbRestaurant.findNextOrderID()-1, 1)
				);
		
		//Non controllato negli altri test, quando un ordine viene effettuato allora un tavolo deve avere una
		//transizione automatica nello stato di cancellato
		assertEquals("Occupied",controllerRestaurant.getTable("1", 2).get().getStateString());
		JsonArray tables=JsonParser.parseString(dbRestaurant.getAllTablesJSON()).getAsJsonArray();
		JsonObject helper;
		boolean check=true;
		for(int i=0;i<tables.size();i++) {
			helper=tables.get(i).getAsJsonObject();
			if(helper.get("tableID").getAsString().equals("1")&&helper.get("tableRoomNumber").getAsDouble()==2) {
				assertEquals("Occupied",helper.get("tableState").getAsString());
				check=false;
			}
			
		}
		if(check)
			assertTrue(false);
		
		Order last=this.controllerRestaurant.getLastOrder().get();
		JsonObject item=JsonParser.parseString(dbRestaurant.getOrderedItemJSON(this.dbRestaurant.findNextOrderID()-1, 1)
				).getAsJsonObject();
		assertEquals("Working",
				last.
				getOrderedItems().get(0).getState());
		assertEquals("Working",item.get("actualState").getAsString());
		//Ora lo stato dell'ordine sarà working
		assertEquals("Working",
				last.getStateString());
		assertFalse(last.isCancellable());
		
		//completiamo ora l'ordine
		this.controllerRestaurant.itemComplete(last.getId(), 1);
		 item=JsonParser.parseString(dbRestaurant.getOrderedItemJSON(this.dbRestaurant.findNextOrderID()-1, 1)
				).getAsJsonObject();
		assertEquals("Completed",
				last.
				getOrderedItems().get(0).getState());
		assertEquals("Completed",item.get("actualState").getAsString());
		//Ora lo stato dell'ordine sarà working
		assertEquals("Completed",
				last.getStateString());
		assertTrue(last.isCancellable());
		//assertTrue(controllerRestaurant.cancelOrder(last.getId()).get());
		
		//Libero il tavolo come 
		this.controllerRestaurant.setTableFree("1", 2); 
		
	}
	
	
	//Ultimo test, aggiungiamo due prodotti e poi vediamo che succede rimuovendone uno man mano
	@Test
	void  generateOrderToTable4() {
		
		List<String> prodotti=new ArrayList<>();
		prodotti.add("Margherita");
		prodotti.add("Patate Fritte");
		prodotti.add("Patate Fritte" );
		List<String> additivi=new ArrayList<>();
		List<String> sottr=new ArrayList<>();
		List<String> additivi2=new ArrayList<>();
		additivi2.add("F003");
		
		List<List<String>> toAdd=new ArrayList<>();
		toAdd.add(additivi);
		toAdd.add(additivi);
		toAdd.add(additivi2);
		
		List<List<String>> toSub=new ArrayList<>();
		toSub.add(sottr);
		toSub.add(sottr);
		toSub.add(sottr);
		
		List<Integer> priority=new ArrayList<>();
		priority.add(2);
		priority.add(1);
		priority.add(1);
		controllerRestaurant.setTableWaiting("2", 1);
		String toRet=controllerRestaurant.generateOrderForTable(prodotti,toAdd, toSub, priority, "2", 1, 1);
		System.out.println("NUOVO"+toRet);
		
		Order o=controllerRestaurant.getLastOrder().get();
		String toRet2=dbRestaurant.getOrderByIdJSON(o.getId());
		assertEquals(toRet,o.getJSONRepresentation(Optional.empty()));
		assertEquals(toRet2,o.getJSONRepresentation(Optional.empty()));
		
		assertTrue(o.isCancellable());
		controllerRestaurant.itemInWorking(o.getId(), 1);
		
		assertFalse(o.isCancellable());
		assertTrue(controllerRestaurant.itemComplete(o.getId(), 2).get());
		boolean check=false;
		for(OrderedItem item:o.getOrderedItems()) {
			if(item.isMe(2) ) {
				check=true;
				assertEquals("Completed",item.getState());
			}
		}
		if(!check)
			assertTrue(false);
		
		controllerRestaurant.itemComplete(o.getId(), 1);
		controllerRestaurant.itemInWorking(o.getId(), 3);
		controllerRestaurant.itemComplete(o.getId(), 3);

		assertTrue(o.isCancellable());
		
		controllerRestaurant.setTableFree("2", 1);
		
		assertEquals("free",controllerRestaurant.getTable("2", 1).get().getStateString());		
	}
	
	//the objective of the test is to add an ordered item and see 
	@Test
	void addAndCancelItem() {
		
		List<String> prodotti=new ArrayList<>();
		prodotti.add("Margherita");
		//prodotti.add("Patate Fritte" ); Not yet..
		List<String> additivi=new ArrayList<>();
		List<String> sottr=new ArrayList<>();
		List<String> additivi2=new ArrayList<>();
		additivi2.add("F003");
		
		List<List<String>> toAdd=new ArrayList<>();
		toAdd.add(additivi);
		toAdd.add(additivi);
		toAdd.add(additivi2);
		
		List<List<String>> toSub=new ArrayList<>();
		toSub.add(sottr);
		toSub.add(sottr);
		toSub.add(sottr);
		
		List<Integer> priority=new ArrayList<>();
		priority.add(2);
		priority.add(1);
		priority.add(1);
		
		controllerRestaurant.setTableWaiting("2", 1);
		String order=controllerRestaurant.generateOrderForTable(prodotti, toAdd, toSub, priority, "2", 1, 1);
		Order o=controllerRestaurant.getLastOrder().get();
		assertEquals(o.getJSONRepresentation(Optional.empty()),order);
		assertEquals(o.getJSONRepresentation(Optional.empty()),dbRestaurant.getOrderByIdJSON(dbRestaurant.findNextOrderID()-1));
		
		
		//Check the items....
		List<OrderedItem> items=o.getOrderedItems();
		assertEquals(1,items.size());
		assertEquals("Margherita",items.get(0).getItem().getName());
		
		//now add..
		prodotti.clear();
		prodotti.add("Patate Fritte");
		order=controllerRestaurant.addItemToOrder(o.getId(), prodotti,
				toAdd, toSub, priority);
		//let's remake the testss..
		
		
		assertEquals(o.getJSONRepresentation(Optional.empty()),order);
		assertEquals(o.getJSONRepresentation(Optional.empty()),dbRestaurant.getOrderByIdJSON(dbRestaurant.findNextOrderID()-1));
		System.out.println("After"+order);
		
		//Check the items....
		items=o.getOrderedItems();
		assertEquals(2,items.size());
		assertEquals("Margherita",items.get(0).getItem().getName());
		assertEquals("Patate Fritte",items.get(1).getItem().getName());
			
		//Let's add and remake the tests...
		
		prodotti.clear();
		prodotti.add("Napoletana"); 
		order=controllerRestaurant.addItemToOrder(o.getId(), prodotti,
				toAdd, toSub, priority);
		
		assertEquals(o.getJSONRepresentation(Optional.empty()),order);
		assertEquals(o.getJSONRepresentation(Optional.empty()),dbRestaurant.getOrderByIdJSON(dbRestaurant.findNextOrderID()-1));
	
		
		controllerRestaurant.itemInWorking(o.getId(), 2);
		assertFalse(o.isCancellable());
		assertEquals("Working",items.get(1).getState());
		
		prodotti.clear();
		prodotti.add("Acqua Natia");
		String newRes=controllerRestaurant.addItemToOrder(o.getId(), prodotti,
				toAdd, toSub, priority); //Can't add to an order inn processing, it must transform to another order
		
		
		assertEquals("itemsNotAdded",newRes);
		
		//let's remake the testss..
		
		//Check the items....
		items=o.getOrderedItems();
		assertEquals(3,items.size());
		assertEquals("Margherita",items.get(0).getItem().getName());
		assertEquals("Patate Fritte",items.get(1).getItem().getName());
		assertEquals("Napoletana",items.get(2).getItem().getName());
		
		//Complete an item
		controllerRestaurant.itemComplete(o.getId(), 2);
		assertEquals("Completed",items.get(1).getState());
		//now  cancel the remaining orders
		controllerRestaurant.deleteItemFromOrder(o.getId(), 1);
		controllerRestaurant.deleteItemFromOrder(o.getId(), 3);
		items=o.getOrderedItems();
		System.out.println(items.size());
		assertEquals("Completed",o.getState().name());
		this.controllerRestaurant.setTableFree("2", 1);
		
	}
	
	//Modify item test
	@Test
	void  modifyItemTest() {
		List<String> prodotti=new ArrayList<>();
		prodotti.add("Margherita");
		List<String> additivi=new ArrayList<>();
		List<String> sottr=new ArrayList<>();
		List<String> additivi2=new ArrayList<>();
		additivi2.add("F003");
		
		List<List<String>> toAdd=new ArrayList<>();
		toAdd.add(additivi);
		toAdd.add(additivi);
		toAdd.add(additivi2);
		
		List<List<String>> toSub=new ArrayList<>();
		toSub.add(sottr);
		toSub.add(sottr);
		toSub.add(sottr);
		
		List<Integer> priority=new ArrayList<>();
		priority.add(2);
		priority.add(1);
		priority.add(1);
		
		controllerRestaurant.setTableWaiting("2", 1);
		String order=controllerRestaurant.generateOrderForTable(prodotti, toAdd, toSub, priority, "2", 1, 1);
		Order o=controllerRestaurant.getLastOrder().get();
		assertEquals(o.getJSONRepresentation(Optional.empty()),order);
		assertEquals(o.getJSONRepresentation(Optional.empty()),dbRestaurant.getOrderByIdJSON(dbRestaurant.findNextOrderID()-1));
		
		additivi.add("G001"); //Origano
		additivi.add("G002"); //aglio
		List<String> status=controllerRestaurant.modifyItemInOrder(o.getId(), o.getGreatestLineNumber(),
				additivi, sottr, 1);
		
		
		
		//Item was found with the order
		assertEquals(3,status.size());
		assertEquals("GoodsAdded",status.get(0));
		assertEquals("GoodsSubbed",status.get(1));
		assertEquals("PriorityChanged",status.get(2));
		int ln=o.getGreatestLineNumber();
		assertEquals(additivi.size(),o.getAdditiveForItem(ln).get().size());
		//check  if contains elements
		boolean[] contains= {false,false};
		int index=0;
		for(String s :o.getAdditiveForItem(ln).get()) {
			for(String k:additivi) {
				if(k.equals(s)) {
					contains[index]=true;
					index++;
				}
			}
		}
		assertTrue(contains[0]);
		assertTrue(contains[1]);
		assertEquals(sottr,o.getSubForItem(ln).get());
		assertEquals(1,o.getPriorityForItem(ln).get());
		
		
		
		//now  try to add  to an item in working
		assertTrue(controllerRestaurant.itemInWorking(o.getId(), ln).get());
		status=controllerRestaurant.modifyItemInOrder(o.getId(), o.getGreatestLineNumber(),
				additivi, sottr, 1);
		assertEquals("GoodsNotAddedInWorking",status.get(0));
		assertEquals("GoodsNotSubbedInWorking",status.get(1));
		assertEquals("PriorityNotChangedWorking",status.get(2));
		assertEquals("Working",o.getOrderedItems().get(0).getState());
		assertTrue(controllerRestaurant.itemComplete(o.getId(), ln).get());
		status=controllerRestaurant.modifyItemInOrder(o.getId(), o.getGreatestLineNumber(),
				additivi, sottr, 1);
		assertEquals("GoodsNotAddedCompleted",status.get(0));
		assertEquals("GoodsNotSubbedCompleted",status.get(1));
		assertEquals("PriorityNotChangedCompleted",status.get(2));
		
		//Let's try when the order does not exist
		assertEquals("orderNotFound",controllerRestaurant.modifyItemInOrder(200, o.getGreatestLineNumber(),
				additivi, sottr, 1).get(0));
		
		//Let's try with a not existing item
		assertEquals("itemNotFound",controllerRestaurant.modifyItemInOrder(o.getId(), 33,
						additivi, sottr, 1).get(0));
		controllerRestaurant.setTableFree("2", 1);
		
	}

}
