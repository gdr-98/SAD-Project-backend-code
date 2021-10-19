package TableAndOrdersArea;

import java.util.List;
import java.util.Optional;

public class TableStateFree extends TableState {

	public TableStateFree(Table t) {
		this.associatedTable=t;
		this.statusCode=TableState.StatusCodes.waiting;
		this.state=TableState.StatesList.free;
	}
	
	/**
	 * @info: does nothing since the table is already free
	 */
	@Override
	public void free() {}
	
	/**
	 * @info:  a customer sat down and the state changes in waitignn for orders
	 */
	@Override
	public void putInWaiting() {this.associatedTable.changeState(new TableStateWaitingForOrders(this.associatedTable));}
	
	/**
	 * @info: reserve the table
	 */
	@Override
	public void reserve() {	this.associatedTable.changeState(new TableStateReserved(this.associatedTable)); }
	
	/**
	 * @info: performs nothing since the table is not reserved
	 */
	@Override
	public void unlockFromReserve(boolean reservationCancelled) { }
	
	/**
	 * @info: can't make an order to a free table
	 */
	@Override
	public  Optional<Order> makeOrder(List<String>itemNames,List<List<String>> additive,List<List<String>>toSub,
			
			List<Integer> priority,Integer userID
			) { 
		this.statusCode=TableState.StatusCodes.orderFailedTableFree;
		return Optional.empty();
	}

}
