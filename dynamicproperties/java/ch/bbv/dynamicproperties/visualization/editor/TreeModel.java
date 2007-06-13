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

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import ch.bbv.dynamicproperties.core.EnumerationValue;
import ch.bbv.dynamicproperties.core.HierarchicalEnumerationElement;
import ch.bbv.dynamicproperties.core.HierarchicalEnumerationType;

public class TreeModel implements javax.swing.tree.TreeModel {

    HierarchicalEnumerationType enumType;

    HierarchicalEnumerationElement currentNode;

    HierarchicalEnumerationElement rootNode;

    public TreeModel(EnumerationValue enumValue) {
        this.enumType = (HierarchicalEnumerationType) enumValue.getProperty().getType();

        this.rootNode = new HierarchicalEnumerationElement(0, 0, null, null, false, null, null, null);
        for (HierarchicalEnumerationElement elem : enumType.getRoots()) {
            this.rootNode.addEnumerationElement(elem);
        }

        this.currentNode = this.rootNode;
    }

    public Object getRoot() {
        return rootNode;
    }

    public int getChildCount(Object parent) {
        return ((HierarchicalEnumerationElement) parent).getChildren().size();
    }

    public Object getChild(Object parent, int index) {
        return ((HierarchicalEnumerationElement) parent).getChildren().get(index);
    }

    public int getIndexOfChild(Object parent, Object child) {
        HierarchicalEnumerationElement currentParent = (HierarchicalEnumerationElement) parent;
        int count = 0;
        for (HierarchicalEnumerationElement currentChild : currentParent.getChildren()) {
            if (currentChild == child) {
                return count;
            }
            count++;
        }
        return 0;
    }

    public void addTreeModelListener(TreeModelListener arg0) {
        // TODO Auto-generated method stub
    }

    public boolean isLeaf(Object arg0) {
        return ((HierarchicalEnumerationElement) arg0).getChildren().size() == 0;
    }

    public void removeTreeModelListener(TreeModelListener arg0) {
        // TODO Auto-generated method stub

    }

    public void valueForPathChanged(TreePath arg0, Object arg1) {
        // TODO Auto-generated method stub
    }
}
