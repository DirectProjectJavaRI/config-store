package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicyGroup;
import org.nhindirect.config.store.CertPolicyGroupDomainReltn;
import org.nhindirect.config.store.Domain;

public class CertPolicyGroupDomainReltnRepository_associatePolicyGroupToDomainTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testAssociatePolicyGroupToDomain_associateDomainAndGroup_assertAssociationAdded()
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
		
		final Collection<CertPolicyGroupDomainReltn> groupReltn = reltnRepo.findByDomain(domain);
		assertEquals(1, groupReltn.size());
		reltn = groupReltn.iterator().next();
		assertEquals(group.getPolicyGroupName(), reltn.getCertPolicyGroup().getPolicyGroupName());
		assertEquals(domain.getDomainName(), reltn.getDomain().getDomainName());
	}
}
