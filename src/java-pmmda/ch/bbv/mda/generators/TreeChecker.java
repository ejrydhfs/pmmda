/* 
 * copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.generators;

import ch.bbv.mda.*;

/**
 * The tree checker completes the tagged values list of each meta element in the
 * model. This mechanism is used to dynamically support new cartridges or to
 * migrate models created with older versions of the application. It also
 * completes the tagged values received from the external model and extend them
 * with their tagged value type information. As additional function the checker
 * insures that the standard UML tags - persistence, transient, and volatile -
 * are defined. The persistence check is updated to guarantee that if a class
 * has a persistent property when the class is persistent.
 * @author marcelbaumann
 * @version $Revision$
 */
public class TreeChecker implements Visitor {
  /**
   * The item factory used to initialize the MDA model tag values.
   */
  private ItemFactory itemFactory;

  /**
   * The persistence tag definition for the model.
   */
  private static TagDefinition persistence;

  /**
   * The volatile tag definition for the model.
   */
  private static TagDefinition volatiles;

  /**
   * The transient tag definition for the model.
   */
  private static TagDefinition transients;

  static {
    persistence = new TagDefinition(UmlConstants.PERSISTENCE_TAG, "false",
        Boolean.TYPE);
    volatiles = new TagDefinition(UmlConstants.VOLATILE_TAG, "false",
        Boolean.TYPE);
    transients = new TagDefinition(UmlConstants.TRANSIENT_TAG, "false",
        Boolean.TYPE);

  }

  /**
   * Default constructor of the class.
   */
  public TreeChecker() {
    itemFactory = CartridgeFactory.getInstance().getItemFactory();
  }

  public void visit(MetaModel metaModel) {
    itemFactory.initialize(metaModel, MetaModel.class);
  }

  public void visit(MetaPackage metaPackage) {
    itemFactory.initialize(metaPackage, MetaPackage.class);
  }

  public void visit(MetaClass metaClass) {
    itemFactory.initialize(metaClass, MetaClass.class);
    complete(metaClass);
  }

  public void visit(MetaIndexedProperty property) {
    itemFactory.initialize(property, MetaIndexedProperty.class);
  }

  public void visit(MetaProperty property) {
    itemFactory.initialize(property, MetaProperty.class);
  }

  public void visit(MetaDatatype datatype) {
    // no MDA initialization for such items
  }

  public void visit(TaggedValue taggedValue) {
    // no MDA initialization for such items
  }

  /**
   * Registers all standard defined UML tags in the model. The persistence flag,
   * the volatile flag and the transient flag are handled.
   * @param clazz clazz which tags should be completed.
   */
  private void complete(MetaClass clazz) {
    getOrAddTag(clazz, persistence);
    boolean isPersistent = false;
    for (MetaProperty property : clazz.getProperties()) {
      isPersistent |= complete(property);
    }
    for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
      isPersistent |= complete(property);
    }
    if (isPersistent) {
      clazz.setTaggedValue(UmlConstants.PERSISTENCE_TAG, isPersistent);
    }
  }

  /**
   * Registers all standard defined UML tags in the model. The persistence flag,
   * the volatile flag and the transient flag are handled.
   * @param property property which tags should be completed.
   * @return flag indication if the property is persistent.
   * @pre property != null
   */
  private boolean complete(MetaProperty property) {
    TaggedValue value = getOrAddTag(property, persistence);
    boolean isPersistent = Boolean.parseBoolean(value.getValue());
    getOrAddTag(property, volatiles);
    getOrAddTag(property, transients);
    if (isPersistent) {
      property.setTaggedValue(UmlConstants.VOLATILE_TAG, false);
      property.setTaggedValue(UmlConstants.TRANSIENT_TAG, false);
    }
    property.setVolatile(property.booleanValueOfTag(UmlConstants.VOLATILE_TAG));
    property.setTransient(property
        .booleanValueOfTag(UmlConstants.TRANSIENT_TAG));
    return isPersistent;
  }

  /**
   * Returns the required tag. If not defined the tag is first added.
   * @param element element from which the tag should be retrieved.
   * @param definition tag defintion of the requested tag.
   * @return the requested tag.
   * @pre (element != null) || (definition != null)
   */
  private TaggedValue getOrAddTag(MetaElement element, TagDefinition definition) {
    TaggedValue tag = element.getTaggedValue(definition.getName());
    if (tag == null) {
      tag = new TaggedValue("false", definition);
      element.addTaggedValue(tag);
    }
    return tag;
  }
}