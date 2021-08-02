package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import org.nhindirect.config.store.CertPolicyGroup;

import reactor.test.StepVerifier;

public class CertPolicyGroupRepository_updateGroupAttributesTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testUpdateGroupAttributes_updateName_assertUpdated()
	{
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicyGroup addedGroup = groupRepo.findById(group.getId()).block();
		assertNotNull(addedGroup);
		
		group.setPolicyGroupName("Test Group 2");

		groupRepo.save(group)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicyGroup updatedGroup =  groupRepo.findById(group.getId()).block();
		
		assertEquals("Test Group 2", updatedGroup.getPolicyGroupName());	
	}
}
