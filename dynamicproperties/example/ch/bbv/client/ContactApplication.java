/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ch.bbv.application.Application;
import ch.bbv.application.ComponentException;
import ch.bbv.application.Locator;
import ch.bbv.dog.CacheHandler;
import ch.bbv.dog.DataObjectHandler;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.LightweightObjectHandlerImpl;
import ch.bbv.dog.persistence.hibernate.PersistenceHandlerHib;
import ch.bbv.utilities.ReflectUtilities;

public class ContactApplication extends Application {

	private static Logger log = Logger.getLogger(ContactApplication.class.toString());

	public ContactApplication() throws ComponentException {
		DataObjectMgr manager = null;
		register(this);
		log.setLevel(Level.ALL);
		log.info("Application started in direct mode");
		log.info("Application started with Hibernate");
		
		manager = new DataObjectMgr(new CacheHandler(
				new PersistenceHandlerHib(), true, true),
				new LightweightObjectHandlerImpl(), null, "TEST");
		
		manager.getDataObjectHandler().openDatasource("TEST", false);
		
		setDataObjectMgr(manager);
		ContactExplorer.registerNodeTypes();
		ContactExplorer.create("Dynamic Property Test Application");
		initialize();
		startup();
	}

	public static ContactApplication getApplication() {
		return (ContactApplication) Locator.getInstance();
	}

	public static void main(String[] args) throws ComponentException {
		new ContactApplication();
	}

	protected final DataObjectHandler createHandler(String qualifiedName) {
		assert qualifiedName != null;
		return (DataObjectHandler) ReflectUtilities
				.createInstance(qualifiedName);
	}
}