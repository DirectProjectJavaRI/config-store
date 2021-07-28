package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import org.nhindirect.config.store.CertPolicyGroup;
import org.nhindirect.config.store.CertPolicyGroupDomainReltn;
import org.nhindirect.config.store.Domain;

import reactor.test.StepVerifier;

public class CertPolicyGroupDomainReltnRepository_disassociatePolicyGroupsFromDomainTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testDisassociatePolicyGroupsFromDomain_associateDomainAndPolicy_assertAssociationRemoved()
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
		
		final CertPolicyGroupDomainReltn addReltn = new CertPolicyGroupDomainReltn();
		addReltn.setPolicyGroupId(group.getId());
		addReltn.setDomainId(domain.getId());
		
		
		reltnRepo.save(addReltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Collection<CertPolicyGroupDomainReltn> reltn = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(1, reltn.size());
		
		reltnRepo.deleteByDomainId(domain.getId()).block();
		
		reltn = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(0, reltn.size());
	}
}
