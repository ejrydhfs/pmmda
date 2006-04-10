package ch.bbv.notification.server;

/**
 * this class is only used for the example
 */
public class Executor implements Runnable{
	
	QueueSender sender;
	MutableEventInfo info;
	
	public void run(){
		for(int i = 0; i < 5;i++){
			info.setNewValues(i);
			info.setOriginalValues(i);
			info.setChangedProperties(i);
			sender.sendEvent(info);
			try{
				Thread.currentThread().sleep(5000);
			}catch(Exception e){e.printStackTrace();}
		}
	}
	
	public Executor(MutableEventInfo info, QueueSender sender){
		this.info = info;
		this.sender = sender;
		Thread thread = new Thread(this);
		thread.start();
	}

}
