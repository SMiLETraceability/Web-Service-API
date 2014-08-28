/**
 * 
 */
package uk.ac.dotrural.smileserver.services;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.dotrural.smileserver.common.ApplicationSecurityController;
import uk.ac.dotrural.smileserver.common.Constants;
import uk.ac.dotrural.smileserver.common.Constants.LogOutStatus;
import uk.ac.dotrural.smileserver.pojo.AuthenticationPojo;
import uk.ac.dotrural.smileserver.pojo.AccessPojo;
import uk.ac.dotrural.smileserver.pojo.ExceptionPojo;


/**
 * @author Charles Ofoegbu
 *
 */

@Path("/auth")
@Produces({ MediaType.APPLICATION_JSON})
public class AccessControlService {

    private static Logger logger = LoggerFactory.getLogger(AccessControlService.class);
    @POST
    @Produces({ MediaType.APPLICATION_JSON})     
    public Response logIn(AuthenticationPojo authPojo) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> POST called - login -- Authorization : ");
	AccessPojo accessPojo = ApplicationSecurityController.AuthenticateUser(authPojo.getEmail(), authPojo.getPassword());
	if(accessPojo == null){
	    return Response.status(Constants.HTTP_CODE.NOT_ACCEPTED.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_ACCEPTED, new String[]{"Authentication failed!"}, "Could not authenticate the user with the supplied credential.")).build();
	}
	return Response.status(Constants.HTTP_CODE.OK.getCode()).entity(accessPojo).build();	    
    }
    
    @POST
    @Path("/all/logout")
    @Produces({ MediaType.APPLICATION_JSON})     
    public Response logOut(@Context HttpHeaders headers) {	
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> POST called - logout -- Authorization : ");
	if(headers.getRequestHeader("Authorization") == null){
	    return Response.status(Constants.HTTP_CODE.NOT_ACCEPTED.getCode()).entity(new ExceptionPojo(Constants.HTTP_CODE.NOT_ACCEPTED, new String[]{"No authorization key supplied!"}, "A valid Authorization key is required for this service.")).build();
	}
	
	String apiKey = headers.getRequestHeader("Authorization").get(0);
	LogOutStatus accessStatus = ApplicationSecurityController.logUserOut(apiKey);
	return Response.status(accessStatus.getCode()).entity(accessStatus).build();	    

    }
    
    @Path("hello")
    public static class HelloResource {
        @GET
        public String getHello() {
            return "Hello World!";
        }
    }
}
