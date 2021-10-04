package MenuAndWareHouseArea;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**JSON Format:
 * {
 *      id : "goods_id"
 *      quantity : "goods_quantity"
 *      priceAsAdditive : "price_as_additive_value"
 *      quantityLowerBound:"quantityLowerBound_value"
 *      name:"goods_name"
 *      unitPrice:"unitPriceName"
 *      inStock : "inStock_value"
 *      menuItems : ["menuItemName1", "MenuItemName2", ... , "MenuItemNameN"]
 * }
 * */
public class Goods {
	@SuppressWarnings("unused")
	private final MenuAndGoodsController controller;
    private String id;
    private int quantity;
    private double priceAsAdditive;
    private int quantityLowerBound;
    private String name;
    private double unitPrice;
    private boolean inStock;
    private List<MenuItem> menuItems=new ArrayList<MenuItem>();


    public Goods(String id, int quantity, double priceAsAdditive, int quantityLowerBound, String name, double unitPrice, boolean inStock,MenuAndGoodsController c) {
        this.id = id;
        this.quantity = quantity;
        this.priceAsAdditive = priceAsAdditive;
        this.quantityLowerBound = quantityLowerBound;
        this.name = name;
        this.unitPrice = unitPrice;
        this.inStock = inStock;
        this.controller=c;
    }
    
    public void setItems(List<MenuItem> items){
    	menuItems.clear();
        menuItems.addAll(items);
    }
    
    public void addItem(MenuItem item) {
    	if(!menuItems.contains(item))
    		menuItems.add(item);
    }
    
    public String getId() { return id; }

    public void setId(String id) { this.id = id;}

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity;}

    public double getPriceAsAdditive() {  return priceAsAdditive;}

    public void setPriceAsAdditive(double priceAsAdditive) { this.priceAsAdditive = priceAsAdditive;}

    public int getQuantityLowerBound() { return quantityLowerBound; }

    public void setQuantityLowerBound(int quantityLowerBound) { this.quantityLowerBound = quantityLowerBound; }

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public double getUnitPrice() { return unitPrice; }

    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public boolean isInStock() { return inStock; }

    public void setStock(boolean s){ this.inStock = s;}

    public void setInStock() { this.inStock = true;}

    public void setOutOfStock(){this.inStock = false;}
    
    public String getJSONRepresentation() {
    	/**REMEMBER: Comparing two objects by comparing this string is useless.
    		A Different JSON Object can have different orders for the same field (and generate different strings even if 
    		they're equal)
    		To compare JSONObject using string must generate new Objects from string and compare the keys.*/
    	String to_return;
    	try {
    		JSONObject obj=new JSONObject();
    		obj.put("id", this.id);
    		obj.put("quantity", this.quantity);
    		obj.put("priceAsAdditive", this.priceAsAdditive);
    		obj.put("quantityLowerBound", this.quantityLowerBound);
    		obj.put("name", this.name);
    		obj.put("unitPrice", this.unitPrice);
    		obj.put("inStock", this.inStock);
        	JSONArray temp=new JSONArray();
        	for(MenuItem item:this.menuItems) {
        		temp.put(item.getName());
        	}
        	obj.put("menuItems",(Object)temp); //the array should be passed as an obj, not string
        	to_return=obj.toString();
    		
    	}
    	catch(JSONException e) {
    		//Should never happen
    		e.printStackTrace();
    		to_return ="";
    	}	
    	return to_return;
    }
    public boolean isMe(String goodsId) {
    	return this.id.equals(goodsId);
    }
    public int addQuantity(int toAdd ) {
    	this.quantity=this.quantity+toAdd;
    	//HERE DB UPDATE (not yet implemented)
    	for(MenuItem item:this.menuItems) {
    		if(!item.isInStock()) { //if some item wasn't in stock then notify
    			item.notifyStock();
    		}
    	}
    	return this.quantity;
    }
    public int subQuantity(int toSub) {
    	this.quantity=Math.max(0, this.quantity-toSub);
    	//HERE DB UPDATE(Not yet implemented)
    	if(this.quantity==0) { //out of stock
    		for(MenuItem item:this.menuItems) {
    			item.setOutOfStock();
    		}
    	}
    	if(this.quantity<this.quantityLowerBound) {
    		//not yet implemented
    	}
    	return this.quantity;
    }
}
