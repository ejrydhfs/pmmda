/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectImpl;
import ch.bbv.dog.LightweightObject;
import ch.bbv.mda.operators.ModelOperators;

/**
 * The meta element is the root of all meta element class of our MDA model. The
 * MDA model hides the particularities of the various persistency format and is
 * optimized for tagged values manipulation in a rich semantic context. In the
 * long term the mda class hierarchy will be migrated to the UML or MOF
 * metamodel.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class MetaElement extends DataObjectImpl implements DataObject,
    LightweightObject {

  /* %% USER DEFINED DECLARATION START %% */

  /**
   * The length of a formatted JavaDoc comment line.
   */
  public static final int DOC_LINE_LENGTH = 80;

  /**
   * Defines a comparator for meta element based on their name as comparison
   * criteria.
   */
  public static final MetaElementNameComparator NAME_COMPARATOR;

  static {
    NAME_COMPARATOR = new MetaElementNameComparator();
  }

  /* %% USER DEFINED DECLARATION END %% */

  /**
   * Name of the UML/MDA element.
   * @serial
   */
  private String name;

  /**
   * The description of the MDA/UML element. The description explains the role
   * and responsibilities of the element to the human reader.
   * @serial
   */
  private String description;

  /**
   * Context property of the item. The value is handled in related accessors and
   * does not need to be persisted but reflection directly manipulating fields
   * do not call the accessors.
   * @serial
   */
  private transient MetaElement context;

  /**
   * Visibility property of the class.
   * @serial
   */
  private VisibilityCode visibility;

  /**
   * Stereotype property of the class.
   * @serial
   */
  private String stereotype;

  /**
   * Flag indicating if the package belongs to the standard packages of the
   * environment or not.
   * @serial
   */
  private boolean system;

  /**
   * Tagged values of the element indexed with the tag.
   * @serial
   */
  private Map<String, TaggedValue> taggedValues;

  /**
   * List of computed attributes of the meta elements. These derived values are
   * transient.
   */
  private transient Map<String, String> attributes;

  /**
   * Default constructor of the class.
   */
  public MetaElement() {
    taggedValues = new HashMap<String, TaggedValue>();
    attributes = new HashMap<String, String>();
    visibility = VisibilityCode.getCode(VisibilityCode.PUBLIC_ID);
  }

  /**
   * Returns true if the package belongs to the standards ones otherwise false.
   * @return the value of the flag.
   * @see #setSystem(boolean)
   */
  public final boolean isSystem() {
    return system;
  }

  /**
   * Sets the new value of the package.
   * @param system true if the package is a standard one otherwise false.
   * @see #isSystem()
   */
  public final void setSystem(boolean system) {
    this.system = system;
  }

  /**
   * Returns the name of the element.
   * @return name of the element.
   * @see #setName(String)
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the element.
   * @param name new value of the name.
   * @see #getName()
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the description of the element.
   * @return description of the element.
   * @see #setDescription(String)
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the element.
   * @param description new value of the description.
   * @see #getDescription()
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the context of the element if defined otherwise null.
   * @return context of the element.
   * @see #setContext(MetaElement)
   */
  public MetaElement getContext() {
    return context;
  }

  /**
   * Sets the context of the element.
   * @param context new context of the context.
   * @see #getContext()
   */
  public void setContext(MetaElement context) {
    this.context = context;
  }

  /**
   * Returns the attribute with the given key.
   * @param key key of the attribute.
   * @return the requested attribute if found otherwise null.
   * @pre key != null
   */
  public String getAttribute(String key) {
    assert key != null;
    return attributes.get(key);
  }

  /**
   * Adds the attribute with the given key. If a previous attribute was defined
   * it is overwritten.
   * @param key key of the attribute.
   * @param attribute the value of the attribute.
   * @pre (key != null) && (attribute != null)
   */
  public void addAttribute(String key, String attribute) {
    assert ((key != null) && (attribute != null));
    attributes.put(key, attribute);
  }

  /**
   * Reutrns all the attributes defined in the element.
   * @return the list of attributes defined in the element.
   */
  public List<String> getAllAttributes() {
    return new ArrayList<String>(attributes.values());
  }

  /**
   * Removes all attributes.
   */
  public void removeAllAttributes() {
    attributes.clear();
  }

  /**
   * Returns the visibility of the element.
   * @return visibility of the element.
   * @see #setVisibility
   */
  public VisibilityCode getVisibility() {
    return visibility;
  }

  /**
   * Sets the visibility of the element.
   * @param visibility new value of the visibility.
   * @see #getVisibility
   */
  public void setVisibility(VisibilityCode visibility) {
    this.visibility = visibility;
  }

  /**
   * Returns the stereotype of the element.
   * @return stereotype of the element.
   * @see #setStereotype
   */
  public String getStereotype() {
    return stereotype;
  }

  /**
   * Sets the stereotype of the element.
   * @param stereotype new value of the stereotype.
   * @see #getStereotype
   */
  public void setStereotype(String stereotype) {
    this.stereotype = stereotype;
  }

  /**
   * Returns the tagged value with the given tag.
   * @param tag name of the tagged value to retrieve.
   * @return the requested tagged value if found otherwise null.
   * @pre tag != null
   * @see #setTaggedValue(String, TaggedValue)
   */
  public TaggedValue getTaggedValue(String tag) {
    assert tag != null;
    return taggedValues.get(tag);
  }

  /**
   * Sets the tagged value with the given tag. The tagged value is either
   * inserted or replace the old value.
   * @param tag name of the tagged value to add.
   * @param taggedValue tagged value to add.
   * @pre (tag != null) && tag.equals(taggedValue.getTag()
   * @see #getTaggedValue(String)
   */
  public void setTaggedValue(String tag, TaggedValue taggedValue) {
    assert (tag != null) && tag.equals(taggedValue.getTag());
    TaggedValue oldValue = getTaggedValue(tag);
    if (oldValue != null) {
      oldValue.setContext(null);
    }
    taggedValue.setContext(this);
    taggedValues.put(tag, taggedValue);
    propertyChangeSupport.firePropertyChange("taggedValues", oldValue,
        taggedValue);
  }

  /**
   * Adds the tagged value.
   * @param taggedValue the tagged value to add.
   */
  public void addTaggedValue(TaggedValue taggedValue) {
    setTaggedValue(taggedValue.getTag(), taggedValue);
  }

  /**
   * Removes the tagged value associated with the tag.
   * @param tag the tag of the value to remove.
   * @return the removed value if found otherwise null.
   */
  public TaggedValue removeTaggedValue(String tag) {
    TaggedValue oldValue = taggedValues.remove(tag);
    if (oldValue != null) {
      oldValue.setContext(null);
    }
    propertyChangeSupport.firePropertyChange("taggedValues", oldValue, null);
    return oldValue;
  }

  /**
   * Returns all the tagged values defined in the element.
   * @return list of all defined tagged values.
   */
  public List<TaggedValue> getTags() {
    return new ArrayList<TaggedValue>(taggedValues.values());
  }

  public LightweightObject deepCopy() {
    return null;
  }

  // %% BEGIN USER CODE

  /**
   * Returns the root of the model.
   * @return the root of the model to which the element belongs.
   */
  public MetaElement getRoot() {
    MetaElement context = this;
    while (context.getContext() != null) {
      context = context.getContext();
    }
    return context;
  }

  /**
   * Returns the model containing the element.
   * @return the model containing the element.
   */
  public MetaModel getModel() {
    return (MetaModel) getRoot();
  }

  /**
   * Convenience method returning the name of element with the first letter
   * uppercase.
   * @return the name of the element formatted as requested.
   */
  public String nameFirstUpper() {
    return ModelOperators.identifierFirstUpper(getName());
  }

  /**
   * Convenience method returning the name of element with the first letter
   * lowercase.
   * @return the name of the element formatted as requested.
   */
  public String nameFirstLower() {
    return ModelOperators.identifierFirstLower(getName());
  }

  /**
   * Convenience method returning the name of element uppercase.
   * @return the name of the element formatted as requested.
   */
  public String nameUpper() {
    return ModelOperators.identifierUpper(getName());
  }

  /**
   * Convenience method returning the name of element in the singular form.
   * @return the name of the element formatted as requested.
   */
  public String nameSingular() {
    return ModelOperators.identifierSingular(getName());
  }

  /**
   * Convenience method returning the name of element in the plural form.
   * @return the name of the element formatted as requested.
   */
  public String namePlural() {
    return ModelOperators.identifierPlural(getName());
  }

  /**
   * Convenience method returning the name of the element in the singular form
   * and the first letter uppercase.
   * @return the name of the element formatted as requested.
   */
  public String nameFirstUpperSingular() {
    return ModelOperators.identifierFirstUpper(nameSingular());
  }

  /**
   * Convenience method returning the name of the element in the plural form and
   * the first letter uppercase.
   * @return the name of the element formatted as requested.
   */
  public String nameFirstUpperPlural() {
    return ModelOperators.identifierFirstUpper(namePlural());
  }

  /**
   * Returns the qualified name of the component by walking through the context
   * hierarchy. The context of the model is ignored because no relevant for the
   * qualified name.
   * @return the qualified name of the meta element.
   */
  public String qualifiedName() {
    StringBuilder qualifiedName = new StringBuilder();
    qualifiedName.append(getName());

    MetaElement context = getContext();
    while ((context != null) && (context.getContext() != null)) {
      qualifiedName.insert(0, ".");
      qualifiedName.insert(0, context.getName());
      context = context.getContext();
    }
    return qualifiedName.toString();
  }

  /**
   * Returns the class with the qualified name. If the class has no package name
   * it is assumed the class is in the same package as the instance.
   * @param qualifiedName qualified name of the class to find.
   * @return the requested meta class if found otherwise null.
   */
  public MetaClass findClass(String qualifiedName) {
    MetaClass clazz = null;
    if (qualifiedName != null) {
      clazz = getModel().findClass(qualifiedName);
    }
    return clazz;
  }

  /**
   * Formats the comment of the element using the JavaDoc conventions. The
   * length of the line is computed and the text formated. New lines are
   * inserted where needed; no existing new lines are removed.
   * @param offset the offset in source code at the beginning of each line. It
   *          is used to compute the effective maximum line length.
   * @return the comment of the element formatted as requested.
   */
  public List docComment(int offset) {
    List<String> lines = new ArrayList<String>();
    String text = getDescription();
    if ((text != null) && (text.length() > 0)) {
      int docLength = text.length();
      int lineLength = DOC_LINE_LENGTH - offset;
      int start = 0;
      while (start < docLength) {
        int end = Math.min(start + lineLength, docLength);
        int i = start;
        while ((i < end) && text.charAt(i) != '\n') {
          i++;
        }
        if ((i < end) || (text.charAt(i - 1) == '\n')) {
          lines.add(text.substring(start, i));
          i++;
        } else {
          if (end != docLength) {
            // detect previous whitespace used as a line terminator.
            i = end - 1;
            while ((i > start) && !Character.isWhitespace(text.charAt(i))) {
              i--;
            }
            if (i != start) {
              i++;
              lines.add(text.substring(start, i));
            } else {
              i = end;
              lines.add(text.substring(start, end) + '-');
            }
          } else {
            lines.add(text.substring(start));
          }
        }
        start = i;
      }
    }
    return lines;
  }

  public String retrieveVisibilityText() {
    return MdaUtilities.visibilityToString(getVisibility().getCode());
  }

  /**
   * Returns the string value of the tagged value with the given tag. This
   * convenience method is provided to simplify the writing of code generator
   * templates.
   * @param tag tag name which value should be returned.
   * @return the value of the tagged value if found otherwise null.
   * @pre tag != null
   */
  public String valueOfTag(String tag) {
    assert tag != null;
    TaggedValue taggedValue = getTaggedValue(tag);
    String value = null;
    if (taggedValue != null) {
      value = taggedValue.getValue();
    }
    return value;
  }

  /**
   * Returns the boolean value of the requested tag. If the tag does not contain
   * a boolean value false is returned.
   * @param tag name of the tag to retrieve.
   * @return boolean value of the tag if found otherwise false.
   * @pre tag != null
   */
  public boolean booleanValueOfTag(String tag) {
    assert tag != null;
    TaggedValue taggedValue = getTaggedValue(tag);
    return ((taggedValue != null) && taggedValue.getValue().equals("true"));
  }

  /**
   * Set a tagged value with a boolean value.
   * @param tag tag name of the tagged value.
   * @param value value of the tagged value.
   * @see #booleanValueOfTag(String)
   * @pre tag != null
   */
  public void setTaggedValue(String tag, boolean value) {
    assert tag != null;
    setTaggedValue(tag, new TaggedValue(tag, Boolean.toString(value)));
  }

  /**
   * Return the boolean value of a tagged value.
   * @param tag tag name of the requested tagged value.
   * @return the boolean value of the tagged value.
   * @see #setTaggedValue(String, boolean)
   * @pre tag != null
   */
  public boolean booleanValueOfAttribute(String tag) {
    assert tag != null;
    String value = getAttribute(tag);
    return ((value != null) && value.equals("true"));
  }

  public String toString() {
    return qualifiedName();
  }

  /**
   * Finds the first element with the given name if the list.
   * @param list list to search for.
   * @param name name of element to find.
   * @return the requested element if found otherwise null.
   * @pre (list != null) && (name != null)
   */
  protected MetaElement findElement(List< ? extends MetaElement> list,
      String name) {
    assert (list != null) && (name != null);
    for (MetaElement element : list) {
      if (name.equals(element.getName())) {
        return element;
      }
    }
    return null;
  }

  /**
   * Reads an object from a persistent stream and creates all transient
   * properties.
   * @param stream object stream read from.
   * @throws IOException if the object could not be read.
   * @throws ClassNotFoundException if the class cannot be found.
   */
  private void readObject(ObjectInputStream stream) throws IOException,
      ClassNotFoundException {
    stream.defaultReadObject();
    attributes = new HashMap<String, String>();
  }

  // %% END USER CODE
}