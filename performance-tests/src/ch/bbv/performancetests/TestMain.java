package ch.bbv.performancetests;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.bbv.performancetests.ejb3.ClientApplication;


@RunWith(Suite.class)
@SuiteClasses(ClientApplication.class)
public class TestMain {

	private static Log log;
	
	@BeforeClass
	public static void setup() {
		//  INITIALIZATION
		log = LogFactory.getLog(TestMain.class);
		log.info("### Starting test ###");
	}
	
	@AfterClass
	public static void cleanup() {
		log.info("### Test terminated ###");
	}
	
	/**
	 *  Used for manual test start-up
	 */
	public static void main(String[] args) {

		try {
			setup();			
			ClientApplication.startInstance();
			cleanup();
		} catch (Exception e) {
			log.error("");
			log.error("***************************************");
			log.error("***           Test failed           ***");
			log.error("Exception occured: ", e);
			log.error("***************************************");
			log.error("");			
		}
		System.console().readLine("Press [Enter] to exit...");
	}

//	@Test
//	public void executeTest() {
//		//  INITIALIZATION
//		Log log = LogFactory.getLog(TestMain.class);
//		try {
//			log.info("### Starting test ###");
//			JUnitCore.runClasses(ClientApplication.class);
//			log.info("### Test terminated successfully ###");
//		}
//		catch (Exception e) {
//			log.error("");
//			log.error("***************************************");
//			log.error("***           Test failed           ***");
//			log.error("Exception occured: ", e);
//			log.error("***************************************");
//			log.error("");
//		}
//	}
}