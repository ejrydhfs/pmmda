/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistency.nsuml;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValueImpl;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MPackage;
import ru.novosoft.uml.model_management.MPackageImpl;
import ch.bbv.mda.*;

/**
 * The MDA transformer defines rules how to transform a MDA element into the
 * corresponding NS-UML representation. The NSUML representation is XMI 1.0 /
 * UML 1.3 compatible. A feature allows the controlled removal of superfluous
 * tagged values from the NSUML model. A list of tag names, which should be
 * removed can be specified and passed to the transformer. During the
 * synchronization all tags with such names will be removed from the NSUML
 * model.
 * @author marcelbaumann
 * @version $Revision$
 */
public class MdaTransformer {
  /**
   * UML modeling tool using NSUML library stores the documentation of an item
   * in a tagged value named documentation. This tag is the standard one defined
   * in the UML 1.4 notation.
   */
  public static final String DOCUMENTATION_TAG = "documentation";

  /**
   * UML modeling tool using NSUML library stores the volatile flag of an item
   * in a tagged value named volatile.
   */
  public static final String VOLATILE_TAG = "volatile";

  /**
   * UML modeling tool using NSUML library stores the transient flag of an item
   * in a tagged value named transient.
   */
  public static final String TRANSIENT_TAG = "transient";

  private static final String TRUE_STRING = "TRUE";

  private static final String FALSE_STRING = "FALSE";

  /**
   * Maps between the source objects used as keys and the target objects used as
   * values. Source objects are MDA entities, target objects are NSUML
   * instances.
   */
  private Map elements;

  /**
   * Default constructor of the class.
   */
  public MdaTransformer() {
    elements = new HashMap();
  }

  public void synchronize(MetaModel metaModel, MModel model) {
    for (Iterator i = metaModel.getPackages().iterator(); i.hasNext();) {
      extractPackage((MetaPackage) i.next(), model);
    }
  }

  protected void extractPackage(MetaPackage metaPackage, MModel context) {
    assert ((metaPackage != null) && (context != null));

    String qualifiedName = metaPackage.getName();
    MPackage mPackage = createPackage(context, qualifiedName);
    elements.put(metaPackage, mPackage);
    extractTags(metaPackage, mPackage);

    for (Iterator i = metaPackage.getClasses().iterator(); i.hasNext();) {
      extractClass((MetaClass) i.next(), mPackage);
    }
  }

  protected void extractClass(MetaClass clazz, MPackage context) {
    assert ((clazz != null) && (context != null));

    MClass mClass = createClass(context, clazz.getName());
    elements.put(clazz, mClass);
    mClass.setAbstract(clazz.isAbstract());
    extractTags(clazz, mClass);

    for (Iterator i = clazz.getProperties().iterator(); i.hasNext();) {
      extractProperty((MetaProperty) i.next(), mClass);
    }

    for (Iterator i = clazz.getIndexedProperties().iterator(); i.hasNext();) {
      extractIndexedProperty((MetaIndexedProperty) i.next(), mClass);
    }
  }

  protected void extractProperty(MetaProperty property, MClass context) {
    assert ((property != null) && (context != null));
    if (property.isAggregation()) {
      MetaPackage metaPackage = (MetaPackage) property.getContext()
          .getContext();
      MPackage mPackage = (MPackage) elements.get(metaPackage);
      MAssociation association = findAssociation(mPackage, context, property
          .getName());
      if (association != null) {
        updatePropertyFlags(property, association);
        extractTags(property, association);
      }
    } else {
      MAttribute mAttribute = createAttribute(context, property.getName());
      MMultiplicity mMultiplicity = mAttribute.getMultiplicity();
      MMultiplicity newMultiplicity = convert(property.getMultiplicity());
      if ((newMultiplicity != null) && (mMultiplicity != newMultiplicity)) {
        mAttribute.setMultiplicity(newMultiplicity);
      }
      updateInitialValue(property, mAttribute);
      updatePropertyFlags(property, mAttribute);
      extractTags(property, mAttribute);
    }
  }

  protected void extractIndexedProperty(MetaIndexedProperty property,
      MClass context) {
    assert ((property != null) && (context != null));
    if (property.isAggregation()) {
      MetaPackage metaPackage = (MetaPackage) property.getContext()
          .getContext();
      MPackage mPackage = (MPackage) elements.get(metaPackage);
      MAssociation association = findAssociation(mPackage, context, property
          .getName());
      if (association != null) {
        updatePropertyFlags(property, association);
        extractTags(property, association);
      }
    } else {
      MAttribute attribute = createAttribute(context, property.getName());
      MMultiplicity multiplicity = attribute.getMultiplicity();
      MMultiplicity newMultiplicity = convert(property.getMultiplicity());
      if ((newMultiplicity != null) && (multiplicity != newMultiplicity)) {
        attribute.setMultiplicity(newMultiplicity);
      }
      updatePropertyFlags(property, attribute);
      extractTags(property, attribute);
    }
  }

  /**
   * Extracts the tagged values from the source and updates the corresponding
   * item. As a side effect the obsolete tagged values are removed from the
   * item.
   * @param source MDA source item containing the tags to synchronize.
   * @param item target item to synchronize.
   */
  public void extractTags(MetaElement source, MModelElement item) {
    String description = source.getDescription();
    if ((description != null) && (description.length() > 0)) {
      MTaggedValue taggedValue = createTag(item, DOCUMENTATION_TAG);
      taggedValue.setValue(description);
    } else {
      MTaggedValue taggedValue = findTag(item, DOCUMENTATION_TAG);
      if (taggedValue != null) {
        taggedValue.setValue("");
      }
    }

    for (Iterator i = source.getTags().iterator(); i.hasNext();) {
      TaggedValue tag = (TaggedValue) i.next();
      String tagName = tag.getTag();
      String tagValue = tag.getValue();
      if (tagValue != null) {
        MTaggedValue taggedValue = createTag(item, tagName);
        taggedValue.setValue(tag.getValue());
      }
    }

    for (Iterator i = MetaFactory.getInstance().getObsoleteTypes(
        source.getClass()).iterator(); i.hasNext();) {
      TaggedValueType type = (TaggedValueType) i.next();
      item.removeTaggedValue(type.getTag());
    }
  }

  public MTaggedValue findTag(MModelElement item, String tagName) {
    assert ((item != null) && (tagName != null));

    MTaggedValue tag = null;
    for (Iterator i = item.getTaggedValues().iterator(); i.hasNext();) {
      tag = (MTaggedValue) i.next();
      if (tagName.equals(tag.getTag())) {
        break;
      }
      tag = null;
    }
    return tag;
  }

  public MTaggedValue createTag(MModelElement item, String tagName) {
    assert ((item != null) && (tagName != null));

    MTaggedValue taggedValue = findTag(item, tagName);
    if (taggedValue == null) {
      taggedValue = new MTaggedValueImpl();
      taggedValue.setTag(tagName);
      item.addTaggedValue(taggedValue);
    }
    return taggedValue;
  }

  /**
   * Creates the package with the given qualified name. All necessary surounding
   * packages are created. If the package already exists no creation action is
   * performed.
   * @param model model containing the package.
   * @param qualifiedName qualified name of the package to create.
   * @return the requested package either found or newly created.
   */
  public MPackage createPackage(MModel model, String qualifiedName) {
    assert ((model != null) && (qualifiedName != null));

    MPackage mPackage = null;
    StringTokenizer tokenizer = new StringTokenizer(qualifiedName, ".");
    MPackage context = model;
    while (tokenizer.hasMoreTokens()) {
      String name = tokenizer.nextToken();
      mPackage = findPackage(context, name);
      if (mPackage == null) {
        mPackage = new MPackageImpl();
        mPackage.setName(name);
        context.addOwnedElement(mPackage);
      }
      context = mPackage;
    }
    return mPackage;
  }

  /**
   * Returns the package with the given qualified name.
   * @param qualifiedName qualified name of the package.
   * @param model model containing the package.
   * @return the requested package if found otherwise null.
   */
  public MPackage findPackage(MModel model, String qualifiedName) {
    assert ((model != null) && (qualifiedName != null));

    MPackage mPackage = null;
    StringTokenizer tokenizer = new StringTokenizer(qualifiedName, ".");
    MPackage context = model;
    while (tokenizer.hasMoreTokens()) {
      String name = tokenizer.nextToken();
      mPackage = findPackage(context, name);
      if (mPackage == null) {
        break;
      }
      context = mPackage;
    }
    return mPackage;
  }

  public MPackage findPackage(MPackage context, String name) {
    assert ((context != null) && (name != null));

    MPackage mPackage = null;
    if (name.equals(context.getName())) {
      mPackage = context;
    } else {
      for (Iterator i = new FilterIterator(context.getOwnedElements(),
          MPackage.class); i.hasNext();) {
        mPackage = (MPackage) i.next();
        if (name.equals(mPackage.getName())) {
          break;
        }
        mPackage = null;
      }
    }
    return mPackage;
  }

  public MClass findClass(MPackage context, String name) {
    assert ((context != null) && (name != null));

    MClass mClass = null;
    for (Iterator i = new FilterIterator(context.getOwnedElements(),
        MClass.class); i.hasNext();) {
      mClass = (MClass) i.next();
      if (name.equals(mClass.getName())) {
        break;
      }
      mClass = null;
    }
    return mClass;
  }

  public MClass createClass(MPackage context, String name) {
    assert ((context != null) && (name != null));

    MClass item = findClass(context, name);
    if (item == null) {
      item = new MClassImpl();
      item.setName(name);
      context.addOwnedElement(item);
    }
    return item;
  }

  public MAttribute findAttribute(MClass context, String name) {
    assert ((context != null) && (name != null));

    MAttribute mAttribute = null;
    for (Iterator i = new FilterIterator(context.getFeatures(),
        MAttribute.class); i.hasNext();) {
      mAttribute = (MAttribute) i.next();
      if (name.equals(mAttribute.getName())) {
        break;
      }
      mAttribute = null;
    }
    return mAttribute;
  }

  public MAttribute createAttribute(MClass context, String name) {
    assert ((context != null) && (name != null));

    MAttribute item = findAttribute(context, name);
    if (item == null) {
      item = new MAttributeImpl();
      item.setName(name);
      context.addOwnedElement(item);
    }
    return item;
  }

  public MAssociation findAssociation(MPackage context, MClass clazz,
      String name) {
    assert ((context != null) && (clazz != null) && (name != null));

    MAssociation association = null;
    for (Iterator i = new FilterIterator(context.getOwnedElements(),
        MAssociation.class); i.hasNext();) {
      association = (MAssociation) i.next();

      MAssociationEnd e1 = association.getConnection(0);
      MAssociationEnd e2 = association.getConnection(1);
      MClass owner = (MClass) e1.getType();
      if (owner == clazz) {
        String associationName = e2.getName();
        if ((associationName == null) || (associationName.length() == 0)) {
          associationName = association.getName();
        }
        if (name.equals(associationName)) {
          break;
        }
        association = null;
      }
    }
    return association;
  }

  protected void updatePropertyFlags(MetaProperty property, MModelElement item) {
    assert ((property != null) && (item != null));
    if (property.isVolatile()) {
      MTaggedValue taggedValue = createTag(item, VOLATILE_TAG);
      taggedValue.setValue(TRUE_STRING);
    } else {
      MTaggedValue taggedValue = findTag(item, VOLATILE_TAG);
      if (taggedValue != null) {
        taggedValue.setValue(FALSE_STRING);
      }
    }
    if (property.isTransient()) {
      MTaggedValue taggedValue = createTag(item, TRANSIENT_TAG);
      taggedValue.setValue(TRUE_STRING);
    } else {
      MTaggedValue taggedValue = findTag(item, TRANSIENT_TAG);
      if (taggedValue != null) {
        taggedValue.setValue(FALSE_STRING);
      }
    }
  }

  protected void updateInitialValue(MetaProperty property, MAttribute attribute) {
    String initialValue = property.getInitialValue();
    MExpression expression = attribute.getInitialValue();
    if (expression == null) {
      if (initialValue != null) {
        attribute.setInitialValue(new MExpression("Java", initialValue));
      }
    } else {
      if (expression.getBody() != initialValue) {
        attribute.setInitialValue(new MExpression("Java", initialValue));
      }
    }
  }

  protected boolean isEqual(Multiplicity metaMultiplicity,
      MMultiplicity mMultiplicity) {
    assert ((metaMultiplicity != null) && (mMultiplicity != null));

    boolean isEqual = false;
    if ((metaMultiplicity != null) && (mMultiplicity != null)) {
      isEqual = (metaMultiplicity.getLowerBound() == mMultiplicity.getLower())
          && (metaMultiplicity.getUpperBound() == mMultiplicity.getUpper());
    }
    return isEqual;
  }

  protected MMultiplicity convert(Multiplicity metaMultiplicity) {
    MMultiplicity mMultiplicity = null;
    int lowerBound = metaMultiplicity.getLowerBound();
    int upperBound = metaMultiplicity.getUpperBound();
    if (lowerBound == 0) {
      if (upperBound == 1) {
        mMultiplicity = MMultiplicity.M0_1;
      } else if (upperBound == Multiplicity.N_MULTIPLICITY) {
        mMultiplicity = MMultiplicity.M0_N;
      }
    } else if (lowerBound == 1) {
      if (upperBound == 1) {
        mMultiplicity = MMultiplicity.M1_1;
      } else if (upperBound == Multiplicity.N_MULTIPLICITY) {
        mMultiplicity = MMultiplicity.M1_N;
      }
    }
    return mMultiplicity;
  }
}