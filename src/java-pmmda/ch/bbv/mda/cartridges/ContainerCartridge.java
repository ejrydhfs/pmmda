/* copyright 2000-2006 Marcel Baumann, Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id: 
 */
package ch.bbv.mda.cartridges;

import static ch.bbv.mda.cartridges.ContainerConstants.CODE_BLOCKS;
import static ch.bbv.mda.cartridges.ContainerConstants.CONSTRUCTOR_BLOCK;
import static ch.bbv.mda.cartridges.ContainerConstants.CONTAINER_CLASS;
import static ch.bbv.mda.cartridges.ContainerConstants.CONTAINER_CLASS_POSTFIX_KEY;
import static ch.bbv.mda.cartridges.ContainerConstants.CONTAINER_CLASS_PREFIX_KEY;
import static ch.bbv.mda.cartridges.ContainerConstants.CONTAINER_PACKAGE;
import static ch.bbv.mda.cartridges.ContainerConstants.CONTAINER_PACKAGE_POSTFIX_KEY;
import static ch.bbv.mda.cartridges.ContainerConstants.DECLARATION_BLOCK;
import static ch.bbv.mda.cartridges.ContainerConstants.DEFINITION_BLOCK;
import static ch.bbv.mda.cartridges.ContainerConstants.IMPORT_BLOCK;
import static ch.bbv.mda.cartridges.ContainerConstants.LANGUAGE_KEY;
import static ch.bbv.mda.cartridges.ContainerConstants.PRIVATE_DEFINITION_BLOCK;
import static ch.bbv.mda.cartridges.ContainerConstants.USER_CODE_BEGIN;
import static ch.bbv.mda.cartridges.ContainerConstants.USER_CODE_END;
import static ch.bbv.mda.cartridges.ContainerConstants.USER_CONSTRUCTORS_BEGIN;
import static ch.bbv.mda.cartridges.ContainerConstants.USER_CONSTRUCTORS_END;
import static ch.bbv.mda.cartridges.ContainerConstants.USER_DECLARATION_BEGIN;
import static ch.bbv.mda.cartridges.ContainerConstants.USER_DECLARATION_END;
import static ch.bbv.mda.cartridges.ContainerConstants.USER_IMPORT_BEGIN;
import static ch.bbv.mda.cartridges.ContainerConstants.USER_IMPORT_END;
import static ch.bbv.mda.cartridges.ContainerConstants.USER_PRIVATE_CODE_BEGIN;
import static ch.bbv.mda.cartridges.ContainerConstants.USER_PRIVATE_CODE_END;
import static ch.bbv.mda.cartridges.ContainerConstants.VIEW_KEY;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.jgoodies.validation.ValidationResult;

import ch.bbv.application.VersionId;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaView;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.operators.FileOperators;
import ch.bbv.mda.operators.ModelOperators;

/**
 * @author 
 * @version
 */
public class ContainerCartridge extends JavaCartridge implements Cartridge {
  /**
   * The prefix of the cartridge.
   */
  public static final String PREFIX = "container";

  /**
   * The name of the generated programming language.
   */
  public static final String LANGUAGE = "java";

  /**
   * Log instance for all instatnces of the class.
   */
  private static Log log = LogFactory.getLog(ContainerCartridge.class);

  /**
   * The actual package being processed in the cartridge.
   */
  private MetaPackage packages;

  /**
   * Root directory of the generated source files.
   */
  private File root;

  /**
   * Constructor of the class. All data object related code generator templates
   * are loaded.
   */
  public ContainerCartridge() {
    super("Java Container Cartridge", PREFIX, new VersionId(0, 3, 0));
  }

  /**
   * Computes the container cartridge relevant attributes for the meta
   * package. The following attributes are defined.
   * <ul>
   * <dt>CONTAINER_PACKAGE</dt>
   * <dd>Name of the package containing the implementation of the views.</dd>
   * </ul>
   * @param packages the package, which attributes should be computed.
   * @param messages to be shown within the validation pane of the user interface or elsewhere.
   * @pre packages != null
   */
  @Override
  public void preProcessPackage(MetaPackage packages, ValidationResult messages) {
    assert (packages != null);
    this.packages = packages;
    String name = packages.getQualifiedName();
    packages.addAttribute(CONTAINER_PACKAGE, expandIdentifier(null, name,
        CONTAINER_PACKAGE_POSTFIX_KEY));
    for (MetaView view : packages.getViews()) {
    	preProcessView(view, messages);
    }
  }

  /**
   * Computes the container relevant attributes for the meta view. The
   * following attributes are defined.
   * <ul>
   * <dt>CONTAINER_CLASS</dt>
   * <dd>Name of the class representing the view implementation.</dd>
   * </ul>
   * @param view the view, which attributes should be computed
   * @param messages to be shown within the validation pane of the user interface or elsewhere.
   * @pre view != null
   */
  private void preProcessView(MetaView view, ValidationResult messages) {
    assert view != null;
    String name = view.getName();
    view.addAttribute(CONTAINER_CLASS, expandIdentifier(CONTAINER_CLASS_PREFIX_KEY, name,
        CONTAINER_CLASS_POSTFIX_KEY));
  }

  @Override
  public void processPackage(MetaPackage metaPackage, ValidationResult messages) {
    for (MetaView view : metaPackage.getViews()) {
    	processView(metaPackage, view, messages);
    }
  }
  
  private void processView(MetaPackage metaPackage, MetaView view, ValidationResult messages) {
    assert view != null;
    VelocityContext context = createContext(view);
    try {
      processJavaTemplate(root, metaPackage.getAttribute(CONTAINER_PACKAGE), view
          .getAttribute(CONTAINER_CLASS), retrieveTemplate("container"), context, messages);
    } catch (IOException e) {
      log.error(e);
    }
  }

  /**
   * Extracts all blocks of declarations defined by the developer. These blocks
   * are stored in the class. The file containing the source code is inferred
   * from the global properties.
   * @param packageName name of the package containing the Java class.
   * @param classname name of the Java class.
   * @return the map of retrieved user code blocks indexed with the block name.
   * @throws IOException if error was encountered when reading the file.
   */
  private Map<String, List<String>> extractUserCode(String packageName,
      String classname) throws IOException {
    Map<String, List<String>> blocks = new HashMap<String, List<String>>();
    File source = FileOperators.createFile(root, packageName, classname
        + JAVA_EXTENSION);
    blocks.put(IMPORT_BLOCK, FileOperators.extractSourceCode(source,
        USER_IMPORT_BEGIN, USER_IMPORT_END));
    blocks.put(DECLARATION_BLOCK, FileOperators.extractSourceCode(source,
        USER_DECLARATION_BEGIN, USER_DECLARATION_END));
    blocks.put(CONSTRUCTOR_BLOCK, FileOperators.extractSourceCode(source,
        USER_CONSTRUCTORS_BEGIN, USER_CONSTRUCTORS_END));
    blocks.put(DEFINITION_BLOCK, FileOperators.extractSourceCode(source,
        USER_CODE_BEGIN, USER_CODE_END));
    blocks.put(PRIVATE_DEFINITION_BLOCK, FileOperators.extractSourceCode(
        source, USER_PRIVATE_CODE_BEGIN, USER_PRIVATE_CODE_END));
    return blocks;
  }

  /**
   * Retrieves the velocity template with the given name.
   * @param name name of the template to retrieve.
   * @return the template if found otherwise null.
   */
  private Template retrieveTemplate(String name) {
    return super.retrieveTemplate(name, getContext().getProperty(LANGUAGE_KEY));
  }

  /**
   * Generates the context for a view, all its properties, imports, and own
   * context. All properties are eligible.
   * @param clazz class to process.
   * @return velocity velocity context for the meta class.
   */
  protected VelocityContext createContext(MetaView view) {
    VelocityContext context = new VelocityContext();
    context.put(CARTRIDGE_KEY, this);
    context.put(CONFIGURATION_KEY, getContext());
    context.put(MODEL_KEY, view.getModel());
    context.put(PACKAGE_KEY, view.getContext());
    context.put(VIEW_KEY, view);
    return context;
  }

}