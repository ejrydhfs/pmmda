/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.generators;

import java.util.List;
import java.util.Properties;

import ch.bbv.application.VersionId;
import ch.bbv.dog.validation.Validator;
import ch.bbv.mda.*;

/**
 * The cartridge defines the interface for all code generators defined in the
 * application. The cartridge calls the related methods when walking through the
 * packages and classes of a model. Code generators can be called for a whole
 * model, a package or a class. The framework guarantees that pre- and
 * post-process methods are called before and after the generation methods. The
 * traversal of the components is a three steps process.
 * <ul>
 * <li>The pre-processing phase is used to compute all the attributes of the
 * component and generates the associated context. The cartridge should process
 * the public attributes to allow dependant cartridges to use them.</li>
 * <li>The processing phase is used to generate all the MDA artifacts specific
 * to the cartridge. Often the artifacts are written during this phase.</li>
 * <li>The post-processing phase is used to perform house-keeping activities.
 * and complete operations. For example file handles can be closed.</li>
 * </ul>
 * @author Marcel Baumann
 * @version $Revision$
 */
public interface Cartridge {
  /**
   * Sets the cartridge context for the cartridge. The context contains all user
   * specific configuration values.
   * @param properties new cartridge context properties. All the properties
   *          available to the application are part of the property parameter.
   * @pre (properties != null)
   */
  void initialize(Properties properties);

  /**
   * Returns the list of tagged value types for the given class type. The class
   * should be a descendent of meta element.
   * @param clazz class, which tagged value types should be returned.
   * @return the list of tagged values for the class. If none exists and the
   *         class is handled in the cartdrige an empty list is returned else
   *         null is returned.
   * @pre (clazz != null)
   */
  List<TagDefinition> getTaggedValueTypes(Class clazz);

  /**
   * Returns the list of obsolete tagged value types for the given class type.
   * The class should be a descended of meta element.
   * @param clazz class, which tagged value types should be returned.
   * @return the list of tagged values for the class. If none exists and the
   *         class is handled in the cartdrige an empty list is returned else
   *         null is returned.
   * @pre (clazz != null)
   */
  List<TagDefinition> getObsoleteTaggedValueTypes(Class clazz);

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
  List<StereotypeType> getAllStereotypes();

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
  List<Cartridge> getUsedCartridges();

  /**
   * Registers the validation rules of the cartridges onto the validator.
   * @param validator validator in charge of the cartridge validation.
   * @pre validator != null
   */
  void registerRules(Validator validator);

  /**
   * Preprocesses the project and computes all the attributes used in the
   * cartridge for the model. The context of the model is also computed.
   * @param model MDA model to process.
   * @pre model != null
   */
  void preProcessModel(MetaModel model);

  /**
   * Processes the project. The context contains the project information.
   * @param model MDA model to process.
   * @pre model != null
   */
  void processModel(MetaModel model);

  /**
   * Postprocesses the project. The context contains the project information.
   * @param model MDA model to process.
   * @pre model != null
   */
  void postProcessModel(MetaModel model);

  /**
   * Preprocesses the datatype and computes all the attributes used in the
   * cartidge for the datatype.
   * @param datatype datatype to process.
   * @pre datatype != null
   */
  void preProcessDatatype(MetaDatatype datatype);
  
  /**
   * Preprocesses the package and computes all the attributes used in the
   * cartridge for the package. The context of the package is also computed.
   * @param metaPackage MDA package to process.
   * @pre metaPackage != null
   */
  void preProcessPackage(MetaPackage metaPackage);

  /**
   * Processes the given package.
   * @param metaPackage MDA package to process.
   * @pre metaPackage != null
   */
  void processPackage(MetaPackage metaPackage);

  /**
   * Postprocesses the given package.
   * @param metaPackage MDA package to process.
   * @pre metaPackage != null
   */
  void postProcessPackage(MetaPackage metaPackage);

  /**
   * Preprocesses the class and computes all the attributes used in the
   * cartridge for the class. The context of the class is also computed.
   * @param clazz MDA class to process.
   * @pre clazz != null
   */
  void preProcessClass(MetaClass clazz);

  /**
   * Processes the given class.
   * @param clazz MDA clas to process.
   * @pre clazz != null
   */
  void processClass(MetaClass clazz);
}