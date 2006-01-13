package ch.bbv.notification.server;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ch.bbv.notification.communication.EventInfo;

/**
 * This class provides a easy interface to send Events to the queues.
 * @author Bruno Persi
 */
public class QueueSender {

	private static final String JNDI_QUEUE_PREFIX = "queue/";
	private String _queue;
	
	/**
	 * create a new instance
	 * @param queue the queue name as <code>String</code>
	 * @pre assert queue != null
	 */
	public QueueSender(String queue) {
		assert queue != null;
		_queue = queue;
	}
	
	/**
	 * send the Event given by argument to the queue which is specified by the constructor
	 * @param info the Event as <code>EventInfo</code>
	 * @pre info != null
	 */
	public void sendEvent(EventInfo info){
		assert info != null;
		InitialContext ic = null;
		QueueConnection con = null;
		QueueSession ses = null;
		javax.jms.QueueSender sender = null;
		try {
			ic = new InitialContext();
			QueueConnectionFactory factory = (QueueConnectionFactory)ic.lookup("jms/QueueConnectionFactory");
			con = factory.createQueueConnection();
			ses = con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = (Queue) ic.lookup(JNDI_QUEUE_PREFIX + _queue);
			sender = ses.createSender(queue);
			con.start();
			ObjectMessage msg = ses.createObjectMessage(info);
			sender.send(msg);
		} catch (NamingException ne){
			ne.printStackTrace();
			throw new IllegalStateException(ne.getMessage());
		} catch (JMSException jmse){
			jmse.printStackTrace();
			throw new IllegalStateException(jmse.getMessage());
		} finally {
			try {
				if (ic != null)ic.close();
				if (con != null)con.close();
				if (ses != null)ses.close();
				if (sender != null)sender.close();
			} catch(Exception e){/*forget it */}
		}
	}

}
