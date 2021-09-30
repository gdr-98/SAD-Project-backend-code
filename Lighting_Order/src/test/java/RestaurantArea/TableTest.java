package RestaurantArea;
/*
package RestaurantArea;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
//import org.junit.jupiter.params.provider.NullSource;
import java.util.Optional;
import java.util.stream.*;
class TableTest {
	
	@BeforeEach
	void initTest() {
		
	}
	@AfterEach
	void cleanTest() {
		
	}
	

	@ParameterizedTest	
	@MethodSource("getTable")
	void orderConstructor(Optional<Table> t) {
		Order o=new Order(10,t);
		assertEquals(10,o.getId());
		assertEquals("WaitingForWorking", o.getStateString());
		String json=o.getJSONRepresentation(Optional.empty());
		JsonObject element=JsonParser.parseString(json).getAsJsonObject();
		assertEquals(10,element.get("orderID").getAsInt());
		assertEquals("WaitingForWorking",element.get("orderState").getAsString());
		if(t.isEmpty()) {
			assertTrue(element.get("tableID").getAsString().isBlank());
			assertTrue(element.get("tableRoomNumber").getAsString().isBlank());
		}
		else {
			assertEquals(o.getTable().get().getId(),element.get("tableID").getAsString());
			assertEquals(o.getTable().get().getRoomNumber(),
					element.get("tableRoomNumber").getAsInt());
		}
		
	}
	private static Stream<Optional<Table>> getTable() {
		return Stream.of (
				 Optional.of(new Table("1a",10,false)),
				 Optional.empty()
				);
	}
	
	@Test
	public void StateTest() {
		Table t=new Table("1a",10,true);
		assertEquals("free",t.getStateString());
		assertEquals("waiting",t.getStatusCodeString());
		t.addOrder(new Order(10,Optional.of(t)));
		assertEquals("orderFailedTableFree",t.getStatusCodeString());
		assertEquals("free",t.getStateString());
		t.setInWaitingForOrders();
		assertEquals("waitingForOrders",t.getStateString());
		t.free();
		t.setInWaitingForOrders();
		t.addOrder(new Order(10,Optional.of(t)));
		assertEquals("OrderAddCompleted",t.getStatusCodeString());
		assertEquals("Occupied",t.getStateString());
	}
	
	@Test
	public void StateTest2() {
		Table t=new Table("1a",10,false);
		assertEquals("reserved",t.getStateString());
		assertEquals("waiting",t.getStatusCodeString());
		t.addOrder(new Order(10,Optional.of(t)));
		assertEquals("orderFailedTableReserved",t.getStatusCodeString());
		t.setInWaitingForOrders();
		t.free();
		assertEquals("reserved",t.getStateString());
		t.unlockFromReservation(true);
		assertEquals("free",t.getStateString());
		t.setInWaitingForOrders();
		assertEquals("waitingForOrders",t.getStateString());
		t.addOrder(new Order(10,Optional.of(t)));
		assertEquals("OrderAddCompleted",t.getStatusCodeString());
		assertEquals("Occupied",t.getStateString());
		t.addOrder(new Order(20,Optional.of(t)));
		assertEquals("OrderAddCompleted",t.getStatusCodeString());
		assertEquals("Occupied",t.getStateString());
		t.free();
		assertEquals("free",t.getStateString());
		assertEquals("waiting",t.getStatusCodeString());
		
	}
	
	@Test
	public void StateTest3() {
		
		Table t=new Table("1a",10,false);
		assertEquals("reserved",t.getStateString());
		assertEquals("waiting",t.getStatusCodeString());
		t.addOrder(new Order(10,Optional.of(t)));
		assertEquals("orderFailedTableReserved",t.getStatusCodeString());
		t.setInWaitingForOrders();
		t.free();
		assertEquals("reserved",t.getStateString());
		t.unlockFromReservation(false);
		assertEquals("waitingForOrders",t.getStateString());
		t.addOrder(new Order(10,Optional.of(t)));
		assertEquals("OrderAddCompleted",t.getStatusCodeString());
		assertEquals("Occupied",t.getStateString());
		t.addOrder(new Order(20,Optional.of(t)));
		assertEquals("OrderAddCompleted",t.getStatusCodeString());
		assertEquals("Occupied",t.getStateString());
		assertEquals(2,t.getOrdersList().size());
		t.free();
		assertEquals("free",t.getStateString());
		assertEquals("waiting",t.getStatusCodeString());
		assertEquals(0,t.getOrdersList().size());
		
	}
	

}*/
