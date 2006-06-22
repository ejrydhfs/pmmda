package ch.bbv.mda.persistence.mdr;

import java.util.Iterator;

import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;

import ch.bbv.mda.VisibilityCode;
/**
 * Utilities for converting between mda/mdr and vice versa
 * 
 * @author UeliKurmann
 * @version $Revision$
 */
public class MdrUtilities {

    /**
     * Converts the visibility modifier from mdr to the pmmda model
     * 
     * @param visibilityKind
     *            of the mdr model
     * @return visibilityCode of the pmmda model
     */
    public static VisibilityCode convertVisibility(VisibilityKind visibilityKind) {
       
        VisibilityCode visibility = VisibilityCode.getCode(VisibilityCode.PUBLIC_ID);

        if (visibilityKind == VisibilityKindEnum.VK_PUBLIC) {
            visibility = VisibilityCode.getCode(VisibilityCode.PUBLIC_ID);
        } else if (visibilityKind == VisibilityKindEnum.VK_PROTECTED) {
            visibility = VisibilityCode.getCode(VisibilityCode.PROTECTED_ID);
        } else if (visibilityKind == VisibilityKindEnum.VK_PRIVATE) {
            visibility = VisibilityCode.getCode(VisibilityCode.PRIVATE_ID);
        } else if (visibilityKind == VisibilityKindEnum.VK_PACKAGE) {
            visibility = VisibilityCode.getCode(VisibilityCode.PACKAGE_ID);
        } 

        return visibility;
    }

    /**
     * Converts the visibility modifier from pmmda to the mdr model
     * 
     * @param visibility
     *            of the pmmda model
     * @return visibility of the mdr model
     */
    public static VisibilityKind convertVisibility(VisibilityCode visibility) {
        VisibilityKind vis = null;

        if(visibility == null){
            System.out.println("Visibility ist null. MDA2MDR");
        }
        
        if (VisibilityCode.PUBLIC_ID == visibility.getCode()) {
            vis = VisibilityKindEnum.VK_PUBLIC;
        } else if (VisibilityCode.PROTECTED_ID == visibility.getCode()) {
            vis = VisibilityKindEnum.VK_PROTECTED;
        } else if (VisibilityCode.PRIVATE_ID == visibility.getCode()) {
            vis = VisibilityKindEnum.VK_PRIVATE;
        } else if (VisibilityCode.PACKAGE_ID == visibility.getCode()) {
            vis = VisibilityKindEnum.VK_PACKAGE;
        }
        return vis;
    }

    /**
     * Converts the multiplicity from mdr to pmmda model
     * 
     * @param multiplicity
     *            in mdr model
     * @return multiplicity in pmmda model
     */
    public static ch.bbv.mda.Multiplicity convertMultiplicity(
            Multiplicity multiplicity) {
        ch.bbv.mda.Multiplicity mdaMultiplicity = null;
        MultiplicityRange range = null;
        try {
            range = (MultiplicityRange) multiplicity.getRange().iterator()
                    .next();
            mdaMultiplicity = new ch.bbv.mda.Multiplicity(range.getLower(),
                    range.getUpper());
        } catch (RuntimeException e) {
            // ignore NullPointerExceptions etc.
        }
        return mdaMultiplicity;
    }

    /**
     * Returns the full qualified name of a classifier
     * 
     * @param clazz
     *            the classifier
     * @return full qualified name as string
     */
    public static String getFullQualifiedName(Classifier clazz) {
        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getName());
        Namespace namespace = clazz.getNamespace();
        while ((namespace != null) && (namespace.getNamespace() != null)) {
            sb.insert(0, namespace.getName() + ".");
            namespace = namespace.getNamespace();
        }
       
        return sb.toString();
    }

    /**
     * Extracts a tagged value with specified name out of a model element
     * 
     * @param source
     *            the model element
     * @param tagName
     *            the name of the tagged value
     * @return the tagged value
     */
    public static org.omg.uml.foundation.core.TaggedValue getTag(
            ModelElement source, String tagName) {
        assert ((source != null) && (tagName != null));
        org.omg.uml.foundation.core.TaggedValue tag = null;
        for (Iterator i = source.getTaggedValue().iterator(); i.hasNext();) {
            tag = (org.omg.uml.foundation.core.TaggedValue) i.next();

            if (tagName.equals(tag.getName())) {
                break;
            }
            tag = null;
        }
        return tag;
    }

}
