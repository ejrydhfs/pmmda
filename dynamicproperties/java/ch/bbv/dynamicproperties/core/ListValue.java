/*********************************************************************** 
 * Copyright 2007 BBV Software Services AG (http://www.bbv.ch)
 *  
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * http://www.apache.org/licenses/LICENSE-2.0
 * you may not use this file except in compliance with the License.
 * 
 * $Id$
 * 
 * History:
 * 2007/06/06      Ueli Kurmann     Initial Release
 ***********************************************************************/

package ch.bbv.dynamicproperties.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ch.bbv.dynamicproperties.pmmda.Visitor;

public class ListValue extends PropertyValue implements List<PropertyValue>, PropertyChangeListener {

    /**
     * internal storage container for list elements
     */
    private List<PropertyValue> values;

    /**
     * anonymous attribute for list elements
     */
    private Property anonymousElementAttribute;

    /**
     * 
     * @param attribute
     * 
     * @pre attribute.getType instanceof AttributeListType
     * 
     */
    public ListValue(Property attribute) {
        /**
         * Sets the list attribute
         */
        setProperty(attribute);

        /**
         * create an anonymous element attribute. this attribute is used to
         * manage atributeValues.
         */
        PropertyType elementType = ((ListType) attribute.getType()).getElementType();
        anonymousElementAttribute = new Property("listElementType", elementType, "", null);

        /**
         * initialise the internal list
         */
        values = new ArrayList<PropertyValue>();
    }

    /**
     * Default Constructor used by Hibernate
     * 
     */
    public ListValue() {

    }

    /**
     * Checks the type compatibility of value attributes
     * 
     * @param value
     * @return
     */
    private boolean checkType(PropertyValue value) {
        return anonymousElementAttribute.getType() == value.getProperty().getType();
    }

    /**
     * instantiates a new list element value
     * 
     * @return a new list element value instance
     */
    public PropertyValue createListElement() {
        return anonymousElementAttribute.createInstance();
    }

    /**
     * instantiates a list element and sets the value
     * 
     * @param value
     * @return
     */
    public PropertyValue createListElement(Serializable value) {
        PropertyValue attributeValue = createListElement();
        attributeValue.setValue(value);
        return attributeValue;
    }

    /**
     * adds a value to the list
     * 
     * @param attributeValue
     * @return
     */
    public boolean add(PropertyValue attributeValue) {
        if (!checkType(attributeValue)) {
            throw new RuntimeException();
        }

        attributeValue.addPropertyChangeListener(this);
        return values.add(attributeValue);
    }

    public boolean add(Serializable element) {
        return add(this.createListElement(element));
    }

    public void add(int index, PropertyValue element) {
        if (!checkType(element)) {
            throw new RuntimeException();
        }

        values.add(index, element);
    }

    public boolean addAll(Collection<? extends PropertyValue> c) {
        return values.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends PropertyValue> c) {
        return values.addAll(index, c);
    }

    public void clear() {
        values.clear();
    }

    public boolean contains(Object o) {
        return values.contains(o);
    }

    public boolean containsAll(Collection c) {
        return values.containsAll(c);
    }

    public PropertyValue get(int index) {
        return values.get(index);
    }

    public int indexOf(Object o) {
        return values.indexOf(o);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public Iterator<PropertyValue> iterator() {
        return values.iterator();
    }

    public int lastIndexOf(Object o) {
        return values.lastIndexOf(o);
    }

    public ListIterator<PropertyValue> listIterator() {
        return values.listIterator();
    }

    public ListIterator<PropertyValue> listIterator(int index) {
        return values.listIterator(index);
    }

    public boolean remove(Object o) {
        return values.remove(o);
    }

    public PropertyValue remove(int index) {
        return values.remove(index);
    }

    public boolean removeAll(Collection c) {
        return values.removeAll(c);
    }

    public boolean retainAll(Collection c) {
        return values.retainAll(c);
    }

    public PropertyValue set(int index, PropertyValue element) {
        return values.set(index, element);
    }

    public int size() {
        return values.size();
    }

    public List<PropertyValue> subList(int fromIndex, int toIndex) {
        return values.subList(fromIndex, toIndex);
    }

    public Object[] toArray() {
        return values.toArray();
    }

    public void setValue(Serializable value) {
        //
    }

    public Serializable getValue() {
        return "liste";
    }

    public <T> T[] toArray(T[] a) {
        return values.toArray(a);
    }

    public void propertyChange(PropertyChangeEvent arg0) {
        propertyChangeSupport.firePropertyChange(getName(), false, true);
    }

    @Override
    protected void traverse(Visitor visitor) {
        super.traverse(visitor);
        for (PropertyValue value : values) {
            value.accept(visitor);
        }
    }
}