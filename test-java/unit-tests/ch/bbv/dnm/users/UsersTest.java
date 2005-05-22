/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dnm.users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.application.Locator;
import ch.bbv.dnm.users.client.Application;
import ch.bbv.dog.CacheHandler;
import ch.bbv.dog.DataObject;
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
  private static final int OFFSET = 100;

  /**
   * Logger of the class.
   */
  private static Log log = LogFactory.getLog(UsersTest.class);

  /**
   * The name of the datasource containing all data objects.
   */
  private static final String DATASOURCE = "ojb-dnm";

  private static final String UPDATE = "update field";

  public static Test suite() {
    return new TestSuite(UsersTest.class);
  }

  public void setUp() throws Exception {
    if (Application.getApplication() == null) {
      new Application(Application.USE_DIRECT, true);
      log.info("Application singleton created");
    }
  }

  /**
   * Test graph RCRURD operations. The database must be empty before starting
   * the test. A graph of data objects are created and compared against the
   * stored version.
   */
  public void testCrudProvider() {
    DataObjectManager manager = getDataObjectManager();
    assertNotNull(manager);
    List providers = manager.getDataObjectHandler().retrieve(DATASOURCE,
        Provider.class, (String) null);
    assertEquals(providers.size(), 0);
    Provider provider = createProviderGraph(1, 5, 5, 5);
    manager.getDataObjectHandler().store(DATASOURCE, Provider.class, provider);
    Provider copy = (Provider) manager.getDataObjectHandler().retrieve(
        DATASOURCE, Provider.class, provider.getId());
    compareGraphs(provider, copy);
    provider.setName(UPDATE);
    manager.getDataObjectHandler().store(DATASOURCE, Provider.class, provider);
    copy = (Provider) manager.getDataObjectHandler().retrieve(DATASOURCE,
        Provider.class, provider.getId());
    assertNotNull(copy);
    assertEquals(copy.getName(), UPDATE);
    manager.getDataObjectHandler().remove(DATASOURCE, Provider.class, provider);
    copy = (Provider) manager.getDataObjectHandler().retrieve(DATASOURCE,
        Provider.class, provider.getId());
    assertNull(copy);
  }

  public void testCrudDomain() {
    DataObjectManager manager = getDataObjectManager();
    assertNotNull(manager);

    Provider provider = createProviderGraph(2, 0, 0, 0);
    manager.getDataObjectHandler().store(DATASOURCE, Provider.class, provider);
    for (int i = 1; i <= 5; i++) {
      Domain domain = createDomainGraph(2, 1, 5, 5);
      provider.addDomain(domain);
      int nrDomains = provider.getDomains().size();
      manager.getDataObjectHandler().store(DATASOURCE, Domain.class, domain);
      provider = (Provider) manager.getDataObjectHandler().retrieve(DATASOURCE,
          provider.getClass(), provider.getId());
      assert (provider.getDomains().size() == nrDomains);
    }
    manager.getDataObjectHandler().remove(DATASOURCE, Provider.class, provider);

    Provider copy = (Provider) manager.getDataObjectHandler().retrieve(
        DATASOURCE, Provider.class, provider.getId());
    assertNull(copy);
  }

  public void testBigCrdProvider() {
    DataObjectManager manager = getDataObjectManager();
    assertNotNull(manager);

    Provider provider = createProviderGraph(1, 10, 10, 10);
    assertNull(provider.getId());
    assertNull(provider.getTimestamp());
    manager.getDataObjectHandler().store(DATASOURCE, Provider.class, provider);
    assertNotNull(provider.getId());
    assertNotNull(provider.getTimestamp());

    Integer id = provider.getId();
    manager.getDataObjectHandler().remove(DATASOURCE, Provider.class, id);
    provider = (Provider) manager.getDataObjectHandler().retrieve(DATASOURCE,
        Provider.class, id);
    assertNull(provider);
  }

  public void testStressCrdProvider() {
    List<Provider> providers = new ArrayList<Provider>();
    DataObjectManager manager = getDataObjectManager();
    assertNotNull(manager);
    for (int i = 0; i < 10; i++) {
      Provider provider = createProviderGraph(1, 10, 10, 10);
      assertNull(provider.getId());
      manager.getDataObjectHandler()
          .store(DATASOURCE, Provider.class, provider);
      assertNotNull(provider.getId());
      providers.add(provider);
    }
    for (Provider provider : providers) {
      Integer id = provider.getId();
      manager.getDataObjectHandler().remove(DATASOURCE, Provider.class,
          provider);
      provider = (Provider) manager.getDataObjectHandler().retrieve(DATASOURCE,
          Provider.class, id);
      assertNull(provider);
    }
    List<DataObject> objects = manager.getDataObjectHandler().retrieve(
        DATASOURCE, Provider.class, (String) null);
    assertEquals(objects.size(), 0);
  }

  /**
   * Test the cache handling of data objects for a data object manager using a
   * cache handler.
   */
  public void testCacheHandler() {
    DataObjectManager manager = getDataObjectManager();
    assertNotNull(manager);
    CacheHandler handler = (CacheHandler) manager.getDataObjectHandler();
    assertNotNull(handler);
    assertTrue(handler.isCachedEnabled());
    assertTrue(handler.isCopiedEnabled());
    Provider provider = createProviderGraph(1, 2, 2, 2);
    // do the caches contain the data objects.
    manager.getDataObjectHandler().store(DATASOURCE, Provider.class, provider);
    assertTrue(handler.isInCopiesCache(provider.getClass(), provider.getId()));
    assertTrue(handler
        .isInOriginalsCache(provider.getClass(), provider.getId()));
    assertTrue(provider == handler.getCopy(provider.getClass(), provider
        .getId()));
    // do the caches clear operations work.
    handler.clearCopies();
    assertFalse(handler.isInCopiesCache(provider.getClass(), provider.getId()));
    handler.clearOriginals();
    assertFalse(handler.isInOriginalsCache(provider.getClass(), provider
        .getId()));
    provider = (Provider) handler.retrieve(DATASOURCE, Provider.class, provider
        .getId());
    assertTrue(handler.isInCopiesCache(provider.getClass(), provider.getId()));
    assertTrue(handler
        .isInOriginalsCache(provider.getClass(), provider.getId()));
    // do the reload operations work
    Provider copy = (Provider) handler.reload(DATASOURCE, provider);
    assertTrue((provider.getId().equals(copy.getId())) && (provider != copy));
    handler.remove(DATASOURCE, Provider.class, provider);
    copy = (Provider) manager.getDataObjectHandler().retrieve(DATASOURCE,
        Provider.class, provider.getId());
    assertNull(copy);
  }

  /**
   * Creates a provider graph with domains, units and users.
   * @param id identifier of the domain name.
   * @param nrDomains number of domains to create.
   * @param nrUnits number of units to create.
   * @param nrUsers number of users to create.
   * @return the root provider of the provider graph.
   */
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

    List<Domain> domainsL = left.getDomains();
    List<Domain> domainsR = right.getDomains();
    assertEquals(domainsL.size(), domainsR.size());
    for (int i = 0; i < domainsL.size(); i++) {
      compare(domainsL.get(i), domainsR.get(i));

      List<Unit> unitsL = (domainsL.get(i)).getUnits();
      List<Unit> unitsR = (domainsR.get(i)).getUnits();
      assertEquals(unitsL.size(), unitsR.size());
      for (int j = 0; j < unitsL.size(); j++) {
        compare(unitsL.get(j), unitsR.get(j));

        List<User> usersL = (unitsL.get(j)).getUsers();
        List<User> usersR = (unitsR.get(j)).getUsers();
        assertEquals(usersL.size(), usersR.size());
        for (int k = 0; k < usersL.size(); k++) {
          compare(usersL.get(k), usersR.get(k));
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
    item.setTrusted(0.0);
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
    return (((((providerId * OFFSET) + domainId) * OFFSET) + unitId) * OFFSET)
        + userId;
  }

  private String id2String(long id) {
    return Long.toString(id);
  }

  /**
   * Returns the data object manager of the application. The manager provides
   * access the data object handler, lock manager and lightweight object
   * handler.
   * @return the data object handler of the application.
   */
  private DataObjectManager getDataObjectManager() {
    Locator locator = Locator.getInstance();
    return (DataObjectManager) locator.getComponent(DataObjectManager.NAME);
  }
}