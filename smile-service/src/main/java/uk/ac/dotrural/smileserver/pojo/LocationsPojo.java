/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import java.util.Collection;

/**
 * @author Charles Ofoegbu
 *
 */
public class LocationsPojo {
    private Collection<LocationPojo> locations;

    public LocationsPojo(){
	
    }
    
    public LocationsPojo(Collection<LocationPojo> locations){
	this.locations = locations;	
    }
    
    public Collection<LocationPojo> getLocations() {
	return locations;
    }

    public void setLocations(Collection<LocationPojo> locationPojo) {
	this.locations = locationPojo;
    }
}
