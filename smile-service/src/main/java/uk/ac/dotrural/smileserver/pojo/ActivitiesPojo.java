/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import java.util.ArrayList;


/**
 * @author Charles Ofoegbu
 *
 */
public class ActivitiesPojo {
    private ArrayList<ActivityPojo> activities;

    public ActivitiesPojo(ArrayList<ActivityPojo> activities){
	this.activities = activities;
    }
    public ActivitiesPojo(){
	
    }
    
    public ArrayList<ActivityPojo> getActivities() {
	return activities;
    }

    public void setActivities(ArrayList<ActivityPojo> activities) {
	this.activities = activities;
    }
}
