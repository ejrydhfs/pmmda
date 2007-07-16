package ch.bbv.performancetests.stat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.persistence.CollectFunctor;
import ch.bbv.dog.updates.DogUpdate;

public class UpdateSerializationCost {
    private static final int INITIAL_STREAM_CAPACITY = 5000;
    
    /** 
	 * Logger of the class.
	 */
	private static Log log = LogFactory.getLog(UpdateSerializationCost.class);
    
	private long actualMemory, runtimeMemory, actualMemoryDOG, runtimeMemoryDOG;   
    private int nObj, nNewObj, nModifiedObj, nDeletedObj;
    private double updateDuration;

	public UpdateSerializationCost(DataObject dataObject, DataObject originalDataObject) {
		// Collect all objects from the original data object graph        
        CollectFunctor functor = CollectFunctor.collect(dataObject);
        nObj = functor.getObjects().size();
        nNewObj = functor.getNewObjects().size();
        nModifiedObj = functor.getUdpatedObjects().size();  

		if (originalDataObject != null) {
			CollectFunctor functorOriginal = CollectFunctor.collect(originalDataObject);
			nDeletedObj = functor.getMissingObjectsFrom(functorOriginal.getObjects()).size();
		} else {
			nDeletedObj = 0;
		}
        
        // Create and serialize a Dog update object
        DogUpdate dogUpdate = new DogUpdate(dataObject);
        
        ByteArrayOutputStream serializationStream = new ByteArrayOutputStream(INITIAL_STREAM_CAPACITY);
        ObjectOutputStream ooStream;

        try {
	        ooStream = new ObjectOutputStream(serializationStream);

	        runGC();

	        long startMemory = getUsedMemory();

	        ooStream.writeObject(dogUpdate);

	        long endMemory = getUsedMemory();

	        runtimeMemory = endMemory - startMemory;
	        actualMemory = serializationStream.size();
        } catch (IOException ex) {
        	log.error("Serialization of data object updates failed", ex);
        }

        // To be able to better assess the performance gain through the use of diff/update objects, we also calculate
        // the cost of serializing the entire data object graph.
        serializationStream = new ByteArrayOutputStream(INITIAL_STREAM_CAPACITY);
        try {
	        ooStream = new ObjectOutputStream(serializationStream);

	        runGC();

	        long startMemory = getUsedMemory();

	        ooStream.writeObject(dataObject);
	        
	        long endMemory = getUsedMemory();

	        runtimeMemoryDOG = endMemory - startMemory;
	        actualMemoryDOG = serializationStream.size();
        } catch (IOException ex) {
        	log.error("Serialization of entire data object graph failed", ex);
        }
    }

    public long getActualMemory() {
        return actualMemory;
    }

    public long getRuntimeOverheadMemory() {
        return runtimeMemory - actualMemory;
    }

    public long getActualMemoryDifference() {
    	return actualMemoryDOG - actualMemory;
    }

    public long getRuntimeOverheadMemoryDifference() {
    	return (runtimeMemoryDOG - runtimeMemory) - getActualMemoryDifference();
    }

    public int getNumObjects() {
        return nObj;
    }

    public int getNumNewObjects() {
        return nNewObj;
    }
    
    public int getNumModifiedObjects() {
        return nModifiedObj;
    }
    
    public int getNumDeletedObjects() {
        return nDeletedObj;
    }

    public double getUpdateDuration() {
        return updateDuration;
    }
        
    public void setUpdateDuration(double duration) {
    	updateDuration = duration;
    }

	private void runGC() {
		// Try to run the GC several times, and wait afterwards. It's bit kludge-y, but
		// it increases the probability that (most) garbage is collected upon method exit.
        for (int i = 0; i < 4; i++) {
        	Runtime.getRuntime().gc();		
        }

        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// Should not happen
		}
	}

	private long getUsedMemory() {	
		Runtime r = Runtime.getRuntime();
		return r.totalMemory() - r.freeMemory();
	}
}
