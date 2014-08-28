/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import java.sql.Timestamp;
import java.util.HashMap;

import uk.ac.dotrural.smile.entity.itemcollection.ItemCollection;

/**
 * @author Charles Ofoegbu
 *
 */
public class CollectionPojo {
    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String name;
    private String description;
    private HashMap<String, String> properties;
    private String[] tags;
    private Long businessId; 

    public CollectionPojo(){
	
    }
    
    public CollectionPojo(ItemCollection itemCollection,  HashMap<String, String> props){
  	this.setId(itemCollection.getId());
  	this.setCreatedAt(itemCollection.getCreatedAt());
  	this.setDescription(itemCollection.getDescription());
  	this.setName(itemCollection.getName());
  	this.setUpdatedAt(itemCollection.getUpdatedAt());
  	this.properties = props; 
  	if(itemCollection.getTags() != null){
  	    this.setTags(itemCollection.getTags().split(":"));
  	}
  	if(itemCollection.getBusiness() != null){
  	    this.businessId = itemCollection.getBusiness().getId();
  	}
    }

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

    public Long getBusinessId() {
	return businessId;
    }

    public void setBusinessId(Long businessId) {
	this.businessId = businessId;
    }

}
