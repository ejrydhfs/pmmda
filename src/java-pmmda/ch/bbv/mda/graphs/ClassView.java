/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.graphs;

import java.awt.*;

import javax.swing.*;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexView;

import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaProperty;
import ch.bbv.utilities.TextUtilities;

/**
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ClassView extends VertexView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  static ClassRenderer renderer = new ClassRenderer();

  ClassCell model;

  /**
   * Constructor of the class.
   * @param cell graph cell displayed in the view.
   */
  public ClassView(Object cell) {
    super(cell);
    this.model = (ClassCell) cell;
  }

  public ClassCell getModel() {
    return model;
  }

  public CellViewRenderer getRenderer() {
    renderer.setView(this);
    return renderer;
  }
  
  /**
   * The renderer of the class view used to display the view. The renderer is
   * used as a lightweight object for all class views.
   * @author MarcelBaumann
   * @version $Revision$
   */
  static class ClassRenderer extends JPanel implements CellViewRenderer {
    /**
     * Serial version identifier coded as back compatibility date.
     */
    private static final long serialVersionUID = 20041201;

    /**
     * Cached suppressAttribute.
     */
    transient private boolean suppressAttribute;

    /**
     * Cached suppressOperation.
     */
    transient private boolean suppressOperation;

    /**
     * The top part of a class in UML diagram, consists of stereo type, class
     * identifier and class visibility.
     */
    private JPanel header;

    /**
     * An instance of JLabel for showing the stereo type.
     */
    private JLabel stereotypeLabel;

    /**
     * An instance of JLabel for showing the class identifier.
     */
    private JLabel classLabel;

    /**
     * An instance of JLabel for showing the stereo type.
     */
    private JLabel visibilityLabel;

    /**
     * An instance of JLabel for showing attributesArea.
     */
    private JTextArea attributesArea;

    /**
     * An instance of JLabel for showing operationsArea.
     */
    protected JTextArea operationsArea;

    private ClassView classView;

    public void setView(ClassView classView) {
      this.classView = classView;
      installAttributes(classView);
    }

    public ClassRenderer() {
      super(new BorderLayout());
      setBorder(BorderFactory.createLineBorder(Color.black));
      header = new JPanel(new BorderLayout());
      stereotypeLabel = new JLabel();
      stereotypeLabel.setHorizontalAlignment(JLabel.CENTER);
      classLabel = new JLabel();
      classLabel.setHorizontalAlignment(JLabel.CENTER);
      visibilityLabel = new JLabel();
      visibilityLabel.setHorizontalAlignment(JLabel.CENTER);
      header.add(stereotypeLabel, BorderLayout.NORTH);
      header.add(classLabel, BorderLayout.CENTER);
      header.add(visibilityLabel, BorderLayout.SOUTH);
      add(header, BorderLayout.NORTH);
      attributesArea = new JTextArea();
      attributesArea.setLineWrap(false);
      add(attributesArea, BorderLayout.CENTER);
      operationsArea = new JTextArea();
      operationsArea.setLineWrap(false);
      add(operationsArea, BorderLayout.SOUTH);
    }

    public Component getRendererComponent(JGraph graph, CellView view,
        boolean sel, boolean focus, boolean preview) {
      this.classView = (ClassView) view;
      installAttributes(view);
      return this;
    }

    public void paint(Graphics g) {
      Graphics2D g2d = (Graphics2D) g;
      Dimension d = getSize();
      g2d.drawRect(0, 0, d.width, d.width);
      super.paint(g2d);
      Rectangle rectangle = attributesArea.getBounds();
      g2d.drawLine(0, rectangle.y, rectangle.width, rectangle.y);
      rectangle = operationsArea.getBounds();
      g2d.drawLine(0, rectangle.y, rectangle.width, rectangle.y);
      g2d.setColor(getForeground());
    }

    /**
     * Install the attributesArea of specified cell in this renderer instance.
     * This means, retrieve every published key from the cells hashtable and set
     * global variables or superclass properties accordingly.
     * @param view to retrieve the attribute values from.
     */
    protected void installAttributes(CellView view) {
      ClassCell cell = classView.getModel();
      MetaClass clazz = cell.getModel();

      classLabel.setText(clazz.getName());
      String stereotype = clazz.getStereotype();
      if (stereotype != null && stereotype.length() > 0) {
        stereotypeLabel.setText("<<" + stereotype + ">>");
      } else {
        stereotypeLabel.setText(null);
      }
      visibilityLabel.setText(clazz.getVisibility().getShortDescription());

      attributesArea.setText(getAttributes(clazz));
      operationsArea.setText(null);
      attributesArea.setVisible(!suppressAttribute);
      operationsArea.setVisible(!suppressOperation);

      // Map map = classView.getAllAttributes();
      // Rectangle2D cellBounds = GraphConstants.getBounds(map);
      // Rectangle bounds = getBounds();
      // GraphConstants.setBounds(map, new Rectangle((int) cellBounds.getX(),
      // (int) cellBounds.getY(), bounds.width, bounds.height));
    }

    private String getAttributes(MetaClass clazz) {
      StringBuilder attributes = new StringBuilder();
      for (MetaProperty property : clazz.getProperties()) {

        attributes.append(
            TextUtilities.getComponentName(property.getTypeName())).append(" ")
            .append(property.getName()).append("\n");
      }
      return attributes.toString();
    }
  }
}
