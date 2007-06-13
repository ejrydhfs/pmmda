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

import ch.bbv.dynamicproperties.core.ComplexType;
import ch.bbv.dynamicproperties.core.ListType;
import ch.bbv.dynamicproperties.core.SimpleType;
import ch.bbv.explorer.GeneralExplorerTree;
import ch.bbv.explorer.IconFactory;
import ch.bbv.explorer.IconSelector;
import ch.bbv.explorer.PopupSelector;
import ch.bbv.explorer.SimpleIconSelector;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.UiNodeTypeFactory;

public class DynamicTypeTree extends GeneralExplorerTree {

    public DynamicTypeTree(UiNodeTypeFactory factory) {
        super(factory);
    }

    /**
     * {@inheritDoc}
     */
    protected IconSelector getIconSelector(IconFactory factory, Object clazz) {

        if (clazz instanceof TreeElement) {
            return null;
        }

        if (clazz instanceof SimpleType) {
            return new SimpleIconSelector("simpletype");
        }

        if (clazz instanceof ComplexType) {
            return new SimpleIconSelector("complextype");
        }

        if (clazz instanceof ListType) {
            return new SimpleIconSelector("listtype");
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    protected PopupSelector getPopupSelector(UiNodeType nodeType, Object clazz) {

        return new PropertyPopupSelector();
    }
}
