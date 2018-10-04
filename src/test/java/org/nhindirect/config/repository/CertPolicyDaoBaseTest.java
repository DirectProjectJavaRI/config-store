package org.nhindirect.config.repository;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.nhindirect.common.crypto.CryptoExtensions;
import org.nhindirect.config.SpringBaseTest;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CertPolicyDaoBaseTest extends SpringBaseTest
{
	@Autowired
	protected CertPolicyRepository polRepo;	
	
	@Autowired
	protected DomainRepository dmRepo;		
	
	@Autowired
	protected CertPolicyGroupRepository groupRepo;
	
	@Autowired 
	protected CertPolicyGroupDomainReltnRepository reltnRepo;
	
	static
	{
		CryptoExtensions.registerJCEProviders();
	}		
	
	
	@Before
	public void setUp()
	{
		clearDomains();
		
		clearPolicies();
	}

	@After
	public void tearDown()
	{
		clearDomains();
		
		clearPolicies();
	}
	
	protected void clearPolicies()
	{
		groupRepo.deleteAll();
		
		assertTrue(groupRepo.findAll().isEmpty());
		
		polRepo.deleteAll();
		
		assertTrue(polRepo.findAll().isEmpty());

	}
	
	protected void clearDomains()
	{
		reltnRepo.deleteAll();
		
		assertTrue(reltnRepo.findAll().isEmpty());
		
		dmRepo.deleteAll();
		
		assertTrue(dmRepo.findAll().isEmpty());
	}
}
