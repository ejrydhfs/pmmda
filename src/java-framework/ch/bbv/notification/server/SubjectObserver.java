/* 
 * copyright 2000-2005 BBV Software Services AG
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 */
package ch.bbv.notification.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import ch.bbv.application.Locator;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectManager;
import ch.bbv.notification.communication.ClientProfile;
import ch.bbv.notification.communication.EventInfo;

/**
 * the SubjectObserver is the central observer for the client on all DataObjects<br>
 * each client has only one SubjectObserver, these observe all from the client requested DataObjects<br>
 * after the creation, the SubjectObserver creates an jms queue with the name: <code>ClientProfile.getClientIdentification()</code><br>
 * then the client can connect as listener to the queue. 
 * @author Bruno Persi
 */
public class SubjectObserver implements PropertyChangeListener{
	
	private List<ClientProfile> _profiles;
	
	/**
	 * create a new SubjectObserver
	 * @param profile the ClientInformation as <code>ClientProfile</code>
	 * @pre profile != null
	 */
	SubjectObserver(ClientProfile profile){
		assert profile != null;
		_profiles = new ArrayList<ClientProfile>(20);
		_profiles.add(profile);
		QueueCreaterFactory factory = QueueCreaterFactory.getInstance();
		QueueCreater creater = factory.getQueueCreater();
		creater.createQueue(profile.getClientIdentification());
		//the example
		if(profile.getClientIdentification().equals("client")){
			fake(profile);
		}
	}
	
	//for example
	private void fake(ClientProfile prof){
		QueueSender sender = new QueueSender(prof.getClientIdentification());
		MutableEventInfo info = new MutableEventInfo(prof.getInfo());
		info.setChangedProperties("changed", "value");
		info.setNewValues("this is the new Value");
		info.setOriginalValues("this is the old value");
		sender.sendEvent(info);
		new Executor(info,sender);
	}
	
	/**
	 * add a new profile to these Observer
	 * @param profile the ClientInformation as <code>ClientProfile</code>
	 * @pre profile != null
	 */
	public void addProfile(ClientProfile profile){
		assert profile != null;
		if (_profiles.contains(profile)){
			return;
		}
		_profiles.add(profile);
		EventInfo event = profile.getInfo();
		int id = event.getSubjectId();
		getDOG(id).addPropertyChangeListener(this);
	}
	
	private DataObject getDOG(int id){
		DataObjectManager manager = (DataObjectManager) Locator.getInstance().getComponent(DataObjectManager.NAME);
		//TODO:PEB pass datasource name!
		return manager.getDataObjectHandler().retrieve("orm-dnm",DataObject.class,id);
	}
	
	/**
	 * remove an profile from these Observer
	 * @param info the Event to be removed as <code>EventInfo</code>
	 * @pre assert info != null
	 */
	public void removeProfile(EventInfo info){
		assert info != null;
		for(int i = 0, l = _profiles.size();i<l;i++){
			if(_profiles.get(i).getInfo().equals(info)){
				Object o = _profiles.remove(i);
				getDOG(info.getSubjectId()).removePropertyChangeListener(this);
				o = null;
				break;
			}
		}
	}

	/**
	 * @see java.beans.PropertyChangeEvent.propertyChange(PropertyChangeEvent evt)
	 * @pre evt instanceof DataObjectPropertyChangeEvent
	 */
	public void propertyChange(PropertyChangeEvent event) {
		DataObject dog = (DataObject)event.getSource();
		//TODO:PEB check not only id, check name to, change to standart interface
		ClientProfile profile = this.getProfile(dog.getId());
		MutableEventInfo info = new MutableEventInfo(profile.getInfo());
		info.setChangedProperties(event.getPropertyName());
		info.setNewValues(event.getNewValue());
		info.setOriginalValues(event.getOldValue());
		QueueSender sender = new QueueSender(profile.getClientIdentification());
		sender.sendEvent(info);
	}
	
	private ClientProfile getProfile(Integer subjectId){
		for (ClientProfile profile : _profiles) {
			if(profile.getInfo().getSubjectId() == subjectId){
				return profile;
			}
		}
		throw new IllegalStateException("None ClientProfile registered for BOG: " + subjectId);
	}
	
	/**
	 * get the count of registered ClientProfile
	 * @return the count of registered ClientProfiles as <code>int</code>
	 */
	public int countClientProfiles(){
		return _profiles.size();
	}
	
	private EventInfo[] getEventInfos(){
		EventInfo[] infos = new EventInfo[_profiles.size()];
		//for better performance, use increment loop
		for (int i = 0, l = _profiles.size();i < l; i++) {
			infos[i] = _profiles.get(i).getInfo();
		}
		return infos;
	}
	
	/**
	 * check has the Observer the Event given by argument registered
	 * @param e the Event as <code>EventInfo</code>
	 * @return <code>true</code> if Event found, else <code>false</code>
	 * @pre e != null
	 */
	public boolean hasEvent(EventInfo e){
		assert e != null;
		EventInfo[] infos = getEventInfos();
		for (EventInfo info : infos) {
			if(info.equals(e)) return true;
		}
		return false;
	}
	
	

}
