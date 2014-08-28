/**
 * 
 */
package uk.ac.dotrural.smileserver.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.dotrural.smile.entity.item.Item;
import uk.ac.dotrural.smile.entity.location.Location;
import uk.ac.dotrural.smile.entity.product.Product;
import uk.ac.dotrural.smile.entity.property.Property;
import uk.ac.dotrural.smileserver.common.ApplicationSecurityController;
import uk.ac.dotrural.smileserver.common.Constants;
import uk.ac.dotrural.smileserver.common.Util;
import uk.ac.dotrural.smileserver.dao.DataAccessObject;
import uk.ac.dotrural.smileserver.pojo.*;

/**
 * @author Charles Ofoegbu
 *
 */

@Path("/item")
@Produces({ MediaType.APPLICATION_JSON})
public class ItemService {

    private static Logger logger = LoggerFactory.getLogger(ItemService.class);
    
    @GET
    @Produces({ MediaType.APPLICATION_JSON})     
    @SuppressWarnings("unchecked")
    public Response getItems( @QueryParam("start") Integer start,  @QueryParam("size") Integer chunkSize, @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get all items" );	
	
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.GET_ITEMS);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	
	Response response = null;
	ItemsPojo itemsPojo = new ItemsPojo();
	try{
	    Collection<Item> items = (start != null && chunkSize != null) ? (Collection<Item>) DataAccessObject.getAllItemByBusinessId(authorizationState.getCurrentBusiness().getId(), start, chunkSize) : (Collection<Item>) DataAccessObject.getAllItemByBusinessId(authorizationState.getCurrentBusiness().getId());
//	    Collection<Item> items = (Collection<Item>) DataAccessObject.getAllItemByBusinessId(authorizationState.getCurrentBusiness().getId()); //getAllRecords(Item.class);
	    for (Item item : items) {
		Location location = (Location) DataAccessObject.getLastKnownLocationForItem(item);
		HashMap<String, String> proppertiesPojo = new HashMap<String, String>();
		Collection<Property> properties = (Collection<Property>) DataAccessObject.getItemPropertiesByItemId(item.getId());
		if (properties != null && !properties.isEmpty()) {
		    for (Property property : properties) {
			proppertiesPojo.put(property.getKey(), property.getValue());
		    }
		}
		ItemPojo itemPojo = new ItemPojo(item, new LocationPojo(location), proppertiesPojo);
		itemsPojo.getItems().add(itemPojo);
	    }
	    response = Response.status(200).entity(itemsPojo).build();
	}catch(Exception e){
	    e.printStackTrace();
	}
	authorizationState = null;
	return response; 
    }
    
    @GET
    @Path("/count")
    @Produces({ MediaType.APPLICATION_JSON})
    public Response getItemsCount(@Context HttpServletRequest req, @Context HttpServletResponse res) throws IOException {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get items count" );	
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.GET_ITEMS_COUNT);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	
	Response response = null;
	try{
	    Long itemsCount = (Long) DataAccessObject.getItemCountByBusinessId(authorizationState.getCurrentBusiness().getId()); //getAllRecords(Item.class);
	    response = Response.status(200).entity(itemsCount).build();
	}catch(Exception e){
	    e.printStackTrace();
	}   
	authorizationState = null;
	return 	response;
	
    }
    

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    @SuppressWarnings("unchecked")
    public Response getItemById(@PathParam("id") Long id, @Context HttpServletRequest req, @Context HttpServletResponse res) throws IOException {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get item by id" );	
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.GET_ITEM);
	if(authorizationState.getExceptionPojo() != null){
	    String[] errors = authorizationState.getExceptionPojo().getErrors();
	    if (errors[0].equalsIgnoreCase("Unknown application.")){
		res.sendRedirect("http://smile.abdn.ac.uk/smile/item-web.php?itmid="+id);
	    }	    
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	
	Item item = (Item)DataAccessObject.getRecordById(Item.class, id);
	if(item == null){
	    return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "item with id = "+id+" was not found!")).build();
	}else{
	    Collection<Property> properties = (Collection<Property>) DataAccessObject.getItemPropertiesByItemId(item.getId());
	    Location location = (Location) DataAccessObject.getLastKnownLocationForItem(item);
	    HashMap<String, String> proppertiesPojo = new HashMap<String, String>();
	    if(properties != null && !properties.isEmpty()){
		for (Property property : properties) {
		    proppertiesPojo.put(property.getKey(), property.getValue());
		}
	    }
	    ItemPojo2 itemPojo = new ItemPojo2(item, new LocationPojo(location),  proppertiesPojo);
	    authorizationState = null;
	    return Response.status(200).entity(itemPojo).build();	    
	}
    }
    

    
    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response createItem(ItemPojo jsonItem, @Context HttpServletRequest req, @Context HttpServletResponse res) throws IOException{
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Post called - create item " );
	
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.POST_ITEM);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	URI uri = null;
	Item item = new Item();
	try{
	    Product product = DataAccessObject.getProductByName(jsonItem.getProduct());
	    if (product == null) {
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Product is null; hence creating a new product");
		product = new Product();
		product.setFunctionalName(jsonItem.getProduct());
		product.setCreatedAt(new Timestamp(new Date().getTime()));
		product.setBusiness(authorizationState.getCurrentBusiness());
		product = (Product) DataAccessObject.createNewRecord(product);
	    }
	    
	    item.setProduct(product);
	    item.setCreatedAt(new Timestamp(new Date().getTime()));
	    item.setDescription(jsonItem.getDescription());
	    item.setName(jsonItem.getName());
	    item.setBusiness(authorizationState.getCurrentBusiness());
	    
	    if (jsonItem.getTags() != null && jsonItem.getTags().length != 0) {
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating tags");
		StringBuilder tagBuilder = new StringBuilder();
		for (String tag : jsonItem.getTags()) {
		    tagBuilder.append(":").append(tag);
		}
		tagBuilder.deleteCharAt(0);
		item.setTags(tagBuilder.toString());
	    }
	    item = (Item) DataAccessObject.createNewRecord(item);
	    Util.createActivity(req, Constants.Actions.CREATE.name(), Constants.Entities.ITEM.name(), item.getId().toString(),  "Created an item");
	    Location location = new Location();
	    location.setLatitude(jsonItem.getLocation().getLatitude());
	    location.setLongitude(jsonItem.getLocation().getLongitude());
	    location.setTimeStamp(new Timestamp(new Date().getTime()));
	    location.setItem(item);
	    
	    DataAccessObject.createNewRecord(location);
	    HashMap<String, String> properties = jsonItem.getProperties();
	    if (properties != null && !properties.isEmpty()) {
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating properties");
		for (Map.Entry<String, String> jsonProperty : properties.entrySet()) {
		    logger.debug("Key = " + jsonProperty.getKey()  + ", Value = " + jsonProperty.getValue());
		    Property property = new Property();
		    property.setItem(item);
		    property.setKey(jsonProperty.getKey());
		    property.setValue(jsonProperty.getValue());
		    DataAccessObject.createNewRecord(property);
		}
	    }
	    try {
		uri = new URI(Util.getContextPath(req) + "/" + Constants.APP_NAME + "/" + Constants.APP_VERSION +  "/item/"+item.getId());
	    } catch (URISyntaxException e) {
		e.printStackTrace();
	    }
	}catch(Exception e ){
	    e.printStackTrace();
	}
	Response re = getItemById(item.getId(), req, res);
	authorizationState = null;
	return Response.status(201).entity(re.getEntity()).location(uri).build();
    }
        
    @PUT 
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response updateItem(@PathParam("id") Long id, ItemPojo jsonItem, @Context HttpServletRequest req, @Context HttpServletResponse res){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Put called - update item" );
	
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.PUT_ITEM);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	
	URI uri = null;
	Item item = null;
	Response response = null;
	try{
	    item = (Item)DataAccessObject.getRecordById(Item.class, id);
	    if(item == null){
		authorizationState = null;
		return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "item with id = "+id+" was not found!")).build();		
	    }

		Product product = DataAccessObject.getProductByName(jsonItem.getProduct());
		if (product == null) {
		    logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Product is null; hence creating a new product");
		    product = new Product();
		    product.setFunctionalName(jsonItem.getProduct());
		    product.setCreatedAt(new Timestamp(new Date().getTime()));
		    product = (Product) DataAccessObject.createNewRecord(product);
		}   
		item.setName(jsonItem.getName());
		item.setDescription(jsonItem.getDescription());
		item.setProduct(product);
		item.setUpdatedAt(new Timestamp(new Date().getTime()));
		if (jsonItem.getTags() != null	&& jsonItem.getTags().length != 0) {
		    logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating tags");
		    StringBuilder tagBuilder = new StringBuilder();
		    for (String tag : jsonItem.getTags()) {
			tagBuilder.append(":").append(tag);
		    }
		    tagBuilder.deleteCharAt(0);
		    item.setTags(tagBuilder.toString());
		}
		DataAccessObject.updateRecord(item);
		Util.createActivity(req, Constants.Actions.UPDATE.name(), Constants.Entities.ITEM.name(), item.getId().toString(),  "Updated an item");	    
		try {
			uri = new URI(Util.getContextPath(req)  + "/" + Constants.APP_NAME + "/" + Constants.APP_VERSION +  "/item/"+id);
		    } catch (URISyntaxException e) {
			e.printStackTrace();
		    }
		Response re = getItemById(id, req, res);
		response = Response.status(200).entity(re.getEntity()).location(uri).build();
	    
	}catch(Exception e ){
	    e.printStackTrace();
	}
	authorizationState = null;
	return response;
    }    
    
    @DELETE 
    @Path("/{id}")    
    @Produces({ MediaType.APPLICATION_JSON})  
    @SuppressWarnings("unchecked")
    public Response deleteItem(@PathParam("id") Long id, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> DELETE called - delete item" );
	
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.DELETE_ITEM);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	
	Item item = null;
	Response response = null;
	try{
	    item = (Item)DataAccessObject.getRecordById(Item.class, id);
	    if(item == null){
		authorizationState = null;
		return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "item with id = "+id+" was not found!")).build();		
	    }else{
		Collection<Property> properties = (Collection<Property>) DataAccessObject.getItemPropertiesByItemId(item.getId());
		Collection<Location> locations = (Collection<Location>) DataAccessObject.getAllLocationsByItemId(item.getId());
		if (properties != null && !properties.isEmpty()) {
		    for (Property property : properties) {
			DataAccessObject.deleteRecord(property);
		    }
		}
		if (locations != null && !locations.isEmpty()) {
		    for (Location location : locations) {
			DataAccessObject.deleteRecord(location);
		    }
		}		
		Util.createActivity(req, Constants.Actions.DELETE.name(), Constants.Entities.ITEM.name(), item.getId().toString(),  "Deleted an item");	
		DataAccessObject.deleteRecord(item);	
		response = Response.status(200).build();
	    }
	}catch(Exception e ){
	    e.printStackTrace();
	}
	authorizationState = null;
	return response;
    }
   
}
