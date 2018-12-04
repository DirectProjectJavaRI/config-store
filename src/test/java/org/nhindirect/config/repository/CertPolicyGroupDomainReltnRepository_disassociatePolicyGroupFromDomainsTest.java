package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicyGroup;
import org.nhindirect.config.store.CertPolicyGroupDomainReltn;
import org.nhindirect.config.store.Domain;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CertPolicyGroupDomainReltnRepository_disassociatePolicyGroupFromDomainsTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testDisassociatePolicyGroupFromDomains_associateDomainAndPolicy_assertAssociationRemoved()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		domain = dmRepo.save(domain);
		
		CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");
		group = groupRepo.save(group);
		
		final CertPolicyGroupDomainReltn addreltn = new CertPolicyGroupDomainReltn();
		addreltn.setCertPolicyGroup(group);
		addreltn.setDomain(domain);
		
		reltnRepo.save(addreltn);
		
		Collection<CertPolicyGroupDomainReltn> reltn = reltnRepo.findByDomain(domain);
		assertEquals(1, reltn.size());
		
		reltnRepo.deleteByCertPolicyGroup(group);
		
		reltn = reltnRepo.findByDomain(domain);
		assertEquals(0, reltn.size());
	}
}
