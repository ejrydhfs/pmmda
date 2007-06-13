package ch.bbv.dynamicproperties.visualization.viewer;

import javax.swing.JComponent;
import javax.swing.JLabel;

import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.core.SimpleValue;
import ch.bbv.dynamicproperties.visualization.IViewer;

@SuppressWarnings("serial")
public class BooleanViewer extends JLabel implements IViewer {

    public BooleanViewer(PropertyValue value) {
        super((String) ((SimpleValue) value).getValue().toString());
    }

    public JComponent getComponent() {
        return this;
    }

}
