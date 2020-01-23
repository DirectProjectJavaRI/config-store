package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicy;
import org.nhindirect.policy.PolicyLexicon;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

@Transactional
public class CertPolicyRepository_deletePoliciesTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testDeletePolicies_singlePolicy_assertPolicyDeleted()
	{
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test Policy");
		policy.setLexicon(PolicyLexicon.XML.ordinal());
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Collection<CertPolicy> policies = polRepo.findAll().collectList().block();
		
		assertEquals(1, policies.size());
		
		polRepo.deleteById(policies.iterator().next().getId()).block();
		
		policies = polRepo.findAll().collectList().block();
		
		assertEquals(0, policies.size());
	}
	
	@Test
	public void testDeletePolicies_multiplePolicies_assertSinglePolicyDeleted()
	{
		// add policy 1
		final CertPolicy policy1 = new CertPolicy();
		policy1.setPolicyName("Test Policy1");
		policy1.setLexicon(PolicyLexicon.XML.ordinal());
		policy1.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		// add policy 2
		final CertPolicy policy2 = new CertPolicy();
		policy2.setPolicyName("Test Policy2");
		policy2.setLexicon(PolicyLexicon.JAVA_SER.ordinal());
		policy2.setPolicyData(new byte[] {4,5,6});
		
		polRepo.save(policy2)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Collection<CertPolicy> policies = polRepo.findAll().collectList().block();
		
		assertEquals(2, policies.size());
		
		polRepo.deleteByIdIn(Arrays.asList(policies.iterator().next().getId())).block();
		
		policies = polRepo.findAll().collectList().block();
		
		assertEquals(1, policies.size());
	}
	
	@Test
	public void testDeletePolicies_multiplePolicyes_assertAllPoliciesDeleted()
	{
		// add policy 1
		final CertPolicy policy1 = new CertPolicy();
		policy1.setPolicyName("Test Policy1");
		policy1.setLexicon(PolicyLexicon.XML.ordinal());
		policy1.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		// add policy 2
		final CertPolicy policy2 = new CertPolicy();
		policy2.setPolicyName("Test Policy2");
		policy2.setLexicon(PolicyLexicon.JAVA_SER.ordinal());
		policy2.setPolicyData(new byte[] {4,5,6});
		
		polRepo.save(policy2)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Collection<CertPolicy> policies = polRepo.findAll().collectList().block();
		
		assertEquals(2, policies.size());
		
		Iterator<CertPolicy> iter = policies.iterator();
		
		polRepo.deleteByIdIn(Arrays.asList(iter.next().getId(), iter.next().getId())).block();
		
		policies = polRepo.findAll().collectList().block();
		
		assertEquals(0, policies.size());
	}	
}
