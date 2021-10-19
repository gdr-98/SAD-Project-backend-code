package TableAndOrdersArea;

import java.util.List;
import java.util.Optional;

public class TableStateReserved extends TableState {

	public TableStateReserved(Table t) {
		this.associatedTable=t;
		this.statusCode=TableState.StatusCodes.waiting;
		this.state=TableState.StatesList.reserved;
	}
	
	/**
	 * @info:A reserved table can't be freed unless the client cancels the reservation
	 */
	@Override
	public void free() {}

	/**
	 * @info: A client can't sit in a reserved table
	 */
	@Override
	public void putInWaiting() {}
	
	/**
	 * @info: Not implemented because of the same problem for waiting annd occupied states
	 */
	@Override
	public void reserve() {}
	
	/**
	 * @info Unlocks from the reserved state 
	 * @param reservationCancelled , if it evaluates true then the new state is free else the client has
	 * 			arrived and the table is waiting for orders
	 */
	@Override
	public void unlockFromReserve(boolean reservationCancelled) {
		if(reservationCancelled)
			this.associatedTable.changeState(new TableStateFree(this.associatedTable));
		else
			this.associatedTable.changeState(new TableStateWaitingForOrders(this.associatedTable));
	}
	
	/**
	 * @info: a reserved table can't make orders
	 */
	@Override
	public  Optional<Order> makeOrder( List<String>itemNames,List<List<String>> additive,List<List<String>>toSub,
			
			List<Integer> priority,Integer userID
			) { 
		this.statusCode=TableState.StatusCodes.orderFailedTableReserved;
		return Optional.empty();
	}

}
