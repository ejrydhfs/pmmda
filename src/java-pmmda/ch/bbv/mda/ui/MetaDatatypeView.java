/* copyright 2000-2005 Marcel Baumann 
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

import ch.bbv.dog.ui.ReferenceCodeComboBox;
import ch.bbv.explorer.NodePropertiesView;
import ch.bbv.mda.MetaDatatype;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.VisibilityCode;
import ch.bbv.mda.generators.CartridgeFactory;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * The meta class view defines the view of a MDA class.
 * @author marcelbaumann
 * @version $Revision$
 */
public class MetaDatatypeView extends NodePropertiesView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20060103;

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
  private static Pattern validName;

  /**
   * Swing component to display the associated property.
   */
  private JTextField nameText;

  /**
   * Swing component to display the associated property.
   */
  private JTextField contextText;

  /**
   * Swing component to display the associated property.
   */
  private ReferenceCodeComboBox visibility;

  /**
   * Swing component to display the associated property.
   */
  private JTextArea descriptionText;

  /**
   * Swing component to display the associated property.
   */
  private JScrollPane descriptionPane;

  private TaggedValuesView tagsView;

  /**
   * Default constructor of the class.
   */
  public MetaDatatypeView() {
    nameText = new JTextField(TEXT_FIELD_SIZE);
    nameText.addFocusListener(new VerificationActions());
    contextText = new JTextField(TEXT_FIELD_SIZE);
    contextText.setEditable(false);
    visibility = new ReferenceCodeComboBox(VisibilityCode.class, VisibilityCode
        .getCodes());
    descriptionText = new JTextArea(3, TEXT_AREA_SIZE);
    descriptionText.setLineWrap(true);
    descriptionText.setWrapStyleWord(true);
    descriptionPane = new JScrollPane(descriptionText,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    tagsView = new TaggedValuesView();
    addNodePropertiesSubView(tagsView);
    initialize();
  }

  @Override
  public void populateView() {
    MetaDatatype model = (MetaDatatype) getDataObject();
    if (this.isNewObject()) {
      CartridgeFactory.getInstance().getItemFactory().initialize(model,
          MetaDatatype.class);
    }
    nameText.setText(model.getName());
    visibility.setSelectedCode(model.getVisibility());
    MetaElement context = model.getContext();
    contextText.setText(context != null ? context.qualifiedName() : null);
    descriptionText.setText(model.getDescription());
  }

  @Override
  public void commitChanges() {
    MetaDatatype model = (MetaDatatype) getDataObject();
    String text = nameText.getText();
    if (text != null) {
      model.setName(text.trim());
    }
    text = descriptionText.getText();
    model.setDescription(text != null ? text.trim() : null);
    model.setStereotype(text != null ? text.trim() : null);

    model.setVisibility((VisibilityCode) visibility.getSelectedCode());
    tagsView.commitChanges();
    getModel().nodeChanged();
  }

  @Override
  public void setViewEditable(boolean editable) {
    nameText.setEditable(editable);
    descriptionText.setEditable(editable);
    visibility.setEnabled(editable);
    tagsView.setEnabled(editable);
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
    builder.append("&Context", contextText);
    //builder.append("&Visibility", visibility);
    builder.append("&Description", descriptionPane, 5);
    builder.appendSeparator("Tagged Values");
    builder.append(tagsView, 7);
    JPanel panel = builder.getPanel();
    add(panel);

    validName = Pattern.compile("^[0-9]"         // no number at the beginning of the identifier
                                +"|[^\\w]");     // no special characters
  }
  
  /**
   * Focus listeners for verification of input. It applies input rules.
   * @author AdrianBachofen
   *
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
}