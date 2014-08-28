/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

/**
 * @author Charles Ofoegbu
 *
 */


public class BirthdayPojo{
	private Integer day;
	private Integer month;
	private Integer year;
	 
	public BirthdayPojo(){
	    
	}
	
	public Integer getDay() {
	    return day;
	}
	public void setDay(Integer day) {
	    this.day = day;
	}
	public Integer getMonth() {
	    return month;
	}
	public void setMonth(Integer month) {
	    this.month = month;
	}
	public Integer getYear() {
	    return year;
	}
	public void setYear(Integer year) {
	    this.year = year;
	}
	
	public String toString(){
	    return new StringBuilder().append(this.year).append(".").append(this.month).append(".").append(this.day).toString();
	}
}