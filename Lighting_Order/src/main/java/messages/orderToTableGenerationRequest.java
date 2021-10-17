package messages;

import java.util.List;

import com.google.gson.annotations.Expose;

public class orderToTableGenerationRequest extends baseMessage {

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
	
	public String tableId;
	
	public int tableRoomNumber;

	public orderParameters orderParams;
}
