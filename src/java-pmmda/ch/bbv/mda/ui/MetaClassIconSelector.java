package ch.bbv.mda.ui;

import javax.swing.ImageIcon;

import ch.bbv.dog.DataObject;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.IconFactory;
import ch.bbv.explorer.IconSelector;
import ch.bbv.mda.MetaClass;

/**
 * The meta class icon selector selects the icon for tree nodes. The icon shows 
 * if the class is an interface or a regular class. The name of the provided icons must 
 * respect the following conventions. The name of the node type is used as
 * first part of the name, the below defined strings are used to indicate 
 * if the icon is for nodes being regular classes or interfaces. 
 * @author adrianbachofen
 * @version $Revision$
 */
public class MetaClassIconSelector implements IconSelector {
  /**
   * Extension for the icons representing a regular class.
   */
  public static final String CLASS_REGULAR = "-regular";

  /**
   * Extension for the icons representing an interface class.
   */
  public static final String CLASS_INTERFACE = "-interface";

  /**
   * Icon factory managing all icons.
   */
  private IconFactory factory;

  /** 
   * Name of the node type which icons are managed.
   */
  private String nodeTypeName;

  /** Constructor of the class.
   * @param factory icon factory where the icons can be retrieved.
   * @param nodeTypeName name of the node type which icon should be displayed.
   */
  public MetaClassIconSelector(IconFactory factory, String nodeTypeName) {
    this.factory = factory;
    this.nodeTypeName = nodeTypeName;
  }

  public ImageIcon select(ExplorerNode node, boolean expanded) {
    StringBuilder buffer = new StringBuilder();
    buffer.append(nodeTypeName);
    DataObject dataObject = node.getDataObject();
    boolean isInterface = false;
    if(dataObject instanceof MetaClass) {
      isInterface = ((MetaClass)dataObject).isInterface();
    }
    if(isInterface) {
      buffer.append(CLASS_INTERFACE);
    } else {
      buffer.append(CLASS_REGULAR);
    }
    return factory.getSmallIconFor(buffer.toString());
  }
}
