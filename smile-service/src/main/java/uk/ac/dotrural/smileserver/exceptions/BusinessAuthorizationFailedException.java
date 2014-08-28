/**
 * 
 */
package uk.ac.dotrural.smileserver.exceptions;

/**
 * @author Charles Ofoegbu
 *
 */
public class BusinessAuthorizationFailedException extends Exception{
    private static final long serialVersionUID = 1L;

    public BusinessAuthorizationFailedException(String message){
	super(message);
    }
}
