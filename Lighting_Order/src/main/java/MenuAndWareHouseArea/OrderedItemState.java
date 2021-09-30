package MenuAndWareHouseArea;
import java.util.List;
public  abstract class OrderedItemState {
	
	/**
	 * @info :List of all possible states
	 * 
	 */
	
	public enum States {
		
		/**
		 * @info : OrderedItem can be cancelled and modified
		 */
		WaitingForWorking,
		
		/**
		 * @info : In this two remaining states OrderedItem can't be cancelled or modified
		 */
		Working,
		Completed;
	}
	OrderedItem orderedItem=null;
	
	/**
	 * @info Status string for the last operation
	 */
	public enum StatusCodes {
		GoodsAdded,
		GoodsNotAddedInWorking,
		GoodsNotAddedCompleted,
		GoodsSubbed,
		GoodsNotSubbedInWorking,
		GoodsNotSubbedCompleted,
		GoodsNotAddedZeroMatches, //We have this even if the good is already present in the menu item
		GoodsAddedNotAll,
		GoodsNotSubbedZeroMatches, //we have this even if the good is already present in the menu item
		GoodsSubbedNotAll,
		PriorityChanged,
		PriorityNotChangedWorking,
		PriorityNotChangedCompleted,
		NoOperation;
	}
	
	protected StatusCodes sc;
	protected States realState;
	
	public abstract   OrderedItemState.StatusCodes setAdditive(List<String>new_additive);
	public abstract  OrderedItemState.StatusCodes setSubGoods(List<String>new_sub);
	public abstract void startWorking();
	public abstract void completeOrderedItem();
	public abstract boolean isCancellable();
	public abstract OrderedItemState.StatusCodes addGoods(List<String>toAdd);
	public abstract OrderedItemState.StatusCodes subGoods(List<String>toSub);
	public abstract OrderedItemState.StatusCodes changePriority(int newPriority);
	
	/**
	 * 
	 * @return the status code of the last operation
	 */
	public StatusCodes getStatusCode(){
		return this.sc;
	}
	
	/**
	 * 
	 * @return the string of the actual state name
	 */
	public OrderedItemState.States getState() { return this.realState; }
	
	/**
	 * @info: function that must be called from the ordered item in his constructor
	 * @param item
	 * @return
	 */
	public static OrderedItemState newState(OrderedItem item) { return new OrderedItemStateWaitingWorking(item); }
	
}