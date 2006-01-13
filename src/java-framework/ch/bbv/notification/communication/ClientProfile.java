package ch.bbv.notification.communication;

import java.io.Serializable;

/**
 * A value object to transport the clientinfos to the registerhandler
 * @author Bruno Persi
 */
public class ClientProfile implements Serializable{
	
	private String _clientIdentification;
	private EventInfo _info;
	
	/**
	 * returns a new <code>ClientProfile</code>
	 * @param clientIdentification the client name as <code>String</code> these value is needed to creating the jms queue which has the same name as the client
	 * @param info the information to the event as <code>EventInfo</code>
	 * @pre clientIdentification != null && info != null
	 */
	public ClientProfile(String clientIdentification, EventInfo info){
		assert clientIdentification != null && info != null;
		_clientIdentification = clientIdentification;
		_info = info;
	}

	/**
	 * get the client identification
	 * @return the clientIdentification as <code>String</code>
	 */
	public String getClientIdentification() {
		return _clientIdentification;
	}

	/**
	 * get the event information
	 * @return event information as <code>EventInfo</code>
	 */
	public EventInfo getInfo() {
		return _info;
	}

}
