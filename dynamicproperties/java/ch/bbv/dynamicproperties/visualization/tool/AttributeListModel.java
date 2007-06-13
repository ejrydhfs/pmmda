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

import javax.swing.AbstractListModel;
import javax.swing.JPanel;

import ch.bbv.dynamicproperties.core.ListValue;
import ch.bbv.dynamicproperties.visualization.IEditor;

/**
 * @author UeliKurmann
 */
@SuppressWarnings("serial")
public class AttributeListModel extends AbstractListModel {

    ListValue list;

    public AttributeListModel(ListValue list) {
        this.list = list;
    }

    public Object getElementAt(int i) {
        IEditor editor = list.get(i).getEditor();
        JPanel panel = new JPanel();
        panel.add(editor.getComponent());
        return panel;
    }

    public int getSize() {
        return list.size();
    }

    public void addElement() {
        list.add(list.createListElement());
        fireIntervalAdded(list.get(list.size() - 1), list.size() - 1, list.size() - 1);
    }
}
