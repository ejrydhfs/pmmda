/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/

package ch.bbv.pim;

import ch.bbv.dog.DataObject;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

/**
 * A entry into the journal of entry associated with an entity.
 * @author Marcel Baumann
 */
public class JournalEntry implements DataObject {

/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * The author of the journal entry.
   */
  private String author;

  /**
   * The creation date of the journal entry.
   */
  private Date creation;

  /**
   * The text of the journal entry.
   */
  private String text;

  /**
   * The duration of the activity documented in the entry in seconds.
   */
  private int duration;

  /**
   * The type of the journal entry.
   */
  private JournalEntryType type;

  /** 
   * Unique identifier of the data object instance distinguishing it from all
   * other instances of the same data object type.
   */
  private Integer id;
   
  /** 
   * Flag indicated if the data object is dirty or not.
   */
  private boolean modified;
  
  /** 
   * Timestamp used to implement optimistic locking. 
   */
  private Timestamp timestamp;   

  /** 
   * Default constructor of the class necessary for Java bean convention. The 
   * object identifier is set to null to indicate it is a new object.
   */
  public JournalEntry() {
  }

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */

  /** 
   * Returns the unique identifier of the data object instance. A new object has
   * the value null.
   * @return the identifier of the instance.
   * @see #setId(Integer)
   */
  public final Integer getId() {
    return id;
  }
  
  /** 
   * Sets the identifier of the data object instance.
   * @param id new identifier of the instance
   * @see #getId()
   */
  public final void setId(Integer id) {
    this.id = id;
  }

  /** 
   * Returns the flag indicating if the data object instance was modified.
   * @return true if the object was modified otherwise false.
   * @see #setModified
   */
  public final boolean isModified() {
    return modified;
  }
  
  /** 
   * Sets the flag indicating if the data object instance was modified.
   * @param modified new value of the flag
   * @see #isModified
   */
  public final void setModified(boolean modified) {
    this.modified = modified;
  }
  
  /** 
   * Convenience method indicating if the instance is a new one or not.
   * @return true if the object is new otherwise false.
   * @see #getId()
   */
  public final boolean isNew()  {
    return (getId() == null);
  }
  
  /** 
   * Returns the timestamp of the data object instance.
   * @return the timestamp of the last persistent modification.
   * @see #setTimestamp(Timestamp)
   */
  public final Timestamp getTimestamp() {
    return timestamp;
  }
  
  /** 
   * Sets the timestamp of the data object instance.
   * @param timestamp timestamp of the instance
   * @see #getTimestamp()
   */
   public final void setTimestamp(Timestamp timestamp) {
     this.timestamp = timestamp;
   }

  /** 
   * Returns the current value of the simple property author.
   * @return the value of the simple property author.
   * @see #setAuthor(String)
   */
  public String getAuthor() {
    return author;
  }

  /** 
   * Sets the new value of the simple property author.
   * @param author new value of the property
   * @see #getAuthor()
   */    
  public void setAuthor(String author) {
    if (((this.author != null) && !this.author.equals(author)) || 
        ((this.author == null) && (author != null)))  {    
      this.author = author;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property creation.
   * @return the value of the simple property creation.
   * @see #setCreation(Date)
   */
  public Date getCreation() {
    return creation;
  }

  /** 
   * Sets the new value of the simple property creation.
   * @param creation new value of the property
   * @see #getCreation()
   */    
  public void setCreation(Date creation) {
    if (((this.creation != null) && !this.creation.equals(creation)) || 
        ((this.creation == null) && (creation != null)))  {    
      this.creation = creation;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property text.
   * @return the value of the simple property text.
   * @see #setText(String)
   */
  public String getText() {
    return text;
  }

  /** 
   * Sets the new value of the simple property text.
   * @param text new value of the property
   * @see #getText()
   */    
  public void setText(String text) {
    if (((this.text != null) && !this.text.equals(text)) || 
        ((this.text == null) && (text != null)))  {    
      this.text = text;
      setModified(true);
    }
  }    

  /** 
   * Returns the current value of the simple property duration.
   * @return the value of the simple property duration.
   * @see #setDuration(int)
   */
  public int getDuration() {
    return duration;
  }

  /** 
   * Sets the new value of the simple property duration.
   * @param duration new value of the property
   * @see #getDuration()
   */    
  public void setDuration(int duration) {
    if (this.duration != duration) {  
      this.duration = duration;
      setModified(true);
    }
  }

  /** 
   * Returns the current value of the simple property type.
   * @return the value of the simple property type.
   * @see #setType(JournalEntryType)
   */
  public JournalEntryType getType() {
    return type;
  }

  /** 
   * Sets the new value of the simple property type.
   * @param type new value of the property
   * @see #getType()
   */    
  public void setType(JournalEntryType type) {
    if (((this.type != null) && !this.type.equals(type)) || 
        ((this.type == null) && (type != null)))  {    
      this.type = type;
      setModified(true);
    }
  }    

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all its relevant children. A relevant
   * child is always another data object.
   * @param visitor visitor to act on the data object
   * @pre visitor != null
   */  
  public void accept(Visitor visitor) {
    assert(visitor != null);
    visitor.setDepth(visitor.getDepth() + 1);
    visitor.visit(this);
    traverse(visitor);
    visitor.setDepth(visitor.getDepth() - 1);
  }
  
  /** 
   * Creates and returns a deep copy of the object and its descendants. The 
   * properties containing keys are not cloned because they are immutable.
   * @return a deep copy of the instance.
   */
  public JournalEntry copy() {
    JournalEntry clone = null;
    try {
      clone = (JournalEntry)clone();
    } catch (CloneNotSupportedException e) {
      // impossible exception because data object is cloneable.
      return null;
    }
    copy(clone);
    clone.setModified(isModified());
    return clone;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void addPropertyChangeListener(String propertyName, 
      PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removePropertyChangeListener(String propertyName, 
      PropertyChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void addVetoableChangeListener(VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void addVetoableChangeListener(String propertyName,
      VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removeVetoableChangeListener(VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }
  
  public void removeVetoableChangeListener(String propertyName,
      VetoableChangeListener listener) {
    throw new UnsupportedOperationException();
  }

  /**
   * Convenience method to set the value of a foreign key.
   * @param bean object which property should be set
   * @param property property to be set
   * @param key key identifier to set
   * @pre (bean != null) && (property != null) && (key != null)
   */
   protected final void setForeignKey(DataObject bean, String property,
      Integer key) {
    assert((bean != null) && (property != null));
    try {
      Field field = bean.getClass().getDeclaredField(property);
      if (field != null) {
        field.setAccessible(true);
        field.set(bean, key);
      }
    } catch (NoSuchFieldException e) {
      assert(false);
    } catch (IllegalArgumentException e) {
      assert(false);
    } catch (IllegalAccessException e) {
      assert(false);
    }
  }

  /** 
   * Copies deeply all the attributes of the class and its ancestors.
   * @param clone the copy which deep copy should be generated
   */
  protected void copy(JournalEntry clone) {
  }

  /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all relevant children of the ancestors
   * and its children. A relevant child is always another data object.
   * @param visitor visitor to act on the data object
   */  
  protected void traverse(Visitor visitor) {
  }  

/* %% USER DEFINED PROTECTED/PRIVATE CODE START %% */
/* %% USER DEFINED PROTECTED/PRIVATE CODE END %% */
}
