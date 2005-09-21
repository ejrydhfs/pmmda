/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.cartridges;

/**
 * The constant class defines the public constant declarations of the cartridge.
 * The stereotypes, tags, attributes and artifact markers are made available.
 * The keys of the properties available to the cartridge are also made
 * available.
 * @author MarcelBaumann
 * @version $Revision$
 */
public final class OjbConstants {
  /**
   * Key part for a property of the cartridge used to retrieve user values.
   */
  public static final String DDL_START_KEY = "ojb-cartridge.ddl.";

  /**
   * Key part for a property of the cartridge used to retrieve user values.
   */
  public static final String DDL_INTEGRITY_START_KEY = "ojb-cartridge.ddl-integrity.";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String SOURCE_ROOT_FOLDER_KEY = "ojb-cartridge.root-folder";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String GENERATE_FK_INDEX_KEY = "ojb-cartridge.generate-fk-index";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String GENERATE_CONSTRAINTS_KEY = "ojb-cartridge.generate-constraints";

  /**
   * Key for a property of the cartridge defining the inheritance mode.
   */
  public static final String INHERITANCE_KEY = "ojb-cartridge.inheritance";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String DATABASE_KEY = "ojb-cartridge.database";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String TABLE_PREFIX_KEY = "obj-cartridge.table-prefix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String TABLE_POSTFIX_KEY = "obj-cartridge.table-postfix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String COLUMN_PREFIX_KEY = "obj-cartridge.column-prefix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String COLUMN_POSTFIX_KEY = "obj-cartridge.column-postfix";

  /**
   * The tag shows if the class is persisten or not.
   */
  public static final String SCHEMA_TAG = "ojb-schema";

  /**
   * If the class is persistent, this tag contains the name of the table in
   * which the instances are persisted.
   */
  public static final String TABLE_NAME_TAG = "ojb-table-name";

  /**
   * JDBC column name of the property if the property is persistent.
   */
  public static final String COLUMN_NAME_TAG = "ojb-column-name";

  /**
   * JDBC type of the property if the property is persistent.
   */
  public static final String COLUMN_TYPE_TAG = "ojb-column-type";

  /**
   * JDBC type size of the property if the property is peristent and if the
   * information is relevant for the associated JDBC type.
   */
  public static final String COLUMN_SIZE_TAG = "ojb-column-size";

  /**
   * Indicates if the property is a primary key for the class it belongs to.
   */
  public static final String IS_PRIMARY_TAG = "ojb-is-primary";

  /**
   * True if the property persistent representation can be null. Such a property
   * must not be a primitive Java type and not a primary key.
   */
  public static final String CAN_BE_NULL_TAG = "ojb-can-be-null";

  /**
   * Defines if the property is used for ordering.
   */
  public static final String ORDER_BY_TAG = "ojb-order-by";

  /**
   * Attribute defining the computed table name.
   */
  public static final String TABLE_NAME = TABLE_NAME_TAG;

  /**
   * Attribute defining the computed column name.
   */
  public static final String COLUMN_NAME = COLUMN_NAME_TAG;

  /**
   * Database identifier for a database product.
   */
  public static final String DB_MYSQL = "MySQL";

  /**
   * Database identifier for a database product.
   */
  public static final String DB_DB2 = "DB2";

  /**
   * Database identifier for a database product.
   */
  public static final String DB_HSQL = "HSQL";

  /**
   * Database identifier for a database product.
   */
  public static final String DB_POSTGRES = "POSTGRES";

  /**
   * Database identifier for a database product.
   */
  public static final String DB_TORQUE = "TORQUE";
}
