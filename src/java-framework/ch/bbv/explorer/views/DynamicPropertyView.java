package ch.bbv.explorer.views;

import javax.swing.JComponent;

import ch.bbv.dog.DataObject;
import ch.bbv.dynamicproperties.core.DynamicProperty;
import ch.bbv.dynamicproperties.core.PropertyValue;

public class DynamicPropertyView extends PropertyView {

	private PropertyValue propertyValue;
	private String propertyName;
	
	private boolean isEditable;

	public DynamicPropertyView(PropertyValue propertyValue) {
		super(propertyValue.getName(), propertyValue.getClass(), propertyValue
				.getName());
		this.propertyValue = propertyValue;
		this.propertyName = propertyValue.getName();
	}

	@Override
	public JComponent getComponent() {
		if (isEditable) {
			return propertyValue.getEditor().getComponent();
		} else {
			return propertyValue.getEditor().getComponent();
		}
	}

	@Override
	protected Object getValue() {
		return propertyValue.getValue();
	}

	@Override
	public void setEditable(boolean editable) {
		isEditable = editable;
	}

	@Override
	protected void setValue(Object value) {
		// empty
	}

	public void setModel(DataObject obj) {
		propertyValue = ((DynamicProperty)obj).getProperty(propertyName);
		setValue(propertyValue.getValue());
	}

	public void update() {
		// empty
	}

}
