/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.explorer.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

/**
 * The list transfer panel allows a user to transfer items from one list to
 * another one. Both lists contains items of the same type. The union of all
 * items of both lists defines the range of possible values. Configuration
 * allows to define if the reference lists contains items part of the second
 * list.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ListsTransferPanel extends JPanel implements ActionListener {
  /**
   * Serial version identifier for the class.
   */
  private static final long serialVersionUID = -8399581519167593998L;

  /**
   * A copy of the list of references objects.
   */
  private DefaultListModel references;

  /**
   * A copy of the list of selected objects.
   */
  private DefaultListModel items;

  /**
   * Left list of the panel.
   */
  private JList leftList;

  /**
   * Right list of the panel.
   */
  private JList rightList;

  /**
   * The button used to move items from left to right.
   */
  private JButton left2right;

  /**
   * The button used to move items from right to left.
   */
  private JButton right2left;

  /**
   * Flag indicating of objects can be in both lists or not.
   */
  boolean allowDuplicates;

  /**
   * Default constructor of the class.
   */
  public ListsTransferPanel() {
    references = new DefaultListModel();
    items = new DefaultListModel();
    leftList = new JList(references);
    rightList = new JList(items);
    left2right = new JButton(">>");
    left2right.addActionListener(this);
    right2left = new JButton("<<");
    right2left.addActionListener(this);
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    add(new JScrollPane(leftList));
    JPanel centerPane = new JPanel();
    centerPane.setLayout(new BoxLayout(centerPane, BoxLayout.Y_AXIS));
    centerPane.add(left2right);
    centerPane.add(Box.createRigidArea(new Dimension(0, 50)));
    centerPane.add(right2left);
    add(centerPane);
    add(new JScrollPane(rightList));
  }

  /**
   * Sets the two list to manipulate.
   * @param references the reference list
   * @param items the item list
   * @param allowDuplicates flag indicating if duplicates between the lists are
   *          allowed or not
   */
  public void setLists(List< ? extends Object> references,
      List< ? extends Object> items, boolean allowDuplicates) {
    this.references.clear();
    for (Object object : references) {
      this.references.addElement(object);
    }
    this.items.clear();
    for (Object object : items) {
      this.items.addElement(object);
    }
    this.allowDuplicates = allowDuplicates;
    if (!allowDuplicates) {
      for (Object object : items) {
        this.references.removeElement(object);
      }
    }
  }

  /**
   * Returns the content of the references list.
   * @return the requested list
   */
  public List getReferences() {
    return Arrays.asList(references.toArray());
  }

  /**
   * Returns the content of the items list.
   * @return the requested list
   */
  public List getItems() {
    return Arrays.asList(items.toArray());
  }

  /**
   * @inheritDoc
   */
  public void actionPerformed(ActionEvent event) {
    Object source = event.getSource();
    if (left2right == source) {
      addPressed();
    } else if (right2left == source) {
      removePressed();
    }
  }

  /**
   * Handles the event of the add button pressed.
   */
  protected void addPressed() {
    for (Object item : leftList.getSelectedValues()) {
      if (!items.contains(item)) {
        items.addElement(item);
        if (!allowDuplicates) {
          references.removeElement(item);
        }
      }
    }
    leftList.clearSelection();
  }

  /**
   * Handles the event of the remove button pressed.
   */
  protected void removePressed() {
    for (Object item : rightList.getSelectedValues()) {
      if (!references.contains(item)) {
        references.addElement(item);
        if (!allowDuplicates) {
          items.removeElement(item);
        }
      }
    }
    rightList.clearSelection();
  }
}
