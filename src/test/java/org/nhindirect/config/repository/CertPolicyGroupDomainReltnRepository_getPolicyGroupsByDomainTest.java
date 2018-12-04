package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicyGroup;
import org.nhindirect.config.store.CertPolicyGroupDomainReltn;
import org.nhindirect.config.store.Domain;

public class CertPolicyGroupDomainReltnRepository_getPolicyGroupsByDomainTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testGetPolicyGroupsByDomain_associationsExist_assertPoliciesRetrieved()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		domain = dmRepo.save(domain);
		
		CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");
		group = groupRepo.save(group);
		
		final CertPolicyGroupDomainReltn reltn = new CertPolicyGroupDomainReltn();
		reltn.setCertPolicyGroup(group);
		reltn.setDomain(domain);
		
		reltnRepo.save(reltn);
		
		final Collection<CertPolicyGroupDomainReltn> policies = reltnRepo.findByDomain(domain);
		assertEquals(1, policies.size());
	}
	
	@Test
	public void testGetPolicyGroupsByDomain_multipleAssociationsExist_assertPoliciesRetrieved()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		domain = dmRepo.save(domain);
		
		CertPolicyGroup group1 = new CertPolicyGroup();
		group1.setPolicyGroupName("Test Group1");
		group1 = groupRepo.save(group1);
		
		CertPolicyGroup group2 = new CertPolicyGroup();
		group2.setPolicyGroupName("Test Group2");
		group2 = groupRepo.save(group2);
		
		CertPolicyGroupDomainReltn reltn = new CertPolicyGroupDomainReltn();
		reltn.setCertPolicyGroup(group1);
		reltn.setDomain(domain);
		
		reltnRepo.save(reltn);
		
		reltn = new CertPolicyGroupDomainReltn();
		reltn.setCertPolicyGroup(group2);
		reltn.setDomain(domain);
		
		reltnRepo.save(reltn);
		
		final Collection<CertPolicyGroupDomainReltn> policies = reltnRepo.findByDomain(domain);
		assertEquals(2, policies.size());
		
		Iterator<CertPolicyGroupDomainReltn> polIter = policies.iterator();
		assertEquals(group1.getPolicyGroupName(), polIter.next().getCertPolicyGroup().getPolicyGroupName());
		assertEquals(group2.getPolicyGroupName(), polIter.next().getCertPolicyGroup().getPolicyGroupName());
	}	
	
	@Test
	public void testGetPolicyGroupsByDomain_multipleAssociationsExist_oneToEachDomain_assertPoliciesRetrieved()
	{
		Domain domain1 = new Domain();
		domain1.setDomainName("Test Domain 1");
		domain1 = dmRepo.save(domain1);
		
		Domain domain2 = new Domain();
		domain2.setDomainName("Test Domain 2");
		domain2 = dmRepo.save(domain2);
		
		CertPolicyGroup group1 = new CertPolicyGroup();
		group1.setPolicyGroupName("Test Group1");
		group1 = groupRepo.save(group1);
		
		CertPolicyGroup group2 = new CertPolicyGroup();
		group2.setPolicyGroupName("Test Group2");
		group2 = groupRepo.save(group2);
		
		CertPolicyGroupDomainReltn reltn = new CertPolicyGroupDomainReltn();
		reltn.setCertPolicyGroup(group1);
		reltn.setDomain(domain1);
		
		reltnRepo.save(reltn);
		
		reltn = new CertPolicyGroupDomainReltn();
		reltn.setCertPolicyGroup(group2);
		reltn.setDomain(domain2);
		
		reltnRepo.save(reltn);
		
		Collection<CertPolicyGroupDomainReltn> policies = reltnRepo.findByDomain(domain1);
		assertEquals(1, policies.size());
		
		Iterator<CertPolicyGroupDomainReltn> polIter = policies.iterator();
		assertEquals(group1.getPolicyGroupName(), polIter.next().getCertPolicyGroup().getPolicyGroupName());
		
		policies = reltnRepo.findByDomain(domain2);
		assertEquals(1, policies.size());
		
		polIter = policies.iterator();
		assertEquals(group2.getPolicyGroupName(), polIter.next().getCertPolicyGroup().getPolicyGroupName());

	}	

	@Test
	public void testGetPolicyGroupsByDomain_multipleAssociationsExist_policyToMultipeDomains_assertPoliciesRetrieved()
	{
		Domain domain1 = new Domain();
		domain1.setDomainName("Test Domain 1");
		domain1 = dmRepo.save(domain1);
		
		Domain domain2 = new Domain();
		domain2.setDomainName("Test Domain 2");
		domain2 = dmRepo.save(domain2);
		
		CertPolicyGroup group1 = new CertPolicyGroup();
		group1.setPolicyGroupName("Test Group1");
		group1 = groupRepo.save(group1);
		
		CertPolicyGroupDomainReltn reltn = new CertPolicyGroupDomainReltn();
		reltn.setCertPolicyGroup(group1);
		reltn.setDomain(domain1);
		
		reltnRepo.save(reltn);
		
		reltn = new CertPolicyGroupDomainReltn();
		reltn.setCertPolicyGroup(group1);
		reltn.setDomain(domain2);
		
		reltnRepo.save(reltn);
		
		Collection<CertPolicyGroupDomainReltn> policies = reltnRepo.findByDomain(domain1);
		assertEquals(1, policies.size());
		
		Iterator<CertPolicyGroupDomainReltn> polIter = policies.iterator();
		assertEquals(group1.getPolicyGroupName(), polIter.next().getCertPolicyGroup().getPolicyGroupName());
		
		policies = reltnRepo.findByDomain(domain2);
		assertEquals(1, policies.size());
		
		polIter = policies.iterator();
		assertEquals(group1.getPolicyGroupName(), polIter.next().getCertPolicyGroup().getPolicyGroupName());

	}
	

	@Test
	public void testGetPolicyGroupsByDomain_noPoliciesInDomain_assertPoliciesNotRetrieved()
	{
		Domain domain1 = new Domain();
		domain1.setDomainName("Test Domain 1");
		domain1 = dmRepo.save(domain1);
		
		Collection<CertPolicyGroupDomainReltn> policies = reltnRepo.findByDomain(domain1);
		assertEquals(0, policies.size());
	}	
}
