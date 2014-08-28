/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import java.sql.Timestamp;
import java.util.HashMap;

import uk.ac.dotrural.smile.entity.item.Item;

/**
 * @author Charles Ofoegbu
 *
 */
	
public class ItemPojo{  
    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String name;
    private String description;
    private LocationPojo location;
    private HashMap<String, String> properties;
    private String[] tags;
    private String product;
    private Long businessId;


    public Long getId() {
	return id;
    } 

    public void setId(Long id) {
	this.id = id;
    }
    
    public Timestamp getCreatedAt() {
	return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
	this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
	return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
	this.updatedAt = updatedAt;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
    
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }       

    public LocationPojo getLocation() {
	return location; 
    }

    public void setLocation(LocationPojo location) {
	this.location = location;
    }       
    
    public HashMap<String,String> getProperties() {
	return properties;
    }

    public void setProperties(HashMap<String, String> properties) {
	this.properties = properties;
    }
 
    public String [] getTags(){
        return this.tags;
    }
    public void setTags(java.lang.String[] tags){
        this.tags = tags;
    }

    public String getProduct() {
	return product;
    }

    public void setProduct(String product) {
	this.product = product;
    }

    public ItemPojo(){
	
    }
    
    public ItemPojo(Item item, LocationPojo loc, HashMap<String, String> props){
  	this.setId(item.getId());
  	this.setCreatedAt(item.getCreatedAt());
  	this.setDescription(item.getDescription());
  	this.setName(item.getName());
  	this.setUpdatedAt(item.getUpdatedAt());
  	this.location = loc;	
  	this.properties = props; 
  	
  	if(item.getProduct() != null){
  	    this.setProduct(item.getProduct().getFunctionalName());  	
  	}
  	if(item.getTags() != null){
  	    this.setTags(item.getTags().split(":"));
  	}
  	if(item.getBusiness() != null){
  	    this.businessId = item.getBusiness().getId();
  	}
    }

    public Long getBusinessId() {
	return businessId;
    }

    public void setBusinessId(Long businessId) {
	this.businessId = businessId;
    }

}
