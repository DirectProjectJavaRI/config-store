package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import org.nhindirect.config.SpringBaseTest;
import org.nhindirect.config.store.Address;
import org.nhindirect.config.store.Domain;
import org.nhindirect.config.store.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import lombok.extern.slf4j.Slf4j;
import reactor.test.StepVerifier;

@Slf4j
public class DomainRepositoryTest  extends SpringBaseTest
{	
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
	
	@BeforeEach
	public void cleanDataBase()
	{
		addRepo.deleteAll()
		.as(StepVerifier::create) 
		.verifyComplete();
		
		domRepo.deleteAll()
		.as(StepVerifier::create) 
		.verifyComplete();
	}
	
	@Test
	public void testCleanDatabase() 
	{
		addRepo.findAll()
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
		
		domRepo.findAll()
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
	}
	
	@Test
	public void testAddDomain() 
	{

		Domain domain = new Domain();
		domain.setDomainName("health.testdomain.com");
		domain.setStatus(EntityStatus.ENABLED.ordinal());
		domRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
	}
	
	@Test
	public void testGetByDomain() 
	{
		Domain domain = new Domain();
		domain.setDomainName("health.testdomain.com");
		domain.setStatus(EntityStatus.ENABLED.ordinal());
		domRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Domain testDomain = domRepo.findByDomainNameIgnoreCase("health.testdomain.com").block();
		log.info("Newly added Domain ID is: {}", testDomain.getId());
		log.info("Newly added Domain Status is: {}", testDomain.getStatus());
		
		assertTrue(testDomain.getDomainName().equals("health.testdomain.com"));
	}

	@Test
	public void testUpdateDomain() 
	{
		Domain domain = new Domain();
		domain.setDomainName("health.testdomain.com");
		domain.setStatus(EntityStatus.ENABLED.ordinal());
		domRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Domain testDomain = domRepo.findByDomainNameIgnoreCase("health.testdomain.cOM").block();
		log.info("Newly added Domain ID is: {}", testDomain.getId());
		log.info("Newly added Domain Status is: {}", testDomain.getStatus());

		assertTrue(testDomain.getDomainName().equals("health.testdomain.com"));
		
		testDomain.setStatus(EntityStatus.DISABLED.ordinal());
		domRepo.save(testDomain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		domain = domRepo.findByDomainNameIgnoreCase("health.testdomain.com").block();
		log.info("Updated Domain ID is: {}", domain.getId());
		log.info("Updated Status is: {}", domain.getStatus());

		
		assertTrue(domain.getStatus() == EntityStatus.DISABLED.ordinal());
	}
	
	
	@Test 
	public void testGetDomain() 
	{
		Domain domain = new Domain();
		domain.setDomainName("health.testdomain.com");
		domain.setStatus(EntityStatus.NEW.ordinal());
		domRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		domain = new Domain();
		domain.setDomainName("health.newdomain.com");
		domain.setStatus(EntityStatus.NEW.ordinal());
		domRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		
		List<String> names = new ArrayList<String>();
		names.add("health.testdomain.com".toUpperCase());
		
		List<Domain> findDoms = domRepo.findByDomainNameInIgnoreCaseAndStatus(names, EntityStatus.NEW.ordinal()).collectList().block();
		assertEquals(findDoms.size(), 1);
		
		findDoms = domRepo.findByStatus(EntityStatus.NEW.ordinal()).collectList().block();
		assertEquals(findDoms.size(), 2);
		
		findDoms = domRepo.findByDomainNameInIgnoreCase(names).collectList().block();
		assertEquals(findDoms.size(), 1);
		
		findDoms = domRepo.findByDomainNameInIgnoreCaseAndStatus(names, EntityStatus.ENABLED.ordinal()).collectList().block();
		assertEquals(findDoms.size(), 0);
		
		findDoms = domRepo.findByDomainNameInIgnoreCaseAndStatus(names, EntityStatus.DISABLED.ordinal()).collectList().block();
		assertEquals(findDoms.size(), 0);
		
		findDoms = domRepo.findAll().collectList().block();
		assertEquals(findDoms.size(), 2);
		
		names.clear();
		names.add("health.baddomain.com");
		
		domRepo.findByDomainNameInIgnoreCase(names)
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
	}
	

	@Test 
	public void testDeleteDomain() 
	{
		Domain domain = new Domain();
		domain.setDomainName("health.newdomain.com");
		domain.setStatus(EntityStatus.NEW.ordinal());
		domRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		assertEquals(Long.valueOf(1), domRepo.count().block());
		
		domRepo.deleteByDomainNameIgnoreCase("health.testdomain.cOM")
		.as(StepVerifier::create) 
		.verifyComplete();
		
		assertEquals(Long.valueOf(1), domRepo.count().block());
		
		
		domRepo.deleteByDomainNameIgnoreCase("health.newdomain.com")
		.as(StepVerifier::create) 
		.verifyComplete();
		
		assertEquals(Long.valueOf(0), domRepo.count().block());
	}

	@Test
	public void testSearchDomain() 
	{
		Domain domain = new Domain();
		domain.setDomainName("health.newdomain.com");
		domain.setStatus(EntityStatus.NEW.ordinal());
		domRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		
		domain = new Domain();
		domain.setDomainName("healthy.domain.com");
		domain.setStatus(EntityStatus.NEW.ordinal());
		domRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		
		String name = "heaL";
		List<Domain> result = domRepo.findByDomainNameContainingIgnoreCase("%" + name.toUpperCase() + "%").collectList().block();
		assertEquals(2, result.size());
		
		name = "coM";
		result = domRepo.findByDomainNameContainingIgnoreCaseAndStatus("%" + name.toUpperCase() + "%", EntityStatus.NEW.ordinal()).collectList().block();
		assertEquals(2, result.size());
		
		name = "coM";
		result = domRepo.findByDomainNameContainingIgnoreCaseAndStatus("%" + name.toUpperCase() + "%", EntityStatus.DISABLED.ordinal()).collectList().block();
		assertEquals(0, result.size());
		
		log.debug("Exit");
	}

	

	@Test 
	public void testAddDomainsWithAddresses() 
	{
		
		Domain domain = new Domain();
		domain.setDomainName("health.newdomain.com");
		domain.setStatus(EntityStatus.NEW.ordinal());

		
		domRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Address addr = new Address(domain.getId(), "test1@health.newdomain.com", "Test1");
		addRepo.save(addr)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		addr = new Address(domain.getId(), "test2@health.newdomain.com", "Test2");
		addRepo.save(addr)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		addr = new Address(domain.getId(), "postmaster@health.newdomain.com", "Test3");
		addRepo.save(addr)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		List<Address> getAddrs = addRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(3, getAddrs.size());
		
		Address postmasterAddr = addRepo.findByEmailAddressIgnoreCase("postmaster@health.newDOMAIN.com").block();
		assertNotNull(postmasterAddr);
		
		Domain getDomain = domRepo.findByDomainNameIgnoreCase("health.newdomain.com").block();
		getDomain.setPostmasterAddressId(postmasterAddr.getId());
		domRepo.save(getDomain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		getDomain = domRepo.findByDomainNameIgnoreCase("health.newdomain.com").block();
		assertEquals(postmasterAddr.getId(), getDomain.getPostmasterAddressId());
		
	}
	
	@Test
	public void testDeleteDomainsWithAddresses() 
	{
		
		Domain domain = new Domain();
		domain.setDomainName("health.newdomain.com");
		domain.setStatus(EntityStatus.NEW.ordinal());
		
		domRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Address addr = new Address(domain.getId(), "test1@health.newdomain.com", "Test1");
		addRepo.save(addr)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		addr = new Address(domain.getId(), "test2@health.newdomain.com", "Test2");
		addRepo.save(addr)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		List<Address> getAddrs = addRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(2, getAddrs.size());
		
		// deleting the domain should cause as error due to addresses being tied to the domain
		Domain addedDomain = domRepo.findByDomainNameIgnoreCase("health.newdomain.com").block();
		boolean exceptionOccured = false;
		try
		{
			domRepo.deleteByDomainNameIgnoreCase("health.newdomain.COM").block();
		}
		catch (DataIntegrityViolationException e)
		{
			exceptionOccured = true;
		}
		
		assertTrue(exceptionOccured);
		
		// Make sure the domain really didn't get deleted
		addedDomain = domRepo.findByDomainNameIgnoreCase("health.newdomain.com").block();
		assertNotNull(addedDomain);
		
		this.addRepo.deleteByDomainId(addedDomain.getId()).block();
		
		getAddrs = addRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(0, getAddrs.size());
		
		// now try to delete the domain again... should work this time
		domRepo.deleteByDomainNameIgnoreCase("health.newdomain.COM").block();
		addedDomain = domRepo.findByDomainNameIgnoreCase("health.newdomain.com").block();
		
		assertNull(addedDomain);
	}
}
