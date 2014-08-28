/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Charles Ofoegbu
 *
 */
public class ItemsPojo {
    private Collection<ItemPojo> items;
    
    public ItemsPojo(){
	this.items = new ArrayList<ItemPojo>();
    }
    public Collection<ItemPojo> getItems() {
	return items;
    }

    public void setItems(Collection<ItemPojo> items) {
	this.items = items;
    }
}
