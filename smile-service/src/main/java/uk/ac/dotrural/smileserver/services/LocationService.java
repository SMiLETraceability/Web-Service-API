/**
 * 
 */
package uk.ac.dotrural.smileserver.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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

import uk.ac.dotrural.smile.entity.item.Item;
import uk.ac.dotrural.smile.entity.location.Location;
import uk.ac.dotrural.smileserver.common.ApplicationSecurityController;
import uk.ac.dotrural.smileserver.common.Constants;
import uk.ac.dotrural.smileserver.dao.DataAccessObject;
import uk.ac.dotrural.smileserver.pojo.ExceptionPojo;
import uk.ac.dotrural.smileserver.pojo.LocationPojo;
import uk.ac.dotrural.smileserver.pojo.LocationsPojo;
import uk.ac.dotrural.smileserver.pojo.ApplicationStatePojo;

/**
 * @author Charles Ofoegbu
 *
 */
@Path("/item/{itemId}/location")
public class LocationService {
    
    private static Logger logger = LoggerFactory.getLogger(LocationService.class);
    
    @GET
    @Produces({ MediaType.APPLICATION_JSON})    
    @SuppressWarnings("unchecked")
    public Response getLocations(@PathParam("itemId") Long itemId, @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get items properties");
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.GET_LOCATION);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	Item item = (Item) DataAccessObject.getRecordById(Item.class, itemId);
	ArrayList<LocationPojo> locationPojoList = new ArrayList<LocationPojo>();
	if (item == null) {
	    authorizationState = null;
	    return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "The Item with id = " + itemId + " was not found! - Hence the location(s) could not be retrived")).build();
	} else {
	    try {
		Collection<Location> locations = (Collection<Location>) DataAccessObject.getAllLocationByItemId(itemId);// properties.getProperties();
		for (Location location : locations) {
		    LocationPojo locationPojo = new LocationPojo(location);
		    locationPojoList.add(locationPojo);
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    authorizationState = null;
	    return Response.status(200).entity(new LocationsPojo(locationPojoList)).build();
	}
    }

    
    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response addLocation(@PathParam("itemId") Long itemId, LocationsPojo locationsPojo, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Post called - create Location" );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.POST_LOCATION);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	authorizationState = null;
	return addOrUpdateLocation(itemId, locationsPojo, req);
    }
    
     
    @PUT 
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response updateLocation(@PathParam("itemId") Long itemId, LocationsPojo locationsPojo, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Put called - update Location" );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.PUT_LOCATION);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	authorizationState = null;
	return addOrUpdateLocation(itemId, locationsPojo, req);
    }
     
    public Response addOrUpdateLocation(Long itemId, LocationsPojo locationsPojo, HttpServletRequest req) {
	Item item = (Item) DataAccessObject.getRecordById(Item.class, itemId);
	if (item == null) {
	    return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "The Item with id = " + itemId + " was not found! - Hence the Location(s) could not be added/updated")).build();
	} else {
	    try {
		Collection<LocationPojo> locations = locationsPojo.getLocations();
		for (LocationPojo locationPojo : locations) {
		    Location location = DataAccessObject.getLocationByItemIdAndId(itemId, locationPojo.getId());
		    if (location != null) {
			location.setLatitude(locationPojo.getLatitude());
			location.setLongitude(locationPojo.getLongitude());
			location.setTimeStamp(new Timestamp(new Date().getTime()));
			DataAccessObject.updateRecord(location);
		    } else {
			location = new Location();
			location.setItem(item);
			location.setLatitude(locationPojo.getLatitude());
			location.setLongitude(locationPojo.getLongitude());
			location.setTimeStamp(new Timestamp(new Date().getTime()));
			DataAccessObject.createNewRecord(location); 
		    }
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    return getLocations(itemId, req);
	}

    }
    
      
    @DELETE 
    @Produces({ MediaType.APPLICATION_JSON}) 
    @SuppressWarnings("unchecked")
    public Response deleteLocation(@PathParam("itemId") Long itemId, @PathParam("to") Timestamp to, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> DELETE called - delete Location");	   
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.DELETE_LOCATION);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	Collection<Location> locations = null;
	if(to != null){
	    locations = (Collection<Location>) DataAccessObject.getAllLocationByTimeStampItemId(itemId, to);
	}else{
	    locations = (Collection<Location>) DataAccessObject.getAllLocationByItemId(itemId);
	}
	
	if (locations == null || locations.isEmpty()) {
	    return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "Locations for item with id = " + itemId + " was not found!")).build();
	} else {
	    for(Location location : locations){
		DataAccessObject.deleteRecord(location);		
	    }
	    authorizationState = null;
	    return Response.status(200).build();
	}

    }
}
