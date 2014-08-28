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
public class PropertiesPojo {
    private ArrayList<PropertyPojo> properties;

    public PropertiesPojo(){
    }
    
    public PropertiesPojo(ArrayList<PropertyPojo> properties){
	this.properties = properties;
    }

    public Collection<PropertyPojo> getProperties() {
	return properties;
    }

    public void setProperties(ArrayList<PropertyPojo> properties) {
	this.properties = properties;
    }
}
