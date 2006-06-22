package ch.bbv.mda.persistence.mdr;

import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Parameter;

/**
 * Wrapper Class for a MDR Parameter instance
 * This class provides methods to simplify the access to the mdr model
 * @author UeliKurmann
 * @version $Revision$
 */

public class MdrUmlParameter extends MdrUmlElement {

    private Parameter parameter;
    
    /**
     * Constructor
     * @param parameter
     */
    public MdrUmlParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public String getName() {
        return parameter.getName();
    }
    
    public String getQualifiedName() {
        return MdrUtilities.getFullQualifiedName((Classifier)parameter);
    }
    
    /**
     * Returns the type of the parameter
     * @return type the wrapped mdr type
     */
    public MdrUmlClass getType(){
        return new MdrUmlClass(parameter.getType());
    }
    
  
    public ModelElement getModelElement(){
        return parameter;
    }    
}
