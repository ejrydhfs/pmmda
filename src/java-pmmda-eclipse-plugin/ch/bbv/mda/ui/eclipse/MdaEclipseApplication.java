/* copyright 2000-2005 Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.ui.eclipse;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import ch.bbv.mda.ui.MdaApplication;

/**
 * The MDA eclipse application defines the <em>pmMDA</em> application as an
 * eclipse plugin. This version is verified with eclipse version 3.1.0
 * @see http://astares.blogspot.com/2007/01/rcp-plugin-startup-in-33m4.html 
 *  
 * @author Adrian Bachofen
 * @version $Revision$
 */

public class MdaEclipseApplication implements IApplication {

  private static String sema = "";
  /*
   * * (non-Javadoc) * *
   * 
   * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
   */
  public Object start(IApplicationContext context) throws Exception {
    MdaApplication.main(new String[0]);
    while(MdaApplication.getApplication().getExplorer() == null){
      synchronized(sema){
        try{
          sema.wait(250);
        }
        catch(InterruptedException e){
          // don't do anything
        }
      }
    }
    MdaApplication.getApplication().getExplorer().getFrame().addWindowListener(new MyWindowListener());
    while(true){
      synchronized(sema) {
        try{
          sema.wait();
          break;
        }
        catch(InterruptedException e){
          // don't do anything
        }
        catch(IllegalMonitorStateException e){
          // don't do anything
        }
      }
    }
    return EXIT_OK ;
  }
  
  static class MyWindowListener extends WindowAdapter {
    public void windowClosed(WindowEvent e) {
      synchronized(sema){
        sema.notifyAll();
      }
    }
  }
  /*
     * * (non-Javadoc) * *
     * 
     * @see org.eclipse.equinox.app.IApplication#stop()
     */
  public void stop() {
  }
}
