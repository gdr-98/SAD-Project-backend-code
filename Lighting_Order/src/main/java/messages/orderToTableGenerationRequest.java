package messages;

import java.util.List;

import com.google.gson.annotations.Expose;

public class orderToTableGenerationRequest extends baseMessage {
	public String tableID;
	public int tableRoomNumber;
	public class orderParameters{
		@Expose(serialize=true,   deserialize=true)
		public List<String> itemNames;
		@Expose(serialize=true, deserialize=true)
		public List<List<String>> addGoods;
		@Expose(serialize=true,deserialize=true)
		public List<List<String>>subGoods;
		@Expose(serialize=true,deserialize=true)
		public List<Integer>  priority;
	}
	public  orderParameters orderParams;
	/**
	 * These parts of the message can be empty,check !
	 */
	public String kitchenOrder;
	public String barOrder;
	public String bakeryOrder;
}
