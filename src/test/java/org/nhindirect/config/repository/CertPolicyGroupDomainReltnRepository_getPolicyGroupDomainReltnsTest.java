package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;

import org.nhindirect.config.store.CertPolicyGroup;
import org.nhindirect.config.store.CertPolicyGroupDomainReltn;
import org.nhindirect.config.store.Domain;

import reactor.test.StepVerifier;

public class CertPolicyGroupDomainReltnRepository_getPolicyGroupDomainReltnsTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testGetPolicyGroupsDomainReltns_emptyReltnStore_assertNoReltnsRetrieved()
	{
		reltnRepo.findAll()
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
	}
	
	@Test
	public void testGetPolicyGroupsDomainReltns_singleEntryInReltns_assertReltnRetrieved()
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
		
		CertPolicyGroupDomainReltn reltn = new CertPolicyGroupDomainReltn();
		reltn.setPolicyGroupId(group.getId());
		reltn.setDomainId(domain.getId());
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		final Collection<CertPolicyGroupDomainReltn> reltns = reltnRepo.findAll().collectList().block();
		assertEquals(1, reltns.size());
		
		reltn = reltns.iterator().next();
		assertEquals(group.getId(), reltn.getPolicyGroupId());
		assertEquals(domain.getId(), reltn.getDomainId());
		
	}	
	
	@Test
	public void testGetPolicyGroupsDomainReltns_multipeEntriesReltns_assertReltnsRetrieved()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		dmRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicyGroup group1 = new CertPolicyGroup();
		group1.setPolicyGroupName("Test Group 1");
		
		CertPolicyGroup group2 = new CertPolicyGroup();
		group2.setPolicyGroupName("Test Group 2");
		
		groupRepo.save(group1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
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
		
		final Collection<CertPolicyGroupDomainReltn> reltns = reltnRepo.findAll().collectList().block();
		assertEquals(2, reltns.size());
		
		Iterator<CertPolicyGroupDomainReltn> iter = reltns.iterator();
		
		reltn = iter.next();
		assertEquals(group1.getId(), reltn.getPolicyGroupId());
		assertEquals(domain.getId(), reltn.getDomainId());
		
		reltn = iter.next();
		assertEquals(group2.getId(), reltn.getPolicyGroupId());
		assertEquals(domain.getId(), reltn.getDomainId());
	}
}
