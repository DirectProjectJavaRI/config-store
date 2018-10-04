package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicy;
import org.nhindirect.policy.PolicyLexicon;
import org.springframework.dao.DataIntegrityViolationException;

public class CertPolicyRepository_addPolicyTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testAddPolicy_addPolicy_assertAdded()
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
		
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test PolicY");
		policy.setLexicon(PolicyLexicon.XML);
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy);
		
		final Collection<CertPolicy> policies = polRepo.findAll();
		
		assertEquals(1, policies.size());
		
		CertPolicy addedPolicy = policies.iterator().next();
		
		assertEquals(policy.getPolicyName(), addedPolicy.getPolicyName());	
		assertEquals(policy.getLexicon(), addedPolicy.getLexicon());
		assertTrue(now.getTimeInMillis() <= addedPolicy.getCreateTime().getTimeInMillis());
		assertTrue(Arrays.equals(policy.getPolicyData(), addedPolicy.getPolicyData()));
	}
	
	
	@Test(expected=DataIntegrityViolationException.class)
	public void testAddPolicy_addExistingPolicy_assertException()
	{
		
		CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test PolicY");
		policy.setLexicon(PolicyLexicon.XML);
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy);
		
		Collection<CertPolicy> policies = polRepo.findAll();
		
		assertEquals(1, policies.size());
		
		
		policy = new CertPolicy();
		policy.setPolicyName("Test PolicY");
		policy.setLexicon(PolicyLexicon.XML);
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy);
	
	}	
}
