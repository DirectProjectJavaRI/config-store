package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicyGroup;
import org.springframework.dao.DataIntegrityViolationException;


public class CertPolicyGroupRepository_addPolicyGroupTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testAddPolicyGroup_addPolicyGroup_assertAdded()
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
	
	
	@Test(expected=DataIntegrityViolationException.class)
	public void testAddPolicyGroup_addExistingPolicy_assertException()
	{
		
		CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");
		
		groupRepo.save(group);
		
		Collection<CertPolicyGroup> groups = groupRepo.findAll();
		
		assertEquals(1, groups.size());

		group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group);
	
	}	
}
