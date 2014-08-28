/**
 *
 */
package uk.ac.dotrural.smileserver.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.dotrural.smile.entity.activity.Activity;
import uk.ac.dotrural.smile.entity.activitytype.ActivityType;
import uk.ac.dotrural.smile.entity.customfield.CustomField;
import uk.ac.dotrural.smileserver.common.ApplicationSecurityController;
import uk.ac.dotrural.smileserver.common.Constants;
import uk.ac.dotrural.smileserver.common.Util;
import uk.ac.dotrural.smileserver.dao.DataAccessObject;
import uk.ac.dotrural.smileserver.pojo.ActivitiesPojo;
import uk.ac.dotrural.smileserver.pojo.ActivityPojo;
import uk.ac.dotrural.smileserver.pojo.ActivityTypePojo;
import uk.ac.dotrural.smileserver.pojo.ExceptionPojo;
import uk.ac.dotrural.smileserver.pojo.ApplicationStatePojo;

/**
 * @author Charles Ofoegbu
 * 
 */
@Path("/activity")
@Produces({ MediaType.APPLICATION_JSON })
public class ActivityService {

    private static Logger logger = LoggerFactory.getLogger(ActivityService.class);

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @SuppressWarnings("unchecked")
    public Response getActivities(@Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get items properties");

	ApplicationStatePojo authorizationState = ApplicationSecurityController
		.isAuthorized(req, Constants.Privileges.GET_ACTIVITES);
	if (authorizationState.getExceptionPojo() != null) {
	    return Response
		    .status(authorizationState.getExceptionPojo()
			    .getStatusCode())
		    .entity(authorizationState.getExceptionPojo()).build();
	}

	ArrayList<ActivityPojo> ActivityPojoList = new ArrayList<ActivityPojo>();
	ArrayList<Activity> activities = (ArrayList<Activity>) DataAccessObject
		.getAllRecords(Activity.class);
	if (activities == null || activities.isEmpty()) {
	    return Response
		    .status(Constants.HTTP_CODE.NOT_FOUND.getCode())
		    .entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND,
			    null, "No Activities found")).build();
	} else {
	    try {
		for (Activity activity : activities) {
		    ArrayList<CustomField> customFieldList = (ArrayList<CustomField>) DataAccessObject
			    .getCustomFiledByActivityAndType(activity,
				    "CUSTOM_FIELD");
		    ArrayList<CustomField> contextList = (ArrayList<CustomField>) DataAccessObject
			    .getCustomFiledByActivityAndType(activity,
				    "CONTEXT");
		    ActivityPojo activityPojo = new ActivityPojo(activity,
			    CustomFieldCollectionToMap(customFieldList),
			    CustomFieldCollectionToMap(contextList));
		    ActivityPojoList.add(activityPojo);
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    authorizationState = null;
	    return Response.status(Constants.HTTP_CODE.OK.getCode())
		    .entity(new ActivitiesPojo(ActivityPojoList)).build();
	}
    }

    private HashMap<String, String> CustomFieldCollectionToMap(
	    ArrayList<CustomField> customFieldList) {
	HashMap<String, String> customFields = new HashMap<String, String>();
	for (CustomField customField : customFieldList) {
	    customFields.put(customField.getKey(), customField.getValue());
	}
	return customFields;
    }

    @GET
    @Path("/{entity}/{recordId}")
    @Produces({ MediaType.APPLICATION_JSON })
    @SuppressWarnings("unchecked")
    public Response getActivitiesByEntityAndRecordId(
	    @PathParam("entity") String entity,
	    @PathParam("recordId") String recordId,
	    @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get items properties");
	ApplicationStatePojo authorizationState = ApplicationSecurityController
		.isAuthorized(req, Constants.Privileges.GET_ACTIVITY);
	if (authorizationState.getExceptionPojo() != null) {
	    return Response
		    .status(authorizationState.getExceptionPojo()
			    .getStatusCode())
		    .entity(authorizationState.getExceptionPojo()).build();
	}

	if (entity == null || recordId == null) {
	    return Response
		    .status(Constants.HTTP_CODE.NOT_FOUND.getCode())
		    .entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND,
			    null, "Bad request!")).build();
	}
	ArrayList<ActivityPojo> ActivityPojoList = new ArrayList<ActivityPojo>();
	ArrayList<Activity> activities = (ArrayList<Activity>) DataAccessObject
		.getActivitiesByEntityAndRecordId(entity.toUpperCase(),
			recordId);
	if (activities == null || activities.isEmpty()) {
	    authorizationState = null;
	    return Response
		    .status(Constants.HTTP_CODE.NOT_FOUND.getCode())
		    .entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND,
			    null, "No Activities found")).build();
	} else {
	    try {
		for (Activity activity : activities) {
		    ArrayList<CustomField> customFieldList = (ArrayList<CustomField>) DataAccessObject
			    .getCustomFiledByActivityAndType(activity,
				    "CUSTOM_FIELD");
		    ArrayList<CustomField> contextList = (ArrayList<CustomField>) DataAccessObject
			    .getCustomFiledByActivityAndType(activity,
				    "CONTEXT");
		    ActivityPojo activityPojo = new ActivityPojo(activity,
			    CustomFieldCollectionToMap(customFieldList),
			    CustomFieldCollectionToMap(contextList));
		    ActivityPojoList.add(activityPojo);
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    authorizationState = null;
	    return Response.status(200)
		    .entity(new ActivitiesPojo(ActivityPojoList)).build();
	}
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response addActivity(ActivityPojo jsonActivity,
	    @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Post called - Add Activity");
	ApplicationStatePojo authorizationState = ApplicationSecurityController
		.isAuthorized(req, Constants.Privileges.POST_ACTIVITY);
	if (authorizationState.getExceptionPojo() != null) {
	    return Response
		    .status(authorizationState.getExceptionPojo()
			    .getStatusCode())
		    .entity(authorizationState.getExceptionPojo()).build();
	}

	ActivityType type = DataAccessObject.getActivityTypeByName(jsonActivity
		.getType().toUpperCase());
	if (type == null) {
	    authorizationState = null;
	    return Response
		    .status(Constants.HTTP_CODE.NOT_FOUND.getCode())
		    .entity(new ExceptionPojo(
			    Constants.HTTP_CODE.NOT_FOUND,
			    null,
			    "The Activity type - "
				    + jsonActivity.getType()
				    + " was not found! - Hence the Activity could not be added"))
		    .build();
	}
	Activity newActivity = new Activity();
	newActivity.setActivityType(type);
	newActivity.setInitiator(Util.getInitiator(req));
	newActivity.setRecordId(jsonActivity.getRecordId());
	newActivity.setDescription(jsonActivity.getDescription());
	newActivity.setInitiatorIp(req.getRemoteHost());
	newActivity.setTargetEntity(jsonActivity.getEntity().toUpperCase());
	newActivity.setDeleted(false);
	if (jsonActivity.getTags() != null
		&& jsonActivity.getTags().length != 0) {
	    StringBuilder tagBuilder = new StringBuilder();
	    for (String tag : jsonActivity.getTags()) {
		tagBuilder.append(tag).append(":");
	    }
	    newActivity.setTags(tagBuilder.toString());
	}
	if (jsonActivity.getLocation() != null) {
	    newActivity.setLatitude(jsonActivity.getLocation().getLatitude());
	    newActivity.setLongitude(jsonActivity.getLocation().getLongitude());
	}
	if (jsonActivity.getTimestamp() == null) {
	    Timestamp currentTimeStamp = new Timestamp(new Date().getTime());
	    newActivity.setTimeStamp(currentTimeStamp);
	    jsonActivity.setTimestamp(currentTimeStamp);
	} else {
	    newActivity.setTimeStamp(jsonActivity.getTimestamp());
	}
	newActivity = (Activity) DataAccessObject.createNewRecord(newActivity);

	HashMap<String, String> contexts = jsonActivity.getContext();
	if (contexts != null && !contexts.isEmpty()) {
	    logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating contexts");
	    for (Map.Entry<String, String> jsonCF : contexts.entrySet()) {
		CustomField custFeild = new CustomField();
		custFeild.setActivity(newActivity);
		custFeild.setKey(jsonCF.getKey());
		custFeild.setValue(jsonCF.getValue());
		custFeild.setType("CONTEXT");
		DataAccessObject.createNewRecord(custFeild);
	    }
	    jsonActivity.setContext(contexts);
	}

	HashMap<String, String> customFields = jsonActivity.getCustomFields();
	if (customFields != null && !customFields.isEmpty()) {
	    logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating custom Fields");
	    for (Map.Entry<String, String> jsonCF : customFields.entrySet()) {
		CustomField custFeild = new CustomField();
		custFeild.setActivity(newActivity);
		custFeild.setKey(jsonCF.getKey());
		custFeild.setValue(jsonCF.getValue());
		custFeild.setType("CUSTOM_FIELD");
		DataAccessObject.createNewRecord(custFeild);
	    }
	    customFields.put("ipAddress", req.getRemoteHost());
	    customFields
		    .put("countryCode", req.getLocale().getDisplayCountry());
	    jsonActivity.setCustomFields(customFields);
	} else {
	    customFields = new HashMap<String, String>();
	    customFields.put("ipAddress", req.getRemoteHost());
	    customFields
		    .put("countryCode", req.getLocale().getDisplayCountry());
	    jsonActivity.setCustomFields(customFields);
	}

	jsonActivity.setId(newActivity.getId());
	jsonActivity.setUser(Util.getInitiator(req));
	jsonActivity.setEntity(newActivity.getTargetEntity());
	authorizationState = null;

	return Response.status(200).entity(jsonActivity).build();
    }

  
    
    @POST
    @Path("/type")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response addActivityType(ActivityTypePojo jsonActivityType,
	    @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Post called - Add Activity Type");
	ApplicationStatePojo authorizationState = ApplicationSecurityController
		.isAuthorized(req, Constants.Privileges.POST_ACTIVITY_TYPE);
	if (authorizationState.getExceptionPojo() != null) {
	    return Response
		    .status(authorizationState.getExceptionPojo()
			    .getStatusCode())
		    .entity(authorizationState.getExceptionPojo()).build();
	}
	ActivityType type = DataAccessObject
		.getActivityTypeByName(jsonActivityType.getName().toUpperCase());
	if (type != null) {
	    jsonActivityType.setId(type.getId());
	    authorizationState = null;
	    return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode())
		    .entity(jsonActivityType).build();
	}
	ActivityType newActivityType = new ActivityType();
	newActivityType.setName(jsonActivityType.getName().toUpperCase());
	newActivityType.setCode(jsonActivityType.getCode());

	if (jsonActivityType.getTags() != null
		&& jsonActivityType.getTags().length != 0) {
	    StringBuilder tagBuilder = new StringBuilder();
	    for (String tag : jsonActivityType.getTags()) {
		tagBuilder.append(tag).append(":");
	    }
	    newActivityType.setTags(tagBuilder.toString());
	}
	newActivityType = (ActivityType) DataAccessObject
		.createNewRecord(newActivityType);
	jsonActivityType.setId(newActivityType.getId());

	authorizationState = null;
	return Response.status(Constants.HTTP_CODE.OK.getCode())
		.entity(jsonActivityType).build();
    }
    @SuppressWarnings("unchecked")
    @PUT
    @Path("/{recordId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateActivity(@PathParam("recordId") Long recordId, ActivityPojo jsonActivity, @Context HttpServletRequest req) {
        logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Put called - Update Activity");
        ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.PUT_ACTIVITY);
        if (authorizationState.getExceptionPojo() != null) {
            return Response.status(authorizationState.getExceptionPojo().getStatusCode())
                    .entity(authorizationState.getExceptionPojo()).build();
        }

        Activity activity = (Activity) DataAccessObject.getRecordById(Activity.class, recordId);

        if (activity == null) {
            return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND,
                                    new String[]{"The activity with the given id was not found!"},
                                    "The Activity you wish to update does not exist!")).build();
        }

        if(jsonActivity.getType() != null && !jsonActivity.getType().equals("")){
            ActivityType type = DataAccessObject.getActivityTypeByName(jsonActivity.getType().toUpperCase());
            if (type == null) {
                authorizationState = null;
                return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo( Constants.HTTP_CODE.NOT_FOUND, null,
                                        "The Activity type - " + jsonActivity.getType()
                                        + " was not found! - Hence the Activity could not be updated")).build();
            }        
            activity.setActivityType(type);
        }
        activity.setInitiator(Util.getInitiator(req));
        activity.setDescription(jsonActivity.getDescription());
        
        if (jsonActivity.getTags() != null  && jsonActivity.getTags().length != 0) {
            StringBuilder tagBuilder = new StringBuilder();
            for (String tag : jsonActivity.getTags()) {
                tagBuilder.append(":").append(tag);
            }
            activity.setTags(tagBuilder.toString());
            tagBuilder.deleteCharAt(0);
        }
        if (jsonActivity.getLocation() != null) {
            activity.setLatitude((jsonActivity.getLocation().getLatitude() != null)?jsonActivity.getLocation().getLatitude() : activity.getLatitude());
            activity.setLongitude((jsonActivity.getLocation().getLongitude() != null)? jsonActivity.getLocation().getLongitude() : activity.getLongitude());
        }
        
        if (jsonActivity.getTimestamp() != null) {
            activity.setTimeStamp(jsonActivity.getTimestamp());
        }
        DataAccessObject.updateRecord(activity);

        

        HashMap<String, String> contexts = jsonActivity.getContext();
        if (contexts != null && !contexts.isEmpty()) {
            ArrayList<CustomField> contextList = (ArrayList<CustomField>) DataAccessObject.getCustomFiledByActivityAndType(activity, "CONTEXT");
            if(contextList != null && !contextList.isEmpty()){        	
                for(CustomField foundContext : contextList){
            	     DataAccessObject.deleteRecord(foundContext);
                }
            }            
            logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating contexts");
            for (Map.Entry<String, String> jsonCF : contexts.entrySet()) {
                CustomField custFeild = new CustomField();
                custFeild.setActivity(activity);
                custFeild.setKey(jsonCF.getKey());
                custFeild.setValue(jsonCF.getValue());
                custFeild.setType("CONTEXT");
                DataAccessObject.createNewRecord(custFeild);
            }
            jsonActivity.setContext(contexts);
        }

        HashMap<String, String> customFields = jsonActivity.getCustomFields();
        if (customFields != null && !customFields.isEmpty()) {
            ArrayList<CustomField> customFieldList = (ArrayList<CustomField>) DataAccessObject.getCustomFiledByActivityAndType(activity, "CUSTOM_FIELD");
            if(customFieldList != null && !customFieldList.isEmpty()){        	
                for(CustomField foundContext : customFieldList){
            	     DataAccessObject.deleteRecord(foundContext);
                }
            }   
            
            logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating custom Fields");
            for (Map.Entry<String, String> jsonCF : customFields.entrySet()) {
                CustomField custFeild = new CustomField();
                custFeild.setActivity(activity);
                custFeild.setKey(jsonCF.getKey());
                custFeild.setValue(jsonCF.getValue());
                custFeild.setType("CUSTOM_FIELD");
                DataAccessObject.createNewRecord(custFeild);
            }
            customFields.put("ipAddress", req.getRemoteHost());
            customFields.put("countryCode", req.getLocale().getDisplayCountry());
            jsonActivity.setCustomFields(customFields);
        } 

        jsonActivity.setId(activity.getId());
        jsonActivity.setUser(Util.getInitiator(req));
        jsonActivity.setEntity(activity.getTargetEntity());
        authorizationState = null;
        return Response.status(200).entity(jsonActivity).build();
    }
    
  
    
    
    @DELETE
    @Path("/{recordId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response deleteActivity(@PathParam("recordId") Long recordId, @Context HttpServletRequest req) {
        logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Delete called - Delete Activity");
        ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.DELETE_ACTIVITY);
        if (authorizationState.getExceptionPojo() != null) {
            return Response.status(authorizationState.getExceptionPojo().getStatusCode())
                    .entity(authorizationState.getExceptionPojo()).build();
        }

        Activity activity = (Activity) DataAccessObject.getRecordById(Activity.class, recordId);

        if (activity == null) {
            return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND,
                                    new String[]{"The activity with the given id was not found!"},
                                    "The Activity you wish to update does not exist!")).build();
        }

        activity.setDeleted(true);
        DataAccessObject.updateRecord(activity);
        return Response.status(Constants.HTTP_CODE.ACCEPTED.getCode()).build();
    }

    
    // @GET
    // @Path("/type")
    // @Produces({ MediaType.APPLICATION_JSON})
    // public Response getActivityType( @Context HttpServletRequest req){
    // logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Post called - Get Activity Type"
    // );
    // ApplicationStatePojo authorizationState =
    // AccessControlObject.isAuthorized(req,
    // Constants.Privileges.GET_ACTIVITY_TYPE);
    // if(authorizationState.getExceptionPojo() != null){
    // return
    // Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
    // }
    // ArrayList<ActivityType> types = (ArrayList<ActivityType>)
    // DataAccessObject.getAllRecords(ActivityType.class);
    // if(types == null || types.isEmpty()){
    // return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).build();
    // }
    //
    // if (jsonActivityType.getTags() != null &&
    // jsonActivityType.getTags().length != 0) {
    // StringBuilder tagBuilder = new StringBuilder();
    // for (String tag : jsonActivityType.getTags()) {
    // tagBuilder.append(tag).append(":");
    // }
    // newActivityType.setTags(tagBuilder.toString());
    // }
    // newActivityType =
    // (ActivityType)DataAccessObject.createNewRecord(newActivityType);
    // jsonActivityType.setId(newActivityType.getId());
    //
    // authorizationState = null;
    // return
    // Response.status(Constants.HTTP_CODE.OK.getCode()).entity(jsonActivityType).build();
    // }
}
