package ch.bbv.performancetests.ejb3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.bbv.performancetests.stat.PerformanceTestsDOHWrapper;
import ch.bbv.performancetests.stat.UpdateSerializationCost;
import ch.bbv.performancetests.tests.ListUpdateTest;
import ch.bbv.performancetests.tests.TreeUpdateTest;
import ch.bbv.application.Application;
import ch.bbv.application.ComponentException;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.LightweightObjectHandlerImpl;
import ch.bbv.dog.remote.ejb3.EjbClient;

@RunWith(Suite.class)
@SuiteClasses({ListUpdateTest.class, TreeUpdateTest.class})
public class ClientApplication extends Application {

	private static Log log = LogFactory.getLog(ClientApplication.class);

	@BeforeClass
	public static void startInstance() throws ComponentException {
		ClientApplication client = new ClientApplication();
		client.start();
	}
	
	private void start() throws ComponentException { 
		
		register(this);

		DataObjectMgr manager = new DataObjectMgr(new PerformanceTestsDOHWrapper(new EjbClient(EJBApplication.DATASOURCE)),
									new LightweightObjectHandlerImpl(), null, EJBApplication.DATASOURCE);

		manager.getDataObjectHandler().openDatasource(EJBApplication.DATASOURCE, false);

		ch.bbv.performancetests.list.TypeFactory listFactory = new ch.bbv.performancetests.list.TypeFactory(manager);
		listFactory.registerAllTypes();
		
		ch.bbv.performancetests.binarytree.TypeFactory treeFactory = new ch.bbv.performancetests.binarytree.TypeFactory(manager);
		treeFactory.registerAllTypes();
		
		setDataObjectMgr(manager);

		initialize();
		startup();
	}

	@AfterClass
	public static void printStats() {
		PerformanceTestsDOHWrapper dataHandler = (PerformanceTestsDOHWrapper)Application.getApplication().getDataObjectMgr().getDataObjectHandler(); 
		
		// Report the performance
		log.info("");
		log.info("*** [START] Performance report for update test ***");
		for(UpdateSerializationCost sCost : dataHandler.getUpdateSerializationCosts()) {               
			int nObjects = sCost.getNumObjects();
			int nNewObjects = sCost.getNumNewObjects();
			int nModifiedObjects = sCost.getNumModifiedObjects();
			int nDeletedObjects = sCost.getNumDeletedObjects();
			  
			log.info("    Update properties for data object graph with initially "
		                          + (nObjects - nNewObjects + nDeletedObjects) + " elements:");
			log.info("       Number of new objects: " + nNewObjects);
			log.info("       Number of modified objects: " + nModifiedObjects);
			log.info("       Number of deleted objects: " + nDeletedObjects);
			log.info("       Serialized size of all updates: " + sCost.getActualMemory() + " bytes");
			log.info("       Difference (between entire DOG and DOG update only) of serialized sizes: " + sCost.getActualMemoryDifference() + " bytes");
			log.info("       Additional memory needed by runtime for serialization of updates: " + sCost.getRuntimeOverheadMemory() + " bytes");
			log.info("       Difference (between entire DOG and DOG update only) of additional memory needed by runtime for serialization: " + sCost.getRuntimeOverheadMemoryDifference() + " bytes");
			log.info("       Duration of update: " + sCost.getUpdateDuration() + " seconds");
			log.info("");
		}
		log.info("*** [END] Performance report for update test ***");
	}
}
