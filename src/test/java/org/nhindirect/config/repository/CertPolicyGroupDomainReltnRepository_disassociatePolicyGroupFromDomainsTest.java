package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


import java.util.Collection;

import org.nhindirect.config.store.CertPolicyGroup;
import org.nhindirect.config.store.CertPolicyGroupDomainReltn;
import org.nhindirect.config.store.Domain;

import reactor.test.StepVerifier;

public class CertPolicyGroupDomainReltnRepository_disassociatePolicyGroupFromDomainsTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testDisassociatePolicyGroupFromDomains_associateDomainAndPolicy_assertAssociationRemoved()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		dmRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicyGroup group = new CertPolicyGroup();
		group.setPolicyGroupName("Test Group");
		groupRepo.save(group)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		final CertPolicyGroupDomainReltn addreltn = new CertPolicyGroupDomainReltn();
		addreltn.setPolicyGroupId(group.getId());
		addreltn.setDomainId(domain.getId());
		
		reltnRepo.save(addreltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Collection<CertPolicyGroupDomainReltn> reltn = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(1, reltn.size());
		
		reltnRepo.deleteByPolicyGroupId(group.getId()).block();
		
		reltn = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(0, reltn.size());
	}
}
