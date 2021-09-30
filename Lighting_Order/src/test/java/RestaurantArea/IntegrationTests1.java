package RestaurantArea;
/*
package RestaurantArea;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import MenuAndWareHouseArea.MenuAndGoodsController;
import MenuAndWareHouseArea.OrderedItem;
import RestaurantArea.Order.OrderStates;
import MenuAndWareHouseArea.OrderedItemState;
import DataAccess.*;
class IntegrationTests1 {
	private  MenuAndGoodsController controller;
	private MenuAndWarehouseDAO dao;
	@BeforeEach
	void initTest() {
		dao=new DBMenuStub();
		controller=new MenuAndGoodsController(dao);
		controller.getMenuJSON(false, null);
	}
	@AfterEach
	void cleanTest() {
		
	}
	@Test
	void integration1() {
		Optional<OrderedItem> item=controller.generateOrderedItem("pizza");
		assertTrue(item.isPresent());
		Order o=new Order(10,Optional.empty());
		assertEquals(0,o.getCompleteItemNumber());
		assertEquals(OrderStates.WaitingForWorking,o.getState());
		o.addOrdedItem(item.get());
		assertEquals(0,o.getCompleteItemNumber());
		assertEquals(OrderStates.WaitingForWorking,o.getState());
		item.get().setLineNumber(1);
		assertTrue(o.takeItemInWorking(1));
		assertEquals(OrderedItemState.States.Working,item.get().getStateRaw());
		assertEquals(1,o.getRemainingItemNumber());
		assertTrue(o.completeItem(1));
		assertEquals(1,o.getCompleteItemNumber());
		assertEquals(0,o.getRemainingItemNumber());
		Optional<OrderedItem> item2=controller.generateOrderedItem("PastaVuota");
		assertTrue(item2.isPresent());
		assertFalse(o.addOrdedItem(item2.get()));
		assertEquals(OrderedItemState.States.Completed,item.get().getStateRaw());
		
	}
	
	@Test
	void integration2() {
		
		//Aggiungiamo degli order item  e proviamo a cancellarli.
		Optional<OrderedItem> item=controller.generateOrderedItem("PastaVuota");
		Optional<OrderedItem> item2=controller.generateOrderedItem("PastaVuota");
		assertTrue(item.isPresent());
		assertTrue(item2.isPresent());
		item.get().setLineNumber(1);
		item2.get().setLineNumber(2);
		
		Order o=new Order(10,Optional.empty());
		assertTrue(o.addOrdedItem(item.get()));
		assertTrue(o.addOrdedItem(item2.get()));
		assertEquals(2,o.getRemainingItemNumber());
		assertTrue(o.cancelOrderedItem(2));
		assertEquals(1,o.getRemainingItemNumber());
		assertTrue(o.takeItemInWorking(1));
		assertFalse(o.cancelOrderedItem(1));
		assertEquals(1,o.getRemainingItemNumber());
		assertTrue(o.completeItem(1));
		assertEquals(0,o.getRemainingItemNumber());
		
	}
	
	@Test
	void integration3() {
		Optional<OrderedItem> item=controller.generateOrderedItem("PastaVuota");
		Optional<OrderedItem> item2=controller.generateOrderedItem("PastaVuota");
		assertTrue(item.isPresent());
		assertTrue(item2.isPresent());
		item.get().setLineNumber(1);
		item2.get().setLineNumber(2);
		Order o=new Order(10,Optional.empty());
		assertTrue(o.addOrdedItem(item.get()));
		assertTrue(o.completeItem(1));
		assertEquals(OrderStates.Completed,o.getState());
		assertFalse(o.addOrdedItem(item2.get())); //not add to a completed
		assertFalse(o.cancelOrderedItem(1)); //not sub to a completed
	}*/
	
	/*
	@Test
	void integration4() {
		Optional<OrderedItem> item=controller.generateOrderedItem("PastaVuota");
		Optional<OrderedItem> item2=controller.generateOrderedItem("PastaVuota");
		assertTrue(item.isPresent());
		assertTrue(item2.isPresent());
		item.get().setLineNumber(1);
		item2.get().setLineNumber(2);
		Order o=new Order(10,Optional.empty());
		assertTrue(o.addOrdedItem(item.get()));
		assertTrue(o.addOrdedItem(item2.get()));
		Optional<OrderedItemState.StatusCodes> ret=o.addGoodsToItem(null, 3);
		assertTrue(ret.isEmpty());
		List<String >goods_to_add= new ArrayList<>();
		goods_to_add.add("1");
		goods_to_add.add("2");
		ret=o.addGoodsToItem(goods_to_add, 1);
		assertEquals(OrderedItemState.StatusCodes.GoodsAdded,ret.get());
		assertEquals(2,o.getAdditiveForItem(1).get().size());
		assertEquals("1",o.getAdditiveForItem(1).get().get(0));
		assertEquals("2",o.getAdditiveForItem(1).get().get(1));
		//Let's try to completely change
		goods_to_add.remove(1);
		ret=o.setAdditiveGoodsForItem(goods_to_add, 1);
		assertEquals(OrderedItemState.StatusCodes.GoodsAdded,ret.get());
		assertEquals(1,o.getAdditiveForItem(1).get().size());
		assertEquals("1",o.getAdditiveForItem(1).get().get(0));
		
	}
	
	
	@Test
	void integration5() {
		Optional<OrderedItem> item=controller.generateOrderedItem("PastaVuota");
		assertTrue(item.isPresent());
		item.get().setLineNumber(1);
		//Try to add a present good
		Order o=new Order(10,Optional.empty());
		o.addOrdedItem(item.get());
		List<String> to_add= new ArrayList<>();
		to_add.add("3");
		Optional<OrderedItemState.StatusCodes> ret=o.addGoodsToItem(to_add, 1);
		assertEquals(OrderedItemState.StatusCodes.GoodsNotAddedZeroMatches,ret.get());
		to_add.add("1");
		ret=o.addGoodsToItem(to_add, 1);
		//When an item is not added we have goods Added not all
		assertEquals(OrderedItemState.StatusCodes.GoodsAddedNotAll,ret.get());
		o.takeItemInWorking(1);
		
		//can't add to an item in working
		ret=o.setAdditiveGoodsForItem(to_add, 1); 
		assertEquals(OrderedItemState.StatusCodes.GoodsNotAddedInWorking,ret.get());
		
		//can't  add to a completed item
		o.completeItem(1);
		ret=o.setAdditiveGoodsForItem(to_add, 1);
		assertEquals(OrderedItemState.StatusCodes.GoodsNotAddedCompleted,ret.get());
	}
	
	@Test
	void integration6() {
		Optional<OrderedItem> item=controller.generateOrderedItem("pizza");
		assertTrue(item.isPresent());
		item.get().setLineNumber(1);
		Order o=new Order(10,Optional.empty());
		o.addOrdedItem(item.get());
		List<String> to_add= new ArrayList<>();
		to_add.add("3");
		Optional<OrderedItemState.StatusCodes> ret=o.addGoodsToItem(to_add, 1);
		assertEquals(OrderedItemState.StatusCodes.GoodsAdded,ret.get());
		//Can't add a good already added
		
		
	}*/
	/*
	@Test
	void integration4() {
		Optional<OrderedItem> item =controller.generateOrderedItem("pizza");
		assertTrue(item.isPresent());
		item.get().setLineNumber(1);
		Order o=new Order(10,Optional.empty());
		o.addOrdedItem(item.get());
		List<String>to_add=new ArrayList<>();
		to_add.add("1");
		to_add.add("2");
		Optional<OrderedItemState.StatusCodes> ret=o.setAdditiveGoodsForItem(to_add, 1);
		//Doesn't add goods if it is already composing an item
		assertEquals(OrderedItemState.StatusCodes.GoodsNotAddedZeroMatches,ret.get());
	}
	
	@Test
	void integration5() {
		Optional<OrderedItem> item =controller.generateOrderedItem("pizza");
		assertTrue(item.isPresent());
		item.get().setLineNumber(1);
		Order o=new Order(10,Optional.empty());
		o.addOrdedItem(item.get());
		List<String>to_add=new ArrayList<>();
		to_add.add("3");
		Optional<OrderedItemState.StatusCodes> ret=o.setAdditiveGoodsForItem(to_add, 1);
		//All goods is added
		assertEquals(OrderedItemState.StatusCodes.GoodsAdded,ret.get());
	}
	
	@Test
	void integration6() {
		Optional<OrderedItem> item =controller.generateOrderedItem("PastaVuota");
		assertTrue(item.isPresent());
		item.get().setLineNumber(1);
		Order o=new Order(10,Optional.empty());
		o.addOrdedItem(item.get());
		List<String>to_add=new ArrayList<>();
		to_add.add("1");
		to_add.add("2");
		to_add.add("2");
		Optional<OrderedItemState.StatusCodes> ret=o.setAdditiveGoodsForItem(to_add, 1);
		//In case of duplicates
		assertEquals(OrderedItemState.StatusCodes.GoodsAddedNotAll,ret.get());
		assertEquals(2,item.get().getAdditiveIDs().size());
		//assertEquals("2",item.get().getAdditiveIDs().get(0));
		//assertEquals("1",item.get().getAdditiveIDs().get(1));
	}
	
	@Test
	void integration7() {
		Optional<OrderedItem> item =controller.generateOrderedItem("PastaVuota");
		assertTrue(item.isPresent());
		item.get().setLineNumber(1);
		Order o=new Order(10,Optional.empty());
		o.addOrdedItem(item.get());
		List<String>to_sub=new ArrayList<>();
		to_sub.add("1");
		Optional<OrderedItemState.StatusCodes> ret=o.setSubGoodsForItem(to_sub, 1);
		//Can't sub a not presennt goods
		assertEquals(OrderedItemState.StatusCodes.GoodsNotSubbedZeroMatches,ret.get());
		
	}
	
	//Sub goods without duplicates
	@Test
	void integration8() {
		Optional<OrderedItem> item =controller.generateOrderedItem("PastaVuota");
		assertTrue(item.isPresent());
		item.get().setLineNumber(1);
		Order o=new Order(10,Optional.empty());
		o.addOrdedItem(item.get());
		List<String>to_sub=new ArrayList<>();
		to_sub.add("3");
		to_sub.add("3");
		Optional<OrderedItemState.StatusCodes> ret=o.setSubGoodsForItem(to_sub, 1);
		//Can't sub a not present goods
		assertEquals(OrderedItemState.StatusCodes.GoodsSubbedNotAll,ret.get());
		
	}
	
	//Sub All goods
	@Test
	void integration9() {
		Optional<OrderedItem> item =controller.generateOrderedItem("Pasta");
		assertTrue(item.isPresent());
		item.get().setLineNumber(1);
		Order o=new Order(10,Optional.empty());
		o.addOrdedItem(item.get());
		List<String>to_sub=new ArrayList<>();
		to_sub.add("3");
		to_sub.add("1");
		Optional<OrderedItemState.StatusCodes> ret=o.setSubGoodsForItem(to_sub, 1);
		//Can't sub a not present goods
		assertEquals(OrderedItemState.StatusCodes.GoodsSubbed,ret.get());
		
	}
	
	//Try to sub multiple goods not present
	@Test
	void integration10() {
		Optional<OrderedItem> item =controller.generateOrderedItem("Pasta");
		assertTrue(item.isPresent());
		item.get().setLineNumber(1);
		Order o=new Order(10,Optional.empty());
		o.addOrdedItem(item.get());
		List<String>to_sub=new ArrayList<>();
		to_sub.add("2");
		to_sub.add("2");
		Optional<OrderedItemState.StatusCodes> ret=o.setSubGoodsForItem(to_sub, 1);
		//Can't sub a not present goods
		assertEquals(OrderedItemState.StatusCodes.GoodsNotSubbedZeroMatches,ret.get());
		
	}
	
	@Test
	void stateIntegration1() {
		Optional<OrderedItem> item =controller.generateOrderedItem("Pasta");
		assertTrue(item.isPresent());
		item.get().setLineNumber(1);
		Order o=new Order(10,Optional.empty());
		assertEquals(Order.OrderStates.WaitingForWorking,o.getState());
		o.addOrdedItem(item.get());
		assertFalse(o.takeItemInWorking(10));
		assertTrue(o.takeItemInWorking(1));
		assertEquals(OrderedItemState.States.Working,item.get().getStateRaw());
		assertEquals(Order.OrderStates.Working,o.getState());
		List<String>goods=new ArrayList<>();
		goods.add("2");
		assertEquals(OrderedItemState.StatusCodes.GoodsNotAddedInWorking,
				o.setAdditiveGoodsForItem(goods, 1).get());
		o.completeItem(1);
		assertEquals(OrderedItemState.States.Completed,item.get().getStateRaw());
		assertEquals(Order.OrderStates.Completed,o.getState());
		assertEquals(OrderedItemState.StatusCodes.GoodsNotAddedCompleted,
				o.setAdditiveGoodsForItem(goods, 1).get());
	}
	
	@Test
	void stateIntegration2() {
		Optional<OrderedItem> item =controller.generateOrderedItem("Pasta");
		assertTrue(item.isPresent());
		item.get().setLineNumber(1);
		Order o=new Order(10,Optional.empty());
		assertEquals(Order.OrderStates.WaitingForWorking,o.getState());
		o.addOrdedItem(item.get());
		assertFalse(o.takeItemInWorking(10));
		assertTrue(o.takeItemInWorking(1));
		assertEquals(OrderedItemState.States.Working,item.get().getStateRaw());
		assertEquals(Order.OrderStates.Working,o.getState());
		List<String>goods=new ArrayList<>();
		goods.add("2");
		assertEquals(OrderedItemState.StatusCodes.GoodsNotSubbedInWorking,
				o.setSubGoodsForItem(goods, 1).get());
		o.completeItem(1);
		assertEquals(OrderedItemState.States.Completed,item.get().getStateRaw());
		assertEquals(Order.OrderStates.Completed,o.getState());
		assertEquals(OrderedItemState.StatusCodes.GoodsNotSubbedCompleted,
				o.setSubGoodsForItem(goods, 1).get());
	}
}*/