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

import uk.ac.dotrural.smile.entity.product.Product;
import uk.ac.dotrural.smile.entity.identifier.Identifier;
import uk.ac.dotrural.smile.entity.property.Property;
import uk.ac.dotrural.smileserver.common.ApplicationSecurityController;
import uk.ac.dotrural.smileserver.common.Constants;
import uk.ac.dotrural.smileserver.common.Util;
import uk.ac.dotrural.smileserver.dao.DataAccessObject;
import uk.ac.dotrural.smileserver.pojo.ExceptionPojo;
import uk.ac.dotrural.smileserver.pojo.ProductPojo;
import uk.ac.dotrural.smileserver.pojo.ProductsPojo;
import uk.ac.dotrural.smileserver.pojo.ApplicationStatePojo;

/**
 * @author Charles Ofoegbu
 *
 */
 

@Path("/product")
@Produces({ MediaType.APPLICATION_JSON})
public class ProductService {
    
    private static Logger logger = LoggerFactory.getLogger(ProductService.class);

    @GET
    @Produces({ MediaType.APPLICATION_JSON})     
    @SuppressWarnings("unchecked")
    public Response getProducts(@QueryParam("start") Integer start,  @QueryParam("size") Integer chunkSize, @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get all products" );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.GET_PRODUCTS);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	Response response = null;
	Collection<ProductPojo> productsPojo = new ArrayList<ProductPojo>();
	try{
	    Collection<Product> products = (start != null && chunkSize != null) ? (Collection<Product>) DataAccessObject.getAllProductByBusinessId(authorizationState.getCurrentBusiness().getId(), start, chunkSize) : (Collection<Product>) DataAccessObject.getAllProductByBusinessId(authorizationState.getCurrentBusiness().getId());
//	    Collection<Product> products = (Collection<Product>) DataAccessObject.getAllProductByBusinessId(authorizationState.getCurrentBusiness().getId());// .getAllRecords(Product.class);
	    for (Product product : products) {
		HashMap<String, String> proppertiesPojo = new HashMap<String, String>();
		Collection<Property> properties = (Collection<Property>) DataAccessObject.getProductPropertiesByProductId(product.getId());
		if (properties != null && !properties.isEmpty()) {
		    for (Property property : properties) {
			proppertiesPojo.put(property.getKey(), property.getValue());
		    }
		}
		HashMap<String, String> identifiersPojo = new HashMap<String, String>();
		Collection<Identifier> identifiers = (Collection<Identifier>) DataAccessObject.getProductIdentifiersByProductId(product.getId());
		if (identifiers != null && !identifiers.isEmpty()) {
		    for (Identifier identifier : identifiers) {
			identifiersPojo.put(identifier.getKey(), identifier.getValue());
		    }
		} 
		ProductPojo productPojo = new ProductPojo(product, proppertiesPojo, identifiersPojo);
		productsPojo.add(productPojo);
	    }
	    response = Response.status(200).entity(new ProductsPojo(productsPojo)).build();
	}catch(Exception e){
	    e.printStackTrace();
	}	
	authorizationState = null; 
	return response;
    }
    
    @GET
    @Path("/count")
    @Produces({ MediaType.APPLICATION_JSON})
    public Response getProductsCount(@Context HttpServletRequest req, @Context HttpServletResponse res) throws IOException {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get products count" );
	
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.GET_PRODUCTS_COUNT);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	
	Response response = null;
	try{
	    Long productsCount = (Long) DataAccessObject.getProductCountByBusinessId(authorizationState.getCurrentBusiness().getId()); 
	    response = Response.status(200).entity(productsCount).build();
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
    public Response getProductById(@PathParam("id") Long id, @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get product by id" );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.GET_PRODUCT);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	Product product = (Product)DataAccessObject.getRecordById(Product.class, id);
	if(product == null){
	    authorizationState = null;
	    return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "product with id = "+id+" was not found!")).build();
	}else{
	    HashMap<String, String> proppertiesPojo = new HashMap<String, String>();
	    Collection<Property> properties = (Collection<Property>) DataAccessObject.getProductPropertiesByProductId(product.getId());
	    if(properties != null && !properties.isEmpty()){
		for (Property property : properties) {
		    proppertiesPojo.put(property.getKey(), property.getValue());
		}
	    }
	    HashMap<String, String> identifiersPojo = new HashMap<String, String>();
	    Collection<Identifier> identifiers = (Collection<Identifier>) DataAccessObject.getProductIdentifiersByProductId(product.getId());
	    if (identifiers != null && !identifiers.isEmpty()) {
		for (Identifier identifier : identifiers) {
		    identifiersPojo.put(identifier.getKey(), identifier.getValue());
		}
	    }
	    ProductPojo productPojo = new ProductPojo(product, proppertiesPojo, identifiersPojo);
	    authorizationState = null;
	    return Response.status(200).entity(productPojo).build();	    
	}
    }

    
    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response createProduct(ProductPojo jsonProduct, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Post called - create product " );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.POST_PRODUCT);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	URI uri = null;
	Product product = DataAccessObject.getProductByNameAndBusinessId(jsonProduct.getFn(), authorizationState.getCurrentBusiness().getId());
	if (product != null) {
	    return Response.status(Constants.HTTP_CODE.NOT_ACCEPTED.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_ACCEPTED, new String[]{"Product already exists"}, "The product with the given name already exist for your business")).build();
	}
	try{
	    product = new Product();
	    product.setCreatedAt(new Timestamp(new Date().getTime()));
	    product.setDescription(jsonProduct.getDescription());
	    product.setFunctionalName(jsonProduct.getFn());
	    product.setBrand(jsonProduct.getBrand());
	    product.setUrl(jsonProduct.getUrl());
	    product.setPrice(jsonProduct.getPrice());
	    product.setBusiness(authorizationState.getCurrentBusiness());
	    
	    if (jsonProduct.getTags() != null && jsonProduct.getTags().length != 0) {
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating tags");
		StringBuilder tagBuilder = new StringBuilder();
		for (String tag : jsonProduct.getTags()) {
		    tagBuilder.append(":").append(tag);
		}
		tagBuilder.deleteCharAt(0);
		product.setTags(tagBuilder.toString());
	    }
	    if (jsonProduct.getCategories() != null && jsonProduct.getCategories().length != 0) {
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating categories");
		StringBuilder categoryBuilder = new StringBuilder();
		for (String category : jsonProduct.getCategories()) {
		    categoryBuilder.append(":").append(category);
		}
		categoryBuilder.deleteCharAt(0);
		product.setCategories(categoryBuilder.toString());
	    }
	    if (jsonProduct.getPhotos() != null && jsonProduct.getPhotos().length != 0) {
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating Photos");
		StringBuilder photoUrlBuilder = new StringBuilder();
		for (String photo : jsonProduct.getPhotos()) {
		    photoUrlBuilder.append("`").append(photo);
		}
		photoUrlBuilder.deleteCharAt(0);
		product.setPhotoURL(photoUrlBuilder.toString());
	    }
	    product = (Product) DataAccessObject.createNewRecord(product);
	    Util.createActivity(req, Constants.Actions.CREATE.name(), Constants.Entities.PRODUCT.name(), product.getId().toString(),  "Created a product");	
	    
	    HashMap<String, String> properties = jsonProduct.getProperties();
	    if (properties != null && !properties.isEmpty()) {
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating properties");
		for (Map.Entry<String, String> jsonProperty : properties.entrySet()) {
		    logger.debug("Key = " + jsonProperty.getKey()  + ", Value = " + jsonProperty.getValue());
		    Property property = new Property();
		    property.setProduct(product);
		    property.setKey(jsonProperty.getKey());
		    property.setValue(jsonProperty.getValue());
		    DataAccessObject.createNewRecord(property);
		}
	    }
	    
	    HashMap<String, String> identifiers = jsonProduct.getIdentifiers();
	    if (identifiers != null && !identifiers.isEmpty()) {
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating identifiers");
		for (Map.Entry<String, String> jsonIdentifier : identifiers.entrySet()) {
		    logger.debug("Key = " + jsonIdentifier.getKey()  + ", Value = " + jsonIdentifier.getValue());
		    Identifier identifier = new Identifier();
		    identifier.setProduct(product);
		    identifier.setKey(jsonIdentifier.getKey());
		    identifier.setValue(jsonIdentifier.getValue());
		    DataAccessObject.createNewRecord(identifier);
		}
	    }
	    try {
		uri = new URI(Util.getContextPath(req)  + "/" + Constants.APP_NAME + "/" + Constants.APP_VERSION +  "/product/"+product.getId());
	    } catch (URISyntaxException e) {
		e.printStackTrace();
	    }
	}catch(Exception e ){
	    e.printStackTrace();
	}
	Response re = getProductById(product.getId(), req);
	authorizationState = null;
	return Response.status(Constants.HTTP_CODE.CREATED.getCode()).entity(re.getEntity()).location(uri).build();
    }
    
    
    @PUT 
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response updateProduct(@PathParam("id") Long id, ProductPojo jsonProduct, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Put called - update product" );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.PUT_PRODUCT);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	URI uri = null;
	Product product = null;
	Response response = null;
	try{
	    product = (Product)DataAccessObject.getRecordById(Product.class, id);
	    if(product == null){
		authorizationState = null;
		return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "product with id = "+id+" was not found!")).build();		
	    } else {
		product.setDescription(jsonProduct.getDescription());
		product.setFunctionalName(jsonProduct.getFn());
		product.setBrand(jsonProduct.getBrand());
		product.setUrl(jsonProduct.getUrl());
		if(jsonProduct.getCreatedAt() != null){
		    product.setCreatedAt(jsonProduct.getCreatedAt());
		}
		product.setUpdatedAt(new Timestamp(new Date().getTime()));
		product.setPrice(jsonProduct.getPrice());
		if (jsonProduct.getTags() != null && jsonProduct.getTags().length != 0) {
		    logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating tags");
		    StringBuilder tagBuilder = new StringBuilder();
		    for (String tag : jsonProduct.getTags()) {
			tagBuilder.append(":").append(tag);
		    }
		    tagBuilder.deleteCharAt(0);
		    product.setTags(tagBuilder.toString());
		}
		if (jsonProduct.getCategories() != null && jsonProduct.getCategories().length != 0) {
		    logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating categories");
		    StringBuilder categoryBuilder = new StringBuilder();
		    for (String category : jsonProduct.getCategories()) {
			categoryBuilder.append(":").append(category);
		    }
		    categoryBuilder.deleteCharAt(0);
		    product.setCategories(categoryBuilder.toString());
		}
		if (jsonProduct.getPhotos() != null && jsonProduct.getPhotos().length != 0) {
		    logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> creating Photos");
		    StringBuilder photoUrlBuilder = new StringBuilder();
		    for (String photo : jsonProduct.getPhotos()) {
			photoUrlBuilder.append(":").append(photo);
		    }
		    photoUrlBuilder.deleteCharAt(0);
		    product.setPhotoURL(photoUrlBuilder.toString());
		}
		DataAccessObject.updateRecord(product);
		Util.createActivity(req, Constants.Actions.UPDATE.name(), Constants.Entities.PRODUCT.name(), product.getId().toString(),  "Updated a product");
		try {
		    uri = new URI(Util.getContextPath(req)  + "/" + Constants.APP_NAME + "/" + Constants.APP_VERSION +  "/product/" + id);
		} catch (URISyntaxException e) {
		    e.printStackTrace();
		}
		Response re = getProductById(id, req);
		response = Response.status(200).entity(re.getEntity()).location(uri).build();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	authorizationState = null;
	return response;
    }  
    
    @DELETE 
    @Path("/{id}")    
    @Produces({ MediaType.APPLICATION_JSON})  
    @SuppressWarnings("unchecked")
    public Response deleteProduct(@PathParam("id") Long id, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> DELETE called - delete product" );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.DELETE_PRODUCT);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	Product product = null;
	Response response = null;
	try{
	    product = (Product)DataAccessObject.getRecordById(Product.class, id);
	    if(product == null){
		authorizationState = null;
		return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "product with id = "+id+" was not found!")).build();		
	    }else{
		Collection<Property> properties = (Collection<Property>) DataAccessObject.getProductPropertiesByProductId(product.getId());
		if (properties != null && !properties.isEmpty()) {
		    for (Property property : properties) {
			DataAccessObject.deleteRecord(property);
		    }
		}
		Collection<Identifier> identifiers = (Collection<Identifier>) DataAccessObject.getProductIdentifiersByProductId(product.getId());
		if (identifiers != null && !identifiers.isEmpty()) {
		    for (Identifier identifier : identifiers) {
			DataAccessObject.deleteRecord(identifier);
		    }
		}		
		Util.createActivity(req, Constants.Actions.DELETE.name(), Constants.Entities.PRODUCT.name(), product.getId().toString(),  "Deleted a product");
		DataAccessObject.deleteRecord(product);		
		response = Response.status(200).build();
	    }
	}catch(Exception e ){
	    e.printStackTrace();
	}
	authorizationState = null;
	return response;
    }
}
