package ch.bbv.notification.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.bbv.notification.communication.ClientProfile;

public class SubjectObserverPoolDirect {
	
	private static SubjectObserverPoolDirect _instance;
	private Map<String,SubjectObserverDirect> _pool;
	
	private SubjectObserverPoolDirect(){
		_pool = Collections.synchronizedMap(new HashMap<String,SubjectObserverDirect>());
	}
	
	/**
	 * get an instance of this singleton
	 * @return an instance of <code>SubjectObserverPool</code>
	 */
	public static synchronized SubjectObserverPoolDirect getInstance(){
		if (_instance == null){
			_instance = new SubjectObserverPoolDirect();
		}
		return _instance;
	}

	/**
	 * @see ch.bbv.notification.server.SubjectObserverPool#addProfile(ClientProfile)
	 */
	public void addProfile(ClientProfile profile) {
		assert profile != null;
		Thread thr = new Thread(new AdderThread(profile));
		thr.setPriority(Thread.MIN_PRIORITY);
		thr.start();
	}
	
	private class AdderThread implements Runnable{
		
		ClientProfile p;
		
		public AdderThread(ClientProfile p){
			this.p = p;
		}
		
		public void run(){
			if (_pool.containsKey(p.getClientIdentification())){
				_pool.get(p.getClientIdentification()).addProfile(p);
			} else {
				_pool.put(p.getClientIdentification(),new SubjectObserverDirect(p));
			}
		}
	}

	/**
	 * @see ch.bbv.notification.server.SubjectObserverPool#hasClientInformation(ClientProfile)
	 */
	public boolean hasClientInformation(ClientProfile profile) {
		assert profile != null;
		return _pool.containsKey(profile.getClientIdentification());
	}

	/**
	 * @see ch.bbv.notification.server.SubjectObserverPool#hasObserver(ClientProfile)
	 */
	public boolean hasObserver(ClientProfile profile) {
		if (hasClientInformation(profile)){
			return _pool.get(profile.getClientIdentification()).hasEvent(profile.getInfo());
		}
		return false;
	}

	/**
	 * @see ch.bbv.notification.server.SubjectObserverPool#removeProfile(ClientProfile)
	 */
	public void removeProfile(ClientProfile profile) {
		if (hasClientInformation(profile)){
			_pool.get(profile.getClientIdentification()).removeProfile(profile.getInfo());
		}
	}
	
	

}
