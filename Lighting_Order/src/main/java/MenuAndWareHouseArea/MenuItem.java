package MenuAndWareHouseArea;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**JSON Format:
 * {
 *      name : "name_value"
 *      description : "description_value"
 *      price : "price_value"
 *      inStock : "inStock_value"
 *      area : "area_value"
 *      goodsID : ["goodsID1", "goodsID2", ... , "goodsIDn"]
 * }
 * */


public class MenuItem {
    protected final MenuAndGoodsController controller; //Default Injection
    private String description;
    protected String name;
    protected double price;
    private boolean inStock;
    protected String area;
    protected List<Goods> goods = new ArrayList<Goods>();
    
    /**
     * Constructor for ordered item
     * @param item
     */
    
    protected MenuItem(MenuItem item) {
    	this.name=item.name;
    	this.price=item.price;
    	this.goods=new ArrayList<Goods>(item.goods);
    	this.area=item.area;
    	this.controller=item.controller;
    }
    
    public MenuItem(String description, String name, double price, String area, boolean inStock, MenuAndGoodsController c){
        this.description = description;
        this.name = name;
        this.price = price;
        this.area = area;
        this.inStock = inStock;
        this.controller = c;
    }

    public void setGoods(List<Goods> goods) {
    	goods.clear();
    	goods.addAll(goods);
    	for(Goods g: this.goods) {
    		if (!g.isInStock())
    			this.setOutOfStock();
    	}
    }
    
    public void addGoods(Goods goods) {
    	if (!this.goods.contains(goods))
    		this.goods.add(goods);
    }
    
    public List<Goods> getGoods() { return goods; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getName() { return name;}

    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price;}

    public boolean isInStock() { return inStock; }

    public void setInStock() { this.inStock = true;}

    public void setOutOfStock(){this.inStock = false;}

    public String getJSONRappresentation(){
    	String to_return=null;
        try {
        	JSONObject obj=new JSONObject();
        	obj.put("name", this.name);
        	obj.put("description",this.description);
        	obj.put("price", this.price);
        	obj.put("inStock", this.inStock);
        	obj.put("area",this.area);
        	JSONArray temp=new JSONArray();
        	for(Goods g:this.goods) {
        		temp.put(g.getId());
        	}
        	obj.put("goodsID", (Object)temp);
        	to_return=obj.toString();
        }
        catch(JSONException e) {
        	//should never happen
        	to_return="";
        	e.printStackTrace();
        }
        return to_return;
    }
    
    public boolean isMe(String name) { return this.name.equals(name); }//Se non è presente nel modello aggiungerla

    public String getArea() {return this.area;} //idem
    
    public void setArea(String area) { this.area=area;}
    
    public void notifyStock() {
    	boolean check=true;
    	for(Goods g:goods) {
    		if(!g.isInStock())
    			check=false;
    	}
    	if(check) {
    	    this.setInStock();
    	    //db update
    	}
    }
    
    /**
     * @info this function helps to customize a menuItem by returning specific goods 
     * @param additive -> Must personalize the item searching for additive Goods or not
     * @param id ->id of the goods to be added or subbed 
     * @return List of goods id, it could have a size < of id in input so check it.
     */
    public List<Goods> menuItemCustomer(boolean additive,List<String >id){
    	List<Goods> to_ret=new ArrayList<>();
    	if (!additive) {
    		
    		//Find the Goods to be subbed to the MenuItem
    		//the Goods is added only if is present on the MenuItem composing Goods list
    		for(String to_sub :id ) {
	    		for(Goods g:this.goods) {
	    			if(g.isMe(to_sub)) 
	    				to_ret.add(g);
	    		}
    		}
    		
    	}
    	else {
    		//Else it is good to add, so ask the controller to find the specific Goods and adds it to the list
    		//if exists
    		for(String to_add :id) {
    			if(this.hasGoods(to_add).isEmpty()) { //If it is already present
    				Optional<Goods> h=this.controller.getGoodsById(to_add);
    				if(h.isPresent()) //If the element is present
    					to_ret.add(h.get());   			
    			}
    		}
    	}
    	
    	return to_ret;
    }
    
    public OrderedItem generateOrder() {
    	return new OrderedItem(this);
    }
    
    /**
     * @return the goods id if the goods is composing the item.
     */
    public Optional<Goods> hasGoods(String GoodsID){
    	for(Goods g:this.goods) {
    		if(g.isMe(GoodsID))
    			return Optional.of(g);
    	}
    	return Optional.empty();
    }
    	
}
