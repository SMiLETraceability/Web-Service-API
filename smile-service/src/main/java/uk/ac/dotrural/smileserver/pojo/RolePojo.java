/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import java.util.ArrayList;

import uk.ac.dotrural.smile.entity.roles.Roles;
import uk.ac.dotrural.smileserver.dao.DataAccessObject;

/**
 * @author Charles Ofoegbu
 *
 */
public class RolePojo {
    private String roleName;
    private String roleCode;
    private Long businessId;
    private String[] privilages;
    public RolePojo(){};
    
    public RolePojo(Roles role){
	this.roleCode = role.getCode();
	this.roleName = role.getAuthority();
	if(role.getBusiness() != null){
	    this.businessId = role.getBusiness().getId();
	}
	
	@SuppressWarnings("unchecked")
	ArrayList<String> privileges = (ArrayList<String>) DataAccessObject.getPrivilegesByRole(role);
	if(privileges != null && !privileges.isEmpty()){
	    String[] privilagesString = new String[privileges.size()];
	    int count = 0;
	    for(Object object : privileges){
		privilagesString[count++] = object.toString();
	    }
	    this.privilages = privilagesString;
	}
    };
    
    public String getRoleName() {
	return roleName;
    }
    public void setRoleName(String roleName) {
	this.roleName = roleName;
    }
    public String getRoleCode() {
	return roleCode;
    }
    public void setRoleCode(String roleCode) {
	this.roleCode = roleCode;
    }
    public Long getBusinessId() {
	return businessId;
    }
    public void setBusinessId(Long businessId) {
	this.businessId = businessId;
    }
    public String[] getPrivilages() {
	return privilages;
    }
    public void setPrivilages(String[] privilages) {
	this.privilages = privilages;
    }
}
