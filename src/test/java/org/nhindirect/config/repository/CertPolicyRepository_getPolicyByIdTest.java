package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicy;
import org.nhindirect.policy.PolicyLexicon;

import reactor.test.StepVerifier;

public class CertPolicyRepository_getPolicyByIdTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testGetPolicyById_emptyStore_assertNoPolicyReturned()
	{
		polRepo.findById(1234L)
		.as(StepVerifier::create) 
		.expectNextCount(0) 
		.verifyComplete();
	}
	
	@Test
	public void testGetPolicyById_singlePolicyInStore_idNotInStore_assertNoPolicyReturned()
	{
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test Policy");
		policy.setLexicon(PolicyLexicon.XML.ordinal());
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		polRepo.findById(1234L)
		.as(StepVerifier::create) 
		.expectNextCount(0) 
		.verifyComplete();
	}
	
	@Test
	public void testGetPolicyById_singlePolicyInStore_assertPolicyReturned()
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
		
		CertPolicy addedPolicy = polRepo.findById(policy.getId()).block();
		
		assertEquals(policy.getPolicyName(), addedPolicy.getPolicyName());	
		assertEquals(policy.getLexicon(), addedPolicy.getLexicon());
		assertTrue(now.compareTo(addedPolicy.getCreateTime()) <= 0);
		assertTrue(Arrays.equals(policy.getPolicyData(), addedPolicy.getPolicyData()));
	}	
}
