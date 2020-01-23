package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;


import org.junit.Test;
import org.nhindirect.config.store.CertPolicy;
import org.nhindirect.policy.PolicyLexicon;

import reactor.test.StepVerifier;

public class CertPolicyRepository_getPoliciesTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testGetPolicies_emptyPolicyStore_assertNoPoliciesRetrieved()
	{		
		polRepo.findAll()
		.as(StepVerifier::create) 
		.expectNextCount(0) 
		.verifyComplete();
	}
	
	@Test
	public void testGetPolicies_singleEntryInPolicyStore_assertPoliciesRetrieved()
	{
		final LocalDateTime now = LocalDateTime.now();
		
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test Policy");
		policy.setLexicon(PolicyLexicon.XML.ordinal());
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		final Collection<CertPolicy> policies = polRepo.findAll().collectList().block();
		
		assertEquals(1, policies.size());
		
		CertPolicy addedPolicy = policies.iterator().next();
		
		assertEquals("Test Policy", addedPolicy.getPolicyName());	
		assertEquals(PolicyLexicon.XML.ordinal(), addedPolicy.getLexicon());
		assertTrue(now.compareTo(addedPolicy.getCreateTime()) <= 0);
		assertTrue(Arrays.equals(new byte[] {1,2,3}, addedPolicy.getPolicyData()));
	}	
	
	@Test
	public void testGetPolicies_multipeEntriesInPolicyStore_assertPoliciesRetrieved()
	{
		final LocalDateTime now = LocalDateTime.now();
		
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
		
		final Collection<CertPolicy> policies = polRepo.findAll().collectList().block();
		
		assertEquals(2, policies.size());
		
		Iterator<CertPolicy> iter = policies.iterator();
		
		CertPolicy addedPolicy = iter.next();
		
		assertEquals(policy1.getPolicyName(), addedPolicy.getPolicyName());	
		assertEquals(policy1.getLexicon(), addedPolicy.getLexicon());
		assertTrue(now.compareTo(addedPolicy.getCreateTime()) <= 0);
		assertTrue(Arrays.equals(policy1.getPolicyData(), addedPolicy.getPolicyData()));
		
		addedPolicy = iter.next();
		
		assertEquals(policy2.getPolicyName(), addedPolicy.getPolicyName());	
		assertEquals(policy2.getLexicon(), addedPolicy.getLexicon());
		assertTrue(now.compareTo(addedPolicy.getCreateTime()) <= 0);
		assertTrue(Arrays.equals(policy2.getPolicyData(), addedPolicy.getPolicyData()));
	}
}
