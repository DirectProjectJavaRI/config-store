package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicyGroup;
import org.nhindirect.config.store.CertPolicyGroupDomainReltn;
import org.nhindirect.config.store.Domain;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CertPolicyGroupDomainReltnRepository_disassociatePolicyGroupsFromDomainTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testDisassociatePolicyGroupsFromDomain_associateDomainAndPolicy_assertAssociationRemoved()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		domain = dmRepo.save(domain);
		
		CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");
		group = groupRepo.save(group);
		
		final CertPolicyGroupDomainReltn addReltn = new CertPolicyGroupDomainReltn();
		addReltn.setCertPolicyGroup(group);
		addReltn.setDomain(domain);
		
		reltnRepo.save(addReltn);
		
		Collection<CertPolicyGroupDomainReltn> reltn = reltnRepo.findByDomain(domain);
		assertEquals(1, reltn.size());
		
		reltnRepo.deleteByDomain(domain);
		
		reltn = reltnRepo.findByDomain(domain);
		assertEquals(0, reltn.size());
	}
}
