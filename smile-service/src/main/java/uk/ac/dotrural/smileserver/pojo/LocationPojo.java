/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import uk.ac.dotrural.smile.entity.location.Location;

/**
 * @author Charles Ofoegbu
 *
 */

public class LocationPojo{   
    private java.lang.Long id;    
    private java.lang.Double longitude;   
    private java.lang.Double latitude;   
    private java.sql.Timestamp timeStamp;
	
    public java.lang.Long getId(){
        return this.id;
    }
    public void setId(java.lang.Long id){
        this.id = id;
    } 
    
    public java.lang.Double getLongitude(){
        return this.longitude;
    }
    public void setLongitude(java.lang.Double longitude){
        this.longitude = longitude;
    }
	
    public java.lang.Double getLatitude(){
        return this.latitude;
    }
    public void setLatitude(java.lang.Double latitude){
        this.latitude = latitude;
    }
	
    public java.sql.Timestamp getTimeStamp(){
        return this.timeStamp;
    }
    public void setTimeStamp(java.sql.Timestamp timeStamp){
        this.timeStamp = timeStamp;
    }
	
    public LocationPojo(){
	
    }
    public LocationPojo(Location location){
	if(location != null){
	    this.setLatitude(location.getLatitude());
	    this.setLongitude(location.getLongitude());
	    this.setTimeStamp(location.getTimeStamp());
	    this.setId(location.getId());
	}
    }


}
