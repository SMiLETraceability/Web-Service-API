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
import uk.ac.dotrural.smile.entity.property.Property;
import uk.ac.dotrural.smileserver.common.ApplicationSecurityController;
import uk.ac.dotrural.smileserver.common.Constants;
import uk.ac.dotrural.smileserver.dao.DataAccessObject;
import uk.ac.dotrural.smileserver.pojo.ExceptionPojo;
import uk.ac.dotrural.smileserver.pojo.PropertiesPojo;
import uk.ac.dotrural.smileserver.pojo.PropertyPojo;
import uk.ac.dotrural.smileserver.pojo.ApplicationStatePojo;

/**
 * @author Charles Ofoegbu
 *
 */

@Path("/item/{itemId}/properties")
@Produces({ MediaType.APPLICATION_JSON})
public class PropertiesService {    
    
    private static Logger logger = LoggerFactory.getLogger(PropertiesService.class);
  
    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})    
    public Response addProperty(@PathParam("itemId") Long itemId, PropertiesPojo propertiesPojo, @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> POST called - add Property");
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.POST_PROPERTY);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	authorizationState = null;	
	return addOrUpdateProperty(itemId, propertiesPojo, req);
    }
    
    @PUT
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})    
    public Response updateProperty(@PathParam("itemId") Long itemId, PropertiesPojo propertiesPojo, @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> PUT called - update Property");
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.PUT_PROPERTY);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	authorizationState = null;	
	return addOrUpdateProperty(itemId, propertiesPojo, req);
    }
    
    public Response addOrUpdateProperty(Long itemId, PropertiesPojo propertiesPojo, @Context HttpServletRequest req){
	Response response = null;
	Item item = null;
	try {
	    item = (Item) DataAccessObject.getRecordById(Item.class, itemId);
	    if(item == null){
		return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "The Item with id = "+itemId+" was not found! - Hence the property/properties could not be added/updated")).build();	
	    }else{
		Collection<PropertyPojo> properties = propertiesPojo.getProperties();
		for(PropertyPojo propertyPojo : properties){
		    Property property = DataAccessObject.getPropertyByItemIdAndKey(itemId, propertyPojo.getKey());
		    if(property != null){
			property.setValue(propertyPojo.getValue());
			property.setTimeStamp(new Timestamp(new Date().getTime()));
			DataAccessObject.updateRecord(property);
		    }else{
			property = new Property();
			property.setItem(item);
			property.setKey(propertyPojo.getKey());
			property.setValue(propertyPojo.getValue());
			property.setTimeStamp(new Timestamp(new Date().getTime()));
			DataAccessObject.createNewRecord(property);
		    }
		}
		response = getProperties(itemId, req);		
	    }	    
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return response;
    }
    
    @GET
    @Produces({ MediaType.APPLICATION_JSON}) 
    @SuppressWarnings("unchecked")
    public Response getProperties(@PathParam("itemId") Long itemId, @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get items properties");
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.GET_PROPERTIES);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	Response response = null;
	Item item = null;
	try{
	    item = (Item) DataAccessObject.getRecordById(Item.class, itemId);
	    if(item == null){
		authorizationState = null;
		return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "The Item with id = "+itemId+" was not found! - Hence the property/properties could not be added/updated")).build();	
	    }else{
		Collection<Property> properties = (Collection<Property>)DataAccessObject.getItemPropertiesByItemId(itemId);// properties.getProperties();
		ArrayList<PropertyPojo> propertiesPojo = new ArrayList<PropertyPojo>();
		for(Property property : properties){
		    PropertyPojo propertyPojo = new PropertyPojo(property);
		    propertiesPojo.add(propertyPojo);
		}
		response = Response.status(200).entity(new PropertiesPojo(propertiesPojo)).build();
	    }
	}catch(Exception e){
	    e.printStackTrace();
	}
	authorizationState = null;
	return response;
    }
    
    @GET
    @Path("/{key}")
    @Produces({ MediaType.APPLICATION_JSON})
    public Response getPropertyById(@PathParam("itemId") Long itemId, @PathParam("key") String key, @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get item by id" );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.GET_PROPERTY);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	Property property = (Property)DataAccessObject.getPropertyByItemIdAndKey(itemId, key);
	if(property == null){
	    authorizationState = null;
	    return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "Property with key = "+key+" and itemId = "+itemId+" was not found!")).build();
	}else{	  
	    PropertyPojo propertyPojo = new PropertyPojo(property);  
	    authorizationState = null;
	    return Response.status(200).entity(propertyPojo).build();	    
	}
    }

      
    @DELETE 
    @Path("/{key}")    
    @Produces({ MediaType.APPLICATION_JSON}) 
    public Response deleteItem(@PathParam("itemId") Long itemId, @PathParam("key") String key, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> DELETE called - delete item");
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.DELETE_PROPERTY);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	Property property = (Property) DataAccessObject.getPropertyByItemIdAndKey(itemId, key);
	if (property == null) {
	    authorizationState = null;
	    return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "Property with key = " + key + " and itemId = " + itemId + " was not found!")).build();
	} else {
	    DataAccessObject.deleteRecord(property);
	    authorizationState = null;
	    return Response.status(200).build();
	}

    }

}
