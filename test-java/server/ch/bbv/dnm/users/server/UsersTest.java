/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dnm.users.server;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.dnm.users.*;
import ch.bbv.dog.DataObjectHandler;
import ch.bbv.dog.DataObjectManager;

/**
 * The test class provides test cases to create and manipulate persistent graphs
 * of data objects.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class UsersTest extends TestCase {
  /**
   * Offset for the string representation of the test identifier.
   */
  private static final int OFFSET = 1000;

  /**
   * Logger of the class.
   */
  private static Log log = LogFactory.getLog(UsersTest.class);

  public void testCrudProvider() {
    Application application = Application.getApplication();
    DataObjectManager manager = application.getDataObjectManager();
    DataObjectHandler handler = manager.getDataObjectHandler();
    Provider provider = createGraph(1, 5, 5, 5);
    handler.store("dnm-ojb", Provider.class, provider);
    Provider copy = (Provider) handler.retrieve("dnm-ojb", Provider.class,
        provider.getId());
    compareGraphs(provider, copy);
    handler.remove("dnm-ojb", Provider.class, provider);
    copy = (Provider) handler.retrieve("dnm-ojb", Provider.class, provider
        .getId());
    assertNull(copy);
  }

  private Provider createGraph(int id, int nrDomains, int nrUnits, int nrUsers) {
    Provider provider = createProvider(createId(id, 0, 0, 0));
    for (int i = 1; i <= nrDomains; i++) {
      Domain domain = createDomain(createId(id, i, 0, 0));
      provider.addDomain(domain);
      for (int j = 1; j <= nrUnits; j++) {
        Unit unit = createUnit(createId(id, i, j, 0));
        domain.addUnit(unit);
        for (int k = 1; k <= nrUsers; k++) {
          User user = createUser(createId(id, i, j, k));
          unit.addUser(user);
        }
      }
    }
    return provider;
  }

  private void compareGraphs(Provider left, Provider right) {
    compare(left, right);
    List domainsL = left.getDomains();
    List domainsR = right.getDomains();
    assertEquals(domainsL.size(), domainsR.size());
    for (int i = 1; i <= domainsL.size(); i++) {
      compare(domainsL.get(i), domainsR.get(i));
      List unitsL = ((Domain) domainsL.get(i)).getUnits();
      List unitsR = ((Domain) domainsR.get(i)).getUnits();
      assertEquals(unitsL.size(), unitsR.size());
      for (int j = 1; j <= unitsL.size(); j++) {
        compare(unitsL.get(j), unitsR.get(j));
        List usersL = ((Unit) unitsL.get(j)).getUsers();
        List usersR = ((Unit) unitsR.get(j)).getUsers();
        assertEquals(usersL.size(), usersR.size());
        for (int k = 1; k <= usersL.size(); k++) {
          compare(usersL.get(k), usersR.get(k));
        }
      }
    }
  }

  private User createUser(long i) {
    User item = new User();
    item.setName("User-" + id2String(i));
    item.setPassword(id2String(i));
    return item;
  }

  private Unit createUnit(long i) {
    Unit item = new Unit();
    item.setName("Unit-" + id2String(i));
    item.setDescription("Unit description " + id2String(i));
    return item;
  }

  private Domain createDomain(long i) {
    Domain item = new Domain();
    item.setName("Domain-" + id2String(i));
    item.setDescription("Domain description " + id2String(i));
    return item;
  }

  private Provider createProvider(long i) {
    Provider item = new Provider();
    item.setName("Provider-" + id2String(i));
    item.setDescription("Provider description " + id2String(i));
    return item;
  }

  private void compare(Object left, Object right) {
    assertNotNull(left);
    assertNotNull(right);
    try {
      assertEquals(BeanUtils.getSimpleProperty(left, "id"), BeanUtils
          .getSimpleProperty(right, "id"));
    } catch (IllegalAccessException e) {
      log.fatal(e);
    } catch (InvocationTargetException e) {
      log.fatal(e);
    } catch (NoSuchMethodException e) {
      log.fatal(e);
    }
  }

  private long createId(long providerId, long domainId, long unitId, long userId) {
    return (((((providerId * OFFSET) + domainId) * OFFSET) + unitId) * OFFSET)
        + userId;
  }

  private String id2String(long id) {
    return Long.toString(id);
  }
}