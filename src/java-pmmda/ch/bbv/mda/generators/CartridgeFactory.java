/*
 * copyright 2000-2004 Marcel Baumann Licensed under the Apache License, Version
 * 2.0 (the "Licence); you may not use this file except in compliance with the
 * License. $Id: CartridgeFactory.java,v 1.2 2004/08/12 08:51:09 adrianbachofen
 * Exp $
 */
package ch.bbv.mda.generators;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import ch.bbv.mda.*;

/**
 * The cartridge factory handles all registered cartridges and provides support
 * methods to execute the cartridge on a subgraph of a meta model. All
 * processing of models should be performed through the services of the factory.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CartridgeFactory {
  /**
   * Logger for all instance of the class.
   */
  private static Log log = LogFactory.getLog(CartridgeFactory.class);

  /**
   * Singleton instance of the class.
   */
  private static CartridgeFactory factory;

  /**
   * Map of all registered cartridges. The key is the abbreviation of the
   * cartridge.
   */
  private Map cartridges;

  private List cartridgeDescriptors;

  /**
   * Initialization properties.
   */
  private Properties properties;

  private NumberFormat format;

  /**
   * Constructor of the class.
   * @param properties properties used to configure the factory.
   */
  protected CartridgeFactory(Properties properties) {
    cartridges = new HashMap();
    cartridgeDescriptors = new ArrayList();
    this.properties = new Properties(properties);
    format = NumberFormat.getInstance();
    format.setParseIntegerOnly(true);
    format.setMinimumIntegerDigits(2);
    format.setMaximumIntegerDigits(2);
    properties.setProperty("file.resource.cache", properties.getProperty(
        "file.resource.cache", "false"));
    properties.setProperty("file.resource.loader.path", properties
        .getProperty("file.resource.loader.path"));
    properties.setProperty("parser.pool.size", properties.getProperty(
        "parser.pool.size", "10"));
    try {
      Velocity.init(properties);
    } catch (ParseErrorException pee) {
      pee.printStackTrace();
    } catch (ResourceNotFoundException rnfe) {
      rnfe.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * All defined cartridges are read from the properties and created. The name
   * of the class is trimmed to avoid space problems with the property file.
   */
  protected void initialize() {
    int i = 1;
    String propertyName = "pm-mda.cartridges." + format.format(i);
    String propertyValue = properties.getProperty(propertyName);
    while (propertyValue != null) {
      propertyValue = propertyValue.trim();

      CartridgeDescriptor descriptor = new CartridgeDescriptor();
      cartridgeDescriptors.add(descriptor);
      descriptor.setClassname(propertyValue);
      descriptor.setActive(Boolean.FALSE);
      descriptor.setLoaded(Boolean.FALSE);

      Class clazz = null;
      try {
        clazz = Class.forName(propertyValue);
        descriptor.setCartridgeClass(clazz);
      } catch (ClassNotFoundException e) {
        log.error(e);
      }
      if (clazz != null) {
        try {
          Cartridge cartridge = (Cartridge) clazz.newInstance();
          descriptor.setLoaded(Boolean.TRUE);
          descriptor.setActive(Boolean.TRUE);
          descriptor.setCartridge(cartridge);
          register(cartridge);
        } catch (InstantiationException ie) {
          log.error(ie);
        } catch (IllegalAccessException iae) {
          log.error(iae);
        }
      }
      i++;
      propertyName = "pm-mda.cartridges." + format.format(i);
      propertyValue = properties.getProperty(propertyName);
    }
  }

  /**
   * Creates the factory singleton instance.
   * @param properties properties containing user specific values.
   */
  public static void create(Properties properties) {
    assert (factory == null);
    synchronized (CartridgeFactory.class) {
      if (factory == null) {
        factory = new CartridgeFactory(properties);
      }
    }
    factory.initialize();
  }

  /**
   * Returns the singleton instance of the class.
   * @return singleton instance of the class.
   */
  public static CartridgeFactory getInstance() {
    return factory;
  }

  /**
   * Returns the list of loaded and instantiated cartridges.
   * @return list of instantiated cartridges.
   */
  public Collection getCartridges() {
    return cartridges.values();
  }

  /**
   * Returns the list of all defined cartridge descriptors.
   * @return list of catridge descriptors.
   */
  public Collection getCartridgeDescriptors() {
    return cartridgeDescriptors;
  }

  /**
   * Returns the cartridge with the given abbreviation.
   * @param abbreviation abbreviation of the requested cartridge.
   * @return the requested cartridge if found otherwise null.
   * @pre abbreviation != null
   */
  public Cartridge getCartridge(String abbreviation) {
    assert (abbreviation != null);
    return (Cartridge) cartridges.get(abbreviation);
  }

  /**
   * Registers the cartridge in the factory. The required tags of the cartridge
   * are communicated to the meta factory for MDA elements. All newly created or
   * read elements will contains all mandatory tagged values either with the
   * default value or the one defined by the user. Obsolete tags are removed.
   * @param cartridge cartridge to register.
   */
  public void register(Cartridge cartridge) {
    assert (cartridge != null);
    cartridge.initialize(properties);
    cartridges.put(cartridge.getPrefix(), cartridge);
    registerTaggedValueTypes(cartridge, MetaModel.class);
    registerTaggedValueTypes(cartridge, MetaPackage.class);
    registerTaggedValueTypes(cartridge, MetaClass.class);
    registerTaggedValueTypes(cartridge, MetaProperty.class);
    registerTaggedValueTypes(cartridge, MetaIndexedProperty.class);
    registerObsoleteTaggedValueTypes(cartridge, MetaModel.class);
    registerObsoleteTaggedValueTypes(cartridge, MetaPackage.class);
    registerObsoleteTaggedValueTypes(cartridge, MetaClass.class);
    registerObsoleteTaggedValueTypes(cartridge, MetaProperty.class);
    registerObsoleteTaggedValueTypes(cartridge, MetaIndexedProperty.class);
  }

  /**
   * Executes the cartridge on the given model.
   * @param cartridge cartridge to execute on the model.
   * @param model model to process.
   */
  public void execute(Cartridge cartridge, MetaModel model) {
    assert ((cartridge != null) && (model != null));
    preprocess(cartridge, model);
    process(cartridge, model);
  }

  /**
   * Executes the cartridge on the given model.
   * @param cartridge cartridge to execute on the model.
   * @param metaPackage package to process.
   */
  public void execute(Cartridge cartridge, MetaPackage metaPackage) {
    assert ((cartridge != null) && (metaPackage != null));
    if (metaPackage.isSystem()) {
      return;
    }
    preprocess(cartridge, metaPackage);
    process(cartridge, metaPackage);
  }

  /**
   * Executes the cartridge on the given class.
   * @param cartridge cartridge to execute.
   * @param clazz class to process.
   */
  public void execute(Cartridge cartridge, MetaClass clazz) {
    assert ((cartridge != null) && (clazz != null));
    if (clazz.isSystem()) {
      return;
    }
    preprocess(cartridge, clazz);
    process(cartridge, clazz);
  }

  /**
   * Processes the cartridge on the given model.
   * @param cartridge cartridge to execute on the model.
   * @param model model to process.
   */
  protected void process(Cartridge cartridge, MetaModel model) {
    logProgressInfo(cartridge, model, "{0} Catridge processing model {1}");
    cartridge.processModel(model);

    if (!((MetaPackage) model).getClasses().isEmpty()) {
      process(cartridge, (MetaPackage) model);
    }

    for (Iterator i = model.getPackages().iterator(); i.hasNext();) {
      MetaPackage metaPackage = (MetaPackage) i.next();
      process(cartridge, metaPackage);
    }
    logProgressInfo(cartridge, model, "{0} Catridge post-processing model {1}");
    cartridge.postProcessModel(model);
  }

  /**
   * Processes the cartridge on the given model.
   * @param cartridge cartridge to execute on the model.
   * @param metaPackage package to process.
   */
  protected void process(Cartridge cartridge, MetaPackage metaPackage) {
    assert ((cartridge != null) && (metaPackage != null));
    if (metaPackage.isSystem()) {
      return;
    }
    logProgressInfo(cartridge, metaPackage,
        "{0} Catridge processing package {1}");
    cartridge.processPackage(metaPackage);

    for (Iterator i = metaPackage.getClasses().iterator(); i.hasNext();) {
      process(cartridge, (MetaClass) i.next());
    }
    logProgressInfo(cartridge, metaPackage,
        "{0} Catridge post-processing package {1}");
    cartridge.postProcessPackage(metaPackage);
  }

  /**
   * Processes the cartridge on the given model.
   * @param cartridge cartridge to execute on the model.
   * @param clazz item to process.
   */
  protected void process(Cartridge cartridge, MetaClass clazz) {
    assert ((cartridge != null) && (clazz != null));
    if (clazz.isSystem()) {
      return;
    }
    logProgressInfo(cartridge, clazz, "{0} Catridge processing class {1}");
    cartridge.processClass(clazz);
  }

  /**
   * Preprocesses the cartridge on the given element.
   * @param cartridge cartridge being preprocessed.
   * @param model element being preprocessed.
   */
  protected void preprocess(Cartridge cartridge, MetaModel model) {
    for (Iterator i = cartridge.getUsedCartridges().iterator(); i.hasNext();) {
      Cartridge supplier = (Cartridge) i.next();
      supplier.preProcessModel(model);
    }
    logProgressInfo(cartridge, model, "{0} Catridge pre-processing model {1}");
    cartridge.preProcessModel(model);

    if (!((MetaPackage) model).getClasses().isEmpty()) {
      preprocess(cartridge, (MetaPackage) model);
    }

    for (Iterator i = model.getPackages().iterator(); i.hasNext();) {
      preprocess(cartridge, (MetaPackage) i.next());
    }
  }

  /**
   * Preprocesses the cartridge on the given element.
   * @param cartridge cartridge being preprocessed.
   * @param metaPackage element being preprocessed.
   */
  protected void preprocess(Cartridge cartridge, MetaPackage metaPackage) {
    if (metaPackage.isSystem()) {
      return;
    }

    for (Iterator i = cartridge.getUsedCartridges().iterator(); i.hasNext();) {
      Cartridge supplier = (Cartridge) i.next();
      supplier.preProcessPackage(metaPackage);
    }
    logProgressInfo(cartridge, metaPackage,
        "{0} Catridge pre-processing package {1}");
    cartridge.preProcessPackage(metaPackage);

    // List dependencies = resolveDependencies(mPackage.getClasses());
    for (Iterator i = metaPackage.getClasses().iterator(); i.hasNext();) {
      preprocess(cartridge, (MetaClass) i.next());
    }
  }

  /**
   * Preprocesses the cartridge on the given element.
   * @param cartridge cartridge being preprocessed.
   * @param clazz element being preprocessed.
   */
  protected void preprocess(Cartridge cartridge, MetaClass clazz) {
    if (clazz.isSystem()) {
      return;
    }

    for (Iterator i = cartridge.getUsedCartridges().iterator(); i.hasNext();) {
      Cartridge supplier = (Cartridge) i.next();
      supplier.preProcessClass(clazz);
    }
    logProgressInfo(cartridge, clazz, "{0} Catridge pre-processing class {1}");
    cartridge.preProcessClass(clazz);
  }

  /**
   * Registers the mandatory tags for the given meta element class needed in the
   * cartrige.
   * @param cartridge cartridge to register.
   * @param clazz meta element class to complete with tags.
   */
  protected void registerTaggedValueTypes(Cartridge cartridge, Class clazz) {
    List taggedValueTypes = cartridge.getTaggedValueTypes(clazz);
    if (taggedValueTypes != null) {
      MetaFactory.getInstance().registerTypes(clazz, taggedValueTypes);
    }
  }

  /**
   * Registers the obsolete tags for the given meta element class needed in the
   * cartrige.
   * @param cartridge cartridge to register.
   * @param clazz meta element class to complete with tags.
   */
  protected void registerObsoleteTaggedValueTypes(Cartridge cartridge,
      Class clazz) {
    List types = cartridge.getObsoleteTaggedValueTypes(clazz);
    if ((types != null) && (types.size() > 0)) {
      MetaFactory.getInstance().addObsoleteTypes(clazz, types);
    }
  }

  /**
   * Logs the progess information during processing.
   * @param cartridge cartridge being applied.
   * @param element element being processed.
   * @param message message to log.
   */
  private void logProgressInfo(Cartridge cartridge, MetaElement element,
      String message) {
    if (log.isInfoEnabled()) {
      Object[] arguments = { cartridge.getPrefix(), element.qualifiedName() };
      log.info(MessageFormat.format(message, arguments));
    }
  }
}