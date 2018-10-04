package org.nhindirect.config.repository;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.nhindirect.config.SpringBaseTest;
import org.nhindirect.config.store.Address;
import org.nhindirect.config.store.Domain;
import org.nhindirect.config.store.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DomainRepositoryTest  extends SpringBaseTest
{

	private static final Log log = LogFactory.getLog(DomainRepositoryTest.class);
	
	@Autowired
	private DomainRepository domRepo;
	
	@Autowired
	private AddressRepository addRepo;	
	
	/*
	 * MOD: Greg Meyer
	 * Do not rely on the order of the tests in the class to ensure that prerequisites are met.  Each test
	 * should be able to run independently without relying on the side affects of other tests.  If side affects
	 * are needed, the execute the dependent tests from within that executing test. 
	 */
	
	@Before
	public void cleanDataBase()
	{
		addRepo.deleteAll();
		domRepo.deleteAll();
	}
	
	@Test
	public void testCleanDatabase() 
	{
		assertEquals(0, addRepo.findAll().size());
		assertEquals(0, domRepo.findAll().size());
	}
	
	@Test
	public void testAddDomain() 
	{

		Domain domain = new Domain("health.testdomain.com");
		domain.setStatus(EntityStatus.ENABLED);
		domRepo.save(domain);
		assertEquals(domRepo.count(), 1);
	}
	
	@Test
	public void testGetByDomain() 
	{
		Domain domain = new Domain("health.testdomain.com");
		domain.setStatus(EntityStatus.ENABLED);
		domRepo.save(domain);
		
		Domain testDomain = domRepo.findByDomainNameIgnoreCase("health.testdomain.com");
		log.info("Newly added Domain ID is: " + testDomain.getId());
		log.info("Newly added Domain Status is: " + testDomain.getStatus());
		
		assertTrue(testDomain.getDomainName().equals("health.testdomain.com"));
	}

	@Test
	public void testUpdateDomain() 
	{
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SS Z");
		Domain domain = new Domain("health.testdomain.com");
		domain.setStatus(EntityStatus.ENABLED);
		domRepo.save(domain);
		
		Domain testDomain = domRepo.findByDomainNameIgnoreCase("health.testdomain.cOM");
		log.info("Newly added Domain ID is: " + testDomain.getId());
		log.info("Newly added Domain Status is: " + testDomain.getStatus());
		log.info("Newly added Domain Update Time is: " + 
				 fmt.format(new Date(testDomain.getUpdateTime().getTimeInMillis())));
		assertTrue(testDomain.getDomainName().equals("health.testdomain.com"));
		
		testDomain.setStatus(EntityStatus.DISABLED);
		domRepo.save(testDomain);
		
		domain = domRepo.findByDomainNameIgnoreCase("health.testdomain.com");
		log.info("Updated Domain ID is: " + domain.getId());
		log.info("Updated Status is: " + domain.getStatus());
		log.info("Updated Update Time is: " + 
				 fmt.format(new Date(domain.getUpdateTime().getTimeInMillis())));
		
		assertTrue(domain.getStatus().equals(EntityStatus.DISABLED));
	}
	
	
	@Test 
	public void testGetDomain() 
	{
		Domain domain = new Domain("health.testdomain.com");
		domain.setStatus(EntityStatus.NEW);
		domRepo.save(domain);
		domain = new Domain("health.newdomain.com");
		domain.setStatus(EntityStatus.NEW);
		domRepo.save(domain);
		
		
		List<String> names = new ArrayList<String>();
		names.add("health.testdomain.com".toUpperCase());
		
		assertEquals(domRepo.findByDomainNameInIgnoreCaseAndStatus(names, EntityStatus.NEW).size(), 1);
		assertEquals(domRepo.findByStatus(EntityStatus.NEW).size(), 2);
		assertEquals(domRepo.findByDomainNameInIgnoreCase(names).size(), 1);
		assertEquals(domRepo.findByDomainNameInIgnoreCaseAndStatus(names, EntityStatus.ENABLED).size(), 0);
		assertEquals(domRepo.findByDomainNameInIgnoreCaseAndStatus(names, EntityStatus.DISABLED).size(), 0);
		assertEquals(domRepo.findAll().size(), 2);
		
		names.clear();
		names.add("health.baddomain.com");
		
		assertEquals(0, domRepo.findByDomainNameInIgnoreCase(names).size());
	}
	

	@Test 
	public void testDeleteDomain() 
	{
		Domain domain = new Domain("health.newdomain.com");
		domain.setPostMasterEmail("postmaster@health.newdomain.com");
		domain.setStatus(EntityStatus.NEW);
		domRepo.save(domain);
		assertEquals(1, domRepo.count());
		domRepo.deleteByDomainNameIgnoreCase("health.testdomain.cOM");
		assertEquals(1, domRepo.count());
		domRepo.deleteByDomainNameIgnoreCase("health.newdomain.com");
		assertEquals(0, domRepo.count());
	}

	@Test
	public void testSearchDomain() 
	{
		log.debug("Enter");
		Domain domain = new Domain("health.newdomain.com");
		domain.setStatus(EntityStatus.NEW);
		domRepo.save(domain);
		
		domain = new Domain("healthy.domain.com");
		domain.setStatus(EntityStatus.NEW);
		domRepo.save(domain);
		
		String name = "heaL";
		List<Domain> result = domRepo.findByDomainNameContainingIgnoreCase(name);
		assertEquals(2, result.size());
		
		name = "coM";
		result = domRepo.findByDomainNameContainingIgnoreCaseAndStatus(name, EntityStatus.NEW);
		assertEquals(2, result.size());
		
		name = "coM";
		result = domRepo.findByDomainNameContainingIgnoreCaseAndStatus(name, EntityStatus.DISABLED);
		assertEquals(0, result.size());
		
		log.debug("Exit");
	}

	
	 //As it turns out, you have to save the owning entity (Domain) before you 
	 //start adding dependent entities to it.
	@Test 
	public void testAddDomainsWithAddresses() 
	{
		
		Domain domain = new Domain("health.newdomain.com");
		domain.setStatus(EntityStatus.NEW);
		domain.getAddresses().add(new Address(domain, "test1@health.newdomain.com", "Test1"));
		domain.getAddresses().add(new Address(domain, "test2@health.newdomain.com", "Test2"));
		domain.getAddresses().add(new Address(domain, "postmaster@health.newdomain.com", "Test3"));
		
		domRepo.save(domain);
		
		Domain getDomain = domRepo.findByDomainNameIgnoreCase("health.newdomain.com");
		assertEquals(3, getDomain.getAddresses().size());
		
		getDomain.setPostMasterEmail("postmaster@health.newdomain.com");
		domRepo.save(getDomain);
		
		getDomain = domRepo.findByDomainNameIgnoreCase("health.newdomain.com");
		assertEquals("postmaster@health.newdomain.com", getDomain.getPostMasterEmail());
		assertEquals(3, getDomain.getAddresses().size());
		
		log.info(domain.toString());
		log.info(getDomain.toString());
		Iterator<Address> iter = getDomain.getAddresses().iterator();
		
		while (iter.hasNext()) {
			Address testAddress = iter.next();
			log.info(testAddress.toString());
		}
	}
	
	
	 //Don't need to clean the db before execution.  @TransactionConfiguration defaults to 
	 //rolling back all transactions at the end of the method.   That should fail ONLY if the
	 //tests db doesn't support transactions (in which case needs to be fixed)
	 //
	@Test
	public void testDeleteDomainsWithAddresses() 
	{
		
		Domain domain = new Domain("health.newdomain.com");
		domain.setStatus(EntityStatus.NEW);
		domain.getAddresses().add(new Address(domain, "test1@health.newdomain.com", "Test1"));
		domain.getAddresses().add(new Address(domain, "test2@health.newdomain.com", "Test2"));
		domRepo.save(domain);
		
		Domain test = domRepo.findByDomainNameIgnoreCase("health.newdomain.com");
		assertEquals(2, test.getAddresses().size());
		
		domRepo.deleteByDomainNameIgnoreCase("health.newdomain.COM");
		test = domRepo.findByDomainNameIgnoreCase("health.newdomain.com");
		assertEquals(null, test);
		
		domain = new Domain("health.domain.com");
		domain.setStatus(EntityStatus.NEW);
		domain.getAddresses().add(new Address(domain, "test1@health.domain.com", "Test1"));
		domain.getAddresses().add(new Address(domain, "test2@health.domain.com", "Test2"));
		domRepo.save(domain);
		
		test = domRepo.findByDomainNameIgnoreCase("health.domain.com");
		Long id = test.getId();
	
		domRepo.deleteById(id);

		assertEquals(Optional.empty(), domRepo.findById(id));

	}
	
}
