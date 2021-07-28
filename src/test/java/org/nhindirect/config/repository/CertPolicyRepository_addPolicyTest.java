package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import org.nhindirect.config.store.CertPolicy;
import org.nhindirect.policy.PolicyLexicon;
import org.springframework.dao.DataIntegrityViolationException;

import reactor.test.StepVerifier;

public class CertPolicyRepository_addPolicyTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testAddPolicy_addPolicy_assertAdded()
	{
		final LocalDateTime now = LocalDateTime.now();
		
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test PolicY");
		policy.setLexicon(PolicyLexicon.XML.ordinal());
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		final Collection<CertPolicy> policies = polRepo.findAll().collectList().block();
		
		assertEquals(1, policies.size());
		
		CertPolicy addedPolicy = policies.iterator().next();
		
		assertEquals(policy.getPolicyName(), addedPolicy.getPolicyName());	
		assertEquals(policy.getLexicon(), addedPolicy.getLexicon());
		assertTrue(now.compareTo(addedPolicy.getCreateTime()) <= 0);
		assertTrue(Arrays.equals(policy.getPolicyData(), addedPolicy.getPolicyData()));
	}
	
	
	@Test
	public void testAddPolicy_addExistingPolicy_assertException()
	{
		Assertions.assertThrows(DataIntegrityViolationException.class, () ->
		{
			CertPolicy policy = new CertPolicy();
			policy.setPolicyName("Test PolicY");
			policy.setLexicon(PolicyLexicon.XML.ordinal());
			policy.setPolicyData(new byte[] {1,2,3});
			
			polRepo.save(policy)
			.as(StepVerifier::create) 
			.expectNextCount(1) 
			.verifyComplete();
			
			Collection<CertPolicy> policies = polRepo.findAll().collectList().block();
			
			assertEquals(1, policies.size());
			
			
			policy = new CertPolicy();
			policy.setPolicyName("Test PolicY");
			policy.setLexicon(PolicyLexicon.XML.ordinal());
			policy.setPolicyData(new byte[] {1,2,3});
			
			polRepo.save(policy).block();
		});
	
	}	
}
