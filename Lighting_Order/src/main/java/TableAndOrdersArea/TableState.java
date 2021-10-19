package TableAndOrdersArea;

import java.util.List;
import java.util.Optional;
public abstract class TableState  {
	
	public enum StatusCodes{
		waiting,
		OrderAddCompleted,
		OrderAddAborted,
		orderFailedTableFree,
		orderFailedTableReserved;
	}
	public enum StatesList{
		free,
		waitingForOrders,
		Occupied,
		reserved;
	}
	
	protected Table associatedTable;
	protected StatesList state;
	protected StatusCodes statusCode;
	
	public StatesList getState() { return this.state;}
	public StatusCodes getStatusCode() { return this.statusCode;}
	
	/**
	 * @info				This function must be called only in the Table constructor
	 * @param t				Table reference
	 * @param isReserved	A table can be initialized in reserved state
	 * @return				the new state
	 */
	public static TableState init(Table t,boolean isReserved) {
		
		if(isReserved)
			return new TableStateFree(t);
		else
			return new TableStateReserved(t);
	}
	public abstract void free();
	public abstract void putInWaiting();
	public abstract void reserve();
	public abstract  void unlockFromReserve(boolean reservationCancelled);
	public abstract Optional<Order> makeOrder( List<String>itemNames,List<List<String>> additive,List<List<String>>toSub,
			
			List<Integer> priority,Integer userID);

}
