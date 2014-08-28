/**
 * 
 */
package uk.ac.dotrural.smileserver.common;

/**
 * @author Charles Ofoegbu
 *
 */
public class Validator {
    
    public static boolean validateAllMandatoryStringParametersIsNotNull(String ... args){
	for(String arg : args)
	    if(arg == null)
		throw new IllegalArgumentException("One of the mandatory parameters was not supplied!");
	return true;
    }
    
    public static boolean validateNotNullorEmptyString(String param){
	if(param == null || param == "")
	    throw new IllegalArgumentException("Null or empty empty string argument supplied!");
	return true;
    }
}
