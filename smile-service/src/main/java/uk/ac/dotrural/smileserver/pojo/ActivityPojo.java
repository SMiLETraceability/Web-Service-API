/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import java.sql.Timestamp;
import java.util.HashMap;

import uk.ac.dotrural.smile.entity.activity.Activity;

/**
 * @author Charles Ofoegbu
 *
 */
public class ActivityPojo {
    private Long id;
    private String type;
    private String entity;
    private String recordId;
    private String user;
    private String description; 
    private Timestamp timestamp;
    private LocationPojo location; 
    private String[] tags;
    private HashMap<String, String> customFields;
    private HashMap<String, String> context;
    private String locationSource;

    public ActivityPojo(){	
    }
    
    public ActivityPojo(Activity activity, HashMap<String, String> customFields, HashMap<String, String> context){	
	this.id = activity.getId();
	this.type = activity.getActivityType().getName();
	this.entity = activity.getTargetEntity();
	this.recordId = activity.getRecordId();
	this.user = activity.getInitiator();
	this.description = activity.getDescription();
	this.timestamp = activity.getTimeStamp();
	LocationPojo location = new LocationPojo();
	location.setLatitude(activity.getLatitude());
	location.setLongitude(activity.getLongitude());
	this.context = context;
	this.customFields = customFields;
	this.location = location;
	
    }
    
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getType() {
	return type;
    }
    public void setType(String type) {
	this.type = type;
    }
    public String getEntity() {
	return entity;
    }
    public void setEntity(String entity) {
	this.entity = entity;
    }
    public String getRecordId() {
	return recordId;
    }
    public void setRecordId(String recordId) {
	this.recordId = recordId;
    }
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Timestamp getTimestamp() {
	return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
	this.timestamp = timestamp;
    }
    public LocationPojo getLocation() {
	return location;
    }
    public void setLocation(LocationPojo location) {
	this.location = location;
    }
    public String[] getTags() {
	return tags;
    }
    public void setTags(String[] tags) {
	this.tags = tags;
    }
    public HashMap<String, String> getCustomFields() {
	return customFields;
    }
    public void setCustomFields(HashMap<String, String> customFields) {
	this.customFields = customFields;
    }
    public HashMap<String, String> getContext() {
	return context;
    }
    public void setContext(HashMap<String, String> context) {
	this.context = context;
    }
    public String getLocationSource() {
	return locationSource;
    }
    public void setLocationSource(String locationSource) {
	this.locationSource = locationSource;
    }

    public String getUser() {
	return user;
    }

    public void setUser(String user) {
	this.user = user;
    }
}
