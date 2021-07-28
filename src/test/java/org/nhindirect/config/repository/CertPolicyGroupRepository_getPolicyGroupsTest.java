package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;

import org.nhindirect.config.store.CertPolicyGroup;

import reactor.test.StepVerifier;

public class CertPolicyGroupRepository_getPolicyGroupsTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testGetPolicies_emptyPolicyGroupStore_assertNoPolicGroupsRetrieved()
	{
		groupRepo.findAll()
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
	}
	
	@Test
	public void testGetPolicies_singleEntryInPolicyGroupStore_assertPolicyGroupRetrieved()
	{
		final LocalDateTime now = LocalDateTime.now();
		
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		final Collection<CertPolicyGroup> groups = groupRepo.findAll().collectList().block();
		
		assertEquals(1, groups.size());
		
		CertPolicyGroup addedGroup = groups.iterator().next();
		
		assertEquals(group.getPolicyGroupName(), addedGroup.getPolicyGroupName());	
		assertTrue(now.compareTo(addedGroup.getCreateTime()) <= 0);
	}	
	
	@Test
	public void testGetPolicies_multipeEntriesInPolicyGroupStore_assertPolicyGroupsRetrieved()
	{
		final LocalDateTime now = LocalDateTime.now();
		
		// add policy group 1
		final CertPolicyGroup group1 = new CertPolicyGroup();
		group1.setPolicyGroupName("Test Group1");
		
		groupRepo.save(group1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		// add policy 2
		final CertPolicyGroup group2 = new CertPolicyGroup();
		group2.setPolicyGroupName("Test Group2");
		
		groupRepo.save(group2)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		final Collection<CertPolicyGroup> groups = groupRepo.findAll().collectList().block();
		
		assertEquals(2, groups.size());
		
		Iterator<CertPolicyGroup> iter = groups.iterator();
		
		CertPolicyGroup addedGroup = iter.next();
		
		assertEquals(group1.getPolicyGroupName(), addedGroup.getPolicyGroupName());	
		assertTrue(now.compareTo(addedGroup.getCreateTime()) <= 0);
		
		addedGroup = iter.next();
		
		assertEquals(group2.getPolicyGroupName(), addedGroup.getPolicyGroupName());	
		assertTrue(now.compareTo(addedGroup.getCreateTime()) <= 0);
	}
}
