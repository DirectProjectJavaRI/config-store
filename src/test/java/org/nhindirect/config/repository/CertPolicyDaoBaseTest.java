package org.nhindirect.config.repository;

import org.junit.After;
import org.junit.Before;
import org.nhindirect.common.crypto.CryptoExtensions;
import org.nhindirect.config.SpringBaseTest;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.test.StepVerifier;

public abstract class CertPolicyDaoBaseTest extends SpringBaseTest
{
	@Autowired
	protected CertPolicyRepository polRepo;	
	
	@Autowired
	protected DomainRepository dmRepo;		
	
	@Autowired
	protected CertPolicyGroupRepository groupRepo;
	
	@Autowired
	protected CertPolicyGroupReltnRepository groupReltRepo;
	
	@Autowired 
	protected CertPolicyGroupDomainReltnRepository reltnRepo;
	
	static
	{
		CryptoExtensions.registerJCEProviders();
	}		
	
	
	@Before
	public void setUp()
	{
		super.setUp();
		
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
		groupReltRepo.deleteAll().block();
		
		groupReltRepo.findAll()
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
		
		groupRepo.deleteAll().block();
		
		groupRepo.findAll()
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
		
		polRepo.deleteAll().block();
		
		polRepo.findAll()
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();

	}
	
	protected void clearDomains()
	{
		reltnRepo.deleteAll().block();
		
		reltnRepo.findAll()
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
		
		dmRepo.deleteAll().block();
		
		dmRepo.findAll()
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
	}
}
