/* 
 * copyright 2000-2005 BBV Software Services AG
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 */
package ch.bbv.notification.server;

import javax.management.MBeanServer;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.jboss.mx.util.MBeanServerLocator;
import org.jboss.mq.server.jmx.DestinationManagerMBean;



/**
 * This is the application server specific Queuecreater class<br>
 * while with standard J2EE is not possible create Queues at runtime and add
 * these to jndi<br>
 * 
 * @author Bruno Persi
 * 
 */
class JBossQueueCreater implements QueueCreater {

	private final static String JBOSS_DESTINATION_MANAGER_JNDI = "jboss.mq:service=DestinationManager";

	private DestinationManagerMBean _mbean;

	public JBossQueueCreater() {
		ObjectName objectName;
		try{
			objectName = new ObjectName(JBOSS_DESTINATION_MANAGER_JNDI);
		} catch(MalformedObjectNameException moe){
			moe.printStackTrace();
			throw new IllegalStateException("JNDI for lookup Jboss destinationmanager is not correctly: " + JBOSS_DESTINATION_MANAGER_JNDI);
		}
		// find the local MBeanServer
		MBeanServer server = MBeanServerLocator.locateJBoss();

		// get a type-safe dynamic proxy
		_mbean = (DestinationManagerMBean) MBeanServerInvocationHandler
			.newProxyInstance(server, objectName,DestinationManagerMBean.class, false);
	}

	public void createQueue(String queueName) {
		assert queueName != null;
		// Use the proxy to dynamically create a queue
		// by providing the queue name and the JNDI location
		try{
			_mbean.createQueue(queueName, JNDI_PREFIX + queueName);
		} catch (Exception e){
			//i know i know, catch Exception is ugly, but see the interface from DestinationManager.createQueue
			throw new IllegalStateException(e);
		}
	}

}
