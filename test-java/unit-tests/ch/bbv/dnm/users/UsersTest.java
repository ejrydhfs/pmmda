/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dnm.users;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.application.Locator;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectManager;

/**
 * The test class provides test cases to create and manipulate persistent graphs
 * of data objects.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class UsersTest extends TestCase {
  /** Offset for the string representation of the test identifier.
   */
  private static final int OFFSET = 1000;

  /** Logger of the class.
   */
  private static Log log = LogFactory.getLog(UsersTest.class);
  private static final String DATASOURCE = "ojb-dnm";
  private static final String UPDATE = "update field";

  /** Test graph RCRURD operations. The database must be empty before starting
   * the test. A graph of data objects are created and compared against the
   * stored version.
   */
  public void testCrudProvider() {
    DataObjectManager manager = getDataObjectManager();
    assertNotNull(manager);

    Provider provider = createProviderGraph(1, 5, 5, 5);
    List providers = manager.getBoHandler().retrieve(DATASOURCE,
        Provider.class, (String) null);
    assertEquals(providers.size(), 0);
    manager.getBoHandler().store(DATASOURCE, Provider.class, provider);

    Provider copy = (Provider) manager.getBoHandler().retrieve(DATASOURCE,
        Provider.class, provider.getId());
    compareGraphs(provider, copy);
    provider.setName(UPDATE);
    manager.getBoHandler().store(DATASOURCE, Provider.class, provider);
    copy = (Provider) manager.getBoHandler().retrieve(DATASOURCE,
        Provider.class, provider.getId());
    assertNotNull(copy);
    assertEquals(copy.getName(), UPDATE);
    manager.getBoHandler().remove(DATASOURCE, Provider.class, provider);
    copy = (Provider) manager.getBoHandler().retrieve(DATASOURCE,
        Provider.class, provider.getId());
    assertNull(copy);
  }

  public void testCrudDomain() {
    DataObjectManager manager = getDataObjectManager();
    assertNotNull(manager);

    Provider provider = createProviderGraph(2, 0, 0, 0);
    manager.getBoHandler().store(DATASOURCE, Provider.class, provider);
    for (int i = 1; i <= 5; i++) {
      Domain domain = createDomainGraph(2, 1, 5, 5);
      provider.addDomain(domain);
      manager.getBoHandler().store(DATASOURCE, Domain.class, domain);
    }
    manager.getBoHandler().remove(DATASOURCE, Provider.class, provider);

    Provider copy = (Provider) manager.getBoHandler().retrieve(DATASOURCE,
        Provider.class, provider.getId());
    assertNull(copy);
  }

  public void testBigCRDProvider() {
    DataObjectManager manager = getDataObjectManager();
    assertNotNull(manager);

    Provider provider = createProviderGraph(1, 10, 10, 10);
    assertNull(provider.getId());
    manager.getBoHandler().store(DATASOURCE, Provider.class, provider);
    assertNotNull(provider.getId());

    Integer id = provider.getId();
    manager.getBoHandler().remove(DATASOURCE, Provider.class, id);
    provider = (Provider) manager.getBoHandler().retrieve(DATASOURCE,
        Provider.class, id);
    assertNull(provider);
  }

  public void testStressCRDProvider() {
    List providers = new ArrayList();
    DataObjectManager manager = getDataObjectManager();
    assertNotNull(manager);
    for (int i = 0; i < 10; i++) {
      Provider provider = createProviderGraph(1, 10, 10, 10);
      assertNull(provider.getId());
      manager.getBoHandler().store(DATASOURCE, Provider.class, provider);
      assertNotNull(provider.getId());
      providers.add(provider);
    }
    for (Iterator i = providers.iterator(); i.hasNext();) {
    	Provider provider = (Provider)i.next();
			Integer id = provider.getId();
			manager.getBoHandler().remove(DATASOURCE, Provider.class, provider);
			provider = (Provider) manager.getBoHandler().retrieve(DATASOURCE,
				Provider.class, id);
			assertNull(provider);
    }
  }

  private Provider createProviderGraph(int id, int nrDomains, int nrUnits,
    int nrUsers) {
    Provider provider = createProvider(createId(id, 0, 0, 0));
    for (int i = 1; i <= nrDomains; i++) {
      Domain domain = createDomainGraph(id, i, nrUnits, nrUsers);
      provider.addDomain(domain);
    }
    return provider;
  }

  private Domain createDomainGraph(int owner, int id, int nrUnits, int nrUsers) {
    Domain domain = createDomain(createId(owner, id, 0, 0));
    for (int j = 1; j <= nrUnits; j++) {
      Unit unit = createUnit(createId(owner, id, j, 0));
      domain.addUnit(unit);
      for (int k = 1; k <= nrUsers; k++) {
        User user = createUser(createId(owner, id, j, k));
        unit.addUser(user);
      }
    }
    return domain;
  }

  private void compareGraphs(Provider left, Provider right) {
    compare(left, right);

    List domainsL = left.getDomains();
    List domainsR = right.getDomains();
    assertEquals(domainsL.size(), domainsR.size());
    for (int i = 0; i < domainsL.size(); i++) {
      compare((Domain)domainsL.get(i), (Domain)domainsR.get(i));

      List unitsL = ((Domain) domainsL.get(i)).getUnits();
      List unitsR = ((Domain) domainsR.get(i)).getUnits();
      assertEquals(unitsL.size(), unitsR.size());
      for (int j = 0; j < unitsL.size(); j++) {
        compare((Unit) unitsL.get(j), (Unit) unitsR.get(j));

        List usersL = ((Unit) unitsL.get(j)).getUsers();
        List usersR = ((Unit) unitsR.get(j)).getUsers();
        assertEquals(usersL.size(), usersR.size());
        for (int k = 0; k < usersL.size(); k++) {
          compare((User)usersL.get(k), (User) usersR.get(k));
        }
      }
    }
  }

  private User createUser(long i) {
    User item = new User();
    item.setName("User-" + id2String(i));
    item.setPassword(id2String(i));
    item.setEmail(item.getName() + "@bbv.ch");
    item.setLogin("login");
    item.setPassword("Password-" + id2String(i));
    item.setValidFrom(new Date());
    item.setValidTo(new Date());
    item.setActive(true);
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
    item.setLongname("Domain-longname-" + id2String(i));
    item.setDescription("Domain description " + id2String(i));
    return item;
  }

  private Provider createProvider(long i) {
    Provider item = new Provider();
    item.setName("Provider-" + id2String(i));
    item.setLongname("Provider-longname-" + id2String(i));
    item.setDescription("Provider description " + id2String(i));
    item.setEmail(item.getName() + "@bbv.ch");
    item.setName("Provider-longname-" + id2String(i));
    return item;
  }

  private void compare(DataObject left, DataObject right) {
    assertNotNull(left);
    assertNotNull(right);
    assertEquals(left.getId(), right.getId());
    if (left.getId() != null) {
      assertNotNull(left.getTimestamp());
    }
    if (right.getId() != null) {
      assertNotNull(right.getTimestamp());
    }
  }

  private long createId(long providerId, long domainId, long unitId, long userId) {
    return (((((providerId * OFFSET) + domainId) * OFFSET) + unitId) * OFFSET) +
    userId;
  }

  private String id2String(long id) {
    return Long.toString(id);
  }

  /** Returns the data object manager of the application. The manager
   * provides access the data object handler, lock manager and lightweight
   * object handler.
   * @return the data object handler of the application.
   */
  private DataObjectManager getDataObjectManager() {
    Locator locator = Locator.getInstance();
    return (DataObjectManager) locator.getComponent(DataObjectManager.NAME);
  }
}
