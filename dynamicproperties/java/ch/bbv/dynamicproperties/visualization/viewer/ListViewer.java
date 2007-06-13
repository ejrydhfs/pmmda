package ch.bbv.dynamicproperties.visualization.viewer;

import javax.swing.JComponent;
import javax.swing.JList;

import ch.bbv.dynamicproperties.core.ListValue;
import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.visualization.IViewer;
import ch.bbv.dynamicproperties.visualization.tool.AttributeListModel;
import ch.bbv.dynamicproperties.visualization.tool.CellRenderer;

public class ListViewer implements IViewer {

    private JList list;

    public ListViewer(PropertyValue attributeValue) {
        list = new JList();
        list.setCellRenderer(new CellRenderer());
        AttributeListModel model = new AttributeListModel((ListValue) attributeValue);
        list.setModel(model);
    }

    public JComponent getComponent() {
        return list;
    }

}
