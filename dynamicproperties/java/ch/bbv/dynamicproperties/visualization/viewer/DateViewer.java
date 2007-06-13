package ch.bbv.dynamicproperties.visualization.viewer;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JLabel;

import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.core.SimpleValue;
import ch.bbv.dynamicproperties.visualization.IViewer;

@SuppressWarnings("serial")
public class DateViewer implements IViewer {

    private JLabel dateLabel;

    private SimpleValue simpleValue;

    public DateViewer(PropertyValue value) {
        dateLabel = new JLabel();
        simpleValue = (SimpleValue) value;
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());

        if (simpleValue.getValue() != null) {
            dateLabel.setText(formatter.format((Date) simpleValue.getValue()));
        } else {
            dateLabel.setText("");
        }
    }

    public JComponent getComponent() {
        return dateLabel;
    }
}
