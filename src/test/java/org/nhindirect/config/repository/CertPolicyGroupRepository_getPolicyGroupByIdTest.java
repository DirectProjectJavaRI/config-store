package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicyGroup;

public class CertPolicyGroupRepository_getPolicyGroupByIdTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testGetPolicyGroupById_emptyStore_assertNoPolicyGroupReturned()
	{
		assertEquals(Optional.empty(), groupRepo.findById(1234L));
	}
	
	@Test
	public void testGetPolicyGroupById_singlePolicyGroupInStore_idNotInStore_assertNoPolicyGroupReturned()
	{
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group);		
		
		assertEquals(Optional.empty(), groupRepo.findById(1234L));
	}
	
	@Test
	public void testGetPolicyGroupById_singlePolicyGroupInStore_assertPolicyGroupReturned()
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
		
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group);
		
		CertPolicyGroup addedGroup = groupRepo.findById(group.getId()).get();
		
		assertEquals(group.getPolicyGroupName(), addedGroup.getPolicyGroupName());	
		assertTrue(now.getTimeInMillis() <= addedGroup.getCreateTime().getTimeInMillis());
	}	
}
