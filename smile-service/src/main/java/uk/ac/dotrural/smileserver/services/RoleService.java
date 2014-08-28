/**
 * 
 */
package uk.ac.dotrural.smileserver.services;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.dotrural.smile.entity.business.Business;
import uk.ac.dotrural.smile.entity.privilege.Privilege;
import uk.ac.dotrural.smile.entity.rights.Rights;
import uk.ac.dotrural.smile.entity.roles.Roles;
import uk.ac.dotrural.smile.entity.users.Users;
import uk.ac.dotrural.smileserver.common.Constants;
import uk.ac.dotrural.smileserver.common.Util;
import uk.ac.dotrural.smileserver.dao.DataAccessObject;
import uk.ac.dotrural.smileserver.pojo.ExceptionPojo;
import uk.ac.dotrural.smileserver.pojo.RoleAssignmentPojo;
import uk.ac.dotrural.smileserver.pojo.RolePojo;
import uk.ac.dotrural.smileserver.pojo.UserPojo;

/**
 * @author Charles Ofoegbu
 *
 */

@Path("/role")
public class RoleService {

    private static Logger logger = LoggerFactory.getLogger(RoleService.class);
    
    @GET
    @Path("/{roleId}")
    @Produces({ MediaType.APPLICATION_JSON})
    public Response getRole(@PathParam("roleId") Long roleId, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Post called - create user " );
	URI uri = null;
	Roles role = (Roles)DataAccessObject.getRecordById(Roles.class, roleId);
	if(role == null){
	    return Response.status(Constants.HTTP_CODE.NOT_ACCEPTED.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_ACCEPTED, null, "The Role specified was not found!")).build();	
	}	
	RolePojo rolePojo = new RolePojo(role);
	return Response.status(Constants.HTTP_CODE.CREATED.getCode()).entity(rolePojo).location(uri).build();
    }
    
    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response createRole(RolePojo jsonRole, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Post called - create Role " );
	URI uri = null;
	Roles userRole = DataAccessObject.getUserRoleByAuthority(jsonRole.getRoleCode());

	if(userRole != null){
	    return Response.status(Constants.HTTP_CODE.NOT_ACCEPTED.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_ACCEPTED, null, "The Role specified already exist!")).build();	
	}
	Roles role = new Roles();
	role.setAuthority(jsonRole.getRoleName());
	role.setCode(jsonRole.getRoleCode());

	if (jsonRole.getBusinessId() != null) {
	    Business business = (Business) DataAccessObject.getRecordById(Business.class, jsonRole.getBusinessId());
	    role.setBusiness(business);
	}
	
	role = (Roles)DataAccessObject.createNewRecord(role);
	
	if(jsonRole.getPrivilages() != null){
	    for(String privilage : jsonRole.getPrivilages()){
		Privilege privilege = DataAccessObject.getPrivilegeByName(privilage);
		if(privilage != null){
		    Rights right = new Rights();
		    right.setPrivilege(privilege);
		    right.setRoles(role);
		    DataAccessObject.createNewRecord(right);
		}
	    }
	}
	
	RolePojo rolePojo = new RolePojo(role);
	try {
		uri = new URI(Util.getContextPath(req)   + "/" + Constants.APP_NAME + "/" + Constants.APP_VERSION +  "/role/"+role.getId());
	    } catch (URISyntaxException e) {
		e.printStackTrace();
	    }
	return Response.status(Constants.HTTP_CODE.CREATED.getCode()).entity(rolePojo).location(uri).build();
    }
    
    @POST
    @Path("/assign")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response AssignRole(RoleAssignmentPojo jsonRoleAss, @Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Post called - assign user Role " );
	
	Roles userRole = (Roles)DataAccessObject.getRecordById(Roles.class, jsonRoleAss.getRoleId());
	if(userRole == null){
	    return Response.status(Constants.HTTP_CODE.NOT_ACCEPTED.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_ACCEPTED, null, "The Role specified does not exist!")).build();	
	}
	Users user = (Users)DataAccessObject.getRecordById(Users.class, jsonRoleAss.getUserId());
	if(user == null){
	    return Response.status(Constants.HTTP_CODE.NOT_ACCEPTED.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_ACCEPTED, null, "The User specified does not exist!")).build();	
	}
	
	if(userRole.getBusiness() == null){
	    user.setRoles(userRole);
	}else{
	    if(user.getBusiness() != null && (user.getBusiness().getId().longValue() == userRole.getBusiness().getId().longValue())){
		 user.setRoles(userRole);
	    }else{
		return Response.status(Constants.HTTP_CODE.NOT_ACCEPTED.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_ACCEPTED, null, "You cannot assign a user from a different business to a role from another business!")).build();
	    }
	}
	
	DataAccessObject.updateRecord(user);
	user = (Users)DataAccessObject.getRecordById(Users.class, jsonRoleAss.getUserId());	
	UserPojo userPojo = new UserPojo(user, null, null);
	
	return Response.status(Constants.HTTP_CODE.CREATED.getCode()).entity(userPojo).build();
    }

    
}
