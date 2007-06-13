package ch.bbv.dynamicproperties.demo.hibernate;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.dog.DataObjectImpl;
import ch.bbv.dynamicproperties.core.PropertyValue;

public class Container extends DataObjectImpl{
	
	private List<PropertyValue> propertyValues;
	
	public Container(){
		propertyValues = new ArrayList<PropertyValue>();
	}
	
	public void add(PropertyValue value){
		propertyValues.add(value);
	}
}
