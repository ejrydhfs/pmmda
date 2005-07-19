/* copyright 2000-2005 Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.ui.eclipse;

/**
 * The MDA eclipse application defines the <em>pmMDA</em> application as an eclipse plugin. 
 * This version is verified with eclipse version 3.1.0
 * @author Adrian Bachofen
 * @version $Revision$
 */

import org.eclipse.core.runtime.IPlatformRunnable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import ch.bbv.mda.ui.MdaApplication;

public class MdaEclipseApplication implements IPlatformRunnable {

  private static String sema = "";
  
  public Object run (Object args){
    MdaApplication.main(new String[0]);
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
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
}
