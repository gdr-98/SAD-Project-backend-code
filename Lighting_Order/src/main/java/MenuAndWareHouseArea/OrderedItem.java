package MenuAndWareHouseArea;
import java.util.List;
/*
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;*/
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
import com.google.gson.annotations.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * 
 * @JsonRepresenation
 * 	item:"AssociatedMenuItemName"
 * 	itemArea:"AssociatedMenuItemArea"
 * 	additive:[ "additiveID1"...]
 * 	sub:["subID1"..]
 *	actualState:"StateName"
 *	lineNumber="LineNumberValue"
 *	priority="priorityValue"
 */
public class OrderedItem  {
	
	@Expose(serialize=false,deserialize=false)
	private MenuItem item;
	
	@Expose(serialize=false,deserialize=false)
	private List<Goods> additive=new ArrayList<Goods>();
	
	@Expose(serialize=false,deserialize=false)
	private List<Goods> sub=new ArrayList<Goods>();
	
	@Expose(serialize=false,deserialize=false)
	private OrderedItemState actualState=null;
	
	@Expose(serialize=true,deserialize=true)
	private int lineNumber;
	
	@Expose(serialize=true,deserialize=true)
	private int priority;
	
	/** 
	 * 
	 * @param item ->Initialize an orderedItem from an item
	 */
	public OrderedItem(MenuItem item) {
		this.item=item;
		this.actualState=OrderedItemState.newState(this);
	}
	
	/**
	 * 
	 * @param priority to set
	 */
	public void setPriority(int priority) { this.priority=priority;}
	
	/**
	 * 
	 * @return the priority of the item
	 */
	public int getPriority() { return this.priority;}
	
	/**
	 * 
	 * @return  the price of the product(price of item+ price of additive Goods)
	 */
	public  double getPrice() {
		double to_ret=item.getPrice();
		for(Goods g:additive) {
			to_ret=to_ret+g.getPriceAsAdditive();
		}
		//Unhandled the case of sub 
		return to_ret;
	}
	
	/**
	 * 
	 * @info	this function is meant to be called by the OrderedState to perform operations.
	 * @return	reference to the menuItem
	 */
	public MenuItem getItem() { return this.item;}
	
	/**
	 * @info this function is meant to be called by the OrderedItem constructor
	 * @param lineNumber, number of line for  this specific ordered product
	 */
	public void setLineNumber(int lineNumber) { this.lineNumber=lineNumber;}
	
	/**
	 * @return line number
	 */
	public int getLineNumber() { return this.lineNumber;}
	
	/**
	 * @returns true if  line numbers corresponds
	 */
	public boolean isMe(int lineNumber) { return this.lineNumber==lineNumber;}
	
	/**
	 * 	@info 			this method is meant to be called by the state, not outside !
	 *	@AdditiveInfo	Duplicates in additive are automatically removed
	 * 	@param 			additive Goods list
	 */
	public void setAdditiveGoods(List<Goods> additive) {
		this.additive.clear();
		this.additive.addAll(new HashSet<>(additive));
	}
	
	/**
	 * @info 			this method is meant to be called by the state, not outside !
	 * @AdditiveInfo	Duplicates in sub are automatically removed
	 * @param 			additive Goods list
	 */
	public void setSubGoods(List<Goods> sub) {
		this.sub.clear();
		this.sub.addAll(new HashSet<> (sub));
	}
	
	/**
	 * @info this method is meant to be called by the state, not outside1 
	 * @param change the actual state
	 */
	public void changeState(OrderedItemState newState) {
		this.actualState=newState;
	}
	/**
	 * 
	 * @param new goods ids 
	 * @return the last status code of the state
	 */
	public OrderedItemState.StatusCodes changeAddGoods(List<String> goods){
		 return actualState.setAdditive(goods);
		 
	}
	/**
	 * 
	 * @param new goods ids
	 * @return the last status code of the state
	 */ 
	public OrderedItemState.StatusCodes changeSubGoods(List<String> goods){
		 return actualState.setSubGoods(goods);
		 
	}
	/**
	 * 
	 * @return the json representation of the string
	 */
	public String getJSONRepresentation() {
		Gson gson = new GsonBuilder()
				  .excludeFieldsWithoutExposeAnnotation()
				  .create();
		JsonObject to_ret=new JsonObject();
		JsonArray helper=new JsonArray();
		
		to_ret.getAsJsonObject().addProperty("item", item.getName());
		to_ret.addProperty("itemArea", item.getArea());
		for(Goods g:this.additive) {
			helper.add(g.getId());
		}
		to_ret.add("additive",helper);
		
		helper=new JsonArray();
		for(Goods g:this.sub) {
			helper.add(g.getId());
		}
		to_ret.add("sub",helper);
		to_ret.addProperty("actualState",this.actualState.getState().name());
		to_ret.addProperty("lineNumber",this.lineNumber);
		to_ret.addProperty("priority", this.priority);
		return gson.toJson(to_ret);
	}
	/**
	 * 
	 * @return The Enum.StateName
	 */
	public OrderedItemState.States getStateRaw(){  return actualState.getState(); }
	
	/**
	 * 
	 * @return the name of the actual state in string
	 */
	public String getState() { return this.getStateRaw().name(); }
	
	/**
	 * @info Complete ordered item. See states for additive infos 
	 */
	public  void complete() {  actualState.completeOrderedItem(); }
	
	/**
	 * @info Complete ordered item. See states for additive infos 
	 */
	public  void setWorking() {  actualState.startWorking(); }
	
	/**
	 * 
	 * @return The Enum.StatusCode
	 */
	public OrderedItemState.StatusCodes getStatusCodeRaw() { return this.actualState.getStatusCode(); }
	
	/**
	 * 
	 * @return the name of the actual state in string
	 */
	public String getStatusCode() { return this.getStatusCodeRaw().name(); }
	
	/**
	 * @return true if the orderedItem is cancellable
	 */
	public boolean isCancellable() { return this.actualState.isCancellable() ; } 
	
	/**
	 * @return Additive goods ids
	 */
	public List<String> getAdditiveIDs() {
		List<String>to_ret=new ArrayList<>();
		for(Goods g:this.additive)
			to_ret.add(g.getId());
		return to_ret;
	}
	
	/**
	 * @return Sub goods ids
	 */
	public List<String> getSubIDs() {
		List<String>to_ret=new ArrayList<>();
		for(Goods g:this.sub)
			to_ret.add(g.getId());
		return to_ret;
	}
	
	/**
	 * @info			Add goods to additive list.
	 * @Additiveinfo	If the goods is already inn list it is simply not added.
	 * @toAdd			List of goods to be added
	 */
	public boolean addGoodsToItemRaw(List<Goods> toAdd) {
		boolean check=this.additive.addAll(toAdd);
		this.additive=new ArrayList<>( new HashSet<>(this.additive));
		return check;
	}
	
	/**
	 * @info			Add goods to sub list.
	 * @Additiveinfo	If the goods is already inn list it is simply not subbed.
	 * @toSub			The list of goods to be subbed to the sub list
	 */
	public boolean addSubToItemRaw(List<Goods> toSub) {
		boolean check=this.sub.addAll(toSub);
		this.sub=new ArrayList<>( new HashSet<>(this.sub));
		return check;
	}
	/**
	 * 
	 * @info this function changes the state using a string 
	 */
	public void setStateFromString(String newState) {
		
		if(newState.equals(OrderedItemState.States.WaitingForWorking.name())) {
			this.actualState=new OrderedItemStateWaitingWorking(this);
		}
		else if(newState.equals(OrderedItemState.States.Completed.name())) {
			//the last code is not saved in the db
			this.actualState=new OrderedItemStateCompleted(this,OrderedItemState.StatusCodes.NoOperation);
		}
		else if(newState.equals(OrderedItemState.States.Working.name())) {
			//the last code is not saved in the db
			this.actualState=new OrderedItemStateWorking(this,OrderedItemState.StatusCodes.NoOperation);
		}
	}
	
	public OrderedItemState.StatusCodes changePriority(int newPriority){
		return this.actualState.changePriority(newPriority);
	}
	
}
