/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectType;
import ch.bbv.dog.DoFunctor;
import ch.bbv.dog.LightweightObject;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.mda.*;
import ch.bbv.mda.generators.CartridgeFactory;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.util.ValidationUtils;

/**
 * The meta package view defines the view of a MDA package.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MetaPackageView extends MetaItemView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 2388221329446034721L;

  /**
   * Flag indicating if the package is a system package.
   */
  private JCheckBox system;

  /**
   * Default constructor of the class.
   */
  public MetaPackageView() {
    system = new JCheckBox();
    system.setEnabled(false);
    add(buildPanel());
  }

  @Override
  public void setViewEditable(boolean editable) {
    super.setViewEditable(editable);
    system.setEnabled(editable);
  }

  @Override
  public void populateView() {
    super.populateView();
    MetaPackage model = (MetaPackage) getDataObject();
    system.setSelected(model.isSystem());
  }

  @Override
  public void commitChanges() {
    MetaPackage model = (MetaPackage) getDataObject();
    if (model.getName() != null
        && !model.getName().equals(name.getText().trim())) {
      changePackageName(model);
      addParentPackages(model);
    }
    super.commitChanges();
    model.setSystem(system.isSelected());
    getModel().nodeChanged();
  }

  @Override
  public void validateView(ValidationResult result) {
    if (ValidationUtils.isBlank(name.getText())) {
      result.add(new SimpleValidationMessage("name is mandatory",
          Severity.ERROR, name));
    }
    if (ValidationUtils.isBlank(description.getText())) {
      result.add(new SimpleValidationMessage("description should be provided",
          Severity.WARNING, description));
    }
    super.validateView(result);
    validationResultModel.setResult(result);
  }

  @Override
  public void cancelChanges() {
    tagsView.cancelChanges();
  }

  @Override
  protected JComponent buildGeneral() {
    DefaultFormBuilder builder = initializeGeneral();
    return builder.getPanel();
  }

  @Override
  protected JComponent buildFlags() {
    DefaultFormBuilder builder = getBuilder(FLAGS_LAYOUT);
    builder.append("&Is System", system);
    return builder.getPanel();
  }

  /**
   * Changes a package name. It also updates references to classes within this
   * package.
   * @param packages package which name should be changed.
   */
  private void changePackageName(MetaPackage packages) {
    String text = name.getText().trim();
    String oldName = packages.getName();
    if (text != null) {
      packages.setName(text);
    }
    MetaModel metaModel = ((MetaPackage) getDataObject()).getModel();
    if (!text.equals(oldName)) {
      metaModel.accept(new UpdateQualifiedTypeFunctor(oldName, text));
    }
  }

  /**
   * If context is not set then this is a new meta package. If name is qualified
   * then verify whether parent packages exist. If not then add them.
   * @param packages package to process
   */
  private void addParentPackages(MetaPackage packages) {
    MetaModel metaModel = ((MetaPackage) getDataObject()).getModel();
    if (metaModel == null)
      return;
    String name = packages.getName();
    int idx = ((ExplorerNode) getNode().getParent()).getIndex(getNode());
    while (name.lastIndexOf(".") > 0) {
      name = name.substring(0, name.lastIndexOf("."));
      MetaPackage mPackage = metaModel.findPackage(name);
      if (mPackage == null) {
        DataObjectType type = getNode().getNodeType().getDataObjectType();
        mPackage = (MetaPackage) type.createBoInstance();
        CartridgeFactory.getInstance().getItemFactory().initialize(mPackage,
            MetaPackage.class);
        mPackage.setName(name);
        mPackage.setVisibility((VisibilityCode) visibility.getSelectedCode());
        node.setDataObject(mPackage);
        tagsView.commitChanges();
        node.setDataObject(packages);
        metaModel.addPackage(mPackage);

        // add explorer node
        Class boClass = type.getBoClass();
        Class lwClass = type.getLwClass();
        LightweightObject lwObject = null;
        if (boClass != null) {
          if ((lwClass != null) && (lwClass != boClass)) {
            lwObject = type.createLwInstance();
          } else {
            lwObject = mPackage;
          }
          ExplorerNode childNode = new ExplorerNode(getNode().getNodeType(),
              lwObject, mPackage);
          childNode.setParent((ExplorerNode) getNode().getParent());
          ((ExplorerNode) getNode().getParent()).insert(childNode, idx);
        }
      }
    }
  }

  /**
   * Functor replacing type references to classes within modified package. If
   * the name of a packages changes then the qualified of its classes also
   * change. This functor adapts the qualified name of references to the new
   * name.
   * @author adrianbachofen
   */
  private class UpdateQualifiedTypeFunctor extends VisitorFunctor implements
      DoFunctor {

    private String oldName = "";

    private String newName = "";

    public UpdateQualifiedTypeFunctor(String oldName, String newName) {
      this.oldName = oldName;
      this.newName = newName;
      setFunctor(this);
    }

    public void execute(DataObject dataObject) {
      if (dataObject instanceof MetaProperty
          || dataObject instanceof MetaIndexedProperty) {
        MetaProperty metaProperty = (MetaProperty) dataObject;
        if (metaProperty.getTypeName().startsWith(oldName)) {
          metaProperty.setTypeName(newName
              + metaProperty.getTypeName().substring(oldName.length()));
        }
      }
    }
  }
}