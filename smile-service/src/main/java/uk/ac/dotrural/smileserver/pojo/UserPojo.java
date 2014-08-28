/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import java.util.HashMap;

import uk.ac.dotrural.smile.entity.users.Users;

/**
 * @author Charles Ofoegbu
 *
 */
public class UserPojo {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String gender;
    private String timeZone;
    private String locale;
    private String userRole;
    private HashMap<String, String> customFields;
    private BirthdayPojo birthday;
    private AddressPojo address;

    public UserPojo(){
	
    }
    
    public UserPojo(Users user, HashMap<String, String> customFields, BirthdayPojo birthday){
	this.id = user.getId();
	this.email = user.getEmail();
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.gender = user.getGender();
	this.timeZone = user.getTimeZone();
	this.locale = user.getLocale();
	this.userRole = user.getRoles().getAuthority();
	this.customFields = customFields;
	this.password = "*****";
	this.birthday = birthday;	
	if( user.getAddress() != null){
	    AddressPojo address = new AddressPojo(user.getAddress());
	    this.address = address;
	}
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }
    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }
    
    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getGender() {
	return gender;
    }

    public void setGender(String gender) {
	this.gender = gender;
    }

    public String getTimeZone() {
	return timeZone;
    }

    public void setTimeZone(String timeZone) {
	this.timeZone = timeZone;
    }

    public String getLocale() {
	return locale;
    }

    public void setLocale(String locale) {
	this.locale = locale;
    }

    public String getUserRole() {
	return userRole;
    }

    public void setUserRole(String userRole) {
	this.userRole = userRole;
    }

    public HashMap<String, String> getCustomFields() {
	return customFields;
    }

    public void setCustomFields(HashMap<String, String> customFields) {
	this.customFields = customFields;
    }

    public BirthdayPojo getBirthday() {
	return birthday;
    }

    public void setBirthday(BirthdayPojo birthday) {
	this.birthday = birthday;
    }

    public AddressPojo getAddress() {
	return address;
    }

    public void setAddress(AddressPojo address) {
	this.address = address;
    }




}
