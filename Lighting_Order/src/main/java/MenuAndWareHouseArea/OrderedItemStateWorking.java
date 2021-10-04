package MenuAndWareHouseArea;

import java.util.List;

public class OrderedItemStateWorking extends OrderedItemState {

	public OrderedItemStateWorking(OrderedItem item,OrderedItemState.StatusCodes code) {
		this.orderedItem=item;
		this.realState=OrderedItemState.States.Working;
		this.sc=code;
	}
	/**
	 * @info : Sets the status string and return the same state, could not modify a working product
	 */
	@Override
	public OrderedItemState.StatusCodes setAdditive(List<String> new_additive) {

		this.sc=OrderedItemState.StatusCodes.GoodsNotAddedInWorking;
		return this.sc;
	}
	
	/**
	 * @info : Same as set Additive
	 */
	@Override
	public OrderedItemState.StatusCodes addGoods(List<String> toAdd) {

		this.sc=OrderedItemState.StatusCodes.GoodsNotAddedInWorking;
		return this.sc;
	}
	
	/**
	 * @info :  could not modify a working product
	 */
	@Override
	public OrderedItemState.StatusCodes setSubGoods(List<String> new_sub) {
		this.sc=OrderedItemState.StatusCodes.GoodsNotSubbedInWorking;
		return this.sc;
	}
	
	/**
	 * @info : same as set Sub goods
	 */
	@Override
	public OrderedItemState.StatusCodes subGoods(List<String> toSub) {
		this.sc=OrderedItemState.StatusCodes.GoodsNotSubbedInWorking;
		return this.sc;
	}
	
	/**
	 * @info :This operation has no effects since the item is just in working state
	 */
	@Override
	public void startWorking() {
		
	}
	
	/**
	 * @info :returns the completed state
	 */
	@Override
	public void completeOrderedItem() {
		 this.orderedItem.changeState(new OrderedItemStateCompleted(this.orderedItem,this.sc));
	}
	
	/**
	 * @info :Not cancellable if it is in working
	 */
	@Override
	public boolean isCancellable() {
		return false;
	}
	/**
	 * @info :Can't change the priority of a working item
	 */
	@Override
	public  OrderedItemState.StatusCodes changePriority(int newPriority){
		return StatusCodes.PriorityNotChangedWorking;
	}
}
