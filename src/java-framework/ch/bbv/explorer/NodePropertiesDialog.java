/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import javax.swing.JFrame;

import ch.bbv.dog.LightweightObjectHandler;
import ch.bbv.explorer.actions.ExplorerNodeAction;
import ch.bbv.explorer.components.SimpleDialog;

import com.jgoodies.validation.ValidationResult;

/**
 * The node properties dialog displays the properties view of the selected node.
 * All viewable properties of the data object are displayed. If the object is
 * editable the properties can be modified. Upon completion the user can either
 * commit his changes or cancel them. The dialog is reused to display the
 * properties of various node types. The views are set from the caller to allow
 * a reuse mechanism. The design goal is to instantiate at most one dialog
 * window and one properties view per node type. Naturally each explorer needs
 * to cache its own instances.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class NodePropertiesDialog extends SimpleDialog {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * View displayed in the dialog.
   */
  private NodePropertiesView view;

  /**
   * The explorer node action to execute.
   */
  private ExplorerNodeAction action;

  /**
   * Constructor of the class.
   * @param owner owner of the dialog.
   * @param modal true if this dialog shall be modal.
   */
  public NodePropertiesDialog(JFrame owner, boolean modal) {
    super(owner);
    setModal(modal);
  }

  /**
   * The data object to display and the view containing its properties are
   * assigned to the dialog.
   * @param node node containing the data object to display
   * @param editable flag indicating if the data object is editable
   * @param news flag indicating if the data object is a new one
   * @param title title of the properties dialog
   * @param action action to execute upon completion of the dialog
   * @pre (node != null) && (title != null)
   */
  public void setModel(ExplorerNode node, boolean editable, boolean news,
      String title, ExplorerNodeAction action) {
    assert (node != null) && (title != null);
    UiNodeTypeFactory factory = node.getNodeOwner().getNodeTypeFactory();
    setTitle(title);
    view = factory.getViewFor(node.getNodeType());
    view.setEditable(editable);
    getOkButton().setEnabled(editable);
    getApplyButton().setEnabled(editable);
    view.setNewObject(news);
    view.setModel(node);
    this.action = action;
    setView(view);
  }

  @Override
  protected boolean processOk() {
    boolean validated = processApply();
    if (validated) {
      if (action != null) {
        action.postExecute(view.getModel(), false);
      }
      view.setModel(null);
    }
    return validated;
  }

  @Override
  protected boolean processCancel() {
    if (view.isEditable()) {
      view.cancelChanges();
    }
    if (action != null) {
      action.postExecute(view.getModel(), true);
    }
    view.setModel(null);
    return true;
  }

  @Override
  protected boolean processApply() {
    ValidationResult result = new ValidationResult();
    view.validateView(result);
    boolean validated = !result.hasErrors();
    if (view.isEditable()) {
      if (validated) {
        view.commitChanges();
        view.populateSubViews();
        ExplorerNode node = view.getModel();
        LightweightObjectHandler handler = node.getNodeOwner().getManager()
            .getLwHandler();
        handler.synchronize(node.getLightweightObject(), node.getDataObject());
      }
    }
    return validated;
  }
}