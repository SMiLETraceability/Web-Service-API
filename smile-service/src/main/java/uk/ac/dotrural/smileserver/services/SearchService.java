/**
 * 
 */
package uk.ac.dotrural.smileserver.services;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.dotrural.smileserver.common.ApplicationSecurityController;
import uk.ac.dotrural.smileserver.common.Constants;
import uk.ac.dotrural.smileserver.common.Constants.SearchableEntity;
import uk.ac.dotrural.smileserver.dao.DataAccessObject;
import uk.ac.dotrural.smileserver.pojo.ApplicationStatePojo;

/**
 * @author Charles Ofoegbu
 *
 */


@Path("/search")
public class SearchService {
    
    private static Logger logger = LoggerFactory.getLogger(SearchService.class);
    
    @SuppressWarnings("unchecked")
    @POST
    @Path("/{entity}/{field}")
    @Produces({ MediaType.APPLICATION_JSON})
    public Response search(@PathParam("entity") String entity, @PathParam("field") String field, @QueryParam("searchTerm") String searchTerm, @Context HttpServletRequest req){
	ApplicationStatePojo authorizationState = ApplicationSecurityController.isAuthorized(req, Constants.Privileges.GET_SEARCH);
	if(authorizationState.getExceptionPojo() != null){
	    return Response.status(authorizationState.getExceptionPojo().getStatusCode()).entity(authorizationState.getExceptionPojo()).build();
	}

	
	for(SearchableEntity sEntity : SearchableEntity.values()){
	    if(entity.equalsIgnoreCase(sEntity.name())){
		for(String sField : sEntity.getSearchableFields()){
		    if(field.equalsIgnoreCase(sField)){
			ArrayList<String> foundWords = (ArrayList<String>) DataAccessObject.executeSearch(authorizationState.getCurrentBusiness(), sEntity.name(), sField, searchTerm);
			authorizationState = null;
			return Response.status(Constants.HTTP_CODE.OK.getCode()).entity(foundWords.toArray()).build();
		    }
		}
	    }
	    
	}	
	authorizationState = null;
	return Response.status(Constants.HTTP_CODE.OK.getCode()).entity(null).build();
    }

}
