/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

/**
 * @author Charles Ofoegbu
 *
 */

public class AccessPojo {
    private int statusCode;
    private String userFullName;
    private String userApiKey;
    private String email;
    private String[] businessApiKeys;
    private String accountType;
    
    public AccessPojo(){
	
    }
    
    public int getStatusCode() {
	return statusCode;
    }

    public void setStatusCode(int statusCode) {
	this.statusCode = statusCode;
    }
    
    public String getUserFullName() {
	return userFullName;
    }
    
    public void setUserFullName(String userFullName) {
	this.userFullName = userFullName;
    }
    
    public String getUserApiKey() {
	return userApiKey;
    }
    
    public void setUserApiKey(String userApiKey) {
	this.userApiKey = userApiKey;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String[] getBusinessApiKeys() {
	return businessApiKeys;
    }

    public void setBusinessApiKeys(String[] businessApiKeys) {
	this.businessApiKeys = businessApiKeys;
    }

    public String getAccountType() {
	return accountType;
    }

    public void setAccountType(String accountType) {
	this.accountType = accountType;
    }



}
