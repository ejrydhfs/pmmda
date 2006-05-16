/* copyright 2000-2006 Marcel Baumann 
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
public final class OrmConstants {
  /**
   * Key part for a property of the cartridge used to retrieve user values.
   */
  public static final String DDL_START_KEY = "orm-cartridge.ddl.";

  /**
   * Key part for a property of the cartridge used to retrieve user values.
   */
  public static final String DDL_INTEGRITY_START_KEY = "orm-cartridge.ddl-integrity.";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String SOURCE_ROOT_FOLDER_KEY = "orm-cartridge.root-folder";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String GENERATE_FK_INDEX_KEY = "orm-cartridge.generate-fk-index";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String GENERATE_CONSTRAINTS_KEY = "orm-cartridge.generate-constraints";

  /**
   * Key for a property of the cartridge defining the inheritance mode.
   */
  public static final String INHERITANCE_KEY = "orm-cartridge.inheritance";

  /**
   * Key for a property of the cartridge defining the DDL generator.
   */
  public static final String GENERATOR_KEY = "orm-cartridge.database-DDL-generator";
  
  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String DATABASE_KEY = "orm-cartridge.database";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String ORM_KEY = "orm-cartridge.orm";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String TABLE_PREFIX_KEY = "orm-cartridge.table-prefix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String TABLE_POSTFIX_KEY = "orm-cartridge.table-postfix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String COLUMN_PREFIX_KEY = "orm-cartridge.column-prefix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String COLUMN_POSTFIX_KEY = "orm-cartridge.column-postfix";

  /**
   * The tag shows if the class is persistent or not.
   */
  public static final String SCHEMA = "orm-schema";

  /**
   * The name of the table in which the instances are persisted.
   */
  public static final String TABLE_NAME = "orm-table-name";

  /**
   * The column name of the property if the property is persistent.
   */
  public static final String COLUMN_NAME = "orm-column-name";

  /**
   * JDBC type of the property if the property is persistent.
   */
  public static final String COLUMN_TYPE = "orm-column-type";

  /**
   * JDBC type size of the property if the property is persistent and if the
   * information is relevant for the associated JDBC type.
   */
  public static final String COLUMN_SIZE = "orm-column-size";

  /**
   * Indicates if the property is a primary key for the class it belongs to.
   */
  public static final String IS_PRIMARY = "orm-is-primary";

  /**
   * Flag defines if the property persistent representation can be null. Such a
   * property must not be a primitive Java type and not a primary key.
   */
  public static final String CAN_BE_NULL = "orm-can-be-null";

  /**
   * Defines if the property is used for ordering.
   */
  public static final String ORDER_BY = "orm-order-by";

  /**
   * The attribute for the classifier representation for the SQL language.
   */
  public static final String ORM_TYPE_SQL = "orm-type-sql";

  /**
   * The attribute for the classifier representation for the JDBC language.
   */
  public static final String ORM_TYPE_JDBC = "orm-type-jdbc";

  /**
   * The attribute for the classifier indicating if he can be null when read
   * from the persistent storage.
   */
  public static final String ORM_TYPE_SQL_CAN_BE_NULL = "orm-type-sql-null";

  /**
   * The attribute for the classifier indicating the number of parameters for
   * the SQL definition.
   */
  public static final String ORM_TYPE_SQL_PARAMETERS = "orm-type-sql-parameters";

  /**
   * The default value for the parameters of the SQL definition.
   */
  public static final String ORM_TYPE_DEFAULT_VALUE = "orm-type-default-value";

  /**
   * Database identifier for a database product.
   */
  public static final String DB_ORACLE = "ORACLE";

  /**
   * Database identifier for a database product.
   */
  public static final String DB_MS_SQL_SERVER = "MSSQLSERVER";

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
