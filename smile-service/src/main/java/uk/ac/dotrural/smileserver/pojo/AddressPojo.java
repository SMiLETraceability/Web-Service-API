/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import uk.ac.dotrural.smile.entity.address.Address;

/**
 * @author Charles Ofoegbu
 *
 */
public class AddressPojo {
    private String number;
    private String street;
    private String postcode;
    private String county;
    private String country;
    
    public AddressPojo(){	
    }

    
    public AddressPojo(Address address) {
	this.country = (address.getCountry());
	this.county = (address.getCounty());
	this.number = (address.getHouseNumber());
	this.postcode = (address.getPostCode());
	this.street = (address.getStreet());
    }
    
    public String getNumber() {
	return number;
    }

    public void setNumber(String number) {
	this.number = number;
    }

    public String getStreet() {
	return street;
    }

    public void setStreet(String street) {
	this.street = street;
    }

    public String getPostcode() {
	return postcode;
    }

    public void setPostcode(String postcode) {
	this.postcode = postcode;
    }

    public String getCounty() {
	return county;
    }

    public void setCounty(String county) {
	this.county = county;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }
}
