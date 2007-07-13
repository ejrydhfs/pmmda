/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.functors;

import java.util.List;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DoFunctor;
import ch.bbv.dog.updates.DataObjectUpdate;

/**
 * The update functor is responsible to collects the update informations after
 * the graph of data objects was commited. The new timestamp values for all
 * modified or created objects are collected and the identifiers of the new
 * objects are collected.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class UpdateFunctor implements DoFunctor {
  /**
   * List of all update information. The type of the items is data object
   * property.
   */
  private List<DataObjectUpdate> infos;

  /**
   * Index in the list.
   */
  private int index;

  /**
   * Resets the functor and defines a new list of updates to execute.
   * @param infos list of data object updates to apply.
   */
  public void reset(List<DataObjectUpdate> infos) {
    this.infos = infos;
    index = 0;
  }

  public void execute(DataObject object) {
    DataObjectUpdate update = null;
    if (object.isModified()) {
      assert (index <= infos.size());
      update = infos.get(index);
      index++;
      assert ((object.getId() == null) || ((object.getId().equals(update
          .getId())) && (object.getClass().equals(update
          .getDoClass()))));
      if (object.getId() == null) {
        object.setId(update.getId());
      }
      object.setTimestamp(update.getTimestamp());
      List updates = update.getUpdates();
      if (updates.size() > 0) {
        // TODO write code for properties update
      }
      object.setModified(false);
      assert ((object.getId() != null) && (object.getTimestamp() != null) && !object
          .isModified());
    }
  }
}