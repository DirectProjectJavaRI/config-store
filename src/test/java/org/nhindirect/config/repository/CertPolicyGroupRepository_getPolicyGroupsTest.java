package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicyGroup;

public class CertPolicyGroupRepository_getPolicyGroupsTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testGetPolicies_emptyPolicyGroupStore_assertNoPolicGroupsRetrieved()
	{
		final Collection<CertPolicyGroup> groups = groupRepo.findAll();
		
		assertTrue(groups.isEmpty());
	}
	
	@Test
	public void testGetPolicies_singleEntryInPolicyGroupStore_assertPolicyGroupRetrieved()
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
		
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group);
		
		final Collection<CertPolicyGroup> groups = groupRepo.findAll();
		
		assertEquals(1, groups.size());
		
		CertPolicyGroup addedGroup = groups.iterator().next();
		
		assertEquals(group.getPolicyGroupName(), addedGroup.getPolicyGroupName());	
		assertTrue(now.getTimeInMillis() <= addedGroup.getCreateTime().getTimeInMillis());
	}	
	
	@Test
	public void testGetPolicies_multipeEntriesInPolicyGroupStore_assertPolicyGroupsRetrieved()
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
		
		// add policy group 1
		final CertPolicyGroup group1 = new CertPolicyGroup();
		group1.setPolicyGroupName("Test Group1");
		
		groupRepo.save(group1);
		
		// add policy 2
		final CertPolicyGroup group2 = new CertPolicyGroup();
		group2.setPolicyGroupName("Test Group2");
		
		groupRepo.save(group2);
		
		final Collection<CertPolicyGroup> groups = groupRepo.findAll();
		
		assertEquals(2, groups.size());
		
		Iterator<CertPolicyGroup> iter = groups.iterator();
		
		CertPolicyGroup addedGroup = iter.next();
		
		assertEquals(group1.getPolicyGroupName(), addedGroup.getPolicyGroupName());	
		assertTrue(now.getTimeInMillis() <= addedGroup.getCreateTime().getTimeInMillis());
		
		addedGroup = iter.next();
		
		assertEquals(group2.getPolicyGroupName(), addedGroup.getPolicyGroupName());	
		assertTrue(now.getTimeInMillis() <= addedGroup.getCreateTime().getTimeInMillis());
	}
}
