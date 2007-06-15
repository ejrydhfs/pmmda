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

package ch.bbv.dynamicproperties.objecteditor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;


import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.Property;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class DynamicPropertyEditor extends JPanel {

    private JTree dataObjectTree;

    private JTree typeTree;

    public DynamicPropertyEditor() {
        initGUIComponents();
    }

    private void initGUIComponents() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, pref", "");
        // setLayout(layout);
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.append(createDataObjectTree());
        builder.append(createTypeTree());
        this.add(builder.getPanel());
    }

    private JTree createDataObjectTree() {
        dataObjectTree = new JTree();
        dataObjectTree.setModel(new DataObjectTreeModel(PropertyInstanceManager.instance()));
        return dataObjectTree;
    }

    private JTree createTypeTree() {
        typeTree = new JTree();
        return typeTree;
    }
}
