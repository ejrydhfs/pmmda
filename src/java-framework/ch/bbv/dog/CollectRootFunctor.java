/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The collect root functor collects all root objects, meaning all objects which
 * data object type has the flag is root set.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CollectRootFunctor implements DoFunctor {
  /**
   * The data object manager responsible for the data objects.
   */
  private DataObjectManager manager;

  /**
   * List of collected root objects found in the data object graph.
   */
  private List<DataObject> roots;

  /**
   * List of collected objects found in the data object graph.
   */
  private List<DataObject> objects;

  /**
   * Constructor of the class.
   * @param manager manager of the data objects part of the traversed tree.
   */
  public CollectRootFunctor(DataObjectManager manager) {
    this.manager = manager;
    reset();
  }

  /**
   * Resets the functor so it is ready to collect the next set of roots.
   */
  public void reset() {
    roots = new ArrayList<DataObject>();
    objects = new ArrayList<DataObject>();
  }

  /**
   * Returns the list of roots collected during the traversal of a data object
   * graph.
   * @return the list of collected roots.
   */
  public List<DataObject> getRoots() {
    return Collections.unmodifiableList(roots);
  }

  /**
   * Returns the list of data objects collected during the traversal of a data
   * object graph.
   * @return the list of collected data objects.
   */
  public List<DataObject> getObjects() {
    return Collections.unmodifiableList(objects);
  }

  public void execute(DataObject object) {
    objects.add(object);
    DataObjectType type = manager.getType(object);
    if (type != null && type.isRoot()) {
      roots.add(object);
    }
  }
}