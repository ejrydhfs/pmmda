/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.resources;

public class MessagesMda {
  public static String actionGenerate = "Generate";

  public static String actionGenerateAll = "Generate All";

  public static String menuGenerate = "Generate";

  public static String menuTools = "Tools";

  public static String titleDefaultModel = "default";

  public static String titleValidator = "Validator";

  public static String titleConfirmation = "Alert";

  public static String msgNoFileDefined = "No file name is defined";

  public static String msgIllegalFilename = "The file name {0} is incorrect or does not exist";

  public static String msgModelhasErrors = "Generation failed because of errors within model - please check validation pane and remove errors";

  public static String msgUnsupportedFileType = "Currently only *.zargo, *.ser, *.xmi, *.mdl, and *.xml files can be saved\nPlease use save as (only xml, ser, zargo, xmi, mdl format supported)";

  public static String msgIllegalFileNameOrType = "Please enter a complete file name with extension\ncurrently only xml, xmi, ser, mdl and zargo format are possible for save as";

  public static String msgOldFormat = "This file has been saved with a previous version of pmMDA.\n Open this file with the previous version and save it as *.xml or *.xmi";

  public static String msgInvalidFormat = "This file does not contain a valid format. Please check extension or content of file";

  public static String[] stereotypeColumNames = { "name" };

  public static final String[] tagTypeColumnNames = { "name", "default",
      "typename" };

  public static String[] propertyColumnNames = { "name", "default", "value" };

  public static String[] cartridgeColumnNames = { "name", "prefix" };

}