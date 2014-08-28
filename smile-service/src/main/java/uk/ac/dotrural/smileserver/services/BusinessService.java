/**
 * 
 */
package uk.ac.dotrural.smileserver.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.dotrural.smile.entity.address.Address;
import uk.ac.dotrural.smile.entity.business.Business;
import uk.ac.dotrural.smile.entity.privilege.Privilege;
import uk.ac.dotrural.smile.entity.rights.Rights;
import uk.ac.dotrural.smile.entity.roles.Roles;
import uk.ac.dotrural.smile.entity.users.Users;
import uk.ac.dotrural.smileserver.common.*;
import uk.ac.dotrural.smileserver.common.Constants.*;
import uk.ac.dotrural.smileserver.dao.DataAccessObject;
import uk.ac.dotrural.smileserver.pojo.BusinessPojo;
import uk.ac.dotrural.smileserver.pojo.ExceptionPojo;
import uk.ac.dotrural.smileserver.pojo.ApplicationStatePojo;

/**
 * @author Charles Ofoegbu
 *
 */

@Path("/business")
public class BusinessService {
    private static Logger logger = LoggerFactory.getLogger(BusinessService.class);
    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response createBusiness(BusinessPojo jsonBusiness, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Post called - create business " );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.POST_BUSINESS);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	
	if(DataAccessObject.isDuplicateUserEmail(jsonBusiness.getEmail())){	    
	    return Response.status(Constants.HTTP_CODE.NOT_ACCEPTED.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "A business with the specified email already exist!")).build();
	}
	
	if(DataAccessObject.isDuplicateBusiness(jsonBusiness.getName())){	    
	    return Response.status(Constants.HTTP_CODE.NOT_ACCEPTED.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "A business with the specified name already exist!")).build();
	}
	 
	URI uri = null;
	Business business = new Business();
	Users user = new Users();
	Address address = new Address();
	Roles businessAdminUserRole = new Roles();
	try{
	    if (jsonBusiness.getAddress() != null) {
		address.setCountry(jsonBusiness.getAddress().getCountry());
		address.setCounty(jsonBusiness.getAddress().getCounty());
		address.setHouseNumber(jsonBusiness.getAddress().getNumber());
		address.setPostCode(jsonBusiness.getAddress().getPostcode());
		address.setStreet(jsonBusiness.getAddress().getStreet());
		address = (Address) DataAccessObject.createNewRecord(address);
		business.setAddress(address);
	    }

	    business.setName(jsonBusiness.getName());
	    business.setTelephone(jsonBusiness.getTelephone());
	    if (jsonBusiness.getPhotos() != null && jsonBusiness.getPhotos().length != 0) {
		StringBuilder photoUrlBuilder = new StringBuilder();
		for (String photo : jsonBusiness.getPhotos()) {
		    photoUrlBuilder.append(photo).append("`");
		}
		business.setPhotoUrl(photoUrlBuilder.toString());
	    }

	    if (jsonBusiness.getParentBusinessId() != null) {
		Business parentBusiness = (Business) DataAccessObject.getParentBusinessById(jsonBusiness.getParentBusinessId());
		business.setParentBusiness(parentBusiness);
	    }

	    business.setDescription(jsonBusiness.getDescription());
	    Timestamp timeStamp = new Timestamp(new Date().getTime());
	    String randomUUId = UUID.randomUUID().toString();
	    String authorizationKey = MD5Util.getMD5(jsonBusiness.getName() + randomUUId + timeStamp);
	    business.setAuthorizationKey(authorizationKey);
	    business.setWebsiteUrl(jsonBusiness.getWebsiteUrl());
	    business = (Business) DataAccessObject.createNewRecord(business);
	    String businessAdminUser = business.getName().toUpperCase().replaceAll(" ", "_").concat("_ADMIN");
	    businessAdminUserRole = createRole(business, businessAdminUser, businessAdminUser);
	    createSuperAdminRightsForRole(businessAdminUserRole);

	    user.setRoles(businessAdminUserRole);
	    user.setEmail(jsonBusiness.getEmail());
	    user.setFirstName(jsonBusiness.getName());
	    user.setLastName(jsonBusiness.getName());
	    user.setPassword(MD5Util.getMD5(jsonBusiness.getPassword()));
	    user.setEnabled(true);
	    user.setBirthday(new Date());
	    user.setLocale("");
	    user.setBusiness(business);
	    user.setGender(Gender.OTHER.name());
	    user.setTimeZone("");
	    user.setBusinessAccount(true);
	    user.setAddress(address);
	    user = (Users)DataAccessObject.createNewRecord(user);	
	}catch(ConstraintViolationException cve){
	    authorizationState = null;
	    return Response.status(Constants.HTTP_CODE.NOT_ACCEPTED.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "A business with the specified name already exist!")).build();
	}catch(Exception e){
	    authorizationState = null;
	    rollback(business, user, businessAdminUserRole);
	    return Response.status(404).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, e.getCause().getMessage())).build();
	}
	BusinessPojo businessPojo = new BusinessPojo(business, user);
	businessPojo.setAuthorizationKey(business.getAuthorizationKey());
	try {
		uri = new URI(Util.getContextPath(req) + "/" + Constants.APP_NAME + "/" + Constants.APP_VERSION + "/business/"+businessPojo.getId());
	    } catch (URISyntaxException e) {
		e.printStackTrace();
	    }
	authorizationState = null;
	return Response.status(Constants.HTTP_CODE.CREATED.getCode()).entity(businessPojo).location(uri).build();
    }
    
    
    
    @PUT
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response updateBusiness(@PathParam("id") Long id, BusinessPojo jsonBusiness, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Post called - update business " );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.PUT_BUSINESS);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}
	URI uri = null;
	Business business = null;
	Users user = null;
	Address address = null;
	 
	try{
	 business = (Business)DataAccessObject.getRecordById(Business.class, id);
	    if(business == null){
		authorizationState = null;
		return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "Business with id = "+id+" was not found!")).build();		
	    }
	    address = business.getAddress();
	    if (jsonBusiness.getAddress() != null) {
		address.setCountry(jsonBusiness.getAddress().getCountry());
		address.setCounty(jsonBusiness.getAddress().getCounty());
		address.setHouseNumber(jsonBusiness.getAddress().getNumber());
		address.setPostCode(jsonBusiness.getAddress().getPostcode());
		address.setStreet(jsonBusiness.getAddress().getStreet());
		DataAccessObject.updateRecord(address);
		business.setAddress(address);
	    }
	    business.setTelephone(jsonBusiness.getTelephone());
	    if (jsonBusiness.getPhotos() != null && jsonBusiness.getPhotos().length != 0) {
		StringBuilder photoUrlBuilder = new StringBuilder();
		for (String photo : jsonBusiness.getPhotos()) {
		    photoUrlBuilder.append(photo).append("`");
		}
		business.setPhotoUrl(photoUrlBuilder.toString());
	    }
	    if (jsonBusiness.getParentBusinessId() != null) {
		Business parentBusiness = (Business) DataAccessObject.getParentBusinessById(jsonBusiness.getParentBusinessId());
		business.setParentBusiness(parentBusiness);
	    }
	    business.setDescription(jsonBusiness.getDescription());
	    business.setWebsiteUrl(jsonBusiness.getWebsiteUrl());
	    DataAccessObject.updateRecord(business);
	    
	    
	    user = DataAccessObject.getBusinessAccountUserByBusinessId(business.getId());
	    if(jsonBusiness.getPassword() != null){
		 user.setPassword(MD5Util.getMD5(jsonBusiness.getPassword()));		
	    }
	    user.setEnabled(true);
	    user.setBirthday(new Date());
	    user.setBusiness(business);
	    user.setGender(Gender.OTHER.name());
	    user.setTimeZone("");
	    user.setAddress(address);
	    DataAccessObject.updateRecord(user);	
	}catch(ConstraintViolationException cve){
	    authorizationState = null;
	    return Response.status(Constants.HTTP_CODE.NOT_ACCEPTED.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "A business with the specified name already exist!")).build();
	}catch(Exception e){
	    authorizationState = null;
	    e.printStackTrace();
	    return Response.status(404).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, e.getMessage())).build();
	}
	BusinessPojo businessPojo = new BusinessPojo(business, user);
	businessPojo.setAuthorizationKey(business.getAuthorizationKey());
	try {
		uri = new URI(Util.getContextPath(req) + "/" + Constants.APP_NAME + "/" + Constants.APP_VERSION + "/business/"+businessPojo.getId());
	    } catch (URISyntaxException e) {
		e.printStackTrace();
	    }
	authorizationState = null;
	return Response.status(Constants.HTTP_CODE.ACCEPTED.getCode()).entity(businessPojo).location(uri).build();
    }
    
    
    public static void rollback(Business business, Users user, Roles role){	
	if (role != null && role.getId() != null){
	    DataAccessObject.deleteRoleRights(role.getId());
	    DataAccessObject.deleteRecord(role);
	}
	if(user != null && user.getId() != null){
	    DataAccessObject.deleteRecord(user);
	}
	if(business != null && business.getId() != null){
	    DataAccessObject.deleteRecord(business);
	}
    }

    
    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    public Response getBusinessById(@PathParam("id") String id, @Context HttpServletRequest req) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get Business by id" );
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.GET_BUSINESS);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}

	Business business = (Business)DataAccessObject.getBusinessByAurhorizationKey(id);
	if (business == null) {
	    business = (Business) DataAccessObject.getRecordById(Business.class, Long.valueOf(id));
	}
	
	
	if(business == null){
	    authorizationState = null;
	    return Response.status(Constants.HTTP_CODE.NOT_FOUND.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_FOUND, null, "business with id = "+id+" was not found!")).build();
	}else{
	    Users user = (Users)DataAccessObject.getBusinessAccountUserByBusinessId(business.getId());	
	    BusinessPojo businessPojo = new BusinessPojo(business, user);
	    authorizationState = null;
	    return Response.status(Constants.HTTP_CODE.OK.getCode()).entity(businessPojo).build();	    
	}
    }
    
    public static Roles createRole(Business business, String authority, String Code){
	Roles role = new Roles();
	role.setBusiness(business);
	role.setAuthority(authority);
	role.setCode(authority);
	
	role = (Roles)DataAccessObject.createNewRecord(role);
	return role;
    }
    
    public static void createSuperAdminRightsForRole(Roles role) {
	for (Privileges privilage : Privileges.values()) {
	    Privilege foundPrivilage = DataAccessObject.getPrivilegeByName(privilage.name());
	    if (foundPrivilage != null) {
		Rights right = new Rights();
		right.setRoles(role);
		right.setPrivilege(foundPrivilage);
		DataAccessObject.createNewRecord(right);
	    }
	}
    }

}
