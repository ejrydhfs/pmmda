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

package ch.bbv.dynamicproperties.visualization.tool;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.bbv.dynamicproperties.core.PropertyValue;

public class SwingTools {

    /**
     * Creates a GridBag constraints object.
     * 
     * @param top
     *            Border at the top.
     * @param left
     *            Border at the left.
     * @param bottom
     *            Border at the bottom.
     * @param right
     *            Border at the right
     * @return the newly created GridBag constraints object.
     */
    public static GridBagConstraints createConstraints(int top, int left, int bottom, int right) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(top, left, bottom, right);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        return gbc;
    } // createConstraints

    /**
     * Creates a GridBag constraints object.
     * 
     * @param gridx
     *            specifies the cell containing the leading edge of the
     *            component's display area, where the first cell in a row has
     *            gridx=0.
     * @param gridy
     *            specifies the cell at the top of the component's display area,
     *            where the topmost cell has 'gridy =0'.
     * @param gridwidth
     *            specifies the number of cells in a row for the component's
     *            display area.
     * @param gridheight
     *            specifies the number of cells in a column for the component's
     *            display area.
     * @param top
     *            border at the top.
     * @param left
     *            border at the left.
     * @param bottom
     *            border at the bottom.
     * @param right
     *            border at the right.
     * @param weightx .
     * @param weighty .
     * @param anchor
     *            used when the component is smaller than its display area. It
     *            determines where to place the component.
     * @param fill
     *            used when the component's display area is larger than the
     *            componenet's requested size. It determines whether to resize
     *            the component, and if so, how.
     * @return the newly created GridBag constraints object.
     */
    public static GridBagConstraints createConstraints(int gridx, int gridy, int gridwidth, int gridheight, int top,
            int left, int bottom, int right, double weightx, double weighty, int anchor, int fill) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(top, left, bottom, right);
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        return gbc;
    } // createConstraints

    /**
     * Creates a GridBag constraints object.
     * 
     * @param gridx
     *            specifies the cell containing the leading edge of the
     *            component's display area, where the first cell in a row has
     *            gridx=0.
     * @param gridy
     *            specifies the cell at the top of the component's display area,
     *            where the topmost cell has 'gridy =0'.
     * @param gridwidth
     *            specifies the number of cells in a row for the component's
     *            display area.
     * @param gridheight
     *            specifies the number of cells in a column for the component's
     *            display area.
     * @param anchor
     *            used when the component is smaller than its display area. It
     *            determines where to place the component.
     * @return the newly created GridBag constraints object.
     */
    public static GridBagConstraints createConstraints(int gridx, int gridy, int gridwidth, int gridheight, int anchor) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.anchor = anchor;
        return gbc;
    } // addComponent

    /**
     * Creates a GridBag constraints object.
     * 
     * @param gridx
     *            specifies the cell containing the leading edge of the
     *            component's display area, where the first cell in a row has
     *            gridx=0.
     * @param gridy
     *            specifies the cell at the top of the component's display area,
     *            where the topmost cell has 'gridy =0'.
     * @return the newly created GridBag constraints object.
     */
    public static GridBagConstraints createConstraints(int gridx, int gridy) {
        return createConstraints(gridx, gridy, 1, 1, GridBagConstraints.NORTHWEST);
    } // createConstraints

    public static Component createViewerPanel(List<PropertyValue> values) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        int i = 0;
        for (PropertyValue value : values) {
            panel.add(new JLabel(value.getName()), createConstraints(0, i));
            panel.add(new JLabel("  "), createConstraints(1, i));
            panel.add(value.getViewer().getComponent(), createConstraints(2, i));
            i++;
        }
        return panel;
    }

    public static Component createEditorPanel(List<PropertyValue> values) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        int i = 0;
        for (PropertyValue value : values) {
            panel.add(new JLabel(value.getName(Locale.getDefault().getLanguage())), createConstraints(0, i));
            panel.add(new JLabel("  "), createConstraints(1, i));
            panel.add(value.getEditor().getComponent(), createConstraints(2, i));
            i++;
        }
        return panel;
    }
}
