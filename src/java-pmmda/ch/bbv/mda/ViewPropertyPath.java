/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda;

import ch.bbv.dog.DataObject;



/**
 * Describes the path to a property in a view
 * 
 * @author Ueli Kurmann
 * 
 */

public class ViewPropertyPath {

  private Class<? extends DataObject> rootElement;
  
  private String path;

  public ViewPropertyPath(Class<? extends DataObject> rootElement, String path) {
    this.rootElement = rootElement;
    this.path = path;
  }
  
  public ViewPropertyPath(Class<? extends DataObject> rootElement){
    this(rootElement, "");
  }
  
  public Class<? extends DataObject> getRootElement(){
    return rootElement;
  }
  
  public void setRootElement(Class<? extends DataObject> rootElement){
    this.rootElement = rootElement;
  }
  
  public String getPath(){
    return path;
  }
  
  public void setPath(String path){
    this.path = path;
  }
}