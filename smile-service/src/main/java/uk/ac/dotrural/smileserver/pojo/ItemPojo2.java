/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;

import uk.ac.dotrural.smile.entity.identifier.Identifier;
import uk.ac.dotrural.smile.entity.item.Item;
import uk.ac.dotrural.smile.entity.property.Property;
import uk.ac.dotrural.smileserver.dao.DataAccessObject;

/**
 * @author Charles Ofoegbu
 *
 */
public class ItemPojo2 {

    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String name;
    private String description;
    private LocationPojo location;
    private HashMap<String, String> properties;
    private String[] tags;
    private ProductPojo product;
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

    public ProductPojo getProduct() {
	return product;
    }

    public void setProduct(ProductPojo product) {
	this.product = product;
    }

    public ItemPojo2(){
	
    }
    
    @SuppressWarnings("unchecked")
    public ItemPojo2(Item item, LocationPojo loc, HashMap<String, String> props){
  	this.setId(item.getId());
  	this.setCreatedAt(item.getCreatedAt());
  	this.setDescription(item.getDescription());
  	this.setName(item.getName());
  	this.setUpdatedAt(item.getUpdatedAt());
  	this.location = loc;	
  	this.properties = props; 
  	if(item.getBusiness() != null){
  	    this.businessId = item.getBusiness().getId();
  	}
  	if(item.getProduct() != null){
	    HashMap<String, String> proppertiesPojo = new HashMap<String, String>();
	    Collection<Property> properties = (Collection<Property>) DataAccessObject.getProductPropertiesByProductId(item.getProduct().getId());
	    if (properties != null && !properties.isEmpty()) {
		for (Property property : properties) {
		    proppertiesPojo.put(property.getKey(), property.getValue());
		}
	    }
	    HashMap<String, String> identifiersPojo = new HashMap<String, String>();
	    Collection<Identifier> identifiers = (Collection<Identifier>) DataAccessObject.getProductIdentifiersByProductId(item.getProduct().getId());
	    if (identifiers != null && !identifiers.isEmpty()) {
		for (Identifier identifier : identifiers) {
		    identifiersPojo.put(identifier.getKey(), identifier.getValue());
		}
	    }
  	this.setProduct(new ProductPojo(item.getProduct(), proppertiesPojo, identifiersPojo));  	
  	}
  	if(item.getTags() != null){
  	    this.setTags(item.getTags().split(":"));
  	}
    }

    public Long getBusinessId() {
	return businessId;
    }

    public void setBusinessId(Long businessId) {
	this.businessId = businessId;
    }

}
