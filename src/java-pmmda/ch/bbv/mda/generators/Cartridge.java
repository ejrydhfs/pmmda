/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.generators;

import java.util.List;
import java.util.Properties;

import ch.bbv.application.VersionId;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.TaggedValue;
import ch.bbv.mda.TaggedValueType;

/**
 * The cartridge defines the interface for all code generators defined in the
 * application. The cartridge calls the related methods when walking through the
 * packages and classes of a model. Code generators can be called for a whole
 * model, a package or a class. The framework guarantees that pre- and
 * post-process methods are called before and after the generation methods. The
 * traversal of the components is a three steps process.
 * <ul>
 * <li>The pre-processing phase is used to compute all the attributes of the
 * component and generates the associated context.</li>
 * <li>The processing phase is used to generate all the MDA artifacts specific
 * to the cartridge.</li>
 * <li>The post-processing phase is used to perform house-keeping activities.
 * and complete operations.</li>
 * </ul>
 * @author Marcel Baumann
 * @version $Revision$
 */
public interface Cartridge {
  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  String USER_IMPORT_BEGIN = "/* %% USER DEFINED IMPORT START %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  String USER_IMPORT_END = "/* %% USER DEFINED IMPORT END %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  String USER_DECLARATION_BEGIN = "/* %% USER DEFINED DECLARATION START %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  String USER_DECLARATION_END = "/* %% USER DEFINED DECLARATION END %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  String USER_CONSTRUCTORS_BEGIN = "/* %% USER DEFINED CONSTRUCTORS START %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  String USER_CONSTRUCTORS_END = "/* %% USER DEFINED CONSTRUCTORS END %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  String USER_CODE_BEGIN = "/* %% USER DEFINED CODE START %% */";

  /**
   * Comment in the generated code to detect a border of a user defined source
   * code.
   */
  String USER_CODE_END = "/* %% USER DEFINED CODE END %% */";

  /**
   * Sets the cartridge context for the cartridge. The context contains all user
   * specific configuration values.
   * @param properties new cartridge context properties.
   * @pre (properties != null)
   */
  void initialize(Properties properties);

  /**
   * Returns the list of tagged value types for the given class type. The class
   * should be a descended of meta element.
   * @param clazz class, which tagged value types should be returned.
   * @return the list of tagged values for the class. If none exists and the
   *         class is handled in the cartdrige an empty list is returned else
   *         null is returned.
   * @pre (clazz != null)
   */
  List getTaggedValueTypes(Class clazz);
  
  /**
   * Returns a tagged value initialized with a cartridge dependent default value.
   * @param taggedValueType being the type for the tagged value to be initialized with a default value.
   * @param element The MetaElement providing additional information to initialize the tagged value.
   * @return TaggedValue the initialized tagged value.
   */
  TaggedValue getTaggedValueWithElementDependentDefault(TaggedValueType taggedValueType, MetaElement element);

  /**
   * Returns the list of obsolete tagged value types for the given class type.
   * The class should be a descended of meta element.
   * @param clazz class, which tagged value types should be returned.
   * @return the list of tagged values for the class. If none exists and the
   *         class is handled in the cartdrige an empty list is returned else
   *         null is returned.
   * @pre (clazz != null)
   */
  List getObsoleteTaggedValueTypes(Class clazz);

  /**
   * Returns all the properties declared in the cartrige with their values and
   * default values.
   * @return list of all properties.
   */
  List getAllProperties();

  /**
   * Returns all the stereotypes used in the cartridge.
   * @return list of all stereotypes.
   */
  List getAllStereotypes();

  /**
   * Returns the cartridge context of the cartridge.
   * @return the requested context.
   */
  Properties getContext();

  /**
   * Returns the name of the cartridge, which should be unique in the set of
   * cartridges.
   * @return name of the cartridge
   */
  String getName();

  /**
   * Returns the version identifier of the cartridge. The version uniquely
   * identifies the version of the cartridge.
   * @return the version of the cartridge.
   */
  VersionId getVersion();

  /**
   * Returns the abbreviation of the cartridge, which should be unique in the
   * set of cartridges.
   * @return abbreviation of the cartridge.
   */
  String getPrefix();

  /**
   * Verifies that all paths defined in properties are to folders accessible.
   * @return true if all path properties are accessible otherwise false.
   */
  boolean checkPathProperties();

  /**
   * Returns the list of cartridges from which the cartridge is dependant. They
   * are attributes are automatically computed before the cartridge generation
   * is started.
   * @return the list of cartridge classes from which the cartridge is
   *         dependant.
   */
  List getUsedCartridges();

  /**
   * Preprocesses the project and computes all the attributes used in the
   * cartridge for the model. The context of the model is also computed.
   * @param model MDA model to process.
   * @pre (model != null)
   */
  void preProcessModel(MetaModel model);

  /**
   * Processes the project. The context contains the project information.
   * @param model MDA model to process.
   * @pre (model != null)
   */
  void processModel(MetaModel model);

  /**
   * Postprocesses the project. The context contains the project information.
   * @param model MDA model to process.
   * @pre (model != null)
   */
  void postProcessModel(MetaModel model);

  /**
   * Preprocesses the package and computes all the attributes used in the
   * cartridge for the package. The context of the package is also computed.
   * @param metaPackage MDA package to process.
   * @pre (metaPackage != null)
   */
  void preProcessPackage(MetaPackage metaPackage);

  /**
   * Processes the given package.
   * @param metaPackage MDA package to process.
   * @pre (metaPackage != null)
   */
  void processPackage(MetaPackage metaPackage);

  /**
   * Postprocesses the given package.
   * @param metaPackage MDA package to process.
   * @pre (metaPackage != null)
   */
  void postProcessPackage(MetaPackage metaPackage);

  /**
   * Preprocesses the class and computes all the attributes used in the
   * cartridge for the class. The context of the class is also computed.
   * @param clazz MDA class to process.
   * @pre (clazz != null)
   */
  void preProcessClass(MetaClass clazz);

  /**
   * Processes the given class.
   * @param clazz MDA clas to process.
   * @pre (clazz != null)
   */
  void processClass(MetaClass clazz);
}