/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import uk.ac.dotrural.smile.entity.application.Application;
import uk.ac.dotrural.smile.entity.business.Business;
import uk.ac.dotrural.smile.entity.users.Users;

/**
 * @author Charles Ofoegbu
 *
 */
public class ApplicationStatePojo {
    private Application currentApplication; 
    private Users currentUser;
    private Business currentBusiness;
    private ExceptionPojo exceptionPojo;
    private Boolean authorizationStatus;
    
    public Application getCurrentApplication() {
	return currentApplication;
    }
    public void setCurrentApplication(Application currentApplication) {
	this.currentApplication = currentApplication;
    }
    public Users getCurrentUser() {
	return currentUser;
    }
    public void setCurrentUser(Users currentUser) {
	this.currentUser = currentUser;
    }
    public Business getCurrentBusiness() {
	return currentBusiness;
    }
    public void setCurrentBusiness(Business currentBusiness) {
	this.currentBusiness = currentBusiness;
    }
    public ExceptionPojo getExceptionPojo() {
	return exceptionPojo;
    }
    public void setExceptionPojo(ExceptionPojo exceptionPojo) {
	this.exceptionPojo = exceptionPojo;
    }
    public Boolean getAuthorizationStatus() {
	return authorizationStatus;
    }
    public void setAuthorizationStatus(Boolean authorizationStatus) {
	this.authorizationStatus = authorizationStatus;
    }

}