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
public class CollectionsPojo {
    private ArrayList<CollectionPojo> collections;

    public CollectionsPojo(){	 
    }
    
    public CollectionsPojo(ArrayList<CollectionPojo> collections){
	this.collections = collections;
    }
    
    public Collection<CollectionPojo> getCollections() {
	return collections;
    }

    public void setCollections(ArrayList<CollectionPojo> collections) {
	this.collections = collections;
    }

}
