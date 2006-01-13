package ch.bbv.notification.server;

/**
 * Interface for Application-Server specific implementation of dynamic queueCreator classes
 * @author Bruno Persi
 */
public interface QueueCreater {
	
	/**
	 * JNDI Prefix for queues as String, value is: <code>queue/</code>
	 */
	public final static String JNDI_PREFIX = "queue/";

	/**
	 * Method create a javax.jms.Queue at runtime, and add these with following jndi prefix to<br>
	 * the jndi environment: queues/<code>queueName</Code>
	 * @param queueName the queue name as <Code>String</Code>
	 * @pre queueName != null<br>
	 * @exception IllegalStateException is thrown at each error an queue creation
	 */
	public void createQueue(String queueName);

}