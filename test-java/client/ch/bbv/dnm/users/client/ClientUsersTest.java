/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dnm.users.client;

import java.io.IOException;

import ch.bbv.application.ComponentException;
import ch.bbv.dnm.users.UsersTest;

/**
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ClientUsersTest extends UsersTest {
  public void setUp() throws ComponentException, IOException {
    new Application(Application.USE_DIRECT);
  }

}