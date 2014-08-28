/**
 * 
 */
package uk.ac.dotrural.smileserver.common;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Charles Ofoegbu
 *
 */
public class ValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void testValidateAllMandatoryStringParametersIsNotNull() {
	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("One of the mandatory parameters was not supplied!");
	Validator.validateAllMandatoryStringParametersIsNotNull("parm1","parm2",null,"parm4");
	
	assertTrue(Validator.validateAllMandatoryStringParametersIsNotNull("parm1","parm2","parm3","parm4"));
    }

    
    @Test
    public void testValidateNotNullorEmptyString() {
	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("Null or empty empty string argument supplied!");
	Validator.validateNotNullorEmptyString(null);
	
	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("Null or empty empty string argument supplied!");
	Validator.validateNotNullorEmptyString("");
	
	assertTrue(Validator.validateNotNullorEmptyString("parm1"));
    }
	

}
