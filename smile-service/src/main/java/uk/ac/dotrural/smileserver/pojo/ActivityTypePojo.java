/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

/**
 * @author Charles Ofoegbu
 *
 */
public class ActivityTypePojo {
    private Long id;
    private String name;
    private String code;
    private String[] tags;
    
    public Long getId() {
	return id;
    }
    public void setId(Long id) {
	this.id = id;
    }
    public String getName() {
	return name;
    }
    public void setName(String name) {
	this.name = name;
    }
    public String getCode() {
	return code;
    }
    public void setCode(String code) {
	this.code = code;
    }
    public String[] getTags() {
	return tags;
    }
    public void setTags(String[] tags) {
	this.tags = tags;
    }
}
