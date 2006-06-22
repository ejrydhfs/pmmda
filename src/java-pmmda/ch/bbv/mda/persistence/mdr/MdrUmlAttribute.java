package ch.bbv.mda.persistence.mdr;

import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.datatypes.MultiplicityRange;

import ch.bbv.mda.Multiplicity;

/**
 * Wrapper Class for a MDR Attribute instance
 * This class provides methods to simplify the access to the mdr model
 * @author UeliKurmann
 * @version $Revision$
 */
public class MdrUmlAttribute extends MdrUmlElement {

    /**
     * the mdr attribute
     */
    Attribute attribute;

    /**
     * the owner class of the attribute
     */
    private MdrUmlClass clazz;

    /**
     * Constructor
     * 
     * @param attribute
     *            the mdr attribute
     * @param clazz
     *            the owner of the attribute
     */
    public MdrUmlAttribute(Attribute attribute, MdrUmlClass clazz) {
        this.attribute = attribute;
        this.clazz = clazz;
    }

    /**
     * Constructor
     * 
     * @param attribute
     *            the mdr attribute
     */
    public MdrUmlAttribute(Attribute attribute) {
        this.attribute = attribute;
        this.clazz = new MdrUmlClass(attribute.getOwner());
    }

    /**
     * returns the owner of the attribute
     * 
     * @return MdrUmlClass the wrapped mdr class
     */
    public MdrUmlClass getOwner() {
        return clazz;
    }

    /**
     * returns the name of the attribute
     * 
     * @return String the name of the attribute
     */
    public String getName() {
        return attribute.getName();
    }

    /**
     * returns the type of the attribute
     * 
     * @return MdrUmlClass the wrapped mdr class
     */
    public MdrUmlClass getType() {
        try {
            return new MdrUmlClass(attribute.getType());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * sets the type of the attribute
     * 
     * @param type
     *            a wrapped mdr class
     */
    public void setType(MdrUmlClass type) {
        attribute.setType((Classifier) type.getModelElement());
    }

    /**
     * returns the multiplicity of the attribute
     * 
     * @return Multiplicity
     */
    public Multiplicity getMultiplicity() {
        return MdrUtilities.convertMultiplicity(attribute.getMultiplicity());
    }

    /**
     * sets the multiplicity of the attribute
     * 
     * @param multiplicity
     *            a mdr multiplicity instance
     */
    public void setMultiplicity(
            org.omg.uml.foundation.datatypes.Multiplicity multiplicity) {
        attribute.setMultiplicity(multiplicity);
    }

    /**
     * sets and creates a new multiplicity instance
     * 
     * @param lowerBound
     *            lower bound of the multiplicity
     * @param upperBound
     *            upper bound of the multiplicity
     */
    public void setMultiplicity(int lowerBound, int upperBound) {

        org.omg.uml.foundation.datatypes.Multiplicity multiplicity = attribute
                .getMultiplicity();
        MultiplicityRange range = (MultiplicityRange) multiplicity.getRange()
                .iterator().next();

        range.setUpper(upperBound);
        range.setLower(lowerBound);
    }

    /**
     * sets the multiplicity
     * 
     * @param multiplicity
     *            a pmMda multiplicity instance
     */
    public void setMultiplicity(Multiplicity multiplicity) {
        setMultiplicity(multiplicity.getLowerBound(), multiplicity
                .getUpperBound());
    }

    /**
     * returns the name of the attribute
     * 
     * @return String the name of the attribute
     */
    public String getQualifiedName() {
        return getName();
    }

    /**
     * returns the initial value of the attribute
     * 
     * @return String the initial value of the attribute
     */
    public String getInitialValue() {
        try {
            return attribute.getInitialValue().getBody();
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * sets the initial value of the attribute
     * 
     * @param language
     *            the language of the initial value
     * @param body
     *            the value of the initial value
     */
    public void setInitialValue(String language, String body) {
        try {
            attribute.getInitialValue().setLanguage(language);
            attribute.getInitialValue().setBody(body);
        } catch (Exception e) {

        }
    }

    /**
     * returns the mdr attribute
     * 
     * @return mdrAttribute the mdr attribute
     */
    public ModelElement getModelElement() {
        return attribute;
    }
}
