/* 
 * copyright 2000-2005 BBV Software Services AG
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 */
package ch.bbv.notification.server;

/**
 * This factory encapsulate the applicationserver specific creation of dynamic QueueCreater class.
 * @author Bruno Persi
 */
public class QueueCreaterFactory {

	private static QueueCreaterFactory _instance;

	private Enum _applicationServer;

	private static final String EXCEPTION_TEXT = " is not supported for dynamic queue creation at the moment";

	private QueueCreaterFactory() {
		// TODO:PEB read properties, and initailize _applicationServer
		_applicationServer = ApplicationServer.JBOSS;
	}

	/**
	 * getting an instance
	 * @return a instance of QueueCreaterFactory
	 */
	public static synchronized QueueCreaterFactory getInstance() {
		if (_instance == null) {
			_instance = new QueueCreaterFactory();
		}
		return _instance;
	}

	/**
	 * create a ApplicationServer specific QueueCreater, the following ApplicationServers are supported:<br>
	 * <ul>JBoss</ul>
	 * <ul>WebLogic</ul>
	 * <ul>WebSphere</ul>
	 * <ul>Geronimo</ul>
	 * @return a QueueCreater instance
	 */
	public QueueCreater getQueueCreater() {
		synchronized (_applicationServer) {
			if (_applicationServer.equals(ApplicationServer.JBOSS)) {
				return new JBossQueueCreater();
			} else if (_applicationServer.equals(ApplicationServer.WEBSPHERE)) {
				throw new NotSupportedException(ApplicationServer.WEBSPHERE.name()
						+ EXCEPTION_TEXT);
			} else if (_applicationServer.equals(ApplicationServer.WEBLOGIC)) {
				throw new NotSupportedException(ApplicationServer.WEBLOGIC.name()
						+ EXCEPTION_TEXT);
			} else {
				// Geronimo case
				throw new NotSupportedException(ApplicationServer.GERONIMO.name()
						+ EXCEPTION_TEXT);
			}
		}
	}

}
