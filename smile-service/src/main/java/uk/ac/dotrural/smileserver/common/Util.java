/**
 * 
 */
package uk.ac.dotrural.smileserver.common;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import uk.ac.dotrural.smile.entity.activity.Activity;
import uk.ac.dotrural.smile.entity.users.Users;
import uk.ac.dotrural.smileserver.dao.DataAccessObject;
import uk.ac.dotrural.smileserver.pojo.ActivityPojo;

/**
 * @author Charles Ofoegbu
 *
 */
public class Util {
    public static String getContextPath(HttpServletRequest req){	
	return "http://"+req.getServerName()+":"+req.getServerPort();
    }
    
    public static String getInitiator(HttpServletRequest req){
	String apiKey =  req.getHeader("Authorization");
	Users user = DataAccessObject.getUserByAuthorizationKey(apiKey);
	if (user == null) {
	    return "";
	}
	return user.getId().toString();
    }

    
    public static Activity createActivity(HttpServletRequest req, String action, String targetEntity, String recordId, String description) {
	Activity newActivity = new Activity();
	newActivity.setActivityType(DataAccessObject.getActivityTypeByName(action)); 
	newActivity.setInitiator(getInitiator(req));
	newActivity.setRecordId(recordId);
	newActivity.setDescription(description);
	newActivity.setInitiatorIp(req.getRemoteHost());
	newActivity.setTargetEntity(targetEntity);
	newActivity.setDeleted(false);
	newActivity.setTimeStamp(new Timestamp(new Date().getTime()));
	return (Activity) DataAccessObject.createNewRecord(newActivity);
    }
    
    public static Activity createActivity(HttpServletRequest req, ActivityPojo activityPojo) {
	Activity newActivity = new Activity();
	newActivity.setActivityType(DataAccessObject.getActivityTypeByName(activityPojo.getType().toUpperCase())); 
	newActivity.setInitiator(getInitiator(req));
	newActivity.setRecordId(activityPojo.getRecordId());
	newActivity.setDescription(activityPojo.getDescription());
	newActivity.setInitiatorIp(req.getRemoteHost());
	newActivity.setTargetEntity(activityPojo.getEntity());
	newActivity.setTimeStamp(new Timestamp(new Date().getTime()));
	newActivity.setDeleted(false);
	return (Activity) DataAccessObject.createNewRecord(newActivity);
    }
}
