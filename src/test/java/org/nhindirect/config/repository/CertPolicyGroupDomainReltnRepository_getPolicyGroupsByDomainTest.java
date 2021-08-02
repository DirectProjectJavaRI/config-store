package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;

import org.nhindirect.config.store.CertPolicyGroup;
import org.nhindirect.config.store.CertPolicyGroupDomainReltn;
import org.nhindirect.config.store.Domain;

import reactor.test.StepVerifier;

public class CertPolicyGroupDomainReltnRepository_getPolicyGroupsByDomainTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testGetPolicyGroupsByDomain_associationsExist_assertPoliciesRetrieved()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		dmRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
				
		
		CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");
		groupRepo.save(group)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();		
		
		final CertPolicyGroupDomainReltn reltn = new CertPolicyGroupDomainReltn();
		reltn.setPolicyGroupId(group.getId());
		reltn.setDomainId(domain.getId());
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		final Collection<CertPolicyGroupDomainReltn> policies = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(1, policies.size());
	}
	
	@Test
	public void testGetPolicyGroupsByDomain_multipleAssociationsExist_assertPoliciesRetrieved()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		dmRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicyGroup group1 = new CertPolicyGroup();
		group1.setPolicyGroupName("Test Group1");
		groupRepo.save(group1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicyGroup group2 = new CertPolicyGroup();
		group2.setPolicyGroupName("Test Group2");
		groupRepo.save(group2)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicyGroupDomainReltn reltn = new CertPolicyGroupDomainReltn();
		reltn.setPolicyGroupId(group1.getId());
		reltn.setDomainId(domain.getId());
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		reltn = new CertPolicyGroupDomainReltn();
		reltn.setPolicyGroupId(group2.getId());
		reltn.setDomainId(domain.getId());
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		final Collection<CertPolicyGroupDomainReltn> policies = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(2, policies.size());
		
		Iterator<CertPolicyGroupDomainReltn> polIter = policies.iterator();
		assertEquals(group1.getId(), polIter.next().getPolicyGroupId());
		assertEquals(group2.getId(), polIter.next().getPolicyGroupId());
	}	
	
	@Test
	public void testGetPolicyGroupsByDomain_multipleAssociationsExist_oneToEachDomain_assertPoliciesRetrieved()
	{
		Domain domain1 = new Domain();
		domain1.setDomainName("Test Domain 1");
		dmRepo.save(domain1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Domain domain2 = new Domain();
		domain2.setDomainName("Test Domain 2");
		dmRepo.save(domain2)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicyGroup group1 = new CertPolicyGroup();
		group1.setPolicyGroupName("Test Group1");
		groupRepo.save(group1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicyGroup group2 = new CertPolicyGroup();
		group2.setPolicyGroupName("Test Group2");
		groupRepo.save(group2)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicyGroupDomainReltn reltn = new CertPolicyGroupDomainReltn();
		reltn.setPolicyGroupId(group1.getId());
		reltn.setDomainId(domain1.getId());
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		reltn = new CertPolicyGroupDomainReltn();
		reltn.setPolicyGroupId(group2.getId());
		reltn.setDomainId(domain2.getId());
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Collection<CertPolicyGroupDomainReltn> policies = reltnRepo.findByDomainId(domain1.getId()).collectList().block();
		assertEquals(1, policies.size());
		
		Iterator<CertPolicyGroupDomainReltn> polIter = policies.iterator();
		assertEquals(group1.getId(), polIter.next().getPolicyGroupId());
		
		policies = reltnRepo.findByDomainId(domain2.getId()).collectList().block();
		assertEquals(1, policies.size());
		
		polIter = policies.iterator();
		assertEquals(group2.getId(), polIter.next().getPolicyGroupId());

	}	

	@Test
	public void testGetPolicyGroupsByDomain_multipleAssociationsExist_policyToMultipeDomains_assertPoliciesRetrieved()
	{
		Domain domain1 = new Domain();
		domain1.setDomainName("Test Domain 1");
		dmRepo.save(domain1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Domain domain2 = new Domain();
		domain2.setDomainName("Test Domain 2");
		dmRepo.save(domain2)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicyGroup group1 = new CertPolicyGroup();
		group1.setPolicyGroupName("Test Group1");
		groupRepo.save(group1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicyGroupDomainReltn reltn = new CertPolicyGroupDomainReltn();
		reltn.setPolicyGroupId(group1.getId());
		reltn.setDomainId(domain1.getId());
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		reltn = new CertPolicyGroupDomainReltn();
		reltn.setPolicyGroupId(group1.getId());
		reltn.setDomainId(domain2.getId());
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Collection<CertPolicyGroupDomainReltn> policies = reltnRepo.findByDomainId(domain1.getId()).collectList().block();
		assertEquals(1, policies.size());
		
		Iterator<CertPolicyGroupDomainReltn> polIter = policies.iterator();
		assertEquals(group1.getId(), polIter.next().getPolicyGroupId());
		
		policies = reltnRepo.findByDomainId(domain2.getId()).collectList().block();
		assertEquals(1, policies.size());
		
		polIter = policies.iterator();
		assertEquals(group1.getId(), polIter.next().getPolicyGroupId());

	}
	

	@Test
	public void testGetPolicyGroupsByDomain_noPoliciesInDomain_assertPoliciesNotRetrieved()
	{
		Domain domain1 = new Domain();
		domain1.setDomainName("Test Domain 1");
		dmRepo.save(domain1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Collection<CertPolicyGroupDomainReltn> policies = reltnRepo.findByDomainId(domain1.getId()).collectList().block();
		assertEquals(0, policies.size());
	}	
}
