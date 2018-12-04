package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicy;
import org.nhindirect.policy.PolicyLexicon;

public class CertPolicyRepository_getPolicyByIdTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testGetPolicyById_emptyStore_assertNoPolicyReturned()
	{
		assertEquals(Optional.empty(), polRepo.findById(1234L));
	}
	
	@Test
	public void testGetPolicyById_singlePolicyInStore_idNotInStore_assertNoPolicyReturned()
	{
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test Policy");
		policy.setLexicon(PolicyLexicon.XML);
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy);
		
		
		assertEquals(Optional.empty(), polRepo.findById(1234L));
	}
	
	@Test
	public void testGetPolicyById_singlePolicyInStore_assertPolicyReturned()
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
		
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test PolicY");
		policy.setLexicon(PolicyLexicon.XML);
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy);
		
		CertPolicy addedPolicy = polRepo.findById(policy.getId()).get();
		
		assertEquals(policy.getPolicyName(), addedPolicy.getPolicyName());	
		assertEquals(policy.getLexicon(), addedPolicy.getLexicon());
		assertTrue(now.getTimeInMillis() <= addedPolicy.getCreateTime().getTimeInMillis());
		assertTrue(Arrays.equals(policy.getPolicyData(), addedPolicy.getPolicyData()));
	}	
}
