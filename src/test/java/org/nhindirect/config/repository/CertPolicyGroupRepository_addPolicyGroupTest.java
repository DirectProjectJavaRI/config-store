package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collection;

import org.nhindirect.config.store.CertPolicyGroup;
import org.springframework.dao.DataIntegrityViolationException;

import reactor.test.StepVerifier;


public class CertPolicyGroupRepository_addPolicyGroupTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testAddPolicyGroup_addPolicyGroup_assertAdded()
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
	public void testAddPolicyGroup_addExistingPolicy_assertException()
	{
		
		Assertions.assertThrows(DataIntegrityViolationException.class, () ->
		{
			CertPolicyGroup group = new CertPolicyGroup();
			group.setPolicyGroupName("Test Group");
			
			groupRepo.save(group)
			.as(StepVerifier::create) 
			.expectNextCount(1) 
			.verifyComplete();
			
			Collection<CertPolicyGroup> groups = groupRepo.findAll().collectList().block();
			
			assertEquals(1, groups.size());
	
			group = new CertPolicyGroup();
			group.setPolicyGroupName("Test Group");
	
			groupRepo.save(group).block();
		});
	
	}	
}
