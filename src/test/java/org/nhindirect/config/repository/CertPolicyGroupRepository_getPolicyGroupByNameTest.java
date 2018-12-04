package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicyGroup;

public class CertPolicyGroupRepository_getPolicyGroupByNameTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testGetPolicyGroupByName_emptyStore_assertNoPolicyReturned()
	{
		assertNull(groupRepo.findByPolicyGroupNameIgnoreCase("Test Group"));
	}
	
	@Test
	public void testGetPolicyGroupByName_singlePolicyGroupInStore_nameNotInStore_assertNoPolicyGroupReturned()
	{
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group);
		
		
		assertNull(groupRepo.findByPolicyGroupNameIgnoreCase("Test Group X"));
	}
	
	@Test
	public void testGetPolicyGroupByName_singlePolicyGroupInStore_assertPolicyGroupReturned()
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
		
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group);
		
		CertPolicyGroup addedGroup = groupRepo.findByPolicyGroupNameIgnoreCase("Test GrouP");
		
		assertEquals(group.getPolicyGroupName(), addedGroup.getPolicyGroupName());	
		assertTrue(now.getTimeInMillis() <= addedGroup.getCreateTime().getTimeInMillis());
	}	
}
