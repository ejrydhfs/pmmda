/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The node properties view defines the view displaying all properties of a data
 * objects. The view can contain child properties view. The framework handles
 * all house keeping activities. The developer needs only to implement a few
 * methods to have a usable properties view tailored for his data object type.
 * @author Marcel Baumann
 * @version $Revision$
 */
public abstract class NodePropertiesView extends JPanel {
  /**
   * Node being displayed in the property view.
   */
  protected ExplorerNode node;

  /**
   * Flag indicating if the object is new or not.
   */
  private boolean newObject;

  /**
   * Flag indicating if the object can be edited or not.
   */
  private boolean editable;

  /**
   * List of subviews defined in the panel.
   */
  private List nodePropertiesSubViews;

  /**
   * Default constructor of the class.
   */
  public NodePropertiesView() {
    nodePropertiesSubViews = new ArrayList();
  }

  /**
   * Sets the model to be edited. The model is a registered data object. The
   * subclass should fill the view with the values of the model.
   */
  public abstract void populateView();

  /**
   * The performed changes should be committed to the given data object. The
   * subclass should commit the changes to the model.
   */
  public abstract void commitChanges();

  /**
   * The performed changes should be cancelled to the given data object. The
   * subclass can perform house keeping tasks.
   */
  public abstract void cancelChanges();

  /**
   * Sets the editable flag for the view. The subclass should toggle the view
   * representation to reflect its new status.
   * @param editable new value of the flag.
   */
  public abstract void setViewEditable(boolean editable);

  /**
   * Adds the node properties subview.
   * @param nodePropertiesView view to add.
   */
  public void addNodePropertiesSubView(NodePropertiesView nodePropertiesView) {
    nodePropertiesSubViews.add(nodePropertiesView);
  }

  /**
   * Returns the list of subview.
   * @return list of subviews.
   */
  public List getNodePropertiesSubViews() {
    return nodePropertiesSubViews;
  }

  /**
   * Sets the is new flag for the views and all its children. The flag is is set
   * only if the flag is toggled.
   * @param newObject new value of the flag.
   * @post isNewObject == isNewObject()
   * @see #isNewObject()
   */
  public void setNewObject(boolean newObject) {
    if (this.newObject != newObject) {
      this.newObject = newObject;

      for (Iterator i = nodePropertiesSubViews.iterator(); i.hasNext();) {
        ((NodePropertiesView) i.next()).setNewObject(newObject);
      }
    }
  }

  /**
   * Returns the value of the is new flag.
   * @return value of the flag.
   * @see #setNewObject(boolean)
   */
  public boolean isNewObject() {
    return newObject;
  }

  /**
   * Sets the editable flag for the views and all its children. The abstract
   * method <code>setViewEditable</code> is called for the subclass and all
   * registered subviews. The call is performed only if the flag is toggled.
   * @param editable new value of the flag.
   * @post editable = isEditable()
   * @see #isEditable()
   */
  public void setEditable(boolean editable) {
    this.editable = editable;
    setViewEditable(editable);

    for (Iterator i = nodePropertiesSubViews.iterator(); i.hasNext();) {
      NodePropertiesView child = (NodePropertiesView) i.next();
      child.setEditable(editable);
      child.setViewEditable(editable);
    }
  }

  /**
   * Returns the value of the is editable flag.
   * @return value of the flag.
   * @see #setEditable(boolean)
   */
  public boolean isEditable() {
    return editable;
  }

  /**
   * Returns the model which properties are displayed in the view.
   * @return the node displayed in the view.
   * @see #setModel(ExplorerNode)
   */
  protected ExplorerNode getModel() {
    return node;
  }

  /**
   * Sets the model to the properties view and all registered subviews.
   * @param node new model explorer node.
   * @see #getModel()
   */
  protected void setModel(ExplorerNode node) {
    propagateModel(node);
    if (node != null) {
      populateSubViews();
    }
  }

  /**
   * Propagates the model to the view and all its subviews.
   * @param node model propagated to all subviews.
   */
  protected void propagateModel(ExplorerNode node) {
    this.node = node;

    Iterator subviews = nodePropertiesSubViews.iterator();
    while (subviews.hasNext()) {
      ((NodePropertiesView) subviews.next()).propagateModel(node);
    }
  }

  /**
   * Populates the view and all subviews. It is guaranteed that all subviews
   * have their model set.
   */
  protected void populateSubViews() {
    populateView();

    Iterator subviews = nodePropertiesSubViews.iterator();
    while (subviews.hasNext()) {
      ((NodePropertiesView) subviews.next()).populateView();
    }
  }

  /**
   * Returns the data object associated with the model. This function is a
   * convenience method for the subclasses.
   * @return the data object stored in the explorer node if defined otherwise
   *         null.
   */
  protected Object getDataObject() {
    Object object = null;
    if (node != null) {
      object = node.getDataObject();
    }
    return object;
  }

  /**
   * Customized Editor, which adds a FocusListener to editors being of type
   * TextField The FocusListener will stop editting when focus is lost.
   */
  protected static class MyEditor extends DefaultCellEditor {

    public MyEditor(JCheckBox checkBox) {
      super(checkBox);
    }

    public MyEditor(JComboBox comboBox) {
      super(comboBox);
    }

    public MyEditor(JTextField textField) {
      super(textField);
      textField.addFocusListener(new MyFocusListener(this));
    }

    /**
     * Implements a focus adapter ending cell editing in case of focus lost.
     */
    private final class MyFocusListener extends FocusAdapter {
      MyEditor editor;

      public MyFocusListener(MyEditor editor) {
        this.editor = editor;
      }

      public void focusGained(FocusEvent e) {
      }

      public void focusLost(FocusEvent e) {
        editor.stopCellEditing();
      }
    }
  }
}