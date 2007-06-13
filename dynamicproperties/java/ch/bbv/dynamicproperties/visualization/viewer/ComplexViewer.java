package ch.bbv.dynamicproperties.visualization.viewer;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import ch.bbv.dynamicproperties.core.ComplexValue;
import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.visualization.IViewer;
import ch.bbv.dynamicproperties.visualization.tool.SwingTools;

/**
 * @author UeliKurmann
 */
public class ComplexViewer implements IViewer {

    private JPanel panel;

    private ComplexValue attributeValue;

    public ComplexViewer(PropertyValue attributeValue) {
        this.attributeValue = (ComplexValue) attributeValue;
        panel = new JPanel();
        init();
    }

    private void init() {
        panel.setLayout(new GridBagLayout());

        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(attributeValue.getName()),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        panel.add(SwingTools.createViewerPanel(attributeValue.getProperties()));
        panel.repaint();
    }

    public JComponent getComponent() {
        return panel;
    }
}
