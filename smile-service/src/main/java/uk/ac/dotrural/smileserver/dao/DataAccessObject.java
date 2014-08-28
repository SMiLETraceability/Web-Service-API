/**
 * 
 */
package uk.ac.dotrural.smileserver.dao;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




import uk.ac.dotrural.smile.ServiceLocator;
import uk.ac.dotrural.smile.entity.activity.Activity;
import uk.ac.dotrural.smile.entity.activitytype.ActivityType;
import uk.ac.dotrural.smile.entity.application.Application;
import uk.ac.dotrural.smile.entity.business.Business;
import uk.ac.dotrural.smile.entity.item.Item;
import uk.ac.dotrural.smile.entity.location.Location;
import uk.ac.dotrural.smile.entity.privilege.Privilege;
import uk.ac.dotrural.smile.entity.product.Product;
import uk.ac.dotrural.smile.entity.property.Property;
import uk.ac.dotrural.smile.entity.rights.Rights;
import uk.ac.dotrural.smile.entity.roles.Roles;
import uk.ac.dotrural.smile.entity.users.Users;
import uk.ac.dotrural.smile.service.SmileService;
import uk.ac.dotrural.smileserver.common.MD5Util;
import uk.ac.dotrural.smileserver.common.Constants.Gender;


/**
 * @author Charles Ofoegbu
 *
 */
public class DataAccessObject {
    private static Logger logger = LoggerFactory.getLogger(DataAccessObject.class);
    private static ServiceLocator serviceLocator = ServiceLocator.getInstance();
    private static SmileService sService = serviceLocator.getSmileService();
    
    public static void main (String [] args){
	logger.debug(">>>>>>> " + Gender.valueOf("male".toUpperCase()).name());
    } 
     
    public static Object createNewRecord(Object object){
        return sService.createNewRecord(object);         
    }
    
    public static void updateRecord(Object record){ 
    	sService.updateRecord(record);
    }
    
    public static void deleteRecord(Object object){
	sService.deleteRecord(object);
    }
    
    public static Object getRecordById(Class<?> entityClass, Long recordId){
        return sService.getRecordById(entityClass, recordId);
    }

    public static Collection<?> getAllRecords(Class<?> entityClass){
    	return sService.getAllRecords(entityClass);
    }
    
    public static boolean isDuplicateUserEmail(String email){
	Users user = (Users)sService.getUniqueRecordByHQL("SELECT u from Users u where u.email='" + email +"'"); 
	return  (user == null)? false : true;
    }
    
    public static boolean isDuplicateBusiness(String businessName){	
	Business business = (Business)sService.getUniqueRecordByHQL("SELECT b from Business b where b.name='" + businessName +"'"); 
	return (business == null)? false : true;
    }    
    
    public static Collection<?> getAllProductByBusinessId(Long businessId){	
	return (Collection<?>)sService.getAllRecordsByHQL("SELECT p from Product p where p.business.id=" + businessId); 
    }
    
    public static Collection<?> getAllProductByBusinessId(Long businessId, int start, int size){	
	return (Collection<?>)sService.getAllRecordsByHQL("SELECT p from Product p where p.business.id=" + businessId, start, size); 
    }

    public static Collection<?> getAllItemByBusinessId(Long businessId, int start, int size){
   	return (Collection<?>)sService.getAllRecordsByHQL("SELECT i from Item i where i.business.id=" + businessId, start, size); 
    }
    
    public static Collection<?> getAllItemCollectionByBusinessId(Long businessId, int start, int size){
   	return (Collection<?>)sService.getAllRecordsByHQL("SELECT i from ItemCollection i where i.business.id=" + businessId, start, size); 
    }
    
    public static Collection<?> getAllItemByBusinessId(Long businessId){
   	return (Collection<?>)sService.getAllRecordsByHQL("SELECT i from Item i where i.business.id=" + businessId); 
    }
    
    public static Collection<?> getAllItemCollectionByBusinessId(Long businessId){
   	return (Collection<?>)sService.getAllRecordsByHQL("SELECT i from ItemCollection i where i.business.id=" + businessId); 
    }
    
    public static Long getItemCountByBusinessId(Long businessId){
   	return (Long)sService.getUniqueRecordByHQL("SELECT count(i) from Item i where i.business.id=" + businessId); 
    }

    public static Long getProductCountByBusinessId(Long businessId){
	return (Long)sService.getUniqueRecordByHQL("SELECT count(p) from Product p where p.business.id=" + businessId); 
    }
    
    public static Long getItemCollectionCountByBusinessId(Long businessId){
   	return (Long)sService.getUniqueRecordByHQL("SELECT count(i) from ItemCollection i where i.business.id=" + businessId); 
    }
    
    public static Collection<?> getAllLocationsByItemId(Long itemId){
	return (Collection<?>)sService.getAllRecordsByHQL("SELECT l from Location l where l.item.id=" + itemId); 
    }
    
    public static Collection<?> getCollectionsItemsByCollectionId(Long collectionId){
	return (Collection<?>)sService.getAllRecordsByHQL("SELECT i from Item i where i.itemCollection.id=" + collectionId); 
    }    

    public static Item getItemByItemCollectionIdAndId(Long collectionId, Long id){
	return (Item)sService.getUniqueRecordByHQL("SELECT i from Item i where i.itemCollection.id=" + collectionId +" and i.id=" + id); 
    }

    public static ActivityType getActivityTypeByName(String name){
	return (ActivityType)sService.getUniqueRecordByHQL("SELECT at from ActivityType at where at.name='" + name +"'"); 
    }    

    public static Application getApplicationByName(String name){
	return (Application)sService.getUniqueRecordByHQL("SELECT a from Application a where a.name='" + name +"'"); 
    }    
    
    public static Privilege getPrivilegeByName(String name){
	return (Privilege)sService.getUniqueRecordByHQL("SELECT p from Privilege p where p.name='" + name +"'"); 
    }

    public static Users getUserByEmailAndPassword(String email, String password){
	return (Users)sService.getUniqueRecordByHQL("SELECT u from Users u where u.email='" + email +"' and u.password='" + MD5Util.getMD5(password)+"' and u.enabled="+Boolean.TRUE); 
    }
    
    public static Rights getRightsByRoleAndPrivilege(Roles role, String privilege){
	return (Rights)sService.getUniqueRecordByHQL("SELECT r from Rights r where r.roles.id=" + role.getId() + " and r.privilege.code='"+privilege+"'");  
    }
    public static Users getBusinessAccountUserByBusinessId(Long businessId){
	return (Users)sService.getUniqueRecordByHQL("SELECT u from Users u where u.business.id=" + businessId +" and u.businessAccount="+Boolean.TRUE); 
    }
    
    public static Application getApplicationByAuthorizationKey(String authorizationKey) /*throws RecordNotFoundException*/{
	Application foundApplication =  (Application)sService.getUniqueRecordByHQL("SELECT a from Application a where a.authorizationKey='" + authorizationKey +"'"); 
//	if(foundApplication.getId() == null)
//	    throw new RecordNotFoundException("Invalid/no Application Authrisation key supplied!");	
	return foundApplication;
    }
    
    public static Business getBusinessByAurhorizationKey(String authorizationKey) /*throws RecordNotFoundException*/{
	Business foundBusiness = (Business)sService.getUniqueRecordByHQL("SELECT b from Business b where b.authorizationKey='" + authorizationKey +"'"); 
//	if(foundBusiness.getId() == null)
//	    throw new RecordNotFoundException("Invalid/no Business Authorization key supplied!");	
	return foundBusiness;
	
    }
    
    public static Business getParentBusinessById(Long parentBusinessId){
	return (Business)sService.getUniqueRecordByHQL("SELECT b from Business b where b.id=" + parentBusinessId); 
    }
    
    public static Users getUserByAuthorizationKey(String authorizationKey) /*throws UserNotFoundException*/{
	Users foundUser = (Users)sService.getUniqueRecordByHQL("SELECT u from Users u where u.authorizationKey='" + authorizationKey + "' and u.enabled="+Boolean.TRUE); 
//	if(foundUser.getId() == null)
//	    throw new UserNotFoundException("User with the supplied authrisation key was not found!");	
	return foundUser;
    }
        
    public static Collection<?> getItemPropertiesByItemId(Long itemId){
	return (Collection<?>)sService.getAllRecordsByHQL("SELECT p from Property p where p.item.id=" + itemId);  
    }

    public static Collection<?> getProductPropertiesByProductId(Long productId){
	return (Collection<?>)sService.getAllRecordsByHQL("SELECT p from Property p where p.product.id=" + productId);  
    }
    public static Collection<?> getCollectionPropertiesByCollectionId(Long collectionId){
	return (Collection<?>)sService.getAllRecordsByHQL("SELECT p from Property p where p.itemCollection.id=" + collectionId);  
    }
        
    public static Collection<?> getProductIdentifiersByProductId(Long productId){
	return (Collection<?>)sService.getAllRecordsByHQL("SELECT i from Identifier i where i.product.id=" + productId);  
    }
    
    public static Collection<?> getAllLocationByItemId(Long itemId){
	return (Collection<?>)sService.getAllRecordsByHQL("SELECT l from Location l where l.item.id=" + itemId);  
    }
    
    public static Collection<?> getAllLocationByTimeStampItemId(Long itemId, Timestamp to){
	return (Collection<?>)sService.getAllRecordsByHQL("SELECT l from Location l where l.item.id=" + itemId + " and l.timeStamp < "+to);  
    } 
    
    public static Collection<?> getActivitiesByEntityAndRecordId(String entity, String recordId){
	return (Collection<?>)sService.getAllRecordsByHQL("SELECT a from Activity a where a.targetEntity='" + entity + "' and a.recordId='"+recordId+"' and a.deleted="+Boolean.FALSE);
    }    

    public static Collection<?> getChildBusinessesByParentBusinessId(Long businessId){
	String hql = "select b from Business b where b.parentBusiness.id ="+businessId;
	return (Collection<?>)sService.getAllRecordsByHQL(hql);
	
    }
    
    public static Collection<?> getChildBusinessesByParentBusinessIds(Long[] businessIds){
	String hql = "select distinct b from Business b where b.parentBusiness.id in (" + longArrayToString(businessIds) + ")";
	logger.debug(hql);
	return (Collection<?>)sService.getAllRecordsByHQL(hql);	
    }
    
    public static String longArrayToString(Long[] businessIds){
	StringBuffer sb = new StringBuffer();
	for(Long id : businessIds){
	    sb.append(",");
	    sb.append(id.toString());	    
	}
	sb.deleteCharAt(0);
	return sb.toString();
    }
    
    public static Collection<?> getPrivilegesByRole(Roles role){
	String hql = "select p.code from Privilege p where p.id in (select ri.privilege.id from Rights ri where ri.roles.id = "+role.getId()+")";
	return (Collection<?>)sService.getAllRecordsByHQL(hql);
    }
    
    public static Collection<?> executeSearch(Business currentBusiness, String entity, String field, String searchTerm){
	if(field.equalsIgnoreCase("fn")){
	    field = "functionalName";
	}
	String busnessQuery = (entity.equalsIgnoreCase("product") || entity.equalsIgnoreCase("item"))? "and x.business.id="+currentBusiness.getId() : "";
	String hql = "select distinct x." + field + " from " + entity + " x where upper(x." + field + ") like upper('%" + searchTerm + "%') "+busnessQuery+" order by x."+field;
//	logger.debug(">>>>>>>>>> query : "+hql);
	return (Collection<?>)sService.getAllRecordsByHQL(hql);
    }
    
    public static Collection<?> getCustomFiledByActivityAndType(Activity activity, String type){
	return (Collection<?>)sService.getAllRecordsByHQL("SELECT c from CustomField c where c.type='" + type + "' and c.activity.id="+activity.getId());  
    }
    
    public static Property getPropertyByItemIdAndKey(Long itemId, String key){
	return (Property)sService.getUniqueRecordByHQL("SELECT p from Property p where p.item.id=" + itemId + " and key='" + key + "'");  
    }
    
    public static Location getLocationByItemIdAndId(Long itemId, Long id){
	return (Location)sService.getUniqueRecordByHQL("SELECT l from Location l where l.item.id=" + itemId + " and l.id=" + id);  
    }
    
    public static Location getLastKnownLocationForItem(Item item){
	String hql = "SELECT l FROM Location l WHERE l.timeStamp IN (SELECT MAX(timeStamp) FROM Location where item.id = " + item.getId() + ")";
        return (Location)sService.getUniqueRecordByHQL(hql);  
    }     
    
    public static Roles getUserRoleByAuthority(String authority){
	String hql = "SELECT u FROM Roles u WHERE u.authority = '" + authority + "'";
        return (Roles)sService.getUniqueRecordByHQL(hql);  
    }     
    
    public static Product getProductByName(String name){
	String hql = "SELECT p FROM Product p WHERE p.functionalName = '" + name + "')";
        return (Product)sService.getUniqueRecordByHQL(hql);  
    }    
    
    public static Product getProductByNameAndBusinessId(String name, Long bussinessId){
	String hql = "SELECT p FROM Product p WHERE p.functionalName = '" + name + "' and p.business.id=" + bussinessId + ")";
        return (Product)sService.getUniqueRecordByHQL(hql);  
    }
    
    public static Property getPropertyByKey(String key){
	String hql = "SELECT p FROM Property p WHERE p.key = '" + key + "')";
        return (Property)sService.getUniqueRecordByHQL(hql);  
    }
    
    public static void deleteRoleRights(Long roleId){ 
	String hql = "DELETE Rights r WHERE r.roles.id = :roleId";
	Map<String, Object> queryParams = new HashMap<String, Object>();
	queryParams.put("roleId", roleId);
	sService.executeHQLUpdate(hql, queryParams);
    }
}
