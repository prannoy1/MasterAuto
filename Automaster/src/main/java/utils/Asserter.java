package utils;

import org.apache.log4j.Logger;
import org.testng.Assert;

public class Asserter  {
	final static Logger logger = Logger.getLogger(Asserter.class);

	public static void assertEquals(Object actual, Object expected, String message) {
		logger.info("Actual value ["+actual+"] Expected value ["+expected+"]");
		Assert.assertEquals(actual, expected,message);
	}	
	
	public static void fail(String message) {
		Assert.fail(message);
	}	
	
	public static void assertNotNull(Object actual,String message) {
		logger.info("Actual value ["+actual+"], Value is expected to be not null");
		Assert.assertNotNull(actual);
	}	
	
	public static void assertNull(Object actual,String message) {
		logger.info("Actual value ["+actual+"] Expected value ["+null+"]");
		Assert.assertNull(actual);
	}
	
	public static void assertEquals(Object actual, Object expected) {
		Assert.assertEquals(actual, expected);		
	}

	public static void assertTrue(boolean value, String message) {
		Assert.assertTrue(value,message);		
	}	
}
