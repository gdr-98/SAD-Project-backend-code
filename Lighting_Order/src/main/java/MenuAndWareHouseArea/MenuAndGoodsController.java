package MenuAndWareHouseArea;
import DataAccess.MenuAndWarehouseDAO;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * */
@Service
@ComponentScan(basePackages= {"DataAccess"})
public class MenuAndGoodsController { //Aggiornato il nome con conseguente aggiunta di lettere maiuscole !
	
	private List<MenuItem> menuItems=new ArrayList<MenuItem>();
    private List<Goods> goods=new ArrayList<Goods>();
    private final MenuAndWarehouseDAO db;
    
    /**Inizializzazione da DB*/
    @Autowired
    public MenuAndGoodsController(@Qualifier("psqlMenu") MenuAndWarehouseDAO db){
    	this.db=db;
    }
    
    /**
     * 
     * @param JSON , the json object of the goods
     * @return instantiate a Goods and returns it's reference if everything goes well
     */
    public Optional<Goods> initGoodsFromJSON(String JSON) {
    	Goods to_init=null;
    	JSONObject parser;
    	JSONArray helper;
    	Optional<MenuItem> temp;
    	try {
    		parser=new JSONObject(JSON);
    		//Initializing the object
    		//System.out.println("[PRICE]"+parser.getDouble("priceAsAdditive"));
    		to_init= new Goods( parser.getString("id"), parser.getInt("quantity"), 
    												parser.getDouble("priceAsAdditive"),
    				 parser.getInt("quantityLowerBound"), parser.getString("name"), 
    				 parser.getDouble("unitPrice"),parser.getBoolean("inStock"),this);
    		helper=parser.getJSONArray("menuItems");
    		goods.add(to_init); //add good to list
    		
    		
    		//Collecting MenuItems
    		for(MenuItem item: this.menuItems) {
    			for(int i=0;i<helper.length();i++) {
    				if(item.isMe(helper.getString(i))) {
    					to_init.addItem(item);
    					helper.remove(i); //Removing from list
    				}
    			}	
    		}
    		//helper1=new JSONArray(helper);
    		
    		//For the remaining Items
    		for(int i=0;i<helper.length();i++) {
    			temp=getMenuItemByName(helper.getString(i));
    			if(temp.isPresent())
    				to_init.addItem(temp.get());
    		}
    		
    		return Optional.of(to_init);
    	}
    	catch(JSONException e) {
    		//Should never happen
    		e.printStackTrace();
    	}
    	return Optional.empty();
    }
    
    /**
     * 
     * @param JSON , the json object of the goods
     * @return instantiate a Goods and returns it's reference if everything goes well
     */
    private Optional<MenuItem> initMenuItemFromJSON(String JSON) {
    	MenuItem to_init=null;
    	JSONObject parser;
    	JSONArray helper;
    	Optional<Goods> temp;
    	
    	try {
    		parser=new JSONObject(JSON);
    		//Initializing the object
    		to_init=new MenuItem(parser.getString("description"),parser.getString("name"),parser.getDouble("price")
    							,parser.getString("area"),parser.getBoolean("inStock"),this);
    		helper=parser.getJSONArray("goodsID");
    		
    		menuItems.add(to_init); //add good to list
    		//Collecting Goods
    		for(Goods ware: this.goods) {
    			for(int i=0;i<helper.length();i++) {
    				if(ware.isMe(helper.getString(i))) {
    					to_init.addGoods(ware);
    					helper.remove(i); //Removing from list
    				}
    			}
    		}	
    		//For the remaining Items
    		for(int i=0;i<helper.length();i++) {
    			temp=this.getGoodsById(helper.getString(i));
    			if(temp.isPresent())
    				to_init.addGoods(temp.get());
    		}
    		
    		return Optional.of(to_init);
    	}
    	
    	catch(JSONException e) {
    		//Should never happen
    		e.printStackTrace();
    	}
    	return Optional.empty();
    }
    
    /**
     * 
     * @param goodsID
     * @return return a ware with the specified goodsID if exists, else an empty optional 
     */
    public Optional<Goods> getGoodsById(String goodsID){	
    	
    	//Search the goods on the current list
    	for(Goods g:this.goods) {
    		if (g.isMe(goodsID))
    			return Optional.of(g);
    	}  	
    	//else maybe it is in the db 
    	String query=db.findGoodsByIdJSON(goodsID);
    	if(!query.isEmpty()) {
    		return initGoodsFromJSON(query);
    	}
    	
    	//if it wasn't in the db then return empty
        return Optional.empty();
    }
    
    public String getGoodsByIdJSON(String goodsID){
    	
    	Optional<Goods> result=getGoodsById(goodsID);
    	
    	if (result.isEmpty())
    		return "";
    	
    	else
    		return result.get().getJSONRepresentation();
    }
    
    /**
     * 
     * @param menuItemName
     * @return returns an empty optional if the elements doesn't exist, else the optional encapsulating the element
     */
    public Optional<MenuItem> getMenuItemByName(String menuItemName){ //Da aggiungere al modello !!
    
    	//Search the menuItem on the current list
    	for(MenuItem item: this.menuItems) {
    		if (item.isMe(menuItemName)) {
    			return Optional.of(item);
    		}
    	}
    	
    	//else maybe it is in the db 
    	String query=db.findMenuItemByNameJSON(menuItemName);
    	if(!query.isEmpty()) {
    		return initMenuItemFromJSON(query);
    	}
    	
    	//if it wasn't in the db then return empty
        return Optional.empty();
    }
    
    /**
     * 
     * @param menuItemName
     * @description returns menuItem in json format
     * @return MenuItem in JSONFormat, if the item doesn't exist returns an empty string
     */
    public String getMenuItemByNameJSON(String menuItemName) {
    	
    	Optional<MenuItem> result=getMenuItemByName(menuItemName);
    	
    	if (result.isEmpty())
    		return "";
    	
    	else
    		return result.get().getJSONRappresentation();
    }
    /**
     * 
     * @param areaFlag -> return only menuItems of a specific area
     * @param area
     * @return list of menuItem in JSON(the entire menu if areaFlag false)
     */
    public String getMenuJSON(boolean areaFlag ,String area) {
    	if(areaFlag &(area.isBlank()))
    		return "";
    	JSONArray helper;
    	List<String> names;
    	JSONObject obj;
    		 
    	//If we have all the menu
    	try {	
    		helper=new JSONArray();
    		
    		names=db.menuItemsNames(); //Find all names
    		for(String name:names) {
    			obj=new JSONObject(getMenuItemByNameJSON(name));
    			if(areaFlag & (obj.getString("area").equals(area)))
    				helper.put(obj);
    			else if(!areaFlag) {
    				helper.put(obj);
    			}
    		}
    		return helper.toString();
	    }
    	
	    catch(JSONException e){
	    	//Should never happen
			e.printStackTrace();
			System.out.println("ERRORE");
			return "";
	    }
	    	
    }
   
    /**
     * 
     * @return menuItems objects list
     * @note this function should be used only with unit tests, for this it is marked as deprecated
     */
    @Deprecated
    public List<MenuItem> getAllItems(){
    	return this.menuItems;
    }
    /**
     * 
     * @return goods objects list
     * @note this function should be used only with unit tests, for this it is marked as deprecated
     */
    @Deprecated
    public List<Goods> getAllGoods(){
    	return this.goods;
    }
    
    /**
     * @info generate a new order associated to a specific menuItem
     * @param menuItemName
     * @return empty if the menuItem doesn't exist, else che optional of the order
     */
    public  Optional<OrderedItem> generateOrderedItem(String menuItemName) {
    	Optional<MenuItem> item=getMenuItemByName(menuItemName);
    	if (item.isEmpty())
    		return Optional.empty();
    	else
    		return Optional.of(item.get().generateOrder());
    }
}
