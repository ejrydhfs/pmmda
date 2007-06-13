package ch.bbv.dynamicproperties.visualization.viewer;

import javax.swing.JComponent;
import javax.swing.JLabel;

import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.visualization.IViewer;

@SuppressWarnings("serial")
public class StringViewer implements IViewer {

    JLabel stringLabel;

    public StringViewer(PropertyValue value) {
        stringLabel = new JLabel();
        if (value.getValue() == null) {
            stringLabel.setText("");
        } else {
            stringLabel.setText(value.getValue().toString());
        }
    }

    public JComponent getComponent() {
        return stringLabel;
    }

}
