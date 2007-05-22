/* 
 * copyright 2005 http://www.bbv.ch
 *  
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * http://www.apache.org/licenses/LICENSE-2.0
 * you may not use this file except in compliance with the License.
 */

package ch.bbv.mda.cartridges.dotNet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import ch.bbv.application.VersionId;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.cartridges.BoConstants;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeImpl;
import ch.bbv.mda.operators.FileOperators;

import com.jgoodies.validation.ValidationResult;

/**
 * Cartridge that generates UserControls and Dialogs for data objects 
 * @author mpederse
 */
public class DogNetGUICartridge extends CartridgeImpl implements Cartridge {  
  public static final String PREFIX = "DotNetDogGUI";
  
  public static String GENERATE_EDITABLE_CONTROLS_TAG = ".net-gui-editable-controls";

  private static Log log = LogFactory.getLog(DogNetGUICartridge.class);
  private static final String ROOT_FOLDER_KEY = "dog.net.gui.cartridge.root-folder";
 
  private File sourceRootDirectory;
  
  private MetaModel metaModel;
  
  private Template templatePrimitivePropertiesControls;
  private Template templatePrimitivePropertiesControlsEditable;
  private Template templateSinglePropertiesControls;
  private Template templateSinglePropertiesControlsEditable;
  private Template templateIndexedPropertiesControls;
  private Template templateIndexedPropertiesControlsEditable;
  private Template templateDoListControls;
  private Template templateDoListControlsEditable;
  private Template templateDoDialogControls;
  private Template templateDoDialogControlsEditable;
  
  /**
   * Instances a new insatnce of the DogNetGUICartridge class.
   */
  public DogNetGUICartridge() {
    super("Dog.NET GUI Cartridge", PREFIX, new VersionId(0, 1, 0));
  }
  
  public void initialize(Properties properties) {
    super.initialize(properties);

    try {
      templatePrimitivePropertiesControls = Velocity.getTemplate("dotNet/dataobject/gui/dog-net-primitive-properties-control.vm");
      templatePrimitivePropertiesControlsEditable = Velocity.getTemplate("dotNet/dataobject/gui/dog-net-primitive-properties-control-editable.vm");
      templateSinglePropertiesControls = Velocity.getTemplate("dotNet/dataobject/gui/dog-net-single-properties-control.vm");
      templateSinglePropertiesControlsEditable = Velocity.getTemplate("dotNet/dataobject/gui/dog-net-single-properties-control-editable.vm");
      templateIndexedPropertiesControls = Velocity.getTemplate("dotNet/dataobject/gui/dog-net-indexed-properties-control.vm");
      templateIndexedPropertiesControlsEditable = Velocity.getTemplate("dotNet/dataobject/gui/dog-net-indexed-properties-control-editable.vm");
      templateDoListControls = Velocity.getTemplate("dotNet/dataobject/gui/dog-net-do-list-control.vm");
      templateDoListControlsEditable = Velocity.getTemplate("dotNet/dataobject/gui/dog-net-do-list-control-editable.vm");
      templateDoDialogControls = Velocity.getTemplate("dotNet/dataobject/gui/dog-net-do-dialog.vm");
      templateDoDialogControlsEditable = Velocity.getTemplate("dotNet/dataobject/gui/dog-net-do-dialog-editable.vm");
    }
    catch (Exception ex) {
      log.error("Could not load all velocity templates used for the GUI generation. " + ex.getMessage());
    }
  }
  
  @Override
  public void preProcessModel(MetaModel model, ValidationResult messages) {
    this.metaModel = model;
    super.preProcessModel(model, messages);
  }
  
  @Override
  public void preProcessPackage(MetaPackage metaPackage, ValidationResult messages) {
    if (DogNetCartridge.IsIgnored(metaPackage)) {
      return;
    }
    Properties context = getContext();
    StringBuffer buffer = new StringBuffer();
    buffer.append(metaPackage.getQualifiedName()).append(context.getProperty(DogNetCartridge.DATA_OBJECTS_PACKAGE_POSTFIX_KEY));
    metaPackage.addAttribute(DogNetCartridge.DATA_OBJECTS_PACKAGE_ATTRIBUTE, buffer.toString());
    
    super.preProcessPackage(metaPackage, messages);
  }
  
  @Override
  public void preProcessClass(MetaClass clazz, ValidationResult messages) {
    if (DogNetCartridge.IsIgnored(clazz)) {
      return;
    }
    
    // Adjust the class attributes with global postfix values
    Properties context = getContext();
    String name = clazz.getName();
    StringBuffer buffer = new StringBuffer();
    buffer.append(context.getProperty(DogNetCartridge.DATA_OBJECTS_CLASS_PREFIX_KEY)).append(name).append(context.getProperty(DogNetCartridge.DATA_OBJECTS_CLASS_POSTFIX_KEY));
    clazz.addAttribute(DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE, buffer.toString());
    
    super.preProcessClass(clazz, messages);
  }
  
  
  @Override
  public void processModel(MetaModel model, ValidationResult messages) {
    String s = getContext().getProperty(ROOT_FOLDER_KEY);
    sourceRootDirectory = new File(getContext().getProperty(ROOT_FOLDER_KEY));
    if (!sourceRootDirectory.exists()) {
      sourceRootDirectory.mkdirs();
    }
    super.processModel(model, messages);
  }
  
  @Override
  public void processClass(MetaClass clazz, ValidationResult messages) {
    if (DogNetCartridge.IsIgnored(clazz)) {
      return;
    }
    if (clazz.isNestedClass()) {
      log.error("Nested classes are not supported!");
      return;
    }
    if (!clazz.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
      return;
    }
    
    generateGUIControls(clazz, messages);
    
    super.processClass(clazz, messages);
  }
  
  
  @Override
  public void processPackage(MetaPackage metaPackage, ValidationResult messages) {
    if (DogNetCartridge.IsIgnored(metaPackage)) {
      return;
    }
    super.processPackage(metaPackage, messages);
  }
  
  @Override
  public void postProcessModel(MetaModel model, ValidationResult messages) {
    this.metaModel = null;
    super.postProcessModel(model, messages);
  }
  
  @Override
  public void postProcessPackage(MetaPackage metaPackage, ValidationResult messages) {
    if (DogNetCartridge.IsIgnored(metaPackage)) {
      return;
    }
    super.postProcessPackage(metaPackage, messages);
  }
  
  private void generateGUIControls(MetaClass clazz, ValidationResult messages) {
    clazz.addAttribute(GENERATE_EDITABLE_CONTROLS_TAG, clazz.valueOfTag(GENERATE_EDITABLE_CONTROLS_TAG));
    boolean generateEditableControls = clazz.booleanValueOfAttribute(GENERATE_EDITABLE_CONTROLS_TAG);
    MetaPackage metaPackage = clazz.retrievePackage();
    String pack = metaPackage.getAttribute(DogNetCartridge.DATA_OBJECTS_PACKAGE_ATTRIBUTE) + ".GUI";
    
    VelocityContext context = createContext(clazz);
    StringBuffer file = new StringBuffer();
    try {
      if (generateEditableControls) {
        file.append("Abstract");
      }
      file.append("Primitive").append(clazz.getAttribute(DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE)).append("Properties");
      processTemplate(sourceRootDirectory, pack, file.toString() + DogNetCartridge.CSHARP_EXTENSION, templatePrimitivePropertiesControls, context, messages);
      file.setLength(0);
      
      if (generateEditableControls) {
        file.append("Abstract");
      }
      file.append("Single").append(clazz.getAttribute(DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE)).append("Properties");
      processTemplate(sourceRootDirectory, pack, file.toString() + DogNetCartridge.CSHARP_EXTENSION, templateSinglePropertiesControls, context, messages);
      file.setLength(0);
      
      if (generateEditableControls) {
        file.append("Abstract");
      }
      file.append("Indexed").append(clazz.getAttribute(DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE)).append("Properties");
      processTemplate(sourceRootDirectory, pack, file.toString() + DogNetCartridge.CSHARP_EXTENSION, templateIndexedPropertiesControls, context, messages);
      file.setLength(0);
      
      if (generateEditableControls) {
        file.append("Abstract");
      }
      file.append(clazz.getAttribute(DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE)).append("List");
      processTemplate(sourceRootDirectory, pack, file.toString() + DogNetCartridge.CSHARP_EXTENSION, templateDoListControls, context, messages);
      file.setLength(0);
      
      if (generateEditableControls) {
        file.append("Abstract");
      }
      file.append(clazz.getAttribute(DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE)).append("Dialog");
      processTemplate(sourceRootDirectory, pack, file.toString() + DogNetCartridge.CSHARP_EXTENSION, templateDoDialogControls, context, messages);
      file.setLength(0);
      
      if (generateEditableControls) {
        File rootFile = new File(sourceRootDirectory, pack.replace('.', '/'));
        File f;
        
        file.append("Primitive").append(clazz.getAttribute(DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE)).append("Properties");
        f = new File(rootFile, file.toString() + DogNetCartridge.CSHARP_EXTENSION);
        if (!f.exists()) {
        	processTemplate(sourceRootDirectory, pack, file.toString() + DogNetCartridge.CSHARP_EXTENSION, templatePrimitivePropertiesControlsEditable, context, messages);
        }
        file.setLength(0);
        
        file.append("Single").append(clazz.getAttribute(DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE)).append("Properties");
        f = new File(rootFile, file.toString() + DogNetCartridge.CSHARP_EXTENSION);
        if (!f.exists()) {
        	processTemplate(sourceRootDirectory, pack, file.toString() + DogNetCartridge.CSHARP_EXTENSION, templateSinglePropertiesControlsEditable, context, messages);
        }
        file.setLength(0);
        
        file.append("Indexed").append(clazz.getAttribute(DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE)).append("Properties");
        f = new File(rootFile, file.toString() + DogNetCartridge.CSHARP_EXTENSION);
        if (!f.exists()) {
          processTemplate(sourceRootDirectory, pack, file.toString() + DogNetCartridge.CSHARP_EXTENSION, templateIndexedPropertiesControlsEditable, context, messages);
        }
        file.setLength(0);
        
        file.append(clazz.getAttribute(DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE)).append("List");
        f = new File(rootFile, file.toString() + DogNetCartridge.CSHARP_EXTENSION);
        if (!f.exists()) {
          processTemplate(sourceRootDirectory, pack, file.toString() + DogNetCartridge.CSHARP_EXTENSION, templateDoListControlsEditable, context, messages);
        }
        file.setLength(0);
        
        file.append(clazz.getAttribute(DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE)).append("Dialog");
        f = new File(rootFile, file.toString() + DogNetCartridge.CSHARP_EXTENSION);
        if (!f.exists()) {
          processTemplate(sourceRootDirectory, pack, file.toString() + DogNetCartridge.CSHARP_EXTENSION, templateDoDialogControlsEditable, context, messages);
        }
        file.setLength(0);
      }
    } catch (IOException ex) {
    	String msg = "Failed to process the velocity templates to generate the GUI's.";
      log.error(msg);
      log.error(ex.getMessage(), ex);
      messages.addError(msg);
    }
  }
  
  protected VelocityContext createContext(MetaClass clazz) {
    VelocityContext context = super.createContext(clazz);
    context.put("netHelper", new NetCartridgeHelper(this, this.metaModel));
    return context;
  }
}
