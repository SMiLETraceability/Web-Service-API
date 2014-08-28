/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import java.sql.Timestamp;

import uk.ac.dotrural.smile.entity.property.Property;

/**
 * @author Charles Ofoegbu
 *
 */
public class PropertyPojo {    
    private String key;   
    private String value;   
    private Timestamp timestamp;   

    public java.lang.String getKey(){
        return this.key;
    }
    public void setKey(java.lang.String key){
        this.key = key;
    }
	
    public java.lang.String getValue(){
        return this.value;
    }
    public void setValue(java.lang.String value){
        this.value = value;
    }
    
    public Timestamp getTimestamp() {
	return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
	this.timestamp = timestamp;
    }
    
    public PropertyPojo(){
	
    }
    
    public PropertyPojo(Property property){
	this.setTimestamp(property.getTimeStamp()); 
	this.setKey(property.getKey());
	this.setValue(property.getValue());
    }

}
