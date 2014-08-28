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
public class MD5UtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getMD5(){
	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("String to hash must not be null!");
	MD5Util.getMD5(null);	
    
	assertTrue(MD5Util.getMD5("test").equals("98f6bcd4621d373cade4e832627b4f6"));
    }
    

}
