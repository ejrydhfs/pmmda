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

package ch.bbv.dynamicproperties.visualization.editor;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import ch.bbv.dynamicproperties.core.EnumerationValue;
import ch.bbv.dynamicproperties.core.HierarchicalEnumerationElement;
import ch.bbv.dynamicproperties.core.PropertyValue;

public class HierarchicalEnumEditor extends DefaultEditor {

    private JTree tree;

    private EnumerationValue value;

    public HierarchicalEnumEditor(PropertyValue value) {
        tree = new JTree();
        tree.setModel(new TreeModel((EnumerationValue) value));
        tree.setRootVisible(false);

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath selected = e.getPath();
                setValue(selected.getLastPathComponent());
            }
        });

        this.value = (EnumerationValue) value;
    }

    public JComponent getComponent() {
        return tree;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value.setValue(((HierarchicalEnumerationElement) value).getCode());
    }
}
