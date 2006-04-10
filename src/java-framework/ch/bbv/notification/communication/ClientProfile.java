/* 
 * copyright 2000-2005 BBV Software Services AG
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 */
package ch.bbv.notification.communication;

import java.io.Serializable;

/**
 * A value object to transport the clientinfos to the registerhandler
 * @author Bruno Persi
 */
public class ClientProfile implements Serializable{
	
	private String _clientIdentification;
	private Object _updateable;
	private EventInfo _info;
	
	@Override public String toString(){
		StringBuilder builder = new StringBuilder(1100);
		builder.append(_clientIdentification);
		builder.append(_info);
		return builder.toString();
	}
	
	
	@Override public boolean equals(Object obj){
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof ClientProfile)) return false;
		ClientProfile p = (ClientProfile)obj;
		return this._clientIdentification.equals(p._clientIdentification) 
			&& this._info.equals(p._info);
	}
	
	@Override public int hashCode(){
		return _clientIdentification.hashCode() ^ _info.hashCode();
	}
	
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
	 * returns a new <code>ClientProfile</code> use in constructor in direct mode case
	 * @param clientIdentification the client name as <code>String</code> these value is needed to creating the jms queue which has the same name as the client
	 * @param info the information to the event as <code>EventInfo</code>
	 * @param updateable the reference object to the updateable
	 * @pre clientIdentification != null && info != null
	 */
	public ClientProfile(String clientIdentification, EventInfo info, Object updateable){
		this(clientIdentification,info);
		assert updateable != null;
		_updateable = updateable;
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
	
	/**
	 * get the updateable object, this method is used only on direct case
	 * @return the updateable as <code>Object</code>
	 */
	public Object getUpdateable(){
		return _updateable;
	}

}
