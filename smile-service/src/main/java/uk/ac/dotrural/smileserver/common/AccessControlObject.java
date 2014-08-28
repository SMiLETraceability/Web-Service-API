///**
// * 
// */
//package uk.ac.dotrural.smileserver.common;
//
///**
// * @author Charles Ofoegbu
// *
// */
//
//import java.sql.Timestamp;
//import java.util.*;
//
//import javax.servlet.http.HttpServletRequest;
//
//import uk.ac.dotrural.smile.entity.application.Application;
//import uk.ac.dotrural.smile.entity.business.Business;
//import uk.ac.dotrural.smile.entity.users.Users;
//import uk.ac.dotrural.smileserver.common.Constants.LogOutStatus;
//import uk.ac.dotrural.smileserver.dao.DataAccessObject;
//import uk.ac.dotrural.smileserver.pojo.*;
//
//
//public class AccessControlObject
//{
//
// 
// public static ApplicationStatePojo isAuthorized(HttpServletRequest req, Constants.Privileges privilage)
// {
//     ApplicationStatePojo currentState = new ApplicationStatePojo();
//     Boolean authorized = Boolean.valueOf(false);
//     String applicationKey = req.getHeader("ApplicationAuthorization");
//     if(applicationKey == null || applicationKey.equals("")){
//         ExceptionPojo ePojo = new ExceptionPojo(Constants.HTTP_CODE.UNAUTHORIZED, new String[] {"Unknown application."
//         }, "You are trying to access a secure method from an unknown application.");
//         currentState.setExceptionPojo(ePojo);
//         return currentState;
//     }
//     Application application = DataAccessObject.getApplicationByAuthorizationKey(applicationKey);
//     if(application != null){
//         currentState.setCurrentApplication(application);
//         if(DataAccessObject.getRightsByRoleAndPrivilege(application.getRoles(), privilage.getCode()) != null)
//             authorized = Boolean.valueOf(true);
//     } else{
//         ExceptionPojo ePojo = new ExceptionPojo(Constants.HTTP_CODE.UNAUTHORIZED, new String[] {
//             "Malicious or outdated application."
//         }, "You are trying to access a secure method from an malicious or oudated application.");
//         currentState.setExceptionPojo(ePojo);
//         return currentState;
//     }
//     String apiKey = req.getHeader("Authorization");
//     Users user = DataAccessObject.getUserByAuthorizationKey(apiKey);
//     if(user != null)
//     {
//         currentState.setCurrentUser(user);
//         if(DataAccessObject.getRightsByRoleAndPrivilege(user.getRoles(), privilage.getCode()) != null)
//             authorized = Boolean.valueOf(true);
//     }
//     String businessKey = req.getHeader("BusinessAuthorization");
//     Business business = DataAccessObject.getBusinessByAurhorizationKey(businessKey);
//     if(business != null)
//         currentState.setCurrentBusiness(business);
//     else
//     if(privilage.isRequiresBusinessAuthorizationKey())
//     {
//         ExceptionPojo ePojo = new ExceptionPojo(Constants.HTTP_CODE.UNAUTHORIZED, new String[] {
//             "Invalid/no Business Authorization key supplied."
//         }, "The method you are trying to access requires a valid Busniess Authorization key. ");
//         currentState.setExceptionPojo(ePojo);
//         return currentState;
//     }
//     if(!authorized.booleanValue())
//     {
//         ExceptionPojo ePojo = new ExceptionPojo(Constants.HTTP_CODE.UNAUTHORIZED, new String[] {
//             "Insufficient privilages"
//         }, "The supplied credentatial has insufficent privilage to access this method.");
//         currentState.setExceptionPojo(ePojo);
//         return currentState;
//     } else
//     {
//         return currentState;
//     }
// }
//
// @SuppressWarnings("unchecked")
//public static AccessPojo AuthenticateUser(String email, String password)
// {
//     Users user = null;
//     try
//     {
//         user = DataAccessObject.getUserByEmailAndPassword(email, password);
//     }
//     catch(Exception e)
//     {
//         e.printStackTrace();
//     }
//     if(user == null)
//         return null;
//     AccessPojo acPojo = new AccessPojo();
//     acPojo.setEmail(user.getEmail());
//     if(user.getBusinessAccount().booleanValue())
//         acPojo.setUserFullName(user.getFirstName());
//     else
//         acPojo.setUserFullName((new StringBuilder()).append(user.getFirstName()).append(" ").append(user.getLastName()).toString());
//     String apiKey = generateApiKey(email, password);
//     acPojo.setUserApiKey(apiKey);
//     acPojo.setStatusCode(Constants.HTTP_CODE.OK.getCode());
//     acPojo.setAccountType(user.getBusinessAccount().booleanValue() ? "Business Account" : "User Account");
//     user.setAuthorizationKey(apiKey);
////     ArrayList authorizationKeys = new ArrayList();
////     if(!user.getBusinessAccount().booleanValue() && user.getBusiness() != null)
////         acPojo.setBusinessApiKeys(new String[] {
////             user.getBusiness().getAuthorizationKey()
////         });
////     else
////     if(user.getBusinessAccount().booleanValue() && user.getBusiness() != null)
////     {
////         authorizationKeys.add(user.getBusiness().getAuthorizationKey());
////         ArrayList childBusnesses = (ArrayList)DataAccessObject.getChildBusinessesByParentBusinessId(user.getBusiness().getId());
////         Long businessIds[] = new Long[childBusnesses.size()];
////         int count;
////         do
////         {
////             count = 0;
////             for(Iterator i$ = childBusnesses.iterator(); i$.hasNext();)
////             {
////                 Business business = (Business)i$.next();
////                 authorizationKeys.add(business.getAuthorizationKey());
////                 businessIds[count++] = business.getId();
////             }
////
////             if(businessIds.length < 1)
////                 break;
////             childBusnesses = (ArrayList)DataAccessObject.getChildBusinessesByParentBusinessIds(businessIds);
////             businessIds = new Long[childBusnesses.size()];
////             count = 0;
////         } while(childBusnesses.size() > 0);
////         count = 0;
////         String authKeys[] = new String[authorizationKeys.toArray().length];
////         Object arr$[] = authorizationKeys.toArray();
////         int len$ = arr$.length;
////         for(int i$ = 0; i$ < len$; i$++)
////         {
////             Object authKey = arr$[i$];
////             authKeys[count++] = authKey.toString();
////         }
////
////         acPojo.setBusinessApiKeys(authKeys);
////     }
////     DataAccessObject.updateRecord(user);
////     return acPojo;
//     
//     
//     
//     
//	    ArrayList<String> authorizationKeys = new ArrayList<String>();
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
// }
//
// public static LogOutStatus logUserOut(String authorizationString)
// {
//     Users user = DataAccessObject.getUserByAuthorizationKey(authorizationString);
//     if(user != null)
//     {
//         user.setAuthorizationKey(null);
//         DataAccessObject.updateRecord(user);
//         return Constants.LogOutStatus.LOGOUT_SUCCESSFUL;
//     } else
//     {
//         return Constants.LogOutStatus.LOGOUT_FAILED;
//     }
// }
//
// public static String generateApiKey(String email, String password)
// {
//     Timestamp timeStamp = new Timestamp((new Date()).getTime());
//     String randomUUId = UUID.randomUUID().toString();
//     return MD5Util.getMD5((new StringBuilder()).append(email).append(password).append(randomUUId).append(timeStamp).toString());
// }
//}
