package ch.bbv.performancetests.ejb3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.application.Application;
import ch.bbv.application.ComponentException;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.LightweightObjectHandlerImpl;
import ch.bbv.dog.persistence.hibernate.PersistenceHandlerHib;

public class ServerApplication extends Application {
	/**
	 * Returns the server application singleton instance. The application uses
	 * the locator pattern.
	 * 
	 * @return the server application unique instance
	 */
	public static ServerApplication getApplication() {
		return (ServerApplication) Application.getApplication();
	}

	/**
	 * Default constructor of the class.
	 * 
	 * @throws ComponentException
	 *             if an error occured creating the server application
	 */
	public ServerApplication()
			throws ComponentException {
		Log log = LogFactory.getLog(Application.class);
		log.info("Starting Java EE performance test server...");
		
		DataObjectMgr manager = null;
		register(this);

		manager = new DataObjectMgr(new PersistenceHandlerHib(),
				new LightweightObjectHandlerImpl(), null, EJBApplication.DATASOURCE);
		setDataObjectMgr(manager);

		ch.bbv.performancetests.list.TypeFactory listFactory = new ch.bbv.performancetests.list.TypeFactory(manager);
		listFactory.registerAllTypes();

		ch.bbv.performancetests.binarytree.TypeFactory treeFactory = new ch.bbv.performancetests.binarytree.TypeFactory(manager);
		treeFactory.registerAllTypes();

		initialize();
		startup();

		manager.getDataObjectHandler().openDatasource(ch.bbv.performancetests.ejb3.EJBApplication.DATASOURCE, false);
		
		log.info("Java EE performance test server is running");
	}

	/**
	 * Entry point to the server application.
	 * 
	 * @param args arguments of the client application
	 */
	public static void main(String[] args) {
		try {
			new ServerApplication();
		} catch (ComponentException e) {
			e.printStackTrace();
		}
	}
}
