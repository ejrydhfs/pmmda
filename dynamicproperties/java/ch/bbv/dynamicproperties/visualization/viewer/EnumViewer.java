package ch.bbv.dynamicproperties.visualization.viewer;

import javax.swing.JComponent;
import javax.swing.JLabel;

import ch.bbv.dynamicproperties.core.EnumerationValue;
import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.visualization.IViewer;

@SuppressWarnings("serial")
public class EnumViewer implements IViewer {

    JLabel label;

    public EnumViewer(PropertyValue value) {
        label = new JLabel();
        EnumerationValue enumValue = (EnumerationValue) value;
        label.setText(enumValue.getValue().toString());
    }

    public JComponent getComponent() {
        return label;
    }

}
