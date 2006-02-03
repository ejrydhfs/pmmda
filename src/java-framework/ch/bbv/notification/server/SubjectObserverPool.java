/* 
 * copyright 2000-2005 BBV Software Services AG
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 */
package ch.bbv.notification.server;

import java.util.HashMap;
import java.util.Map;

import ch.bbv.notification.communication.ClientProfile;

/**
 * This class holding all SubjectObservers
 * @author Bruno Persi
 */
public class SubjectObserverPool {
	
	private Map<String,SubjectObserver> _pool;
	private static SubjectObserverPool _instance;
	
	private SubjectObserverPool(){
		_pool = new HashMap<String,SubjectObserver>(20);
	}
	
	/**
	 * get an instance of this singleton
	 * @return an instance of <code>SubjectObserverPool</code>
	 */
	public static synchronized SubjectObserverPool getInstance(){
		if (_instance == null){
			_instance = new SubjectObserverPool();
		}
		return _instance;
	}
	
	/**
	 * tell the subjectobserver to listen for the Event given by argument
	 * @param profile the ClientProfile with the EventInfo
	 * @pre profile != null
	 */
	public void addProfile(ClientProfile profile){
		assert profile != null;
		synchronized (_pool) {
			if (_pool.containsKey(profile.getClientIdentification())){
				_pool.get(profile.getClientIdentification()).addProfile(profile);
			} else {
				_pool.put(profile.getClientIdentification(),new SubjectObserver(profile));
			}
		}
	}
	
	/**
	 * remove an Event from the subjectobserver
	 * @param profile the clientprofile
	 * @pre profile != null
	 */
	public void removeProfile(ClientProfile profile){
		if (hasClientInformation(profile)){
			_pool.get(profile.getClientIdentification()).removeProfile(profile.getInfo());
		}
	}
	
	/**
	 * return <Code>true</Code> if for the clientProfile passed by argument an subjectobserver exists.
	 * @param profile the clienten informations as <code>ClientProfile</code>
	 * @return true if the client has an subjectobserver
	 * @pre profile != null
	 */
	public boolean hasClientInformation(ClientProfile profile){
		assert profile != null;
		return _pool.containsKey(profile.getClientIdentification());
	}
	
	/**
	 * check exists an Observer for the client given by argument 
	 * @param profile the ClientInformation as <code>ClientProfile</code>
	 * @return <code>true</code> has an Observer found, else <code>false</code>
	 */
	public boolean hasObserver(ClientProfile profile){
		if (hasClientInformation(profile)){
			return _pool.get(profile.getClientIdentification()).hasEvent(profile.getInfo());
		}
		return false;
	}

}
