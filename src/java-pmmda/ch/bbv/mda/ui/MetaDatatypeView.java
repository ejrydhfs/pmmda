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

import javax.swing.JPanel;

import ch.bbv.mda.MetaDatatype;
import ch.bbv.mda.generators.CartridgeFactory;

import com.jgoodies.forms.builder.DefaultFormBuilder;

/**
 * The meta class view defines the view of a MDA class.
 * @author marcelbaumann
 * @version $Revision$
 */
public class MetaDatatypeView extends MetaItemView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20060103;

  /**
   * Pattern for validating identifiers
   */
  static Pattern validName;

  /**
   * Default constructor of the class.
   */
  public MetaDatatypeView() {
    nameText.addFocusListener(new VerificationActions());
    initialize();
  }

  @Override
  public void populateView() {
    MetaDatatype model = (MetaDatatype) getDataObject();
    if (this.isNewObject()) {
      CartridgeFactory.getInstance().getItemFactory().initialize(model,
          MetaDatatype.class);
    }
    super.populateView();
  }

  @Override
  public void commitChanges() {
    super.commitChanges();
    getModel().nodeChanged();
  }

  /**
   * Initialize the view component with the user interface elements.
   */
  protected void initialize() {
    DefaultFormBuilder builder = initializeGeneral();
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