/* 
 * copyright 2000-2005 BBV Software Services AG
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 */
package ch.bbv.notification.server;

import ch.bbv.notification.communication.EventInfo;

/**
 * This class is an extension to EventInfo. This extension is requried while the EventInfo is an ValueObject<br>
 * ValueObjects are normally Imutable, but in this case, we need the setter methods which this class provides.
 * @author Bruno Persi
 */
public class MutableEventInfo extends EventInfo {

	MutableEventInfo(EventInfo info) {
		super(info.getSubjectId(), info.getSubjectName(), info.getEventAction());
	}
	
	protected void setChangedProperties(Object...properties){
		super.setChangedProperties(properties);
	}
	
	protected void setNewValues(Object...values){
		super.setNewValues(values);
	}
	
	protected void setOriginalValues(Object...values){
		super.setOriginalValues(values);
	}

}
