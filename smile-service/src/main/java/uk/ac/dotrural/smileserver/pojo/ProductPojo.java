/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import java.sql.Timestamp;
import java.util.HashMap;

import uk.ac.dotrural.smile.entity.product.Product;

/**
 * @author Charles Ofoegbu
 *
 */
public class ProductPojo {
    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;    
    private String fn;    
    private String description;
    private String brand;
    private String[] categories;
    private String[] photos;
    private String url;
    private Double price;
    private HashMap<String, String> properties;
    private HashMap<String, String> identifiers;
    private String[] tags;
    private Long businessId;
    
    public ProductPojo(){	
    } 
    
    public ProductPojo(Product product, HashMap<String, String> properties, HashMap<String, String> identifiers){
	this.id = product.getId();
	this.createdAt = product.getCreatedAt();
	this.updatedAt = product.getUpdatedAt();
	this.fn = product.getFunctionalName();
	this.description = product.getDescription();
	this.brand = product.getBrand();
	if(product.getCategories() != null){
	    this.categories = product.getCategories().split(":");
	}
	if(product.getPhotoURL() != null){
	    this.photos = product.getPhotoURL().split("`");
    	}
	if(product.getTags() != null){
	    this.tags = product.getTags().split(":");
    	}
	this.url = product.getUrl();
	this.price = product.getPrice();
	this.properties = properties;  
	this.identifiers = identifiers;
  	if(product.getBusiness() != null){
  	    this.businessId = product.getBusiness().getId();
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


    public String getFn() {
	return fn;
    }


    public void setFn(String fn) {
	this.fn = fn;
    }


    public String getDescription() {
	return description;
    }


    public void setDescription(String description) {
	this.description = description;
    }


    public String getBrand() {
	return brand;
    }


    public void setBrand(String brand) {
	this.brand = brand;
    }


    public String[] getCategories() {
	return categories;
    }


    public void setCategories(String[] categories) {
	this.categories = categories;
    }


    public String[] getPhotos() {
	return photos;
    }


    public void setPhoto(String[] photos) {
	this.photos = photos;
    }


    public String getUrl() {
	return url;
    }


    public void setUrl(String url) {
	this.url = url;
    }


    public Double getPrice() {
	return price;
    }


    public void setPrice(Double price) {
	this.price = price;
    }


    public HashMap<String, String> getProperties() {
	return properties;
    }


    public void setProperties(HashMap<String, String> properties) {
	this.properties = properties;
    }


    public HashMap<String, String> getIdentifiers() {
	return identifiers;
    }


    public void setIdentifiers(HashMap<String, String> identifiers) {
	this.identifiers = identifiers;
    }

    public String[] getTags() {
	return tags;
    }

    public void setTags(String[] tags) {
	this.tags = tags;
    }

    public Long getBusinessId() {
	return businessId;
    }

    public void setBusinessId(Long businessId) {
	this.businessId = businessId;
    }


}
