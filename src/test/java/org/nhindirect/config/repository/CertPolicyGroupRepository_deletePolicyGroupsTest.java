package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicyGroup;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CertPolicyGroupRepository_deletePolicyGroupsTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testDeletePolicyGroups_singlePolicyGroup_assertPolicyGroupDeleted()
	{
		final CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");

		groupRepo.save(group);
		
		Collection<CertPolicyGroup> groups = groupRepo.findAll();
		
		assertEquals(1, groups.size());
		
		groupRepo.deleteById(groups.iterator().next().getId());
		
		groups = groupRepo.findAll();
		
		assertEquals(0, groups.size());
	}
	
	@Test
	public void testDeletePolicyGroups_multiplePolicyGroups_assertSinglePolicyGroupDeleted()
	{
		// add policy group 1
		final CertPolicyGroup group1 = new CertPolicyGroup();
		group1.setPolicyGroupName("Test Group1");
		
		groupRepo.save(group1);
		
		// add policy 2
		final CertPolicyGroup group2 = new CertPolicyGroup();
		group2.setPolicyGroupName("Test Group2");
		
		groupRepo.save(group2);
		
		Collection<CertPolicyGroup> groups = groupRepo.findAll();
		
		assertEquals(2, groups.size());
		
		groupRepo.deleteById(groups.iterator().next().getId());
		
		groups = groupRepo.findAll();
		
		assertEquals(1, groups.size());
	}
	
	@Test
	public void testDeletePolicyGroups_multiplePolicyGroups_assertAllPolicyGroupsDeleted()
	{
		// add policy group 1
		final CertPolicyGroup group1 = new CertPolicyGroup();
		group1.setPolicyGroupName("Test Group1");
		
		groupRepo.save(group1);
		
		// add policy 2
		final CertPolicyGroup group2 = new CertPolicyGroup();
		group2.setPolicyGroupName("Test Group2");
		
		groupRepo.save(group2);
		
		Collection<CertPolicyGroup> groups = groupRepo.findAll();
		
		assertEquals(2, groups.size());
		
		Iterator<CertPolicyGroup> iter = groups.iterator();
		
		groupRepo.deleteByIdIn(Arrays.asList(iter.next().getId(), iter.next().getId()));
		
		groups = groupRepo.findAll();
		
		assertEquals(0, groups.size());
	}	
}
