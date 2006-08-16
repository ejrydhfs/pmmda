/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.cartridges;

import java.io.PrintWriter;

class OrmSchema {
  enum SchemaState {
    prefix, body, postfix
  }

  /**
   * Name of the schema.
   */
  private String name;

  /**
   * State of the schema.
   */
  private SchemaState state;

  /**
   * Writer used to generate the associated artifact.
   */
  PrintWriter ddlWriter;

  /**
   * Writer used to generate the associated artifact.
   */
  PrintWriter ddlIntegrityWriter;

  /**
   * Writer used to generate the DDL utils XML artifacts.
   */
  PrintWriter ddlXmlWriter;

  /**
   * Writer used to generate the associated artifact.
   */
  PrintWriter ormBoWriter;

  /**
   * Writer used to generate the associated artifact.
   */
  PrintWriter ormLwWriter;

  public OrmSchema(String name) {
    this.name = name;
    state = SchemaState.prefix;
  }

  public String getName() {
    return name;
  }

  public void setState(SchemaState state) {
    this.state = state;
  }

  public SchemaState getState() {
    return state;
  }
}
