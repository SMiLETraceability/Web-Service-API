/**
 * 
 */
package uk.ac.dotrural.smileserver.exceptions;

/**
 * @author Charles Ofoegbu
 *
 */

public class NullOrEmptyStringValidationException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public NullOrEmptyStringValidationException(String message){
	super(message);
    }
}
