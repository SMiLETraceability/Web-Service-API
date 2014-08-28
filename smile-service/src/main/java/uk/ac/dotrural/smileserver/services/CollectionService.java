/**
 * 
 */
package uk.ac.dotrural.smileserver.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.dotrural.smile.entity.itemcollection.ItemCollection; 
import uk.ac.dotrural.smile.entity.property.Property;
import uk.ac.dotrural.smileserver.common.ApplicationSecurityController;
import uk.ac.dotrural.smileserver.common.Constants;
import uk.ac.dotrural.smileserver.common.Util;
import uk.ac.dotrural.smileserver.dao.DataAccessObject;
import uk.ac.dotrural.smileserver.pojo.ExceptionPojo;
import uk.ac.dotrural.smileserver.pojo.CollectionPojo;
import uk.ac.dotrural.smileserver.pojo.CollectionsPojo;
import uk.ac.dotrural.smileserver.pojo.ApplicationStatePojo;
import uk.ac.dotrural.smile.entity.item.Item;

/**
 * @author Charles Ofoegbu
 *
 */


@Path("/collections")
@Produces({ MediaType.APPLICATION_JSON})
public class CollectionService {
    
    Logger logger = LoggerFactory.getLogger(CollectionService.class);

    @GET
    @Produces({ MediaType.APPLICATION_JSON})     
    @SuppressWarnings("unchecked")
    public Response getCollections(@QueryParam("start") Integer start,  @QueryParam("size") Integer chunkSize, @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get all collections" );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.GET_COLLECTIONS);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	Response response = null;
	ArrayList<CollectionPojo> collectionPojoList = new ArrayList<CollectionPojo>(); 
	try{
	    Collection<ItemCollection> collections = (start != null && chunkSize != null) ? (Collection<ItemCollection>) DataAccessObject.getAllItemCollectionByBusinessId(authorizationState.getCurrentBusiness().getId(), start, chunkSize) : (Collection<ItemCollection>) DataAccessObject.getAllItemCollectionByBusinessId(authorizationState.getCurrentBusiness().getId());
	    for (ItemCollection collection : collections) {
		HashMap<String, String> proppertiesPojo = new HashMap<String, String>();
		Collection<Property> properties = (Collection<Property>) DataAccessObject.getCollectionPropertiesByCollectionId(collection.getId());
		if (properties != null && !properties.isEmpty()) {
		    for (Property property : properties) {
			proppertiesPojo.put(property.getKey(), property.getValue());
		    }
		}
		CollectionPojo collectionPojo = new CollectionPojo(collection, proppertiesPojo);
		collectionPojoList.add(collectionPojo);
	    }
	    response = Response.status(200).entity(new CollectionsPojo(collectionPojoList)).build();
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
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get itemCollections count" );
	
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.GET_ITEMS_COLLECTION_COUNT);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	
	Response response = null;
	try{
	    Long itemCollectionsCount = (Long) DataAccessObject.getItemCollectionCountByBusinessId(authorizationState.getCurrentBusiness().getId()); //getAllRecords(Item.class);
	    response = Response.status(200).entity(itemCollectionsCount).build();
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
    public Response getCollectionById(@PathParam("id") Long id, @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get collection by id" );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.GET_COLLECTION);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	ItemCollection collection = (ItemCollection)DataAccessObject.getRecordById(ItemCollection.class, id);
	if(collection == null){
	    return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "collection with id = "+id+" was not found!")).build();
	}else{
	    Collection<Property> properties = (Collection<Property>) DataAccessObject.getCollectionPropertiesByCollectionId(collection.getId());
	    HashMap<String, String> proppertiesPojo = new HashMap<String, String>();
	    if(properties != null && !properties.isEmpty()){
		for (Property property : properties) {
		    proppertiesPojo.put(property.getKey(), property.getValue());
		}
	    }
	    CollectionPojo collectionPojo = new CollectionPojo(collection, proppertiesPojo);
	    authorizationState = null;
	    return Response.status(200).entity(collectionPojo).build();	    
	}
    }    
    
    @GET
    @Path("/{id}/items")
    @Produces({ MediaType.APPLICATION_JSON})
    @SuppressWarnings("unchecked")
    public Response getCollectionItemsByCollectionId(@PathParam("id") Long id, @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get a collection's items by id");
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.GET_COLLECTION_ITEMS);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	Collection<Item> items = (Collection<Item>) DataAccessObject.getCollectionsItemsByCollectionId(id);
	if (items != null && !items.isEmpty()) {
	    Long[] itemNames = new Long[items.size()];
	    int count = 0;
	    for (Item item : items) {		
		itemNames[count++] = item.getId();
	    }
	    return Response.status(200).entity(itemNames).build();
	} else {
	    authorizationState = null;
	    return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "No items found for collection with id = "+id+" was not found!")).build();
	}
    }
    
    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response createCollection(CollectionPojo jsonItemCollection, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Post called - create collection " );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.POST_COLLECTION);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	URI uri = null;
	ItemCollection collection = new ItemCollection();
	try{
	    collection.setCreatedAt(new Timestamp(new Date().getTime()));
	    collection.setDescription(jsonItemCollection.getDescription());
	    collection.setName(jsonItemCollection.getName());
	    collection.setBusiness(authorizationState.getCurrentBusiness());
	    if (jsonItemCollection.getTags() != null && jsonItemCollection.getTags().length != 0) {
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating tags");
		StringBuilder tagBuilder = new StringBuilder();
		for (String tag : jsonItemCollection.getTags()) {
		    tagBuilder.append(tag).append(":");
		}
		collection.setTags(tagBuilder.toString());
	    }
	    collection = (ItemCollection) DataAccessObject.createNewRecord(collection);
	    Util.createActivity(req, Constants.Actions.CREATE.name(), Constants.Entities.COLLECTION.name(), collection.getId().toString(),  "Created a collection");	
	    
	    HashMap<String, String> properties = jsonItemCollection.getProperties();
	    if (properties != null && !properties.isEmpty()) {
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating properties");
		for (Map.Entry<String, String> jsonProperty : properties.entrySet()) {
		    logger.debug("Key = " + jsonProperty.getKey()  + ", Value = " + jsonProperty.getValue());
		    Property property = new Property();
		    property.setItemCollection(collection);
		    property.setKey(jsonProperty.getKey());
		    property.setValue(jsonProperty.getValue());
		    DataAccessObject.createNewRecord(property);
		}
	    }
	    try {
		uri = new URI(Util.getContextPath(req)  + "/" + Constants.APP_NAME + "/" + Constants.APP_VERSION + "/collection/"+collection.getId());
	    } catch (URISyntaxException e) {
		e.printStackTrace();
	    }
	}catch(Exception e ){
	    e.printStackTrace();
	}
	Response re = getCollectionById(collection.getId(), req);
	authorizationState = null;
	return Response.status(201).entity(re.getEntity()).location(uri).build();
    }
        
    @PUT 
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response updateCollection(@PathParam("id") Long id, CollectionPojo jsonItemCollection, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Put called - update collection" );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.PUT_COLLECTION);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	URI uri = null;
	ItemCollection collection = null;
	Response response = null;
	try{
	    collection = (ItemCollection)DataAccessObject.getRecordById(ItemCollection.class, id);
	    if(collection == null){
		authorizationState = null;
		return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "collection with id = "+id+" was not found!")).build();		
	    }else{
		collection.setName(jsonItemCollection.getName());
		collection.setDescription(jsonItemCollection.getDescription());
		collection.setUpdatedAt(new Timestamp(new Date().getTime()));
		if (jsonItemCollection.getTags() != null	&& jsonItemCollection.getTags().length != 0) {
		    logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating tags");
		    StringBuilder tagBuilder = new StringBuilder();
		    for (String tag : jsonItemCollection.getTags()) {
			tagBuilder.append(tag).append(":");
		    }
		    collection.setTags(tagBuilder.toString());
		}
		DataAccessObject.updateRecord(collection);
		Util.createActivity(req, Constants.Actions.UPDATE.name(), Constants.Entities.COLLECTION.name(), collection.getId().toString(),  "Updated a collection");		    
		try {
			uri = new URI(Util.getContextPath(req)  + "/" + Constants.APP_NAME + "/" + Constants.APP_VERSION + "/collection/"+id);
		    } catch (URISyntaxException e) {
			e.printStackTrace();
		    }
		Response re = getCollectionById(id, req);
		response = Response.status(200).entity(re.getEntity()).location(uri).build();
	    }
	}catch(Exception e ){
	    e.printStackTrace();
	}
	authorizationState = null;
	return response;
    }    
       
    @PUT
    @Path("/{id}/items")
    @Produces({ MediaType.APPLICATION_JSON})
    public Response addItemsToCollection(@PathParam("id") Long id, Long[] jsonItemIds, @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get a collection's items by id");
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.PUT_COLLECTION_ITEMS);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	ArrayList<String> itemsAdded = new ArrayList<String>();
	StringBuilder itemsAddedBuilder = new StringBuilder();
	ItemCollection itemCollection = (ItemCollection) DataAccessObject.getRecordById(ItemCollection.class, id);
	if (itemCollection == null) {
	    authorizationState = null;
	    return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "collection with id = " + id + " was not found!")).build();
	} else {
	    for (Long itemId : jsonItemIds) {
		Item item = (Item) DataAccessObject.getRecordById(Item.class, itemId);
		if (item != null) {
		    try {
			item.setItemCollection(itemCollection);
			DataAccessObject.updateRecord(item);
			itemsAdded.add(itemId.toString());
			itemsAddedBuilder.append(itemId.toString()).append(",");
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
		Util.createActivity(req, Constants.Actions.UPDATE.name(), Constants.Entities.COLLECTION.name(), itemCollection.getId().toString(),  "Added items to this collection"+itemsAddedBuilder.toString());
	    }
	    authorizationState = null;	    
	    return Response.status(200).entity(itemsAdded.toArray()).build();
	}
    }
    
    @DELETE 
    @Path("/{id}")    
    @Produces({ MediaType.APPLICATION_JSON})  
    @SuppressWarnings("unchecked")
    public Response deleteCollection(@PathParam("id") Long id, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> DELETE called - delete collection" );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.DELETE_COLLECTION);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	ItemCollection collection = null;
	Response response = null;
	try{
	    collection = (ItemCollection)DataAccessObject.getRecordById(ItemCollection.class, id);
	    if(collection == null){
		return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "collection with id = "+id+" was not found!")).build();		
	    }else{
		Collection<Property> properties = (Collection<Property>) DataAccessObject.getCollectionPropertiesByCollectionId(collection.getId());
		if (properties != null && !properties.isEmpty()) {
		    for (Property property : properties) {
			DataAccessObject.deleteRecord(property);
		    }
		}	
		DataAccessObject.deleteRecord(collection);
		Util.createActivity(req, Constants.Actions.DELETE.name(), Constants.Entities.COLLECTION.name(), collection.getId().toString(),  "Deleted a collection");		
		response = Response.status(200).build();
	    }
	}catch(Exception e ){
	    e.printStackTrace();
	}
	authorizationState = null;
	return response;
    }
    
    @DELETE
    @Path("/{id}/items")
    @Produces({ MediaType.APPLICATION_JSON})
    @SuppressWarnings("unchecked")
    public Response deleteCollectionsItems(@PathParam("id") Long id, @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> DELETE called - delete a collection's items");
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.DELETE_COLLECTION_ITEMS);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	StringBuilder itemsAddedBuilder = new StringBuilder();
	Collection<Item> items = (Collection<Item>) DataAccessObject.getCollectionsItemsByCollectionId(id);
	if (items != null && !items.isEmpty()) {
	    for (Item item : items) {		
		itemsAddedBuilder.append(item.getId().toString()).append(",");
		item.setItemCollection(null);
		DataAccessObject.updateRecord(item);
	    }
	    Util.createActivity(req, Constants.Actions.UPDATE.name(), Constants.Entities.COLLECTION.name(), id.toString(),  "Removed the following items : "+itemsAddedBuilder.toString()+" from the collection : "+id);
	    return Response.status(200).build();
	} else {
	    authorizationState = null;
	    return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "No items found for collection with id = "+id+" was not found!")).build();
	}
    }
    
    @DELETE
    @Path("/{id}/items/{itemId}")
    @Produces({ MediaType.APPLICATION_JSON})
    public Response deleteCollectionsItem(@PathParam("id") Long id, @PathParam("itemId") Long itemId, @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> DELETE called - delete a collection's item");
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.DELETE_COLLECTION_ITEM);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	Item item = DataAccessObject.getItemByItemCollectionIdAndId(id, itemId);	
	if (item == null) {
	    return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "Item with id = " + itemId + " and Collection id = " + id + " was not found!")).build();
	} else {
	    item.setItemCollection(null);
	    DataAccessObject.updateRecord(item);Util.createActivity(req, Constants.Actions.UPDATE.name(), Constants.Entities.COLLECTION.name(), id.toString(),  "Removed the following item : "+item.getId().toString()+" from the collection : "+id);
	    authorizationState = null;
	    return Response.status(200).build();
	}	
    }
    
}
