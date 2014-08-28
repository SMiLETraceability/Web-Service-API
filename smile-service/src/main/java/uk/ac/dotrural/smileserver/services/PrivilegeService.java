/**
 * 
 */
package uk.ac.dotrural.smileserver.services;

import java.net.URI;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.dotrural.smile.entity.privilege.Privilege;
import uk.ac.dotrural.smileserver.common.Constants;
import uk.ac.dotrural.smileserver.dao.DataAccessObject;
import uk.ac.dotrural.smileserver.pojo.ExceptionPojo;
import uk.ac.dotrural.smileserver.pojo.PrivilegePojo;
import uk.ac.dotrural.smileserver.pojo.PrivilegesPojo;

/**
 * @author Charles Ofoegbu
 *
 */

@Path("/privilege")
public class PrivilegeService {

    private static Logger logger = LoggerFactory.getLogger(PrivilegeService.class);
    @SuppressWarnings("unchecked")
    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public Response getPrivileges(@Context HttpServletRequest req){
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get privileges " );
	URI uri = null;
	ArrayList<Privilege> privileges = (ArrayList<Privilege>)DataAccessObject.getAllRecords(Privilege.class);
	if(privileges == null || privileges.isEmpty()){
	    return Response.status(Constants.HTTP_CODE.NOT_ACCEPTED.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_ACCEPTED, null, "No Priviliege was found in the Database!")).build();	
	}	

	ArrayList<PrivilegePojo> privilegesPojo = new ArrayList<PrivilegePojo>();
	for(Privilege privilege : privileges){
	    PrivilegePojo pPojo = new PrivilegePojo(privilege);
	    privilegesPojo.add(pPojo);
	}
	
	PrivilegesPojo privPojo = new PrivilegesPojo(privilegesPojo);
	return Response.status(Constants.HTTP_CODE.OK.getCode()).entity(privPojo).location(uri).build();
    }
}
