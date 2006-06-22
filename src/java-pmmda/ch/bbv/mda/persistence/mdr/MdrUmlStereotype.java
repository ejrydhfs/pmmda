package ch.bbv.mda.persistence.mdr;

import org.omg.uml.foundation.core.Stereotype;

/**
 * Wrapper Class for a MDR Stereotype instance
 * This class provides methods to simplify the access to the mdr model
 * @author UeliKurmann
 * @version $Revision$
 */

public class MdrUmlStereotype {

    private Stereotype stereotype;
    
    /**
     * Constructor
     * @param stereotype
     */
    public MdrUmlStereotype(Stereotype stereotype){
        this.stereotype = stereotype;
    }
    
    /**
     * Returns the stereotype
     * @return stereotype
     */
    public Stereotype getStereotype(){
        return stereotype;
    }
    
    /**
     * Sets the stereotype
     * @return stereotype
     */
    public String getName(){
        return stereotype.getName();
    }   
}
