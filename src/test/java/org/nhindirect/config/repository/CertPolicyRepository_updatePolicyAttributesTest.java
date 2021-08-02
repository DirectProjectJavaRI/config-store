package org.nhindirect.config.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import org.nhindirect.config.store.CertPolicy;
import org.nhindirect.policy.PolicyLexicon;

import reactor.test.StepVerifier;

public class CertPolicyRepository_updatePolicyAttributesTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testUpdatePolicyAttributes_updateName_assertUpdated()
	{
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test PolicY");
		policy.setLexicon(PolicyLexicon.XML.ordinal());
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicy addedPolicy = polRepo.findById(policy.getId()).block();
		assertNotNull(addedPolicy);
		
		addedPolicy.setPolicyName("Test Pol");
		polRepo.save(addedPolicy)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicy updatedPolicy =  polRepo.findById(policy.getId()).block();
		
		assertEquals("Test Pol", updatedPolicy.getPolicyName());	
		assertEquals(policy.getLexicon(), updatedPolicy.getLexicon());
		assertTrue(Arrays.equals(policy.getPolicyData(), updatedPolicy.getPolicyData()));
	}
	
	@Test
	public void testUpdatePolicyAttributes_updateData_assertUpdated()
	{
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test PolicY");
		policy.setLexicon(PolicyLexicon.XML.ordinal());
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicy addedPolicy = polRepo.findById(policy.getId()).block();
		assertNotNull(addedPolicy);
		
		
		addedPolicy.setPolicyData(new byte[] {4,5,6});
		polRepo.save(addedPolicy)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicy updatedPolicy =  polRepo.findById(policy.getId()).block();
		
		assertEquals(policy.getPolicyName(), updatedPolicy.getPolicyName());	
		assertEquals(policy.getLexicon(), updatedPolicy.getLexicon());
		assertTrue(Arrays.equals(new byte[] {4,5,6}, updatedPolicy.getPolicyData()));
	}
	
	@Test
	public void testUpdatePolicyAttributes_updateLexicon_assertUpdated()
	{
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test PolicY");
		policy.setLexicon(PolicyLexicon.XML.ordinal());
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicy addedPolicy = polRepo.findById(policy.getId()).block();
		assertNotNull(addedPolicy);
		
		addedPolicy.setLexicon(PolicyLexicon.JAVA_SER.ordinal());
		polRepo.save(addedPolicy)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		CertPolicy updatedPolicy =  polRepo.findById(policy.getId()).block();
		
		assertEquals(policy.getPolicyName(), updatedPolicy.getPolicyName());	
		assertEquals(PolicyLexicon.JAVA_SER.ordinal(), updatedPolicy.getLexicon());
		assertTrue(Arrays.equals(policy.getPolicyData(), updatedPolicy.getPolicyData()));
	}

}
