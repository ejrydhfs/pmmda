/* copyright 2000-2007 Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id:  
 */
package ch.bbv.explorer;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectImpl;
import ch.bbv.dog.DataObjectType;
import ch.bbv.dog.LightweightObject;
import ch.bbv.explorer.views.GenericNodePropertiesView;

/**
 * SimpleExplorerTree adds support to an ExplorerTree for an environment not having factories, data objects and all that.
 * It is easy to use. Derive from SimpleExplorerTree and implement getIconSelector and getPopupSelector as described there.
 * @author AdrianBachofen
 *
 */
public abstract class GeneralExplorerTree extends ExplorerTree {

  /**
   * Factory controlling the ui node types visualized in this dialog
   */
  private UiNodeTypeFactory uiNodeTypeFactory;
  
  /**
   * Standard constructor
   * @param factory used to construct ExplorerNodes.
   */
  public GeneralExplorerTree(UiNodeTypeFactory factory) {
  	this.uiNodeTypeFactory = factory;
  }

  /**
   * Creates a node type for being visualized as an ExplorerNode within the tree if not already registered.
   * @param clazz Object to determine class from.
   * @return UiNodeType for passed clazz.
   */
  public UiNodeType createOrGetNodeTypeForAnObject(Object clazz) {
    UiNodeType nodeType = uiNodeTypeFactory.getNodeTypeForBo(clazz.getClass());
    if(nodeType == null) { 
	    DataObjectType type = new DataObjectType(clazz.getClass(),
	    		clazz.getClass(), clazz.getClass(), null, null, null);
      type.setRoot(false);
      nodeType = new UiNodeTypeImpl(type, "name", 
        GenericNodePropertiesView.class);
      uiNodeTypeFactory.register(nodeType);
      IconSelector iconSelector = getIconSelector(uiNodeTypeFactory.getIconFactory(), clazz);
      if(iconSelector != null) {
      	uiNodeTypeFactory.getIconFactory().registerIconSelector(nodeType, iconSelector);
      }
      PopupSelector popupSelector = getPopupSelector(nodeType, clazz);
      if (popupSelector != null) {
      	uiNodeTypeFactory.getPopupFactory().registerPopupSelector(nodeType, popupSelector);
      }
    }
    return nodeType;
  }
  
  /**
   * Creates an ExplorerNode based on a content object and a display name. The content object will not be access but
   * stored within a treeElement object.
   * @param content to be stored within a tree element.
   * @param displayName to be shown for the node within the tree.
   * @return ExplorerNode object to be added to the tree.
   */
  public ExplorerNode createExplorerNode(Object content, String displayName) {
  	TreeElement treeElement = new TreeElement();
		treeElement.setName(displayName);
		treeElement.setContent(content);
		return new ExplorerNode(createOrGetNodeTypeForAnObject(content), treeElement, treeElement);
  }
  
  /**
   * Returns an IconSelector for the passed object. If you want to use default icons just return null.
   * @param factory IconFactory used for creating some IconSelectors
   * @param clazz to obtain the IconSelector for. For same classes of passed object you have to return the same IconSelector.
   * @return IconSelector representation of an icon
   * e.g. if(clazz instanceof AnyClass) return new SimpleIconSelector("test");
   */
  protected abstract IconSelector getIconSelector(IconFactory factory, Object clazz);

  /**
   * Returns a PopupSelector for the passed object. If you want to use no popup menus just return null.
   * @param nodeType we want to set the pop up menu for.
   * @param clazz to obtain the PopupSelector for. For same classes of passed object you have to return the same PopupSelector.
   * @return PopupSelector for passed clazz.
   * e.g. return new PopupSelectorDefault(nodeType);
   */
  protected abstract PopupSelector getPopupSelector(UiNodeType nodeType, Object clazz);

  /**
   * The TreeElement is the visualized element by this tree. To comply with current implementation it has
   * to be both a ligthweight object and a data object (NamedEntity). It stores the content to be shown within
   * the tree in its content member.
   * @author AdrianBachofen
   *
   */
  public static class TreeElement extends DataObjectImpl implements DataObject, LightweightObject {
  	
  	// Content to be visualized in the tree
  	private Object content;
  	
  	// Name to be displayd within tree
  	private String name;
  	
  	/**
  	 * Sets the content for this tree element. The content will be visualized within the tree. 
  	 * @param content to be shown
  	 */
  	public void setContent(Object content) {
  		this.content = content;
  	}
  	
  	/**
  	 * Returns the content of this tree element. The content will be visualized within the tree
  	 * @return content to be shown within the tree.
  	 */
  	public Object getContent() {
  		return content;
  	}

  	/**
  	 * Returns name to be displayed within tree.
  	 * @return name as String.
  	 */
  	public String getName() {
  		return name;
  	}
  	
  	/**
  	 * Sets the name to be displayd within the tree.
  	 * @param name String to be displayed.
  	 */
  	public void setName(String name) {
  		this.name = name;
  	}
  	
    public LightweightObject deepCopy() {
      return null;
    }
  }
}
