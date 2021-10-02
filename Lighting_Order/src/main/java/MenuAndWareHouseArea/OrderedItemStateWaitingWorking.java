package MenuAndWareHouseArea;

import java.util.List;

public class OrderedItemStateWaitingWorking extends OrderedItemState {

	public OrderedItemStateWaitingWorking(OrderedItem item) {
		this.orderedItem=item;
		this.realState=OrderedItemState.States.WaitingForWorking;
		this.sc=OrderedItemState.StatusCodes.NoOperation;
	}
	
	/**
	 * @info :  sets a new list of additive Goods
	 */
	@Override
	public OrderedItemState.StatusCodes setAdditive(List<String> new_additive) {
		
		if(new_additive.size()==0) {
			//If no goods to add then return goods added
			this.sc=OrderedItemState.StatusCodes.GoodsAdded;
			return this.sc;
		}
		
		MenuItem helper=orderedItem.getItem();	
		List<Goods> to_add=helper.menuItemCustomer(true, new_additive);
		//if nothing was found abort the operation
		if(to_add.size()==0) {
			this.sc=OrderedItemState.StatusCodes.GoodsNotAddedZeroMatches;
			return this.sc;
		}
		
		this.orderedItem.setAdditiveGoods(to_add); //add the goods
		
		//If one or more goods are not added(not found or duplicates) signals it
		if(this.orderedItem.getAdditiveIDs().size()< new_additive.size()) 
			this.sc=OrderedItemState.StatusCodes.GoodsAddedNotAll;
		
		else if(this.orderedItem.getAdditiveIDs().size()== new_additive.size())
			this.sc=OrderedItemState.StatusCodes.GoodsAdded;
		

		return this.sc; //return
	}
	
	/**
	 * @info :  add Goods to the item
	 */
	@Override
	public OrderedItemState.StatusCodes addGoods(List<String> toAdd) {
		//actually this function is completely useless.
		return this.sc;
	}
	
	/**
	 * @info :  subGoods Goods to the item
	 */
	@Override
	public OrderedItemState.StatusCodes subGoods(List<String> toSub) {
		//actually this function is completely useless.
		return this.sc;
	}

	/**
	 * @info : sets a new list of sub goods
	 */
	@Override
	public OrderedItemState.StatusCodes setSubGoods(List<String> new_sub) {
		if(new_sub.size()==0) {
			//if  no goods simply return a successful message
			this.sc=OrderedItemState.StatusCodes.GoodsSubbed;
			return this.sc;
		}
		MenuItem helper=orderedItem.getItem();
		List<Goods> to_sub=helper.menuItemCustomer(false, new_sub);
		//if nothing was found abort the operation
		if(to_sub.size()==0) {
			this.sc=OrderedItemState.StatusCodes.GoodsNotSubbedZeroMatches;
			return this.sc;
		}
		
		this.orderedItem.setSubGoods(to_sub); //sub the goods
		
		//If one or more goods are not added(not found or duplicates) signals it
		if(this.orderedItem.getSubIDs().size()< new_sub.size()) 
			this.sc=OrderedItemState.StatusCodes.GoodsSubbedNotAll;
		
		else if(to_sub.size()== new_sub.size())
			this.sc=OrderedItemState.StatusCodes.GoodsSubbed;
		
		return this.sc; //return
	}
	
	/**
	 * @info :Put this specific OrderedItem in working state 
	 */
	@Override
	public void startWorking() {
		this.orderedItem.changeState (new OrderedItemStateWorking(this.orderedItem,this.sc));
	}
	
	/**
	 * @info : 
	 */
	@Override
	public void completeOrderedItem() {
		this.orderedItem.changeState(new OrderedItemStateCompleted(this.orderedItem,this.sc));
	}
	
	/**
	 * @info :It is cancellable in this state
	 */
	@Override
	public boolean isCancellable() {
		return true;
	}
	
	/**
	 * @info :Can't change the priority of a working item
	 */
	@Override
	public  OrderedItemState.StatusCodes changePriority(int newPriority){
		this.orderedItem.setPriority(newPriority);
		return StatusCodes.PriorityChanged;
	}
}
