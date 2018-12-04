package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicy;
import org.nhindirect.policy.PolicyLexicon;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CertPolicyRepository_deletePoliciesTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testDeletePolicies_singlePolicy_assertPolicyDeleted()
	{
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test Policy");
		policy.setLexicon(PolicyLexicon.XML);
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy);
		
		Collection<CertPolicy> policies = polRepo.findAll();
		
		assertEquals(1, policies.size());
		
		polRepo.deleteById(policies.iterator().next().getId());
		
		policies = polRepo.findAll();
		
		assertEquals(0, policies.size());
	}
	
	@Test
	public void testDeletePolicies_multiplePolicies_assertSinglePolicyDeleted()
	{
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
		
		Collection<CertPolicy> policies = polRepo.findAll();
		
		assertEquals(2, policies.size());
		
		polRepo.deleteByIdIn(Arrays.asList(policies.iterator().next().getId()));
		
		policies = polRepo.findAll();
		
		assertEquals(1, policies.size());
	}
	
	@Test
	public void testDeletePolicies_multiplePolicyes_assertAllPoliciesDeleted()
	{
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
		
		Collection<CertPolicy> policies = polRepo.findAll();
		
		assertEquals(2, policies.size());
		
		Iterator<CertPolicy> iter = policies.iterator();
		
		polRepo.deleteByIdIn(Arrays.asList(iter.next().getId(), iter.next().getId()));
		
		policies = polRepo.findAll();
		
		assertEquals(0, policies.size());
	}	
}
