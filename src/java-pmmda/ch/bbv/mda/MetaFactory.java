/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda;

import java.util.*;

/**
 * The meta factory class initializes newly created meta element instances. The
 * configured tagged values for a specific subtype of meta element are added to
 * the new instance. This approach decouples the configuration and
 * initialization of tagged values from the core MDA meta model. The
 * initialization algorithm is written in a way to support two complementary
 * functions. First the method is used to populate newly created elements with
 * their tagged values. Second the method is also used to add missing tags when
 * the tag list definition was modified and an old model is migrated to the new
 * definition. As a side effect registered obsolete tagged values are removed
 * from existing items simplifying the cleaning of models when a generator is
 * upgraded.
 * @author marcelbaumann
 * @version $Revision$
 */
public class MetaFactory {
  /**
   * The singleton instance of the meta factory.
   */
  private static MetaFactory factory;

  /**
   * Map associating a class with all the tagged values it supports.
   */
  private Map taggedValueTypes;

  /**
   * Map associating a class with all the obsolete tagged values it had.
   * Obsolete tagged values are automatically removed.
   */
  private Map obsoleteTaggedValueTypes;

  /**
   * Default constructor of the class.
   */
  public MetaFactory() {
    taggedValueTypes = new HashMap();
    obsoleteTaggedValueTypes = new HashMap();
  }

  /**
   * Returns the singleton instance of the class.
   * @return singleton instance of the class.
   */
  public static MetaFactory getInstance() {
    if (factory == null) {
      synchronized (MetaFactory.class) {
        if (factory == null) {
          factory = new MetaFactory();
        }
      }
    }
    return factory;
  }

  /**
   * Registers the list of tagged value types to the given class.
   * @param clazz class owning the tagged value types.
   * @param taggedValueTypes tagged value types to register.
   */
  public void registerTypes(Class clazz, List taggedValueTypes) {
    List types = (List) this.taggedValueTypes.get(clazz);
    if (types == null) {
      this.taggedValueTypes.put(clazz, taggedValueTypes);
    } else {
      types.addAll(taggedValueTypes);
    }
  }

  /**
   * Registers the list of obsolete tagged value types to the given class.
   * @param clazz class owning the tagged value types.
   * @param taggedValueTypes tagged value types to register.
   * @pre clazz != null && taggedValueTypes != null
   */
  public void addObsoleteTypes(Class clazz, List taggedValueTypes) {
    List types = (List) obsoleteTaggedValueTypes.get(clazz);
    if (types == null) {
      obsoleteTaggedValueTypes.put(clazz, taggedValueTypes);
    } else {
      types.addAll(taggedValueTypes);
    }
  }

  /**
   * Returns the list of obsolete tagged value types of the given meta element.
   * @param clazz class of the meta element.
   * @return the list of obsolete tagged value types or an empty list if none is
   *         defined.
   */
  public List getObsoleteTypes(Class clazz) {
    List types = (List) obsoleteTaggedValueTypes.get(clazz);
    if (types != null) {
      types = Collections.unmodifiableList(types);
    } else {
      types = Collections.EMPTY_LIST;
    }
    return types;
  }

  /**
   * Finds the first tagged value type with the given name.
   * @param name name of the tagged value type to find.
   * @return the requested tagged value type if found otherwise null.
   * @pre name != null && name.length() > 0
   */
  public TaggedValueType findType(String name) {
    assert ((name != null) && (name.length() > 0));

    TaggedValueType type = null;
    for (Iterator i = taggedValueTypes.values().iterator(); i.hasNext();) {
      List list = (List) i.next();
      for (Iterator j = list.iterator(); j.hasNext();) {
        type = (TaggedValueType) j.next();
        if (!type.getTag().equals(name)) {
          type = null;
        } else {
          break;
        }
      }
    }
    return type;
  }

  public void initialize(MetaModel element) {
    if (!element.isSystem()) {
      initialize(element, MetaModel.class);
      initialize((MetaPackage) element);
    }
  }

  /**
   * Initialize the element with the defined tagged value types. All missing
   * tagged values are added with their default value and all obsolete ones are
   * removed.
   * @param element meta element to initialize.
   */
  public void initialize(MetaPackage element) {
    if (!element.isSystem()) {
      initialize(element, MetaPackage.class);
    }
  }

  /**
   * Initialize the element with the defined tagged value types. All missing
   * tagged values are added with their default value and all obsolete ones are
   * removed.
   * @param element meta element to initialize.
   */
  public void initialize(MetaClass element) {
    if (!element.isSystem()) {
      initialize(element, MetaClass.class);
    }
  }

  /**
   * Initialize the element with the defined tagged value types. All missing
   * tagged values are added with their default value and all obsolete ones are
   * removed.
   * @param element meta element to initialize.
   */
  public void initialize(MetaProperty element) {
    initialize(element, MetaProperty.class);
  }

  /**
   * Initializes the element with the defined tagged value types. All missing
   * tagged values are added with their default value and all obsolete ones are
   * removed.
   * @param element meta element to initialize.
   */
  public void initialize(MetaIndexedProperty element) {
    initialize(element, MetaIndexedProperty.class);
  }

  /**
   * Initializes the element with the defined tagged value types. All missing
   * tagged values are added with their default value and all obsolete ones are
   * removed. Existing tags are completed with their tagged value type.
   * @param element meta element to initialize.
   * @param clazz class of the meta element to initialize.
   */
  protected void initialize(MetaElement element, Class clazz) {
    List types = (List) taggedValueTypes.get(clazz);
    if (types != null) {
      for (int i = 0; i < types.size(); i++) {
        TaggedValueType type = (TaggedValueType) types.get(i);
        TaggedValue value = element.getTaggedValue(type.getTag());
        if (value == null) {
          element.setTaggedValue(type.getTag(), new TaggedValue(type));
        } else {
          value.setType(type);
        }
      }
    }
    types = (List) obsoleteTaggedValueTypes.get(clazz);
    if (types != null) {
      for (int i = 0; i < types.size(); i++) {
        TaggedValueType type = (TaggedValueType) types.get(i);
        element.removeTaggedValue(type.getTag());
      }
    }
  }
}