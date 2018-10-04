package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;


import org.junit.Test;
import org.nhindirect.config.store.CertPolicy;
import org.nhindirect.policy.PolicyLexicon;

public class CertPolicyRepository_getPoliciesTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testGetPolicies_emptyPolicyStore_assertNoPoliciesRetrieved()
	{
		final Collection<CertPolicy> policies = polRepo.findAll();
		
		assertTrue(policies.isEmpty());
	}
	
	@Test
	public void testGetPolicies_singleEntryInPolicyStore_assertPoliciesRetrieved()
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
		
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test Policy");
		policy.setLexicon(PolicyLexicon.XML);
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy);
		
		final Collection<CertPolicy> policies = polRepo.findAll();
		
		assertEquals(1, policies.size());
		
		CertPolicy addedPolicy = policies.iterator().next();
		
		assertEquals("Test Policy", addedPolicy.getPolicyName());	
		assertEquals(PolicyLexicon.XML, addedPolicy.getLexicon());
		assertTrue(now.getTimeInMillis() <= addedPolicy.getCreateTime().getTimeInMillis());
		assertTrue(Arrays.equals(new byte[] {1,2,3}, addedPolicy.getPolicyData()));
	}	
	
	@Test
	public void testGetPolicies_multipeEntriesInPolicyStore_assertPoliciesRetrieved()
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
		
		// add policy 1
		final CertPolicy policy1 = new CertPolicy();
		policy1.setPolicyName("Test Policy1");
		policy1.setLexicon(PolicyLexicon.XML);
		policy1.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy1);
		
		// add policy 2
		final CertPolicy policy2 = new CertPolicy();
		policy2.setPolicyName("Test Policy2");
		policy2.setLexicon(PolicyLexicon.JAVA_SER);
		policy2.setPolicyData(new byte[] {4,5,6});
		
		polRepo.save(policy2);
		
		final Collection<CertPolicy> policies = polRepo.findAll();
		
		assertEquals(2, policies.size());
		
		Iterator<CertPolicy> iter = policies.iterator();
		
		CertPolicy addedPolicy = iter.next();
		
		assertEquals(policy1.getPolicyName(), addedPolicy.getPolicyName());	
		assertEquals(policy1.getLexicon(), addedPolicy.getLexicon());
		assertTrue(now.getTimeInMillis() <= addedPolicy.getCreateTime().getTimeInMillis());
		assertTrue(Arrays.equals(policy1.getPolicyData(), addedPolicy.getPolicyData()));
		
		addedPolicy = iter.next();
		
		assertEquals(policy2.getPolicyName(), addedPolicy.getPolicyName());	
		assertEquals(policy2.getLexicon(), addedPolicy.getLexicon());
		assertTrue(now.getTimeInMillis() <= addedPolicy.getCreateTime().getTimeInMillis());
		assertTrue(Arrays.equals(policy2.getPolicyData(), addedPolicy.getPolicyData()));
	}
}
