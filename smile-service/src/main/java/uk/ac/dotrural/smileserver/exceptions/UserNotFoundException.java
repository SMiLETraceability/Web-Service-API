/**
 * 
 */
package uk.ac.dotrural.smileserver.exceptions;

/**
 * @author Charles Ofoegbu
 *
 */
public class UserNotFoundException extends Exception{
    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String message){
	super(message);
    }
}
