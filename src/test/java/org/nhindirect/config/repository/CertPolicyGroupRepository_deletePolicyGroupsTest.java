package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicyGroup;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

@Transactional
public class CertPolicyGroupRepository_deletePolicyGroupsTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testDeletePolicyGroups_singlePolicyGroup_assertPolicyGroupDeleted()
	{
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Collection<CertPolicyGroup> groups = groupRepo.findAll().collectList().block();
		
		assertEquals(1, groups.size());
		
		groupRepo.deleteById(groups.iterator().next().getId()).block();
		
		groups = groupRepo.findAll().collectList().block();
		
		assertEquals(0, groups.size());
	}
	
	@Test
	public void testDeletePolicyGroups_multiplePolicyGroups_assertSinglePolicyGroupDeleted()
	{
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
		
		Collection<CertPolicyGroup> groups = groupRepo.findAll().collectList().block();
		
		assertEquals(2, groups.size());
		
		groupRepo.deleteById(groups.iterator().next().getId()).block();
		
		groups = groupRepo.findAll().collectList().block();
		
		assertEquals(1, groups.size());
	}
	
	@Test
	public void testDeletePolicyGroups_multiplePolicyGroups_assertAllPolicyGroupsDeleted()
	{
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
		
		Collection<CertPolicyGroup> groups = groupRepo.findAll().collectList().block();
		
		assertEquals(2, groups.size());
		
		Iterator<CertPolicyGroup> iter = groups.iterator();
		
		groupRepo.deleteByIdIn(Arrays.asList(iter.next().getId(), iter.next().getId())).block();
		
		groups = groupRepo.findAll().collectList().block();
		
		assertEquals(0, groups.size());
	}	
}
