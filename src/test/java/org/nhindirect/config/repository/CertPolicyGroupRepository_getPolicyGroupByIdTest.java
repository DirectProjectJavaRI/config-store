package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicyGroup;

import reactor.test.StepVerifier;

public class CertPolicyGroupRepository_getPolicyGroupByIdTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testGetPolicyGroupById_emptyStore_assertNoPolicyGroupReturned()
	{
		groupRepo.findById(1234L)
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();

	}
	
	@Test
	public void testGetPolicyGroupById_singlePolicyGroupInStore_idNotInStore_assertNoPolicyGroupReturned()
	{
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		groupRepo.findById(1234L)
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
	}
	
	@Test
	public void testGetPolicyGroupById_singlePolicyGroupInStore_assertPolicyGroupReturned()
	{
		final LocalDateTime now = LocalDateTime.now();
		
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicyGroup addedGroup = groupRepo.findById(group.getId()).block();
		
		assertEquals(group.getPolicyGroupName(), addedGroup.getPolicyGroupName());	
		assertTrue(now.compareTo(addedGroup.getCreateTime()) <= 0);
	}	
}
