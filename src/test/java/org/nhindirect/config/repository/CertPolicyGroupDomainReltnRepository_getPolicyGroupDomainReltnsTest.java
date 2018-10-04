package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicyGroup;
import org.nhindirect.config.store.CertPolicyGroupDomainReltn;
import org.nhindirect.config.store.Domain;

public class CertPolicyGroupDomainReltnRepository_getPolicyGroupDomainReltnsTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testGetPolicyGroupsDomainReltns_emptyReltnStore_assertNoReltnsRetrieved()
	{
		final Collection<CertPolicyGroupDomainReltn> groups = reltnRepo.findAll();
		
		assertTrue(groups.isEmpty());
	}
	
	@Test
	public void testGetPolicyGroupsDomainReltns_singleEntryInReltns_assertReltnRetrieved()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		domain = dmRepo.save(domain);
		
		CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");
		group = groupRepo.save(group);
		
		CertPolicyGroupDomainReltn reltn = new CertPolicyGroupDomainReltn();
		reltn.setCertPolicyGroup(group);
		reltn.setDomain(domain);
		
		reltnRepo.save(reltn);
		
		final Collection<CertPolicyGroupDomainReltn> reltns = reltnRepo.findAll();
		assertEquals(1, reltns.size());
		
		reltn = reltns.iterator().next();
		assertEquals(group.getPolicyGroupName(), reltn.getCertPolicyGroup().getPolicyGroupName());
		assertEquals(domain.getDomainName(), reltn.getDomain().getDomainName());
		
	}	
	
	@Test
	public void testGetPolicyGroupsDomainReltns_multipeEntriesReltns_assertReltnsRetrieved()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		domain = dmRepo.save(domain);
		
		CertPolicyGroup group1 = new CertPolicyGroup();
		group1.setPolicyGroupName("Test Group 1");
		
		CertPolicyGroup group2 = new CertPolicyGroup();
		group2.setPolicyGroupName("Test Group 2");
		
		group1 = groupRepo.save(group1);
		group2 = groupRepo.save(group2);
		
		CertPolicyGroupDomainReltn reltn = new CertPolicyGroupDomainReltn();
		reltn.setCertPolicyGroup(group1);
		reltn.setDomain(domain);
		
		reltnRepo.save(reltn);
		
		reltn = new CertPolicyGroupDomainReltn();
		reltn.setCertPolicyGroup(group2);
		reltn.setDomain(domain);
		
		reltnRepo.save(reltn);
		
		final Collection<CertPolicyGroupDomainReltn> reltns = reltnRepo.findAll();
		assertEquals(2, reltns.size());
		
		Iterator<CertPolicyGroupDomainReltn> iter = reltns.iterator();
		
		reltn = iter.next();
		assertEquals(group1.getPolicyGroupName(), reltn.getCertPolicyGroup().getPolicyGroupName());
		assertEquals(domain.getDomainName(), reltn.getDomain().getDomainName());
		
		reltn = iter.next();
		assertEquals(group2.getPolicyGroupName(), reltn.getCertPolicyGroup().getPolicyGroupName());
		assertEquals(domain.getDomainName(), reltn.getDomain().getDomainName());
	}
}
