/* copyright 2000-2005 Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.ui.eclipse;

import javax.swing.JFileChooser;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import ch.bbv.explorer.actions.SimpleFileFilter;
import ch.bbv.mda.persistence.rose.MdaRoseUtil;
import ch.bbv.mda.ui.actions.FileTypes;

public class EclipseUtilities {
  
  /**
   * Returns true if the querying application is running as eclipse plugin, otherwise false
   * @return true for running as eclipse plugin, otherwise false.
   */
  public static boolean isEclipseRunning()
  {
    try{
      ResourceSet result = new ResourceSetImpl();
      result.getURIConverter().getURIMap().putAll(EcorePlugin.computePlatformURIMap());
      new MdaRoseUtil(result.getURIConverter());
    }
    catch (Exception e){
      return false;
    }
    return true;
  }
  
  public static void addEclipseFileFilters(JFileChooser chooser)
  {
    if(ch.bbv.mda.ui.eclipse.EclipseUtilities.isEclipseRunning())
    {
      chooser.addChoosableFileFilter(new SimpleFileFilter(
          FileTypes.RATIONAL_ROSE_EXT, FileTypes.RATIONAL_ROSE_TEXT));
      chooser.addChoosableFileFilter(new SimpleFileFilter(
          FileTypes.RATIONAL_ROSE_CAT_EXT, FileTypes.RATIONAL_ROSE_CAT_TEXT));
    }
  }

}
