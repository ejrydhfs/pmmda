/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.graphs;

import org.jgraph.graph.DefaultGraphCell;

import ch.bbv.mda.MetaClass;

/**
 * The class implements the marker for UML classes. The graph framework uses
 * this information to associate the corresponding view and renderer.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ClassCell extends DefaultGraphCell {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  private MetaClass model;

  /**
   * Default constructor of the class.
   */
  public ClassCell() {
    this(null);
  }

  public ClassCell(MetaClass model) {
    super(model);
    this.model = model;
  }

  public MetaClass getModel() {
    return model;
  }
}
