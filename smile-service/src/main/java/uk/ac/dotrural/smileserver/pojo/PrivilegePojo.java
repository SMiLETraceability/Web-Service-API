/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import uk.ac.dotrural.smile.entity.privilege.Privilege;

/**
 * @author Charles Ofoegbu
 *
 */
public class PrivilegePojo {
    private String privilege;
    private String description;
    public PrivilegePojo(){}
    public PrivilegePojo(Privilege privilege){
	this.privilege = privilege.getName();
	this.description = privilege.getDescription();
    }
    
    public String getPrivilege() {
	return privilege;
    }
    public void setPrivilege(String privilege) {
	this.privilege = privilege;
    }
    public String getDescription() {
	return description;
    }
    public void setDescription(String description) {
	this.description = description;
    }
}
