/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.graphs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgraph.graph.*;
import org.jgraph.layout.RadialTreeLayoutAlgorithm;
import org.jgraph.util.JGraphUtilities;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.NodeView;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.mda.*;
import ch.bbv.mda.cartridges.BoConstants;

/**
 * The class graph view displays a UML class diagram of the package or of the
 * class. The JGraph component is used to display the graphs.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ClassGraphView extends JPanel implements NodeView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Scrolling pane containing the view.
   */
  private JScrollPane pane;

  /**
   * graph model to display.
   */
  private GraphModel model;

  /**
   * The graph view on the graph model.
   */
  private JGraph graph;

  /**
   * Layout algorithm for the class diagram.
   */
  RadialTreeLayoutAlgorithm layout;

  private Map<GraphCell, Map> attributes;

  private static class UmlCellViewFactory extends DefaultCellViewFactory {
    /**
     * Serial version identifier coded as back compatibility date.
     */
    private static final long serialVersionUID = 20041201;

    public CellView createView(GraphModel model, Object cell) {
      if (cell instanceof ClassCell) {
        return new ClassView(cell);
      }
      return super.createView(model, cell);
    }
  }

  /**
   * Creates an instance of the class.
   * @param type user interface type of the nodes to display.
   * @param object parameters list for the constructor.
   * @return a new instance of the class.
   */
  public static ClassGraphView create(UiNodeType type, Object object) {
    return new ClassGraphView();
  }

  /**
   * Default constructor of the class.
   */
  public ClassGraphView() {
    super(new BorderLayout());
    attributes = new HashMap<GraphCell, Map>();
    model = new DefaultGraphModel();
    graph = new JGraph(model);
    graph.setEditable(false);
    graph.getGraphLayoutCache().setFactory(new UmlCellViewFactory());
    layout = new RadialTreeLayoutAlgorithm();
    pane = new JScrollPane(graph);
    add(pane, BorderLayout.CENTER);
  }

  public void nodeChanged(ExplorerNode node) {
  }

  public void nodeInserted(ExplorerNode node, int index) {
  }

  public void nodeRemoved(ExplorerNode parent, ExplorerNode node, int index) {
  }

  /**
   * Sets the model associated whith the node.
   * @param node node which model should be set.
   */
  public void setModel(ExplorerNode node) {
    attributes.clear();
    model = new DefaultGraphModel();
    graph.setModel(model);
    MetaElement element = (MetaElement) node.getDataObject();
    if (element instanceof MetaClass) {
      generateDependencies((MetaClass) element);
      JGraphUtilities.applyLayout(graph, layout);
    }
  }

  public void viewClosed() {
  }

  public void setViewEditable(boolean editable) {
  }

  /**
   * Generates all classes in the package. Simple and indexed properties which
   * type is a class defined in the package are shown as aggregations.
   * @param packages package which dependencies should be drawn.
   */
  protected void generateDependencies(MetaPackage packages) {
  }

  /**
   * Generates the graph of dependencies of the class. Simple and indexed
   * properties which type is not a data object or a reference code are drawn as
   * properties. Properties with other types are drawn as aggregations and the
   * type is shown as target class.
   * @param clazz class which dependencies graph should be drawn.
   */
  protected void generateDependencies(MetaClass clazz) {
    MetaModel model = clazz.getModel();
    GraphCell source = createVertex(clazz);
    for (MetaProperty property : clazz.getProperties()) {
      String qualifiedName = property.getTypeName();
      MetaClass type = model.getClass(qualifiedName);
      if ((type != null)
          && (type.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE) || type
              .hasStereotype(BoConstants.REFERENCE_CODE_STEREOTYPE))) {
        GraphCell target = createVertex(type);
        createEdge(source, target, property.getName(),
            AssociationEdge.Type.aggregation);
      }
    }
    for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
      String qualifiedName = property.getTypeName();
      MetaClass type = model.getClass(qualifiedName);
      if ((type != null)
          && (type.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE) || type
              .hasStereotype(BoConstants.REFERENCE_CODE_STEREOTYPE))) {
        GraphCell target = createVertex(type);
        createEdge(source, target, property.getName(),
            AssociationEdge.Type.aggregation);
      }
    }
    for (MetaClass ancestor : clazz.retrieveAncestors()) {
      GraphCell target = createVertex(ancestor);
      createEdge(source, target, null, AssociationEdge.Type.inheritance);
    }
    for (MetaClass interfaces : clazz.retrieveInterfaces()) {
      GraphCell target = createVertex(interfaces);
      createEdge(source, target, null, AssociationEdge.Type.inheritance);
    }
  }

  protected GraphCell createVertex(MetaElement element) {
    DefaultGraphCell node = new ClassCell((MetaClass) element);
    Map nodeAttributes = new HashMap();
    GraphConstants.setOpaque(nodeAttributes, true);
    GraphConstants.setBorderColor(nodeAttributes, Color.BLACK);
    GraphConstants.setBounds(nodeAttributes, new Rectangle(20, 20, 40, 20));
    GraphConstants.setAutoSize(nodeAttributes, true);
    GraphConstants.setResize(nodeAttributes, true);
    DefaultPort port = new DefaultPort();
    node.add(port);
    attributes.put(node, nodeAttributes);
    Object[] nodes = { node };
    model.insert(nodes, attributes, null, null, null);
    return node;
  }

  protected Edge createEdge(GraphCell source, GraphCell target, String name,
      AssociationEdge.Type type) {
    // AssociatonEdge edge = new AssociationEdge();
    DefaultEdge edge = new DefaultEdge(name);
    Map edgeAttributes = new HashMap();
    int arrow = GraphConstants.ARROW_CLASSIC;
    Port sourcePort = getDefaultPort(source);
    Port targetPort = getDefaultPort(target);
    GraphConstants.setLineEnd(edgeAttributes, arrow);
    GraphConstants.setEndFill(edgeAttributes, true);
    ConnectionSet connectionSet = new ConnectionSet(edge, sourcePort,
        targetPort);
    attributes.put(edge, edgeAttributes);
    Object[] edges = { edge };
    model.insert(edges, attributes, connectionSet, null, null);
    return edge;
  }

  protected Port getDefaultPort(Object vertex) {
    for (int i = 0; i < model.getChildCount(vertex); i++) {
      Object child = model.getChild(vertex, i);
      if (child instanceof Port) {
        return (Port) child;
      }
    }
    return null;
  }
}
