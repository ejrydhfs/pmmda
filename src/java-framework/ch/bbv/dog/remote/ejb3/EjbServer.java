/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.remote.ejb3;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import ch.bbv.application.Application;
import ch.bbv.application.Locator;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectHandler;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.remote.DataObjectRemoteHandler;
import ch.bbv.dog.updates.DataObjectUpdate;
import ch.bbv.dog.updates.DogUpdate;
import ch.bbv.dog.LightweightObject;

/**
 * The EJB server offers access to the server-side application to a client, in
 * order to retrieve and store data objects. The server bean needs to be
 * deployed on a Java EE application server, along with the server application.
 * 
 * @author UeliKurmann
 * @author ZlatkoFranjcic
 * @version $Revision$
 */
@Stateless
@Remote(ch.bbv.dog.remote.DataObjectRemoteHandler.class)
public class EjbServer implements DataObjectRemoteHandler, Serializable {
  private static final String PROPERTY_PARAMETER = "Application";

  private static final String EJB3SERVER_PROPERTY_FILE = "ejb3server.properties";

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20070712;

  /**
   * The server-side data object handler as provided by the application.
   */
  private DataObjectHandler handler;

  public EjbServer() {
    Locator.create(getPmMdaApplicationClass(), null);
    DataObjectMgr manager = Application.getApplication().getDataObjectMgr();
    handler = manager.getDataObjectHandler();
  }

  public void remove(String datasource, Class<? extends DataObject> clazz,
      DataObject root) {
    handler.remove(datasource, clazz, root);
  }

  public void remove(String datasource, Class<? extends DataObject> clazz,
      Integer id) {
    handler.remove(datasource, clazz, id);
  }

  public DataObject retrieve(String datasource,
      Class<? extends DataObject> clazz, Integer id) {
    return handler.retrieve(datasource, clazz, id);
  }

  public List<DataObject> retrieve(String datasource,
      Class<? extends DataObject> clazz, String constraints) {
    return handler.retrieve(datasource, clazz, constraints);
  }

  public LightweightObject retrieveLw(String datasource,
      Class<? extends LightweightObject> clazz, Integer id) {
    return handler.retrieveLw(datasource, clazz, id);
  }

  public List<LightweightObject> retrieveLw(String datasource,
      Class<? extends LightweightObject> clazz, String constraints) {
    return handler.retrieveLw(datasource, clazz, constraints);
  }

  public List<DataObjectUpdate> store(String datasource, DogUpdate dogUpdate) {
    try {
      DataObject root = dogUpdate.execute();
      handler.store(datasource, root.getClass(), root);
      dogUpdate.clearUpdates();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dogUpdate.getUpdates();
  }

  /**
   * Get the class name of the server application's main class.
   * 
   * @return the name of the application's main class
   */
  private static String getPmMdaApplicationClass() {
    Properties properties = new Properties();
    try {
      properties.load(EjbServer.class.getClassLoader().getResourceAsStream(
          EJB3SERVER_PROPERTY_FILE));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return properties.getProperty(PROPERTY_PARAMETER);
  }
}
