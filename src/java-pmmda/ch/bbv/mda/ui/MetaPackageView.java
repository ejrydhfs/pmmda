/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectType;
import ch.bbv.dog.DoFunctor;
import ch.bbv.dog.LightweightObject;
import ch.bbv.dog.ui.ReferenceCodeComboBox;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.NodePropertiesView;
import ch.bbv.mda.*;
import ch.bbv.mda.generators.CartridgeFactory;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * The meta package view defines the view of a MDA package.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MetaPackageView extends NodePropertiesView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * The size of a text field in the view.
   */
  private final static int TEXT_FIELD_SIZE = 20;

  /**
   * The size of a text area in the view.
   */
  private final static int TEXT_AREA_SIZE = 40;

  /**
   * Pattern for validating identifiers
   */
  static Pattern validName;

  JTextField nameText;

  private JTextField stereotypeText;

  private JTextField contextText;

  private JTextArea descriptionText;

  private JScrollPane descriptionPane;

  private JCheckBox system;

  private TaggedValuesView tagsView;

  private ReferenceCodeComboBox visibility;

  /**
   * Default constructor of the class.
   */
  public MetaPackageView() {
    nameText = new JTextField(TEXT_FIELD_SIZE);
    nameText.addFocusListener(new VerificationActions());
    stereotypeText = new JTextField(TEXT_FIELD_SIZE);
    contextText = new JTextField(TEXT_FIELD_SIZE);
    contextText.setEditable(false);
    descriptionText = new JTextArea(3, TEXT_AREA_SIZE);
    descriptionText.setLineWrap(true);
    descriptionText.setWrapStyleWord(true);
    descriptionPane = new JScrollPane(descriptionText,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    system = new JCheckBox();
    system.setEnabled(false);
    tagsView = new TaggedValuesView();
    addNodePropertiesSubView(tagsView);
    visibility = new ReferenceCodeComboBox(VisibilityCode.class, VisibilityCode
        .getCodes());
    initialize();
  }

  @Override
  public void setViewEditable(boolean editable) {
    nameText.setEditable(editable);
    nameText.setBackground(Color.WHITE);
    stereotypeText.setEditable(editable);
    descriptionText.setEditable(editable);
    tagsView.setViewEditable(editable);
    visibility.setEnabled(editable);
    system.setEnabled(editable);
  }

  @Override
  public void populateView() {
    MetaPackage model = (MetaPackage) getDataObject();
    if (this.isNewObject()) {
      CartridgeFactory.getInstance().getItemFactory().initialize(model,
          MetaPackage.class);
    }
    nameText.setText(model.getName());
    stereotypeText.setText(model.getStereotype());
    descriptionText.setText(model.getDescription());
    system.setSelected(model.isSystem());
    visibility.setSelectedCode(model.getVisibility());
    MetaElement context = model.getContext();
    if (context != null) {
      contextText.setText(context.qualifiedName());
    } else {
      contextText.setText(null);
    }
  }

  @Override
  public void commitChanges() {
    MetaPackage model = (MetaPackage) getDataObject();
    String text = descriptionText.getText();
    if (text != null) {
      model.setDescription(text.trim());
    }
    text = stereotypeText.getText();
    if (text != null) {
      model.setStereotype(text.trim());
    }
    model.setVisibility((VisibilityCode) visibility.getSelectedCode());
    model.setSystem(system.isSelected());
    tagsView.commitChanges();
    if (model.getName() != null
        && !model.getName().equals(nameText.getText().trim())) {
      changePackageName(model);
      addParentPackages(model);
    }
    getModel().nodeChanged();
  }

  /**
   * Changes a package name. It also updates references to classes within this
   * package.
   * @param metaPackage package which name should be changed.
   */
  private void changePackageName(MetaPackage metaPackage) {
    String text = nameText.getText().trim();
    String oldName = metaPackage.getName();
    if (text != null) {
      metaPackage.setName(text);
    }
    MetaModel metaModel = getMetaModel();
    if (!text.equals(oldName)) {
      metaModel.accept(new UpdateQualifiedTypeFunctor(oldName, text));
    }
  }

  /**
   * If context is not set then this is a new meta package. If name is qualified
   * then verify whether parent packages exist. If not then add them.
   * @param metaPackage
   */
  private void addParentPackages(MetaPackage metaPackage) {
    MetaModel metaModel = getMetaModel();
    if (metaModel == null)
      return;
    String name = metaPackage.getName();
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
        node.setDataObject(metaPackage);
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
   * Looks up the associated meta model. If none found then null is returned.
   * @return null if no meta model found, otherwise the MetaModel associated
   *         with this package.
   */
  private MetaModel getMetaModel() {
    ExplorerNode explorerNode = getNode();
    while (explorerNode.getDataObject() != null
        && !(explorerNode.getDataObject() instanceof MetaModel)) {
      explorerNode = (ExplorerNode) explorerNode.getParent();
    }
    if (explorerNode != null && explorerNode.getDataObject() != null
        && explorerNode.getDataObject() instanceof MetaModel)
      return (MetaModel) explorerNode.getDataObject();
    return null;
  }

  protected boolean validateContent() {
    return false;
  }

  @Override
  public void cancelChanges() {
    tagsView.cancelChanges();
  }

  /**
   * Initialize the view component with the user interface elements.
   */
  protected void initialize() {
    FormLayout layout = new FormLayout(
        "right:pref, 3dlu, pref, 7dlu, right:pref, 3dlu, pref", "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.setDefaultDialogBorder();
    builder.appendSeparator("General");
    builder.append("&Name", nameText);
    builder.append("&Stereotype", stereotypeText);
    builder.append("&Context", contextText);
    builder.append("&Visibility", visibility);
    builder.append("&Description", descriptionPane, 5);
    builder.appendSeparator("Modifiers");
    builder.append("&Is System", system);
    builder.nextLine();
    builder.appendSeparator("Tagged Values");
    builder.append(tagsView, 7);
    JPanel panel = builder.getPanel();
    add(panel);

    validName = Pattern.compile("^[0-9\\.]" // no number and no dot at the
        // beginning of the identifier
        + "|[^\\w&&[^\\.]]" // no special characters other than dot
        + "|\\.[0-9]"); // no number after a dot
  }

  /**
   * Focus listeners for verification of input. It applies input rules.
   * @author AdrianBachofen
   * @version $Revision$
   */
  private class VerificationActions implements FocusListener {
    public void focusLost(FocusEvent e) {
      if (e.getSource() == nameText) {
        Matcher matcher = validName.matcher(nameText.getText());
        if (matcher.find()) {
          nameText.setBackground(ALERT_COLOR);
        } else {
          nameText.setBackground(Color.WHITE);
        }
      }
    }

    public void focusGained(FocusEvent e) {
      // don't do anything
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