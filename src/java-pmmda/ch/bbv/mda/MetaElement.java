/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.util.*;

import ch.bbv.dog.DataObjectImpl;
import ch.bbv.utilities.TextUtilities;

/**
 * The meta element is the root of all meta element class of our MDA model. The
 * MDA model hides the particularities of the various persistency format and is
 * optimized for tagged values manipulation in a rich semantic context. In the
 * long term the mda class hierarchy will be migrated to the UML or MOF
 * metamodel.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class MetaElement extends DataObjectImpl {
  // %% BEGIN USER DECLARATION
  /**
   * Defines a comparator for meta element based on their name as comparison
   * criteria.
   */
  public static final MetaElementNameComparator NAME_COMPARATOR;

  static {
    NAME_COMPARATOR = new MetaElementNameComparator();
  }

  // %% END USER DECLARATION

  /**
   * Context property of the class. The value is handled in related accessors
   * and does not need to be persisted but reflection directly manipulating
   * fields do not call the accessors.
   */
  private MetaElement context;

  /**
   * Name property of the class.
   */
  private String name;

  /**
   * Description property of the class.
   */
  private String description;

  /**
   * Visibility property of the class.
   */
  private VisibilityCode visibility;

  /**
   * Stereotype property of the class.
   */
  private String stereotype;

  /**
   * List of computed attributes of the meta elements. These derived values are
   * transient.
   */
  private transient Map attributes;

  /**
   * Indexed property of the class.
   */
  private Map taggedValues;

  /**
   * Default constructor of the class.
   */
  public MetaElement() {
    init();
  }

  /**
   * Initializes member variables. Only complex types have to be initialized
   * here. The init() method is called after deserializing and loading from xml
   * with XStream. Transient members have to be initialized with this init()
   * function.
   */
  public void init() {
    if (attributes == null)
      attributes = new HashMap();
    if (taggedValues == null)
      taggedValues = new HashMap();
    if (visibility == null)
      visibility = VisibilityCode.getCode(VisibilityCode.PUBLIC_VISIBILITY);
  }

  /**
   * Returns the name of the element.
   * @return name of the element.
   * @see #setName
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the element.
   * @param name new value of the name.
   * @see #getName
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the description of the element.
   * @return description of the element.
   * @see #setDescription
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the element.
   * @param description new value of the description.
   * @see #getDescription
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the context of the element if defined otherwise null.
   * @return context of the element.
   * @see #setContext
   */
  public MetaElement getContext() {
    return context;
  }

  /**
   * Sets the context of the element.
   * @param context new context of the context.
   * @see #getContext
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
    assert (key != null);
    return (String) attributes.get(key);
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

  public List getAllAttributes() {
    return new ArrayList(attributes.values());
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
   * @see #setTaggedValue(String, String)
   */
  public TaggedValue getTaggedValue(String tag) {
    return (TaggedValue) taggedValues.get(tag);
  }

  /**
   * Sets the tagged value with the given tag. The tagged value is either
   * inserted or replace the old value.
   * @param tag name of the tagged value to add.
   * @param taggedValue tagged value to add.
   * @pre (tag != null) && tag.equals(taggedValue.getTag()
   * @see #getTaggedValue
   */
  public void setTaggedValue(String tag, TaggedValue taggedValue) {
    assert ((tag != null) && tag.equals(taggedValue.getTag()));
    taggedValues.put(tag, taggedValue);
  }

  public TaggedValue removeTaggedValue(String tag) {
    return (TaggedValue) taggedValues.remove(tag);
  }

  public List getTags() {
    return new ArrayList(taggedValues.values());
  }

  protected void setTaggedValue(String tag, String value) {
    if (value == null) {
      taggedValues.remove(tag);
    } else {
      taggedValues.put(tag, value);
    }
  }

  // %% BEGIN USER CODE

  public void addTaggedValue(TaggedValue taggedValue) {
    taggedValues.put(taggedValue.getTag(), taggedValue);
  }

  public MetaElement getRoot() {
    MetaElement context = this;
    while (context.getContext() != null) {
      context = context.getContext();
    }
    return context;
  }

  /**
   * Convenience method returning the name of element with the first letter
   * uppercase.
   * @return the name of the element formatted as requested.
   */
  public String nameFirstUpper() {
    return TextUtilities.identifierFirstUpper(getName());
  }

  /**
   * Convenience method returning the name of element with the first letter
   * lowercase.
   * @return the name of the element formatted as requested.
   */
  public String nameFirstLower() {
    return TextUtilities.identifierFirstLower(getName());
  }

  /**
   * Convenience method returning the name of element uppercase.
   * @return the name of the element formatted as requested.
   */
  public String nameUpper() {
    return TextUtilities.identifierUpper(getName());
  }

  /**
   * Convenience method returning the name of element in the singular form.
   * @return the name of the element formatted as requested.
   */
  public String nameSingular() {
    return TextUtilities.identifierSingular(getName());
  }

  /**
   * Convenience method returning the name of element in the plural form.
   * @return the name of the element formatted as requested.
   */
  public String namePlural() {
    return TextUtilities.identifierPlural(getName());
  }

  /**
   * Convenience method returning the name of the element in the singular form
   * and the first letter uppercase.
   * @return the name of the element formatted as requested.
   */
  public String nameFirstUpperSingular() {
    return TextUtilities.identifierFirstUpper(nameSingular());
  }

  /**
   * Convenience method returning the name of the element in the plural form and
   * the first letter uppercase.
   * @return the name of the element formatted as requested.
   */
  public String nameFirstUpperPlural() {
    return TextUtilities.identifierFirstUpper(namePlural());
  }

  /**
   * Returns the qualified name of the component by walking through the context
   * hierarchy. The context of the model is ignored because no relevant for the
   * qualified name.
   * @return the qualified name of the meta element.
   */
  public String qualifiedName() {
    StringBuffer qualifiedName = new StringBuffer();
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
      clazz = ((MetaModel) getRoot()).findClass(qualifiedName);
    }
    return clazz;
  }

  /**
   * Formats the comment of the element using the JavaDoc conventions.
   * @return the comment of the element formatted as requested.
   */
  public List javaDocComment() {
    List lines = new ArrayList();
    String text = getDescription();
    if ((text != null) && (text.length() > 0)) {
      StringBuffer buffer = new StringBuffer();
      int start = 0;
      int end = start + 75;
      while (end < (text.length() - 1)) {
        while ((end > start) && !Character.isWhitespace(text.charAt(end - 1))) {
          end--;
        }
        buffer.append("* ");
        if (end > start) {
          buffer.append(text.substring(start, end));
        } else {
          end = start + 75;
          buffer.append(text.substring(start, end));
          buffer.append("-");
        }
        lines.add(buffer.toString());
        buffer.setLength(0);
        start = end;
        end = start + 75;
      }
      if (start < (text.length() - 1)) {
        buffer.append("* ");
        buffer.append(text.substring(start));
        lines.add(buffer.toString());
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
   */
  public String valueOfTag(String tag) {
    TaggedValue taggedValue = getTaggedValue(tag);
    String value = null;
    if (taggedValue != null) {
      value = taggedValue.getValue();
    }
    return value;
  }

  public boolean booleanValueOfTag(String key) {
    TaggedValue taggedValue = getTaggedValue(key);
    return ((taggedValue != null) && taggedValue.getValue().equals("true"));
  }

  public void setTaggedValue(String key, boolean value) {
    setTaggedValue(key, Boolean.toString(value));
  }

  public boolean booleanValueOfAttribute(String key) {
    String value = getAttribute(key);
    return ((value != null) && value.equals("true"));
  }

  protected MetaElement findElement(List list, String name) {
    assert ((list != null) && name != null);
    MetaElement element = null;
    for (Iterator i = list.iterator(); i.hasNext();) {
      element = (MetaElement) i.next();
      if (!name.equals(element.getName())) {
        element = null;
      } else {
        break;
      }
    }
    return element;
  }

  public ImportStatements createImportStatements() {
    return new ImportStatements();
  }

  public ImportStatements createImportStatements(String context) {
    return new ImportStatements(context);
  }

  public boolean isLeaf() {
    assert (false);
    return false;
  }
  // %% END USER CODE
}