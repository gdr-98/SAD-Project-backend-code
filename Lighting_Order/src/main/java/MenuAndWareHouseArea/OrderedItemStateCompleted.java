package MenuAndWareHouseArea;

import java.util.List;

public class OrderedItemStateCompleted extends OrderedItemState {

	public OrderedItemStateCompleted(OrderedItem item,OrderedItemState.StatusCodes code) {
		this.orderedItem=item;
		this.realState=OrderedItemState.States.Completed;
		this.sc=code;
	}
	
	/**
	 * @info : Sets the status string and return the same state, could not modify a completed OrderedProduct
	 */
	@Override
	public OrderedItemState.StatusCodes setAdditive(List<String> new_additive) {
		
		this.sc=OrderedItemState.StatusCodes.GoodsNotAddedCompleted;
		return this.sc;
	}
	
	/**
	 * @info : same as setAdditive
	 */
	@Override
	public OrderedItemState.StatusCodes addGoods(List<String> toAdd) {
		
		this.sc=OrderedItemState.StatusCodes.GoodsNotAddedCompleted;
		return this.sc;
	}
	
	
	/**
	 * @info : Sets the status string and return the same state, could not modify a completed OrderedProduct
	 */
	@Override
	public OrderedItemState.StatusCodes setSubGoods(List<String> new_sub) {
		this.sc=OrderedItemState.StatusCodes.GoodsNotSubbedCompleted;
		return this.sc;
	}
	
	/**
	 * @info : same as setSubGoods
	 */
	@Override
	public OrderedItemState.StatusCodes subGoods(List<String> toSub) {
		this.sc=OrderedItemState.StatusCodes.GoodsNotSubbedCompleted;
		return this.sc;
	}
	
	/**
	 * @info :This operation has no effects since the item is in completed state
	 */
	@Override
	public void startWorking() {
		
	}
	
	/**
	 * @info :No effects 
	 */
	@Override
	public void completeOrderedItem() {
		
	}
	
	/**
	 * @info :Not cancellable if it is in working
	 */
	@Override
	public boolean isCancellable() {
		return false;
	}
	/**
	 * @info: Can't change the priority of an ordered item
	 */
	public  OrderedItemState.StatusCodes changePriority(int newPriority){
		return StatusCodes.PriorityNotChangedCompleted;
	}
}
