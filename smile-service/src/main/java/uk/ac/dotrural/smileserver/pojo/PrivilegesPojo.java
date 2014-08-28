/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import java.util.ArrayList;

/**
 * @author Charles Ofoegbu
 *
 */
public class PrivilegesPojo {
    public PrivilegesPojo(){}
    public PrivilegesPojo( ArrayList<PrivilegePojo> privilegesPojo){
	this.privilegesPojo = privilegesPojo;
    }
    
    private ArrayList<PrivilegePojo> privilegesPojo;

    public ArrayList<PrivilegePojo> getPrivilegePojo() {
	return privilegesPojo;
    }

    public void setPrivilegePojo(ArrayList<PrivilegePojo> privilegesPojo) {
	this.privilegesPojo = privilegesPojo;
    }

}
