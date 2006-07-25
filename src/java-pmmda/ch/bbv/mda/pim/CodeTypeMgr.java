/* This code was generated automatically with the pmMDA generator. Please do 
 NOT add code outside the marked zones. All changes outside the marked areas 
 will be discarded during the next execution of the MDA generator. 
 $Id$
 */

package ch.bbv.mda.pim;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.bbv.dog.functors.IsModifiedFlagFunctor;
import ch.bbv.dog.functors.ModifiedFlagFunctor;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.operators.FileOperators;

import com.thoughtworks.xstream.XStream;

/**
 * The code type manager handles all code types and their values defined in the
 * model. Due to the limitations of the UML meta model, reference code values
 * are stored outside the XMI representation of the model.
 * @version $Revision$
 * @author Marcel Baumann
 */
public class CodeTypeMgr {

  /* %% USER DEFINED DECLARATION START %% */
  /**
   * XStream library to serialize/deserialize the code values to xml files.
   */
  private XStream xstream;

  /* %% USER DEFINED DECLARATION END %% */

  /**
   * The root directory where the code values of a code type are stored. The
   * package structure is mapped to a directory hierarchy.
   */
  private File root;

  /**
   * All the code types handled in the manager.
   */
  private Map<MetaClass, CodeType> types;

  /* %% USER DEFINED CONSTRUCTORS START %% */
  /**
   * Default constructor of the class necessary for Java bean convention. The
   * object identifier is set to null to indicate it is a new object.
   */
  public CodeTypeMgr() {
    initialize();
    xstream = new XStream();
    xstream.alias("codeType", CodeType.class);
    xstream.alias("code", CodeValue.class);
    xstream.alias("storage-kind", StorageKind.class);
  }

  /* %% USER DEFINED CONSTRUCTORS END %% */

  /* %% USER DEFINED CODE START %% */

  /**
   * Reset the type manager with a new root. All stored code values are
   * discarded.
   * @param root the new root directory where the code values should be stored.
   */
  public void reset(File root) {
    setRoot(root);
    types.clear();
  }

  /**
   * Retrieves the code values of the given reference code.
   * @param clazz class wich reference code values should be retrieved
   * @return the requested code type if found otherwise an empty code type is
   *         returned
   * @throws IOException if the file could not be written
   * @pre clazz != null
   */
  public CodeType retrieve(MetaClass clazz) throws IOException {
    assert clazz != null;
    if (!types.containsKey(clazz)) {
      CodeType type = null;
      type = load(clazz);
      if (type == null) {
        type = new CodeType();
      }
      synchronize(clazz, type);
      types.put(clazz, type);
    }
    return types.get(clazz);
  }

  /**
   * Saves the reference code values associated with the class.
   * @param clazz class which reference codes should be saved
   * @throws IOException if the file could not be written
   * @pre clazz != null
   */
  public void save(MetaClass clazz) throws IOException {
    assert clazz != null;
    CodeType type = types.get(clazz);
    synchronize(clazz, type);
    ModifiedFlagFunctor functor = new ModifiedFlagFunctor();
    VisitorFunctor visitor = new VisitorFunctor(functor);
    functor.setModified(false);
    type.accept(visitor);
    File file = FileOperators.createFile(root, clazz.getContext()
        .getQualifiedName(), clazz.getName() + ".xml");
    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    xstream.toXML(type, writer);
    writer.close();
  }

  /**
   * Saves all modified code types and thier code values.
   * @throws IOException if the file could not be written
   */
  public void saveAll() throws IOException {
    IsModifiedFlagFunctor functor = new IsModifiedFlagFunctor();
    VisitorFunctor visitor = new VisitorFunctor(functor);
    for (MetaClass clazz : types.keySet()) {
      functor.reset();
      CodeType type = types.get(clazz);
      type.accept(visitor);
      if (functor.isModified()) {
        save(clazz);
      }
    }
  }

  /* %% USER DEFINED CODE END %% */

  /**
   * Returns the current value of the simple property root.
   * @return the value of the simple property root
   * @see #setRoot(File)
   */
  public File getRoot() {
    return root;
  }

  /**
   * Sets the new value of the simple property root.
   * @param root new value of the property
   * @see #getRoot()
   */
  public void setRoot(File root) {
    if (((this.root != null) && !this.root.equals(root))
        || ((this.root == null) && (root != null))) {
      this.root = root;
    }
  }

  /**
   * Gets all elements of the ordered relation types.
   * @return non mutable list of all children
   * @see java.util.Map#values()
   */
  public Collection<CodeType> getTypes() {
    return types.values();
  }

  /**
   * Returns the element with the specified key in the ordered indexed property
   * types.
   * @param key key of element to return
   * @return the element at the specified position in this list
   * @see #setType(MetaClass, CodeType)
   * @see java.util.Map#get(Object)
   */
  public CodeType getType(MetaClass key) {
    return types.get(key);
  }

  /**
   * Inserts the given child with the given key in the ordered indexed property
   * types.
   * @param key key at which the specified element is to be inserted
   * @param value value to be inserted
   * @see #getType(MetaClass)
   */
  public void setType(MetaClass key, CodeType value) {
    types.put(key, value);
  }

  /**
   * Removes the element with the specified key.
   * @param key key whose mappingis to be removed from the map
   * @return true if this list contained the specified element
   * @see java.util.Map#remove(Object)
   */
  public CodeType removeType(MetaClass key) {
    return types.remove(key);
  }

  /**
   * Initializes the attributes generated through the MDA tool.
   */
  private void initialize() {
    types = new HashMap<MetaClass, CodeType>();
  }

  /* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
  /**
   * Loads the reference code values associated with the class.
   * @param clazz claxx wich reference coes should be loaded
   * @return the reference code type and values of the class. If not found null
   *         is returned
   * @throws IOException if the file could not be read
   */
  private CodeType load(MetaClass clazz) throws IOException {
    CodeType type = null;
    File file = FileOperators.createFile(root, clazz.getContext()
        .getQualifiedName(), clazz.getName() + ".xml");
    if (file.canRead()) {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      type = (CodeType) xstream.fromXML(reader);
    }
    return type;
  }

  private void synchronize(MetaClass clazz, CodeType type) {
    type.setQualifiedName(clazz.getQualifiedName());
  }
  /* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}
