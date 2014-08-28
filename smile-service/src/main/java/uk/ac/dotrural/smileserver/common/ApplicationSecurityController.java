/**
 * 
 */
package uk.ac.dotrural.smileserver.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import uk.ac.dotrural.smile.entity.application.Application;
import uk.ac.dotrural.smile.entity.business.Business;
import uk.ac.dotrural.smile.entity.users.Users;
import uk.ac.dotrural.smileserver.common.Constants.LogOutStatus;
import uk.ac.dotrural.smileserver.dao.DataAccessObject;
import uk.ac.dotrural.smileserver.pojo.*;

/**
 * @author Charles Ofoegbu
 *
 */
public class ApplicationSecurityController { 
//       
//    @SuppressWarnings("finally")
//    public static ApplicationStatePojo isAuthorized(HttpServletRequest req, Privileges privilage){
//	ApplicationStatePojo currentState = new ApplicationStatePojo();
//	ExceptionPojo ePojo = null;
//	try{
//	    checkAppAuthorization(req, privilage, currentState);
//	    checkUserAuthorization(req, privilage, currentState);
//	    checkBusinessAuthorization(req, privilage, currentState);
//	    if (!currentState.getAuthorizationStatus()) {
//		ePojo = new ExceptionPojo(Constants.HTTP_CODE.UNAUTHORIZED, new String[] { "Insufficient privilages" }, "The supplied credentatial has insufficent privilage to access this method.");
//		currentState.setExceptionPojo(ePojo);
//		return currentState;
//	    }
//	    return currentState;    
//	}catch(IllegalArgumentException e){
//	    ePojo = handleIException(e);
//	}catch(RecordNotFoundException e){
//	    ePojo = handleIException(e);
//	}finally{
//	    currentState.setExceptionPojo(ePojo);
//	    return currentState;
//	}
//	 
//    }
//    
//    public static void checkAppAuthorization(HttpServletRequest req, Privileges privilage, ApplicationStatePojo currentState) throws RecordNotFoundException{
//	String applicationKey = req.getHeader("ApplicationAuthorization");
//	Validator.validateNotNullorEmptyString(applicationKey);
//	Application application = DataAccessObject.getApplicationByAuthorizationKey(applicationKey);
//	    if (DataAccessObject.getRightsByRoleAndPrivilege(application.getRoles(), privilage.getCode()) != null) {
//		currentState.setAuthorizationStatus(true);
//	    }	
//	    currentState.setCurrentApplication(application);
//    }    
//    
//    public static void checkUserAuthorization(HttpServletRequest req, Privileges privilage, ApplicationStatePojo currentState){
//	String applicationKey = req.getHeader("Authorization");
//	Validator.validateNotNullorEmptyString(applicationKey);
//	Users user;
//	try {
//	    user = DataAccessObject.getUserByAuthorizationKey(applicationKey);
//	    if (DataAccessObject.getRightsByRoleAndPrivilege(user.getRoles(), privilage.getCode()) != null) {
//		currentState.setAuthorizationStatus(true);
//	    }	
//	    currentState.setCurrentUser(user);
//	} catch (UserNotFoundException e) {
//	    e.printStackTrace();
//	}
//    }
//    
//    public static void checkBusinessAuthorization(HttpServletRequest req, Privileges privilage, ApplicationStatePojo currentState) throws BusinessAuthorizationFailedException{
//	    String businessKey = req.getHeader("BusinessAuthorization");
//	    Business business;
//	    try {
//		business = DataAccessObject.getBusinessByAurhorizationKey(businessKey);
//		currentState.setCurrentBusiness(business);
//	    } catch (RecordNotFoundException e) {
//		if (privilage.isRequiresBusinessAuthorizationKey()) {
//		    throw new BusinessAuthorizationFailedException("The method you are trying to access requires a valid Busniess Authorization key.");		   
//		}
//	    }	   
//    }
//    
//    public static ExceptionPojo handleIException(Exception e){
//	if(e instanceof NullOrEmptyStringValidationException){	    
//	   return new ExceptionPojo(Constants.HTTP_CODE.UNAUTHORIZED, new String[] { "Authorization failed!" }, "One of the Authorizations keys is null or empty");
//	}else if(e instanceof RecordNotFoundException){
//	   return new ExceptionPojo(Constants.HTTP_CODE.UNAUTHORIZED, new String[] { "Authorization failed!" }, e.getMessage());
//	}else if(e instanceof UserNotFoundException){
//	    return new ExceptionPojo(Constants.HTTP_CODE.UNAUTHORIZED, new String[] { "Authorization faied!" }, e.getMessage());
//	}else if(e instanceof BusinessAuthorizationFailedException){
//	    return new ExceptionPojo(Constants.HTTP_CODE.UNAUTHORIZED, new String[] { "Authorization faied!" }, e.getMessage());
//	}
//	return new ExceptionPojo(null, null, null);
//    }
//
//    @SuppressWarnings("unchecked")
//    public static AccessPojo AuthenticateUser(String email, String password){
//	Users user = null;
//	try{
//	    user = DataAccessObject.getUserByEmailAndPassword(email, password);
//	}catch(Exception e){
//	    e.printStackTrace();
//	}
//	
//	if(user == null){
//	    return null;
//	}else{
//	    AccessPojo acPojo = new AccessPojo();
//	    acPojo.setEmail(user.getEmail());
//	    if(user.getBusinessAccount()){
//		acPojo.setUserFullName(user.getFirstName());
//	    }else{
//		acPojo.setUserFullName(user.getFirstName() + " " + user.getLastName());
//	    }
//	    String apiKey = generateApiKey(email, password);
//	    acPojo.setUserApiKey(apiKey);
//	    acPojo.setStatusCode(Constants.HTTP_CODE.OK.getCode());
//	    acPojo.setAccountType(user.getBusinessAccount()? "Business Account":"User Account");
//	    user.setAuthorizationKey(apiKey);
//
//  	    ArrayList<String> authorizationKeys = new ArrayList<String>();
//	    if (!user.getBusinessAccount() && user.getBusiness() != null) {
//		acPojo.setBusinessApiKeys(new String[] { user.getBusiness().getAuthorizationKey() });
//	    } else if (user.getBusinessAccount() && user.getBusiness() != null) {
//		authorizationKeys.add(user.getBusiness().getAuthorizationKey());
//		ArrayList<Business> childBusnesses = (ArrayList<Business>) DataAccessObject.getChildBusinessesByParentBusinessId(user.getBusiness().getId());
//		Long[] businessIds = new Long[childBusnesses.size()];
//		do {
//		    int count = 0;
//		    for (Business business : childBusnesses) {
//			authorizationKeys.add(business.getAuthorizationKey());
//			businessIds[count++] = business.getId();
//		    }
//		    if (businessIds.length < 1) {
//			break;
//		    }
//		    childBusnesses = (ArrayList<Business>) DataAccessObject.getChildBusinessesByParentBusinessIds(businessIds);
//		    businessIds = new Long[childBusnesses.size()];
//		    count = 0;
//		} while (childBusnesses.size() > 0);
//
//		int count = 0;
//		String[] authKeys = new String[authorizationKeys.toArray().length];
//		for (Object authKey : authorizationKeys.toArray()) {
//		    authKeys[count++] = authKey.toString();
//		}
//		acPojo.setBusinessApiKeys(authKeys);
//	    }
//	    DataAccessObject.updateRecord(user);
//	    return acPojo;
//	}
//    }
//    
//    public static LogOutStatus logUserOut(String authorizationString){
//	Users user;
//	try {
//	    user = DataAccessObject.getUserByAuthorizationKey(authorizationString);
//	    user.setAuthorizationKey(null);
//	    DataAccessObject.updateRecord(user);
//	    return Constants.LogOutStatus.LOGOUT_SUCCESSFUL;
//	} catch (UserNotFoundException e) {
//	    return Constants.LogOutStatus.LOGOUT_FAILED;
//	}
//    }
//
//    public static String generateApiKey(String email, String password){
//	Timestamp timeStamp = new Timestamp(new Date().getTime());
//	String randomUUId = UUID.randomUUID().toString();	
//	return MD5Util.getMD5(email+password+randomUUId+timeStamp);
//    }
//
//   
    
    
    
    
    

    
    public static ApplicationStatePojo isAuthorized(HttpServletRequest req, Constants.Privileges privilage)
    {
        ApplicationStatePojo currentState = new ApplicationStatePojo();
        Boolean authorized = Boolean.valueOf(false);
        String applicationKey = req.getHeader("ApplicationAuthorization");
        if(applicationKey == null || applicationKey.equals("")){
            ExceptionPojo ePojo = new ExceptionPojo(Constants.HTTP_CODE.UNAUTHORIZED, new String[] {"Unknown application."
            }, "You are trying to access a secure method from an unknown application.");
            currentState.setExceptionPojo(ePojo);
            return currentState;
        }
        Application application = DataAccessObject.getApplicationByAuthorizationKey(applicationKey);
        if(application != null){
            currentState.setCurrentApplication(application);
            if(DataAccessObject.getRightsByRoleAndPrivilege(application.getRoles(), privilage.getCode()) != null)
                authorized = Boolean.valueOf(true);
        } else{
            ExceptionPojo ePojo = new ExceptionPojo(Constants.HTTP_CODE.UNAUTHORIZED, new String[] {
                "Malicious or outdated application."
            }, "You are trying to access a secure method from an malicious or oudated application.");
            currentState.setExceptionPojo(ePojo);
            return currentState;
        }
        String apiKey = req.getHeader("Authorization");
        Users user = DataAccessObject.getUserByAuthorizationKey(apiKey);
        if(user != null)
        {
            currentState.setCurrentUser(user);
            if(DataAccessObject.getRightsByRoleAndPrivilege(user.getRoles(), privilage.getCode()) != null)
                authorized = Boolean.valueOf(true);
        }
        String businessKey = req.getHeader("BusinessAuthorization");
        Business business = DataAccessObject.getBusinessByAurhorizationKey(businessKey);
        if(business != null)
            currentState.setCurrentBusiness(business);
        else
        if(privilage.isRequiresBusinessAuthorizationKey())
        {
            ExceptionPojo ePojo = new ExceptionPojo(Constants.HTTP_CODE.UNAUTHORIZED, new String[] {
                "Invalid/no Business Authorization key supplied."
            }, "The method you are trying to access requires a valid Busniess Authorization key. ");
            currentState.setExceptionPojo(ePojo);
            return currentState;
        }
        if(!authorized.booleanValue())
        {
            ExceptionPojo ePojo = new ExceptionPojo(Constants.HTTP_CODE.UNAUTHORIZED, new String[] {
                "Insufficient privilages"
            }, "The supplied credentatial has insufficent privilage to access this method.");
            currentState.setExceptionPojo(ePojo);
            return currentState;
        } else
        {
            return currentState;
        }
    }

    @SuppressWarnings("unchecked")
    public static AccessPojo AuthenticateUser(String email, String password)
    {
        Users user = null;
        try{
            user = DataAccessObject.getUserByEmailAndPassword(email, password);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(user == null)
            return null;
        AccessPojo acPojo = new AccessPojo();
        acPojo.setEmail(user.getEmail());
        if(user.getBusinessAccount().booleanValue())
            acPojo.setUserFullName(user.getFirstName());
        else
            acPojo.setUserFullName((new StringBuilder()).append(user.getFirstName()).append(" ").append(user.getLastName()).toString());
        String apiKey = generateApiKey(email, password);
        acPojo.setUserApiKey(apiKey);
        acPojo.setStatusCode(Constants.HTTP_CODE.OK.getCode());
        acPojo.setAccountType(user.getBusinessAccount().booleanValue() ? "Business Account" : "User Account");
	user.setAuthorizationKey(apiKey);

	ArrayList<String> authorizationKeys = new ArrayList<String>();
	if (!user.getBusinessAccount() && user.getBusiness() != null) {
	    acPojo.setBusinessApiKeys(new String[] { user.getBusiness().getAuthorizationKey() });
	} else if (user.getBusinessAccount() && user.getBusiness() != null) {
	    authorizationKeys.add(user.getBusiness().getAuthorizationKey());
	    ArrayList<Business> childBusnesses = (ArrayList<Business>) DataAccessObject.getChildBusinessesByParentBusinessId(user.getBusiness().getId());
	    Long[] businessIds = new Long[childBusnesses.size()];
	    do {
		int count = 0;
		for (Business business : childBusnesses) {
		    authorizationKeys.add(business.getAuthorizationKey());
		    businessIds[count++] = business.getId();
		}
		if (businessIds.length < 1) {
		    break;
		}
		childBusnesses = (ArrayList<Business>) DataAccessObject.getChildBusinessesByParentBusinessIds(businessIds);
		businessIds = new Long[childBusnesses.size()];
		count = 0;
	    } while (childBusnesses.size() > 0);

	    int count = 0;
	    String[] authKeys = new String[authorizationKeys.toArray().length];
	    for (Object authKey : authorizationKeys.toArray()) {
		authKeys[count++] = authKey.toString();
	    }
	    acPojo.setBusinessApiKeys(authKeys);
	}
	DataAccessObject.updateRecord(user);
	return acPojo;
    }

    public static LogOutStatus logUserOut(String authorizationString)
    {
        Users user = DataAccessObject.getUserByAuthorizationKey(authorizationString);
        if(user != null)
        {
            user.setAuthorizationKey(null);
            DataAccessObject.updateRecord(user);
            return Constants.LogOutStatus.LOGOUT_SUCCESSFUL;
        } else
        {
            return Constants.LogOutStatus.LOGOUT_FAILED;
        }
    }

    public static String generateApiKey(String email, String password)
    {
        Timestamp timeStamp = new Timestamp((new Date()).getTime());
        String randomUUId = UUID.randomUUID().toString();
        return MD5Util.getMD5((new StringBuilder()).append(email).append(password).append(randomUUId).append(timeStamp).toString());
    }
}
