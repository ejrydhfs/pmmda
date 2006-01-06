/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
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

import ch.bbv.application.Component;
import ch.bbv.application.ComponentException;
import ch.bbv.application.ComponentImpl;
import ch.bbv.application.Locator;
import ch.bbv.dog.validation.Validator;
import ch.bbv.mda.*;

/**
 * The cartridge factory handles all registered cartridges and provides support
 * methods to execute the cartridge on a subgraph of a meta model. All
 * processing of models should be performed through the services of the factory.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CartridgeFactory extends ComponentImpl implements Component {
  /**
   * Name of the component to find it through the locator service.
   */
  public static final String NAME = "Cartridge Factory";

  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(CartridgeFactory.class);

  /**
   * Map of all registered cartridges. The key is the abbreviation of the
   * cartridge.
   */
  private Map<String, Cartridge> cartridges;

  /**
   * List of all cartridge descriptors defined in the factory.
   */
  private List<CartridgeDescriptor> cartridgeDescriptors;

  /**
   * List of all registered listeners.
   */
  private List<CartridgeListener> listeners;

  /**
   * The item factory of the cartridge factory.
   */
  private ItemFactory itemFactory;

  /**
   * Initialization properties.
   */
  private Properties properties;

  /**
   * Format instance used to read the cartridge specifications in the properties
   * file.
   */
  private NumberFormat format;

  /**
   * Returns the singleton instance of the class. The services of the
   * application framework are used to provide access to the single instance of
   * the class in the application.
   * @return singleton instance of the class.
   */
  public static CartridgeFactory getInstance() {
    return (CartridgeFactory) Locator.getInstance().getComponent(NAME);
  }

  /**
   * Constructor of the class.
   * @param name name of the instance as accessible through the locator.
   * @param properties properties used to configure the factory.
   */
  public CartridgeFactory(String name, Properties properties) {
    super(name);
    cartridges = new HashMap<String, Cartridge>();
    cartridgeDescriptors = new ArrayList<CartridgeDescriptor>();
    listeners = new ArrayList<CartridgeListener>();
    this.properties = properties;
    itemFactory = new ItemFactory();
    format = NumberFormat.getInstance();
    format.setParseIntegerOnly(true);
    format.setMinimumIntegerDigits(2);
    format.setMaximumIntegerDigits(2);
    try {
      Velocity.init(properties);
    } catch (ParseErrorException e) {
      log.error(e);
    } catch (ResourceNotFoundException e) {
      log.error(e);
    } catch (Exception e) {
      log.error(e);
    }
  }

  /**
   * All defined cartridges are read from the properties and created. The name
   * of the class is trimmed to avoid space problems with the property file.
   * @throws ComponentException if an initialization problem occurs.
   */
  public void initialize() throws ComponentException {
    super.initialize();
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
        } catch (InstantiationException e) {
          log.error(e);
        } catch (IllegalAccessException e) {
          log.error(e);
        }
      }
      i++;
      propertyName = "pm-mda.cartridges." + format.format(i);
      propertyValue = properties.getProperty(propertyName);
    }
  }

  /**
   * Returns the item factory of the cartridge factory.
   * @return the item factory of the cartridge factory.
   */
  public ItemFactory getItemFactory() {
    return itemFactory;
  }

  /**
   * Returns the list of loaded and instantiated cartridges.
   * @return list of instantiated cartridges.
   */
  public Collection<Cartridge> getCartridges() {
    return cartridges.values();
  }

  /**
   * Returns the list of all defined cartridge descriptors.
   * @return list of catridge descriptors.
   */
  public Collection<CartridgeDescriptor> getCartridgeDescriptors() {
    return cartridgeDescriptors;
  }

  /**
   * Returns the cartridge with the given abbreviation.
   * @param abbreviation abbreviation of the requested cartridge.
   * @return the requested cartridge if found otherwise null.
   */
  public Cartridge getCartridge(String abbreviation) {
    return cartridges.get(abbreviation);
  }

  /**
   * Registers the cartridge in the factory. The required tags of the cartridge
   * are communicated to the meta factory for MDA elements. All newly created or
   * read elements will contains all mandatory tagged values either with the
   * default value or the one defined by the user. Obsolete tags are removed.
   * @param cartridge cartridge to register.
   */
  public void register(Cartridge cartridge) {
    assert cartridge != null;
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
    Validator validator = (Validator) Locator.getInstance().getComponent(
        Validator.NAME);
    cartridge.registerRules(validator);
  }

  /**
   * Registers a listener in the factory.
   * @param listener listener to register.
   * @pre listener != null
   * @see #unregister(CartridgeListener)
   */
  public void register(CartridgeListener listener) {
    assert listener != null;
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  /**
   * Unregisters a listener from the factory.
   * @param listener listener to unregister.
   * @pre listener != null
   * @see #register(CartridgeListener)
   */
  public void unregister(CartridgeListener listener) {
    assert listener != null;
    listeners.remove(listener);
  }

  /**
   * Returns the list of cartridges and their dependancies sorted using the
   * dependency depth. A cartridge is dependant of itself.
   * @param cartridges cartridges to process.
   * @return the list of dependant cartridges.
   * @pre cartridges != null
   */
  public List<Cartridge> generateOrder(Collection<Cartridge> cartridges) {
    assert cartridges != null;
    List<Cartridge> dependants = new ArrayList<Cartridge>();
    for (Cartridge cartridge : cartridges) {
      generateOrder(cartridge, dependants);
    }
    return dependants;
  }

  /**
   * Executes the cartridge on the given model.
   * @param cartridge cartridge to execute on the model.
   * @param model model to process.
   * @pre (cartridge != null) && (model != null)
   */
  public void execute(Cartridge cartridge, MetaModel model) {
    assert (cartridge != null) && (model != null);
    preprocess(cartridge, model);
    process(cartridge, model);
  }

  /**
   * Executes the cartridge on the given model.
   * @param cartridge cartridge to execute on the model.
   * @param metaPackage package to process.
   * @pre (cartridge != null) && (metaPackage != null)
   */
  public void execute(Cartridge cartridge, MetaPackage metaPackage) {
    assert (cartridge != null) && (metaPackage != null);
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
   * @pre (cartridge != null) && (clazz != null)
   */
  public void execute(Cartridge cartridge, MetaClass clazz) {
    assert (cartridge != null) && (clazz != null);
    if (!clazz.isSystem()) {
      preprocess(cartridge, clazz);
      process(cartridge, clazz);
    }
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
    for (MetaPackage metaPackage : model.getPackages()) {
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

    for (MetaClass clazz : metaPackage.getClasses()) {
      process(cartridge, clazz);
    }
    logProgressInfo(cartridge, metaPackage,
        "{0} Catridge post-processing package {1}");
    cartridge.postProcessPackage(metaPackage);
  }

  /**
   * Processes the cartridge on the given model.
   * @param cartridge cartridge to execute on the model.
   * @param clazz item to process.
   * @pre (cartridge != null) && (clazz != null)
   */
  protected void process(Cartridge cartridge, MetaClass clazz) {
    assert (cartridge != null) && (clazz != null);
    if (!clazz.isSystem()) {
      logProgressInfo(cartridge, clazz, "{0} Catridge processing class {1}");
      cartridge.processClass(clazz);
    }
  }

  /**
   * Preprocesses the cartridge on the given element.
   * @param cartridge cartridge being preprocessed.
   * @param model element being preprocessed.
   */
  protected void preprocess(Cartridge cartridge, MetaModel model) {
    for (Cartridge supplier : cartridge.getUsedCartridges()) {
      supplier.preProcessModel(model);
    }
    logProgressInfo(cartridge, model, "{0} Catridge pre-processing model {1}");
    cartridge.preProcessModel(model);

    if (!((MetaPackage) model).getClasses().isEmpty()) {
      preprocess(cartridge, (MetaPackage) model);
    }

    for (MetaDatatype item : model.getDatatypes()) {
      preprocess(cartridge, item);
    }

    for (MetaPackage item : model.getPackages()) {
      preprocess(cartridge, item);
    }
  }

  /**
   * Preprocesses the cartridge on the given element.
   * @param cartridge catridge being processed.
   * @param datatype element being processed.
   */
  protected void preprocess(Cartridge cartridge, MetaDatatype datatype) {
    for (Cartridge supplier : cartridge.getUsedCartridges()) {
      supplier.preProcessDatatype(datatype);
    }
    logProgressInfo(cartridge, datatype,
        "{0} Catridge pre-processing datatype {1}");
    cartridge.preProcessDatatype(datatype);

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

    for (Cartridge supplier : cartridge.getUsedCartridges()) {
      supplier.preProcessPackage(metaPackage);
    }
    logProgressInfo(cartridge, metaPackage,
        "{0} Catridge pre-processing package {1}");
    cartridge.preProcessPackage(metaPackage);

    // List dependencies = resolveDependencies(mPackage.getClasses());
    for (MetaClass clazz : metaPackage.getClasses()) {
      preprocess(cartridge, clazz);
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

    for (Cartridge supplier : cartridge.getUsedCartridges()) {
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
    getItemFactory().registerTypes(clazz, cartridge.getTaggedValueTypes(clazz));
  }

  /**
   * Registers the obsolete tags for the given meta element class needed in the
   * cartrige.
   * @param cartridge cartridge to register.
   * @param clazz meta element class to complete with tags.
   */
  protected void registerObsoleteTaggedValueTypes(Cartridge cartridge,
      Class clazz) {
    getItemFactory().addObsoleteTypes(clazz,
        cartridge.getObsoleteTaggedValueTypes(clazz));
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

  /**
   * Returns the list of dependant cartridges of the given cartridge.
   * @param cartridge catridge to process.
   * @param dependants list of dependant cartridges.
   */
  private void generateOrder(Cartridge cartridge,
      Collection<Cartridge> dependants) {
    for (Cartridge item : cartridge.getUsedCartridges()) {
      if (!dependants.contains(item)) {
        generateOrder(item, dependants);
      }
    }
    if (!dependants.contains(cartridge)) {
      dependants.add(cartridge);
    }
  }
}