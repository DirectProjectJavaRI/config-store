package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;
import org.nhindirect.config.store.CertPolicy;
import org.nhindirect.policy.PolicyLexicon;

public class CertPolicyRepository_getPolicyByNameTest extends CertPolicyDaoBaseTest
{
	@Test
	public void testGetPolicyByName_emptyStore_assertNoPolicyReturned()
	{
		assertNull(polRepo.findByPolicyNameIgnoreCase("Test Policy"));
	}
	
	@Test
	public void testGetPolicyByName_singlePolicyInStore_nameNotInStore_assertNoPolicyReturned()
	{
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test Policy");
		policy.setLexicon(PolicyLexicon.XML);
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy);
		
		
		assertNull(polRepo.findByPolicyNameIgnoreCase("Test Policy X"));
	}
	
	@Test
	public void testGetPolicyByName_singlePolicyInStore_assertPolicyReturned()
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
		
		final CertPolicy policy = new CertPolicy();
		policy.setPolicyName("Test PolicY");
		policy.setLexicon(PolicyLexicon.XML);
		policy.setPolicyData(new byte[] {1,2,3});
		
		polRepo.save(policy);
		
		CertPolicy addedPolicy = polRepo.findByPolicyNameIgnoreCase("Test POLicY");
		
		assertEquals(policy.getPolicyName(), addedPolicy.getPolicyName());	
		assertEquals(policy.getLexicon(), addedPolicy.getLexicon());
		assertTrue(now.getTimeInMillis() <= addedPolicy.getCreateTime().getTimeInMillis());
		assertTrue(Arrays.equals(policy.getPolicyData(), addedPolicy.getPolicyData()));
	}	
	
}
