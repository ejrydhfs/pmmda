package ch.bbv.unittests.ejb3;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.bbv.application.Application;
import ch.bbv.application.ComponentException;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.LightweightObjectHandlerImpl;
import ch.bbv.dog.remote.ejb3.EjbClient;
import ch.bbv.unittests.tests.*;

@RunWith(Suite.class)
@SuiteClasses({PropertiesTest.class, IndexedPropertyTest.class})
public class ClientApplication extends Application {

	@BeforeClass
	public static void startInstance() throws ComponentException {
		ClientApplication client = new ClientApplication();
		client.start();
	}
	
	private void start() throws ComponentException { 
		
		register(this);

		DataObjectMgr manager = new DataObjectMgr(new EjbClient(EJBApplication.DATASOURCE),
									new LightweightObjectHandlerImpl(), null, EJBApplication.DATASOURCE);

		manager.getDataObjectHandler().openDatasource(EJBApplication.DATASOURCE, false);

		ch.bbv.unittests.props.TypeFactory factory = new ch.bbv.unittests.props.TypeFactory(manager);
		factory.registerAllTypes();

		ch.bbv.unittests.indexedprops.TypeFactory ipFactory = new ch.bbv.unittests.indexedprops.TypeFactory(manager);
		ipFactory.registerAllTypes();
		
		setDataObjectMgr(manager);

		initialize();
		startup();
	}
}
