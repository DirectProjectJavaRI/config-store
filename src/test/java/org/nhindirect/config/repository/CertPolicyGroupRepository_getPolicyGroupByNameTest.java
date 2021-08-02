package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import org.nhindirect.config.store.CertPolicyGroup;

import reactor.test.StepVerifier;

public class CertPolicyGroupRepository_getPolicyGroupByNameTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testGetPolicyGroupByName_emptyStore_assertNoPolicyReturned()
	{
		groupRepo.findByPolicyGroupNameIgnoreCase("Test Group")
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();

	}
	
	@Test
	public void testGetPolicyGroupByName_singlePolicyGroupInStore_nameNotInStore_assertNoPolicyGroupReturned()
	{
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		assertNull(groupRepo.findByPolicyGroupNameIgnoreCase("Test Group X").block());
	}
	
	@Test
	public void testGetPolicyGroupByName_singlePolicyGroupInStore_assertPolicyGroupReturned()
	{
		final LocalDateTime now = LocalDateTime.now();
		
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicyGroup addedGroup = groupRepo.findByPolicyGroupNameIgnoreCase("Test GrouP").block();
		
		assertEquals(group.getPolicyGroupName(), addedGroup.getPolicyGroupName());	
		assertTrue(now.compareTo(addedGroup.getCreateTime()) <= 0);
	}	
}
