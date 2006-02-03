/* 
 * copyright 2000-2005 BBV Software Services AG
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 */
package ch.bbv.notification.client;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ch.bbv.notification.communication.EventInfo;

/**
 * this class wrapps the client side listening to the notification event queues<br>
 * example usage:<br><pre>
 * public class Client extends JFrame implements Updateable {
 * 		//some code
 * 
 * 		
 * 		public void registerThisClient(){
 * 			RegisterHandlerWrapper regHandler = new RegisterHandlerWrapper();
 * 			regHandler.register(clientProfile);
 * 			//client is now registered, but none events can be received at the moment
 * 
 * 			new QueueReceiver(queueName,this);
 * 			//after this line, events will be send to the update method from Updateable interface
 * 		}
 * 		
 * 		public void update(EventInfo info) {
 * 			assert info != null;
 * 			java.awt.EventQueue.invokeLater(new ActionExecutor(e,this));
 * 		}
 * 	
 * 		private class ActionExecutor implements Runnable{
 * 			private EventInfo e;
 * 			private Client client;
 * 
 * 			public void run(){
 * 				//do somthing wie e
 * 			}
 * 
 * 			public ActionExecutor(EventInfo e, Client outerClass){
 * 				this.e = e;
 * 				this.client = outerClass;
 * 			}
 * 		}
 * 
 * }
 * </pre>
 * @see ch.bbv.notification.client.Updateable
 * @author Bruno Persi
 */
public class QueueReceiver implements Runnable{

	private String _queueName;
	private Updateable _obj;
	private Context _jndiContext;
    private QueueConnectionFactory _connectionFactory;
    private QueueConnection _connection;
    private QueueSession _session;
    private Queue _queue;
    private MessageConsumer _consumer;
	
	/**
	 * create a new instance
	 * @param queueName the queuename as <code>String</code>
	 * @param obj the reference to the <tt>Updateable</tt>
	 * @pre queueName != null && obj != null
	 */
	public QueueReceiver(String queueName, Updateable obj) {
		assert queueName != null && obj != null;
		_obj = obj;
		_queueName = queueName;
		initalizeJMS();
		Thread thread = new Thread(this);
		thread.start();
	}
	
	private void initalizeJMS(){
        try {
            _jndiContext = new InitialContext();
        } catch (NamingException e) {
            throw new IllegalStateException("Could not create JNDI API context: " +
                e.toString());
        }

        try {
            _connectionFactory =
                (QueueConnectionFactory) _jndiContext.lookup("QueueConnectionFactory");
            _queue = (Queue)_jndiContext.lookup("queue/"+ _queueName);
        	_connection = _connectionFactory.createQueueConnection();
        	_session = _connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        	_consumer = _session.createReceiver(_queue);
        	_connection.start();
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new IllegalStateException("JNDI API lookup failed: " + e.toString());
        }
	}
	
	private boolean run = true;
	
	public void stoppReceiving(){
		run = false;
	}

	public void run() {
		try{
			try{
				while(run){
					ObjectMessage msg = (ObjectMessage)_consumer.receive();
					msg.acknowledge();
					_obj.update((EventInfo)msg.getObject());
				}
			} catch( JMSException jmse){
				jmse.printStackTrace();
				throw new IllegalStateException("problems occured on JMS operations: " + jmse.getMessage());
			}
		} finally {
			try{
				if (_jndiContext != null )_jndiContext.close();
			    if (_connection != null ){
			    	_connection.stop();
			    	_connection.close();
			    }
			    if (_session != null)_session.close();
			} catch(Exception e){/*forget it*/}
		}
	}
	

}
