/**
 * 
 */
package uk.ac.dotrural.smileserver.common;

/**
 * @author Charles Ofoegbu
 *
 */
public class Constants {
    public static final String APP_NAME = "/smile-server";
    public static final String APP_VERSION = "api-1.1";
    
    public static String AUTHORIZATION_KEY_TOKEN = "$MiL3";

    public static enum Actions {
	CREATE, DELETE, UPDATE, READ, SCAN;
    }
    
    public static enum SearchableEntity {
	Product("fn","description","brand"), 
	Property("key"), 
	Item("name","description"), 
	Identifer("key"); 
//	Tag("name"), 
//	Category("name");
	
	private String[] searchableFields;

	private SearchableEntity(String ... searchableFields){
	    this.searchableFields = searchableFields;
	}
	
	public String[] getSearchableFields() {
	    return searchableFields;
	}

	public void setSearchableFields(String ... searchableFields) {
	    this.searchableFields = searchableFields;
	}
    }

    public static enum Entities {
	ITEM, PRODUCT, COLLECTION, PHOTO, PROPERTY, USERS;
    }

    public static enum Gender{
	MALE, FEMALE, OTHER;
    }
    
    public static enum DefaultApplication{
	DEVELOPER, WEB, MOBILE;
    }
    

    
    public static enum HTTP_CODE{
	CONTINUE(100),
	OK(200),
	CREATED(201),
	ACCEPTED(202),
	NO_CONTENT(204),
	BAD_REQUEST(400),
	UNAUTHORIZED(401),
	PAYMENT_REQUIRED(402),
	FORBIDDEN(403),
	NOT_FOUND(404),
	METHOD_NOT_ALLOWED(405),
	NOT_ACCEPTED(406), 
	PROXY_AUTHENTICATION_REQUIRED(407),
	REQUEST_TIME_OUT(408),
	CONFLICT(409),
	GONE(410),
	UNSUPPORTED_MEDIA_TYPE(415),
	INTERNAL_SERVER_ERROR(500),
	NOT_IMPLEMENTED(501),
	BAD_GATEWAY(502),
	SERVICE_UNAVAILABLE(503),
	GATEWAY_TIME_OUT(504),
	HTTP_VERSION_NOT_SUPPORTED(505);	
	
	private int code;	
	HTTP_CODE(int code){
	    this.setCode(code);
	}
	public int getCode() {
	    return code;
	}
	public void setCode(int code) {
	    this.code = code;
	}
    }
    
    public static enum LogOutStatus {
	LOGOUT_SUCCESSFUL("Logout ok!", HTTP_CODE.OK.getCode()), 
	LOGOUT_FAILED("Logout failed!", HTTP_CODE.NOT_ACCEPTED.getCode());
	private String status;
	private int code;
	LogOutStatus(String status, int code) {
	    this.status = status;
	    this.code = code;
	}
	LogOutStatus() {
	}
	public String getStatus() {
	    return status;
	}
	public int getCode(){
	    return code;
	}

    }
    
    public static enum Privileges{
	//Item service Privileges 
	GET_ITEMS("GET_ITEMS", "Privilage for retrieving all Items for a given business", true),
	GET_ITEMS_COUNT("GET_ITEMS_COUNT", "Privilage for retrieving Items count for a given business", true),
	GET_ITEM("GET_ITEM", "Privilage for retrieving a unique Item for a given business using the items Id", false),
	POST_ITEM("POST_ITEM", "Privilage for creating an Item for a given business", true),
	PUT_ITEM("PUT_ITEM", "Privilage for updating a unique Item for a given business", true),
	DELETE_ITEM("DELETE_ITEM", "Privilage for deleting an Item for a given business", true),	
	//Product service Privileges
	GET_PRODUCTS("GET_PRODUCTS", "Privilage for retrieving all Products for a given business", true),
	GET_PRODUCTS_COUNT("GET_PRODUCTS_COUNT", "Privilage for retrieving Products count for a given business", true),
	GET_PRODUCT("GET_PRODUCT", "Privilage for retrieving a unique Product for a given business", false),
	POST_PRODUCT("POST_PRODUCT", "Privilage for creating a Product for a given business", true),
	PUT_PRODUCT("PUT_PRODUCT", "Privilage for updating a unique Product for a given business", true),
	DELETE_PRODUCT("DELETE_PRODUCT", "Privilage for deleting a Product for a given business", true),
	//Collection service Privileges
	GET_COLLECTIONS("GET_COLLECTIONS", "Privilage for retrieving all Collections for a given business", true),
	GET_ITEMS_COLLECTION_COUNT("GET_ITEMS_COLLECTION_COUNT", "Privilage for retrieving Collections count for a given business", true),
	GET_COLLECTION("GET_COLLECTION", "Privilage for retrieving a unique Collection for a given business", false),
	GET_COLLECTION_ITEMS("GET_COLLECTION_ITEMS", "Privilage for viewing a unique Collection's Items for a given business", false),
	POST_COLLECTION("POST_COLLECTION", "Privilage for creating a Collection for a given business", true),
	PUT_COLLECTION("PUT_COLLECTION", "Privilage for updating a Collection for a given business", true),
	PUT_COLLECTION_ITEMS("PUT_COLLECTION_ITEMS", "Privilage for adding Items to a Collection for a given business", true),
	DELETE_COLLECTION("DELETE_COLLECTION", "Privilage for deleting a Collection for a given business", true),
	DELETE_COLLECTION_ITEM("DELETE_COLLECTION_ITEM", "Privilage for removing a unique Item from a Collection for a given business", true),
	DELETE_COLLECTION_ITEMS("DELETE_COLLECTION_ITEMS", "Privilage for removing multiple Item from a Collection for a given business", true),
	//Location service Privileges
	GET_LOCATION("GET_LOCATION", "Privilage for retrieving all Location for a given business", false),
	POST_LOCATION("POST_LOCATION", "Privilage for creating a Location for a given business", false),
	PUT_LOCATION("PUT_LOCATION", "Privilage for updating a Location for a given business", false),
	DELETE_LOCATION("DELETE_LOCATION", "Privilage for deleting a Location for a given business", false),
	//Property service Privileges
	GET_PROPERTY("GET_PROPERTY", "Privilage for retrieving a unique Property using the Property's key for a given business", false),
	GET_PROPERTIES("GET_PROPERTIES", "Privilage for retrieving all Properties for a given business", false),
	POST_PROPERTY("POST_PROPERTY", "Privilage for creating a Property for a given business", false),
	PUT_PROPERTY("PUT_PROPERTY", "Privilage for updating a Property for a given business", false),
	DELETE_PROPERTY("DELETE_PROPERTY", "Privilage for deleting a Property for a given business", false),	
	//Activity service Privileges
	GET_ACTIVITY("GET_ACTIVITY", "Privilage for retrieving a unique Activity for a given business", false),
	GET_ACTIVITES("GET_ACTIVITES", "Privilage for retrieving Activities for a given business", true),
	GET_ACTIVITY_TYPE("GET_ACTIVITY_TYPE", "Privilage for retrieving Activity Type for a given business", false),
	POST_ACTIVITY("POST_ACTIVITY", "Privilage for creating an Activity for a given business", true),
	PUT_ACTIVITY("PUT_ACTIVITY", "Privilage for updating an Activity for a given business", true),	
	DELETE_ACTIVITY("DELETE_ACTIVITY", "Privilage for deleting an Activity for a given business", true),	
	POST_ACTIVITY_TYPE("POST_ACTIVITY_TYPE", "Privilage for creating an ActivityType for a given business", true),	
	//Business service Privileges	
	GET_BUSINESS("GET_BUSINESS", "Privilage for retrieving a unique business", false),	
	POST_BUSINESS("POST_BUSINESS", "Privilage for creating a business", false),	
	PUT_BUSINESS("PUT_BUSINESS", "Privilage for updating a business", false),
	//User service privileges
	POST_USER("POST_USER", "Privilage for creating a User for a given business", true),
	
	//Search service privileges
	GET_SEARCH("GET_SEARCH", "Privilage for Searching", true);	
	
	private String code;
	private String description;
	private boolean requiresBusinessAuthorizationKey;

	
	Privileges(String code, String desciption, boolean requiresBusinessAuthorizationKey){
	    this.code = code;
	    this.description = desciption;
	    this.requiresBusinessAuthorizationKey = requiresBusinessAuthorizationKey;
	}
	
	public String getCode(){
	    return this.code;
	}
	public String getDescription(){
	    return this.description;
	}	
	public boolean isRequiresBusinessAuthorizationKey() {
	    return requiresBusinessAuthorizationKey;
	}
	public void setRequiresBusinessAuthorizationKey(boolean requiresBusinessAuthorizationKey) {
	    this.requiresBusinessAuthorizationKey = requiresBusinessAuthorizationKey;
	} 
    }
}
