package TableAndOrdersArea;

import java.util.List;
import java.util.Optional;

public class TableStateWaitingForOrders extends TableState {

	public TableStateWaitingForOrders(Table t) {
		this.associatedTable=t;
		this.statusCode=TableState.StatusCodes.waiting;
		this.state=TableState.StatesList.waitingForOrders;
	}
	
	/**
	 * @info the client wants to go without ordering, change the state to free
	 */
	@Override
	public void free() { this.associatedTable.changeState(new TableStateFree(this.associatedTable));}
	
	/**
	 * @info : already in this state
	 */
	@Override
	public void putInWaiting() {}
	
	/**
	 * @info: Can't reserve a table still occupied.
	 * 	The problem with reservation is time dependant.
	 * 	Basically we can't reserve a table with clients, but if the reservation is for tomorrow or for 
	 * 	many hours ahead?
	 * 	For this reason this method does nothing until we find a working logic
	 */
	@Override
	public void reserve() {	}
	
	/**
	 * @info: No operation, the table is not reserved.
	 */
	@Override
	public void unlockFromReserve(boolean reservationCancelled) {}

	@Override
	public Optional<Order> makeOrder(  List<String>itemNames,List<List<String>> additive,List<List<String>>toSub,
			
			List<Integer> priority,Integer userID
			) {
		int id=this.associatedTable.getController().getDB().findNextOrderID();
		Order newOrder=new Order(id,Optional.of(this.associatedTable),itemNames,additive,toSub,priority,userID,this.associatedTable.getController());
		if(newOrder.numOfItems()>0) {
			this.associatedTable.addOrderRaw(newOrder);
			this.associatedTable.changeState(new TableStateOccupied(this.associatedTable));
			return Optional.of(
					newOrder
						);
		}
		else
			return Optional.empty();
		
	}

}
