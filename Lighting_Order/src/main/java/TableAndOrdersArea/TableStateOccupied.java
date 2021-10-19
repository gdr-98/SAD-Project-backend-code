package TableAndOrdersArea;

import java.util.List;
import java.util.Optional;

public class TableStateOccupied extends TableState {

	public  TableStateOccupied(Table t) {
		this.associatedTable=t;
		this.state=TableState.StatesList.Occupied; //Enters in this state only when the first order is added
		this.statusCode=TableState.StatusCodes.OrderAddCompleted;
	}
	
	/**
	 * @info:	free the table when the client goes away
	 * 			Ideally this operation should be performed only when the client goes away and
	 * 			not during payment act
	 */
	@Override
	public void free() { 
		//IMPORTANT, MUST CLEAR ALL THE ORDERS IN HERE
		boolean check=true;
		List<Order> orders=this.associatedTable.getOrdersList();
		int index=0;
		while(check && index <orders.size()) { //free an occupied table only if all orders are completed
			if(!orders.get(index).isCancellable())
				check=false;
			index++;
		}
		if(check) {
			
			this.associatedTable.clearOrders();
			this.associatedTable.changeState(new TableStateFree(this.associatedTable));
		}
	}
	
	/**
	 * @info: Ann occupied state can't be put in waiting state since the client has ordered
	 */
	@Override
	public void putInWaiting() {}
	
	/**
	 * @info: Same problem of WaitingForOrders
	 */
	@Override
	public void reserve() {}
	
	/**
	 * @info: The table is not reserved.
	 */
	@Override
	public void unlockFromReserve(boolean reservationCancelled) {}

	/**
	 * @info: add a new order to the table
	 */
	@Override
	public Optional<Order> makeOrder( List<String>itemNames,List<List<String>> additive,List<List<String>>toSub,
			
			List<Integer> priority,Integer userID
			) {
			this.statusCode=TableState.StatusCodes.OrderAddCompleted;
			int id=this.associatedTable.getController().getDB().findNextOrderID();
			Order newOrder=new Order(id,Optional.of(this.associatedTable),itemNames,additive,toSub,priority,userID,this.associatedTable.getController());
			if(newOrder.numOfItems()>0) {
				this.associatedTable.addOrderRaw(newOrder);
				return Optional.of(
						newOrder
							);
			}
			else
				return Optional.empty();
	}

}
