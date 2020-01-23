package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicyGroup;
import org.nhindirect.config.store.CertPolicyGroupDomainReltn;
import org.nhindirect.config.store.Domain;

import reactor.test.StepVerifier;

public class CertPolicyGroupDomainReltnRepository_associatePolicyGroupToDomainTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testAssociatePolicyGroupToDomain_associateDomainAndGroup_assertAssociationAdded()
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
		
		CertPolicyGroupDomainReltn reltn = new CertPolicyGroupDomainReltn();
		reltn.setCertPolicyGroupId(group.getId());
		reltn.setDomainId(domain.getId());
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		final Collection<CertPolicyGroupDomainReltn> groupReltn = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(1, groupReltn.size());
		reltn = groupReltn.iterator().next();
		assertEquals(group.getId(), reltn.getCertPolicyGroupId());
		assertEquals(domain.getId(), reltn.getDomainId());
	}
}
