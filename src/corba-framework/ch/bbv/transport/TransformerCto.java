/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.transport;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.dog.DataObjectUpdate;
import ch.bbv.dog.DataPropertyUpdate;

/**
 * The transformer provides services to transform nullable constructs used over
 * CORBA 2.1 to the equivalent constructs in Java.
 * @author MarcelBaumann
 * @version $Revision$
 */
public final class TransformerCto {
  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(TransformerCto.class);

  /**
   * Format used to format dates.
   */
  private static SimpleDateFormat dateFormat;

  /**
   * The representation of a null string.
   */
  public static NullString nullString;

  /**
   * The representation of a null boolean.
   */
  public static NullBoolean nullBoolean;

  /**
   * The representation of a null short.
   */
  public static NullShort nullShort;

  /**
   * The representation of a null long.
   */
  public static NullInteger nullInteger;

  /**
   * The representation of a null long.
   */
  public static NullLong nullLong;

  /**
   * The representation of a null double.
   */
  public static NullDouble nullDouble;

  /**
   * The representation of a null date.
   */
  public static NullDate nullDate;

  /**
   * The representation of a null timestamp.
   */
  public static NullTimestamp nullTimestamp;

  static {
    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    nullString = new NullString("", false);
    nullBoolean = new NullBoolean(false, false);
    nullShort = new NullShort((short) 0, false);
    nullInteger = new NullInteger(0, false);
    nullLong = new NullLong(0, false);
    nullDouble = new NullDouble(0, false);
    nullDate = new NullDate("", false);
    nullTimestamp = new NullTimestamp("", false);
  }

  /**
   * Transforms the date into its string representation.
   * @param date date to transform.
   * @return string representation of the date.
   */
  public static final String transformDate(Date date) {
    return dateFormat.format(date);
  }

  /**
   * Transforms the primitive wrapped value into a nullable representation so
   * that it can be transferred over a CORBA transport channel.
   * @param value value to transform.
   * @return the nullable representation of the value.
   * @see #extract(NullShort)
   */
  public static final NullShort create(Short value) {
    return ((value == null) ? nullShort : new NullShort(value.shortValue(),
        true));
  }

  /**
   * Transforms the primitive wrapped value into a nullable representation so
   * that it can be transferred over a CORBA transport channel.
   * @param value value to transform.
   * @return the nullable representation of the value.
   * @see #extract(NullLong)
   */
  public static final NullLong create(Long value) {
    return ((value == null) ? nullLong : new NullLong(value.intValue(), true));
  }

  /**
   * Transforms the primitive wrapped value into a nullable representation so
   * that it can be transferred over a CORBA transport channel.
   * @param value value to transform.
   * @return the nullable representation of the value.
   * @see #extract(NullInteger)
   */
  public static final NullInteger create(Integer value) {
    return ((value == null) ? nullInteger : new NullInteger(value.intValue(),
        true));
  }

  /**
   * Transforms the primitive wrapped value into a nullable representation so
   * that it can be transferred over a CORBA transport channel.
   * @param value value to transform.
   * @return the nullable representation of the value.
   * @see #extract(NullDouble)
   */
  public static final NullDouble create(Double value) {
    return ((value == null) ? nullDouble : new NullDouble(value.doubleValue(),
        true));
  }

  /**
   * Transforms the primitive wrapped value into a nullable representation so
   * that it can be transferred over a CORBA transport channel.
   * @param value value to transform.
   * @return the nullable representation of the value.
   * @see #extract(NullString)
   */
  public static final NullString create(String value) {
    return ((value == null) ? nullString : new NullString(value, true));
  }

  /**
   * Transforms the primitive wrapped value into a nullable representation so
   * that it can be transferred over a CORBA transport channel.
   * @param value value to transform.
   * @return the nullable representation of the value.
   * @see #extract(NullBoolean)
   */
  public static final NullBoolean create(Boolean value) {
    return ((value == null) ? nullBoolean : new NullBoolean(value
        .booleanValue(), true));
  }

  /**
   * Transforms the primitive wrapped value into a nullable representation so
   * that it can be transferred over a CORBA transport channel.
   * @param value value to transform.
   * @return the nullable representation of the value.
   * @see #extract(NullDate)
   */
  public static final NullDate create(Date value) {
    return ((value == null) ? nullDate : new NullDate(dateFormat.format(value),
        true));
  }

  /**
   * Transforms the primitive wrapped value into a nullable representation so
   * that it can be transferred over a CORBA transport channel.
   * @param value value to transform.
   * @return the nullable representation of the value.
   * @see #extract(NullTimestamp)
   */
  public static final NullTimestamp create(Timestamp value) {
    return ((value == null) ? nullTimestamp : new NullTimestamp(value
        .toString(), true));
  }

  public static final DataPropertyUpdateCto create(DataPropertyUpdate value) {
    DataPropertyUpdateCto cto = new DataPropertyUpdateCto();
    cto.property = value.getProperty();
    cto.value = create(value.getValue());
    return cto;
  }

  public static final DataPropertyUpdateCto[] createUpdates(List infos) {
    DataPropertyUpdateCto[] ctos = new DataPropertyUpdateCto[infos.size()];
    for (int i = 0; i < ctos.length; i++) {
      ctos[i] = create((DataPropertyUpdate) infos.get(i));
    }
    return ctos;
  }

  public static final DataObjectUpdateCto create(DataObjectUpdate value) {
    DataObjectUpdateCto cto = new DataObjectUpdateCto();
    cto.classname = value.getClassname();
    cto.id = value.getId().intValue();
    cto.timestamp = create(value.getTimestamp());
    cto.updates = createUpdates(value.getUpdates());
    return cto;
  }

  /**
   * Creates a CTO representation of the CTO exception.
   * @param value value to transform.
   * @return the CTO representation of the instance.
   */
  public static final CtoExceptionCto create(CtoException value) {
    CtoExceptionCto cto = new CtoExceptionCto();
    cto.text = create(value.getMessage());
    cto.reason = create(value.getReason());
    return cto;
  }

  public static final DataObjectUpdateCto[] createInfos(List infos) {
    DataObjectUpdateCto[] ctos = new DataObjectUpdateCto[infos.size()];
    for (int i = 0; i < ctos.length; i++) {
      ctos[i] = create((DataObjectUpdate) infos.get(i));
    }
    return ctos;
  }

  /**
   * Transforms the nullable value into a wrapped primitive value. If the
   * nullable contains a null value, null is returned. that it can be
   * transferred over a CORBA transport channel.
   * @param value nullable value to transform.
   * @return the wrapped primitive value if defined otherwise null.
   * @see #create(Short)
   */
  public static final Short extract(NullShort value) {
    return (value.hasValue ? new Short(value.value) : null);
  }

  /**
   * Transforms the nullable value into a wrapped primitive value. If the
   * nullable contains a null value, null is returned. that it can be
   * transferred over a CORBA transport channel.
   * @param value nullable value to transform.
   * @return the wrapped primitive value if defined otherwise null.
   * @see #create(Integer)
   */
  public static final Integer extract(NullInteger value) {
    return (value.hasValue ? new Integer(value.value) : null);
  }

  /**
   * Transforms the nullable value into a wrapped primitive value. If the
   * nullable contains a null value, null is returned. that it can be
   * transferred over a CORBA transport channel.
   * @param value nullable value to transform.
   * @return the wrapped primitive value if defined otherwise null.
   * @see #create(Long)
   */
  public static final Long extract(NullLong value) {
    return (value.hasValue ? new Long(value.value) : null);
  }

  /**
   * Transforms the nullable value into a wrapped primitive value. If the
   * nullable contains a null value, null is returned. that it can be
   * transferred over a CORBA transport channel.
   * @param value nullable value to transform.
   * @return the wrapped primitive value if defined otherwise null.
   * @see #create(Double)
   */
  public static final Double extract(NullDouble value) {
    return (value.hasValue ? new Double(value.value) : null);
  }

  /**
   * Transforms the nullable value into a wrapped primitive value. If the
   * nullable contains a null value, null is returned. that it can be
   * transferred over a CORBA transport channel.
   * @param value nullable value to transform.
   * @return the wrapped primitive value if defined otherwise null.
   * @see #create(String)
   */
  public static final String extract(NullString value) {
    return (value.hasValue ? value.value : null);
  }

  /**
   * Transforms the nullable value into a wrapped primitive value. If the
   * nullable contains a null value, null is returned. that it can be
   * transferred over a CORBA transport channel.
   * @param value nullable value to transform.
   * @return the wrapped primitive value if defined otherwise null.
   * @see #create(Boolean)
   */
  public static final Boolean extract(NullBoolean value) {
    Boolean bool = null;
    if (value.hasValue) {
      bool = (value.value ? Boolean.TRUE : Boolean.FALSE);
    }
    return bool;
  }

  /**
   * Transforms the nullable value into a wrapped primitive value. If the
   * nullable contains a null value, null is returned. that it can be
   * transferred over a CORBA transport channel.
   * @param value nullable value to transform.
   * @return the wrapped primitive value if defined otherwise null.
   * @see #create(Date)
   */
  public static final Date extract(NullDate value) {
    return (value.hasValue ? extractDate(value.value) : null);
  }

  /**
   * Transforms the nullable value into a wrapped primitive value. If the
   * nullable contains a null value, null is returned. that it can be
   * transferred over a CORBA transport channel.
   * @param value nullable value to transform.
   * @return the wrapped primitive value if defined otherwise null.
   * @see #create(Timestamp)
   */
  public static final Timestamp extract(NullTimestamp value) {
    return (value.hasValue ? Timestamp.valueOf(value.value) : null);
  }

  /**
   * Transforms the nullable value into a wrapped primitive value. If the
   * nullable contains a null value, null is returned. that it can be
   * transferred over a CORBA transport channel.
   * @param value nullable value to transform.
   * @return the wrapped primitive value if defined otherwise null.
   * @see #create(Date)
   */
  public static final Date extractDate(String value) {
    Date date = null;
    try {
      date = new Date(dateFormat.parse(value).getTime());
    } catch (ParseException e) {
      log.error("Date Conversion Error", e);
    }

    return date;
  }

  public static final CtoException extract(CtoExceptionCto cto) {
    return new CtoException(extract(cto.text), extract(cto.reason));
  }

  /**
   * Transforms the CTO representation of a data property update into its Java
   * representation.
   * @param cto CORBA representation of a property update.
   * @return the Java representation of a property update.
   */
  public static final DataPropertyUpdate extract(DataPropertyUpdateCto cto) {
    return new DataPropertyUpdate(cto.property, extract(cto.value));
  }

  /**
   * Transforms the CTO representation of a data object update into its Java
   * representation.
   * @param cto CORBA representation of an object update.
   * @return the Java representation of an object update.
   */
  public static final DataObjectUpdate extract(DataObjectUpdateCto cto) {
    DataObjectUpdate update = new DataObjectUpdate();
    update.setClassname(cto.classname);
    update.setId(new Integer(cto.id));
    update.setTimestamp(extract(cto.timestamp));
    for (int i = 0; i < cto.updates.length; i++) {
      update.addUpdate(extract(cto.updates[i]));
    }
    return update;
  }

  /**
   * Transforms a list of CORBA data object updates into their Java
   * reprensentation
   * @param ctos CORBA transfer object updates to transform.
   * @return the Java representation of the list.
   */
  public static final List extractInfos(DataObjectUpdateCto[] ctos) {
    List updates = new ArrayList();
    for (int i = 0; i < ctos.length; i++) {
      updates.add(extract(ctos[i]));
    }
    return updates;
  }

  /**
   * Returns the value of the foreign key handle stored in the property.
   * @param bean bean containing the handle to retrieve.
   * @param property property containing the foreign key handle.
   * @return the foreign key handle stored as nullable integer.
   * @see #setHandle(Bean, String, NullInteger)
   */
  public static final NullInteger getHandle(Object bean, String property) {
    Integer key = null;
    try {
      Field field = bean.getClass().getDeclaredField(property);
      if (field != null) {
        field.setAccessible(true);
        key = (Integer) field.get(bean);
      }
    } catch (NoSuchFieldException e) {
      assert (false);
    } catch (IllegalArgumentException e) {
      assert (false);
    } catch (IllegalAccessException e) {
      assert (false);
    }
    return create(key);
  }

  /**
   * Sets the value of the foreign key handle stored in the property.
   * @param bean bean containing the handle to retrieve.
   * @param property property containing the foreign key handle.
   * @param key value of the foreign key handle.
   * @see #getHandle(Bean, String)
   */
  public static final void setHandle(Object bean, String property,
      NullInteger key) {
    try {
      Field field = bean.getClass().getDeclaredField(property);
      if (field != null) {
        field.setAccessible(true);
        field.set(bean, extract(key));
      }
    } catch (NoSuchFieldException e) {
      assert (false);
    } catch (IllegalArgumentException e) {
      assert (false);
    } catch (IllegalAccessException e) {
      assert (false);
    }
  }
}