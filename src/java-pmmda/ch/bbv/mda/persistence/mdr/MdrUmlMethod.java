package ch.bbv.mda.persistence.mdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;

/**
 * Wrapper Class for a MDR Method instance
 * This class provides methods to simplify the access to the mdr model
 * @author UeliKurmann
 * @version $Revision$
 */

public class MdrUmlMethod {

    private Operation method;

    private Collection<MdrUmlParameter> parameters;

    private MdrUmlClass clazz;

    public MdrUmlMethod(Operation method, MdrUmlClass clazz) {
        this.method = method;
        this.clazz = clazz;
        this.parameters = new ArrayList<MdrUmlParameter>();

        for (Iterator iter = method.getParameter().iterator(); iter.hasNext();) {
            Parameter parameter = (Parameter) iter.next();
            if (ParameterDirectionKindEnum.PDK_RETURN != parameter.getKind()) {
                parameters.add(new MdrUmlParameter(parameter));
            }
        }
    }

    public MdrUmlClass getOwnerClass(){
        return clazz;
    }
    
    public String getName() {
        return method.getName();
    }

    /**
     * Returns the returnType of the method
     * @return the retunType or null
     */
    public MdrUmlClass getReturnType() {
        for (Iterator iter = method.getParameter().iterator(); iter.hasNext();) {
            Parameter parameter = (Parameter) iter.next();
            if (ParameterDirectionKindEnum.PDK_RETURN == parameter.getKind()) {
                return new MdrUmlClass(parameter.getType());
            }
        }
        return null;
    }
    
    /**
     * setReturnType
     * @param clazz the new return type
     * @author UeliKurmann
     * 
     * TODO: unsafe and dirty...
     */
    public void setReturnType(MdrUmlClass clazz){
        for (Iterator iter = method.getParameter().iterator(); iter.hasNext();) {
            Parameter parameter = (Parameter) iter.next();
            if (ParameterDirectionKindEnum.PDK_RETURN == parameter.getKind()) {
                 parameter.setType((Classifier)clazz.getModelElement());
                 break;
            }
        } 
    }
    
    public boolean isAbstract(){
        return method.isAbstract();
    }
    
    public void setAbstract(boolean isAbstract){
        method.setAbstract(isAbstract);
    }
    
    public boolean isFinal(){
        return method.isLeaf();
    }
    
    public void setFinal(boolean isFinal){
        method.setLeaf(isFinal);
    }
    
    public void addParameter(MdrUmlParameter parameter){
        method.getParameter().add(parameter.getModelElement());
    }

}
