/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import uk.ac.dotrural.smile.entity.business.Business;
import uk.ac.dotrural.smile.entity.users.Users;

/**
 * @author Charles Ofoegbu
 *
 */
public class BusinessPojo {
    private Long id;
    private String name;
    private String description;
    private String telephone;
    private String websiteUrl;
    private String[] photos;
    private AddressPojo address;
    private String authorizationKey;
    private String email;
    private String password;
    private Long parentBusinessId;
    
    public BusinessPojo(){	
    }   
    
    public BusinessPojo(Business business, Users user){	
	this.id = business.getId();
	this.name = business.getName();
	this.description = business.getDescription();
	this.telephone = business.getTelephone();
	if( business.getAddress() != null){
	    AddressPojo address = new AddressPojo(business.getAddress());
	    this.address = address;
	}
	if(business.getParentBusiness() != null){
	    this.parentBusinessId = business.getParentBusiness().getId();
	}
	if(business.getPhotoUrl() != null){
	    this.photos = business.getPhotoUrl().split("`");
    	}
	this.websiteUrl = business.getWebsiteUrl();
	this.email = user.getEmail();
	this.password = "*****";
	this.authorizationKey = "*****";
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
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

    public String getTelephone() {
	return telephone;
    }

    public void setTelephone(String telephone) {
	this.telephone = telephone;
    }

    public String[] getPhotos() {
	return photos;
    }

    public void setPhotos(String[] photos) {
	this.photos = photos;
    }

    public AddressPojo getAddress() {
	return address;
    }

    public void setAddress(AddressPojo address) {
	this.address = address;
    }

    public String getAuthorizationKey() {
	return authorizationKey;
    }

    public void setAuthorizationKey(String authorizationKey) {
	this.authorizationKey = authorizationKey;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public Long getParentBusinessId() {
	return parentBusinessId;
    }

    public void setParentBusinessId(Long parentBusinessId) {
	this.parentBusinessId = parentBusinessId;
    }

    public String getWebsiteUrl() {
	return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
	this.websiteUrl = websiteUrl;
    }
}
