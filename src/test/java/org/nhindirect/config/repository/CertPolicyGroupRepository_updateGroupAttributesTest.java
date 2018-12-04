package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicyGroup;

public class CertPolicyGroupRepository_updateGroupAttributesTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testUpdateGroupAttributes_updateName_assertUpdated()
	{
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group);
		
		CertPolicyGroup addedGroup = groupRepo.findById(group.getId()).get();
		assertNotNull(addedGroup);
		
		group.setPolicyGroupName("Test Group 2");
		groupRepo.save(group);
		
		CertPolicyGroup updatedGroup =  groupRepo.findById(group.getId()).get();
		
		assertEquals("Test Group 2", updatedGroup.getPolicyGroupName());	
	}
}
