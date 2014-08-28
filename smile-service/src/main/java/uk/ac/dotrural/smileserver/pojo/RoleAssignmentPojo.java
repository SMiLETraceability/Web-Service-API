/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

/**
 * @author Charles Ofoegbu
 *
 */
public class RoleAssignmentPojo {

    private Long userId;
    private Long roleId;
    
    public RoleAssignmentPojo(){}
    
    public Long getUserId() {
	return userId;
    }
    public void setUserId(Long userId) {
	this.userId = userId;
    }
    public Long getRoleId() {
	return roleId;
    }
    public void setRoleId(Long roleId) {
	this.roleId = roleId;
    }
}
