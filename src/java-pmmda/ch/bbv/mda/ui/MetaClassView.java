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

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import ch.bbv.mda.MetaClass;
import ch.bbv.mda.VisibilityCode;

import com.jgoodies.forms.builder.DefaultFormBuilder;

/**
 * The meta class view defines the view of a MDA class.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class MetaClassView extends MetaItemView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Pattern for validating identifiers
   */
  static Pattern validName;

  /**
   * Swing component to display the associated property.
   */
  private JCheckBox abstracts;

  /**
   * Swing component to display the associated property.
   */
  private JCheckBox finals;

  /**
   * Swing component to display the associated property.
   */
  private JCheckBox interfaces;

  /**
   * Swing component to display the associated property.
   */
  private JCheckBox statics;

  /**
   * Default constructor of the class.
   */
  public MetaClassView() {
    nameText.addFocusListener(new VerificationActions());
    abstracts = new JCheckBox();
    finals = new JCheckBox();
    interfaces = new JCheckBox();
    statics = new JCheckBox();
    initialize();
  }

  @Override
  public void populateView() {
    super.populateView();
    MetaClass model = (MetaClass) getDataObject();
    visibility.setSelectedCode(model.getVisibility());
    abstracts.setSelected(model.isAbstract());
    interfaces.setSelected(model.isInterface());
    statics.setSelected(model.isStatic());
    finals.setSelected(model.isFinal());
  }

  @Override
  public void commitChanges() {
    super.commitChanges();
    MetaClass model = (MetaClass) getDataObject();
    model.setVisibility((VisibilityCode) visibility.getSelectedCode());
    model.setAbstract(abstracts.isSelected());
    model.setFinal(finals.isSelected());
    model.setInterface(interfaces.isSelected());
    model.setStatic(statics.isSelected());
    getModel().nodeChanged();
  }

  @Override
  public void setViewEditable(boolean editable) {
    super.setViewEditable(editable);
    abstracts.setEnabled(editable);
    finals.setEnabled(editable);
    interfaces.setEnabled(editable);
    statics.setEnabled(editable);
  }

  /**
   * Initialize the view component with the user interface elements.
   */
  protected void initialize() {
    DefaultFormBuilder builder = initializeGeneral();
    builder.appendSeparator("Modifiers");
    builder.append("&abstract", abstracts);
    builder.append("&interface", interfaces);
    builder.append("&final", finals);
    builder.append("&static", statics);
    builder.appendSeparator("Tagged Values");
    builder.append(tagsView, 7);
    JPanel panel = builder.getPanel();
    add(panel);

    validName = Pattern.compile("^[0-9]" // no number at the beginning of the
        // identifier
        + "|[^\\w]"); // no special characters
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
}