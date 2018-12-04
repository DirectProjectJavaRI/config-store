package org.nhindirect.config.repository;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicy;
import org.nhindirect.policy.PolicyLexicon;

public class CertPolicyRepository_updatePolicyAttributesTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testUpdatePolicyAttributes_updateName_assertUpdated()
	{
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test PolicY");
		policy.setLexicon(PolicyLexicon.XML);
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy);
		
		CertPolicy addedPolicy = polRepo.findById(policy.getId()).get();
		assertNotNull(addedPolicy);
		
		addedPolicy.setPolicyName("Test Pol");
		polRepo.save(addedPolicy);
		
		CertPolicy updatedPolicy =  polRepo.findById(policy.getId()).get();
		
		assertEquals("Test Pol", updatedPolicy.getPolicyName());	
		assertEquals(policy.getLexicon(), updatedPolicy.getLexicon());
		assertTrue(Arrays.equals(policy.getPolicyData(), updatedPolicy.getPolicyData()));
	}
	
	@Test
	public void testUpdatePolicyAttributes_updateData_assertUpdated()
	{
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test PolicY");
		policy.setLexicon(PolicyLexicon.XML);
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy);
		
		CertPolicy addedPolicy = polRepo.findById(policy.getId()).get();
		assertNotNull(addedPolicy);
		
		
		addedPolicy.setPolicyData(new byte[] {4,5,6});
		polRepo.save(addedPolicy);
		
		CertPolicy updatedPolicy =  polRepo.findById(policy.getId()).get();
		
		assertEquals(policy.getPolicyName(), updatedPolicy.getPolicyName());	
		assertEquals(policy.getLexicon(), updatedPolicy.getLexicon());
		assertTrue(Arrays.equals(new byte[] {4,5,6}, updatedPolicy.getPolicyData()));
	}
	
	@Test
	public void testUpdatePolicyAttributes_updateLexicon_assertUpdated()
	{
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test PolicY");
		policy.setLexicon(PolicyLexicon.XML);
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy);
		
		CertPolicy addedPolicy = polRepo.findById(policy.getId()).get();
		assertNotNull(addedPolicy);
		
		addedPolicy.setLexicon(PolicyLexicon.JAVA_SER);
		polRepo.save(addedPolicy);
		
		CertPolicy updatedPolicy =  polRepo.findById(policy.getId()).get();
		
		assertEquals(policy.getPolicyName(), updatedPolicy.getPolicyName());	
		assertEquals(PolicyLexicon.JAVA_SER, updatedPolicy.getLexicon());
		assertTrue(Arrays.equals(policy.getPolicyData(), updatedPolicy.getPolicyData()));
	}

}
