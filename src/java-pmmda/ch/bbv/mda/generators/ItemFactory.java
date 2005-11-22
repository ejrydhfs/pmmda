/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.generators;

import java.beans.PropertyChangeListener;
import java.util.*;

import ch.bbv.dog.DataObject;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.TagDefinition;
import ch.bbv.mda.TaggedValue;

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
public class ItemFactory {
  /**
   * Map associating a class with all the active tagged values it supports.
   */
  private Map<Class, List<TagDefinition>> tagDefinitions;

  /**
   * Map associating a class with all the obsolete tagged values it had.
   * Obsolete tagged values are automatically removed.
   */
  private Map<Class, List<TagDefinition>> obsoleteTaggedValueTypes;

  /**
   * Map associating a class with the listeners interested in.
   */
  private Map<Class, List<PropertyChangeListener>> listeners;

  /**
   * Default constructor of the class.
   */
  public ItemFactory() {
    tagDefinitions = new HashMap<Class, List<TagDefinition>>();
    obsoleteTaggedValueTypes = new HashMap<Class, List<TagDefinition>>();
    listeners = new HashMap<Class, List<PropertyChangeListener>>();
  }

  /**
   * Registers the listener interested in the given class.
   * @param clazz class the listener is interested in.
   * @param listener listener to register.
   * @pre (clazz != null) && (listener != null)
   */
  public void registerListener(Class clazz, PropertyChangeListener listener) {
    assert (clazz != null) && (listener != null);
    if (!listeners.containsKey(clazz)) {
      listeners.put(clazz, new ArrayList<PropertyChangeListener>());
    }
    listeners.get(clazz).add(listener);
  }

  /**
   * Registers the list of tagged value types to the given class.
   * @param clazz class owning the tagged value types.
   * @param taggedValueTypes tagged value types to register.
   * @pre (clazz != null) && (taggedValueTypes != null)
   * @post tagDefinitions.
   */
  public void registerTypes(Class clazz, List<TagDefinition> taggedValueTypes) {
    assert (clazz != null) && (taggedValueTypes != null);
    List<TagDefinition> types = tagDefinitions.get(clazz);
    if (types == null) {
      tagDefinitions.put(clazz, taggedValueTypes);
    } else {
      types.addAll(taggedValueTypes);
    }
  }

  /**
   * Registers the list of obsolete tagged value types to the given class.
   * @param clazz class owning the tagged value types.
   * @param taggedValueTypes tagged value types to register.
   * @pre (clazz != null) && (taggedValueTypes != null)
   */
  public void addObsoleteTypes(Class clazz, List<TagDefinition> taggedValueTypes) {
    assert (clazz != null) && (taggedValueTypes != null);
    List<TagDefinition> types = obsoleteTaggedValueTypes.get(clazz);
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
   * @pre clazz != null
   */
  public List<TagDefinition> getObsoleteTypes(Class clazz) {
    assert clazz != null;
    List<TagDefinition> types = obsoleteTaggedValueTypes.get(clazz);
    if (types != null) {
      types = Collections.unmodifiableList(types);
    } else {
      types = Collections.emptyList();
    }
    return types;
  }

  /**
   * Finds the first tagged value type with the given name.
   * @param name name of the tagged value type to find.
   * @return the requested tagged value type if found otherwise null.
   * @pre name != null && name.length() > 0
   */
  public TagDefinition findType(String name) {
    assert (name != null) && (name.length() > 0);
    for (List<TagDefinition> types : tagDefinitions.values()) {
      for (TagDefinition type : types) {
        if (type.getTag().equals(name)) {
          return type;
        }
      }
    }
    return null;
  }

  /**
   * Initializes the element with the defined tagged value types. All missing
   * tagged values are added with their default value and all obsolete ones are
   * removed. Existing tags are completed with their tagged value type. Finally
   * the tag initialization routines of the cartridge are applied.
   * @param element meta element to initialize.
   * @param clazz class of the meta element to initialize.
   * @pre (element != null) && (clazz != null)
   */
  public void initialize(MetaElement element, Class clazz) {
    assert (element != null) && (clazz != null);
    if (element.isSystem()) {
      return;
    }
    register(clazz, element);
    List<TagDefinition> types = tagDefinitions.get(clazz);
    if (types != null) {
      for (TagDefinition type : types) {
        TaggedValue value = element.getTaggedValue(type.getTag());
        if (value == null) {
          value = new TaggedValue(type);
          element.addTaggedValue(value);
        } else {
          value.setType(type);
        }
        register(TaggedValue.class, value);
      }
    }
    types = obsoleteTaggedValueTypes.get(clazz);
    if (types != null) {
      for (TagDefinition type : types) {
        element.removeTaggedValue(type.getTag());
      }
    }
  }

  private void register(Class clazz, DataObject object) {
    if (listeners.containsKey(clazz)) {
      for (PropertyChangeListener listener : listeners.get(clazz)) {
        object.addPropertyChangeListener(listener);
      }
    }
  }
}