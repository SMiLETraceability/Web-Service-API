/**
 * 
 */
package uk.ac.dotrural.smileserver.listeners;


import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.dotrural.smile.ServiceLocator;
import uk.ac.dotrural.smile.entity.activitytype.ActivityType;
import uk.ac.dotrural.smile.entity.application.Application;
import uk.ac.dotrural.smile.entity.privilege.Privilege;
import uk.ac.dotrural.smile.entity.rights.Rights;
import uk.ac.dotrural.smile.entity.roles.Roles;
import uk.ac.dotrural.smile.service.SmileService;
import uk.ac.dotrural.smileserver.common.MD5Util;
import uk.ac.dotrural.smileserver.common.Constants.Actions;
import uk.ac.dotrural.smileserver.common.Constants.DefaultApplication;
import uk.ac.dotrural.smileserver.common.Constants.Privileges;
import uk.ac.dotrural.smileserver.dao.DataAccessObject;
  

/**
 * Web application lifecycle listener.
 *
 * @author Charles Ofoegbu
 */
public class SystemInitializer implements ServletContextListener {
    
    Logger logger = LoggerFactory.getLogger(SystemInitializer.class);
    private SmileService sService;
    
    public void contextInitialized(ServletContextEvent sce) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Inside SMiLE SystemCompnentInitializer <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        sService = serviceLocator.getSmileService();   
        createUserRoles();
        createDefaultApplications();
        createDefaultActivityTypes();
        createPrivilages();
        logger.debug("found user roles : >>>>>>>>>>>>>>>>>>>>>>>>>> "+sService.getAllRecords(Roles.class).size());    
    }
    

    public void createUserRoles(){
	Roles adminUser = (Roles)DataAccessObject.getUserRoleByAuthority("ADMIN");
	if(adminUser == null){
	    adminUser = new Roles(); 
	    adminUser.setAuthority("ADMIN");
	    adminUser.setCode("Admin");
	    DataAccessObject.createNewRecord(adminUser);
	}
	
    }

    public void createDefaultApplications(){
	for (DefaultApplication app : DefaultApplication.values()) {	    
	    Application application = DataAccessObject.getApplicationByName(app.name());
	    if(application == null){
		Roles role = new Roles();
		role.setAuthority(app.name()+"_APP");
		role.setCode(app.name()+"_APP");
		role = (Roles)DataAccessObject.createNewRecord(role);
		if(role.getCode().equals("DEVELOPER_APP")){
		    createAdminRightsForRole(role);
		}
		application = new Application();
		application.setName(app.name());
		application.setDescription(app.name());
		application.setRoles(role);		
		Timestamp timeStamp = new Timestamp(new Date().getTime());
		String randomUUId = UUID.randomUUID().toString();			
		String authorizationKey = MD5Util.getMD5(app.name()+randomUUId+timeStamp);
		application.setAuthorizationKey(authorizationKey);
		DataAccessObject.createNewRecord(application);
		
	    }
	  }
    }    

    public void createAdminRightsForRole(Roles role){
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
    
    public void createDefaultActivityTypes(){
	for (Actions action : Actions.values()) {	    
	    ActivityType activityType = DataAccessObject.getActivityTypeByName(action.name());
	    if(activityType == null){
		activityType = new ActivityType();
		activityType.setName(action.name());
		activityType.setCode(action.name());
		DataAccessObject.createNewRecord(activityType);
	    }
	  }
    }    
    
    public void createPrivilages(){
	for (Privileges privilage : Privileges.values()) {	
	    Privilege foundPrivilage = DataAccessObject.getPrivilegeByName(privilage.name());
	    if(foundPrivilage == null){
		foundPrivilage = new Privilege();
		foundPrivilage.setName(privilage.name());
		foundPrivilage.setCode(privilage.getCode());
		foundPrivilage.setDescription(privilage.getDescription());
		DataAccessObject.createNewRecord(foundPrivilage);
	    }else{
		foundPrivilage.setCode(privilage.getCode());
		foundPrivilage.setDescription(privilage.getDescription());
		DataAccessObject.updateRecord(foundPrivilage);
	    }
	  }
    }
    
    
    public void contextDestroyed(ServletContextEvent sce) {
	logger.debug("Operation not yet supported...");
    } 
}
 