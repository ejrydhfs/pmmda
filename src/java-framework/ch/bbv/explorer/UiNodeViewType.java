/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.explorer;

/**
 * The user interface node view describes a standard right pane view containing
 * the children of an instance of a node type. The view is used to create the
 * view when requested.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class UiNodeViewType {
  /**
   * Name of the view as displayed to identify it.
   */
  private String name;

  private UiNodeType nodeType;

  private Class nodeView;

  private Object parameters;

  private boolean preferred;

  public UiNodeViewType(String name, UiNodeType nodeType, Class nodeView,
      Object parameters, boolean preferred) {
    this.name = name;
    this.nodeType = nodeType;
    this.nodeView = nodeView;
    this.parameters = parameters;
    this.preferred = preferred;
  }

  /**
   * Returns the name of the user interface node view.
   * @return name of the view.
   */
  public String getName() {
    return name;
  }

  public Class getNodeView() {
    return nodeView;
  }

  public UiNodeType getNodeType() {
    return nodeType;
  }

  public Object getParameters() {
    return parameters;
  }

  /**
   * Sets the value of the preferred flag.
   * @param preferred new value of the preferred flag.
   * @see #isPreferred()
   */
  public void setPreferred(boolean preferred) {
    this.preferred = preferred;
  }

  /**
   * Returns the value of the default flag.
   * @return the value of the default flag.
   * @see #setPreferred(boolean)
   */
  public boolean isPreferred() {
    return preferred;
  }
}