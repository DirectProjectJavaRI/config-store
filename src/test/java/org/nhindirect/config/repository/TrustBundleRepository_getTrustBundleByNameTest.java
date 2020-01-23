package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;
import org.nhindirect.config.store.TrustBundle;

import reactor.test.StepVerifier;

public class TrustBundleRepository_getTrustBundleByNameTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testTetTrustBundleByName_emptyStore_assertNoBundleReturned()
	{
		assertNull(tbRepo.findByBundleNameIgnoreCase("Test Bundle").block());
	}
	
	@Test
	public void testTetTrustBundleByName_singleBundleInStore_nameNotInStore_assertNoBundleReturned()
	{
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(LocalDateTime.now());
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		assertNull(tbRepo.findByBundleNameIgnoreCase("Test Bundle X").block());
	}
	
	@Test
	public void testTetTrustBundleByName_singleBundleInStore_assertBundleReturned()
	{
		final LocalDateTime now = LocalDateTime.now();
		
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(LocalDateTime.now());
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		TrustBundle addedBundle = tbRepo.findByBundleNameIgnoreCase("Test BundLE").block();
		
		assertEquals("Test Bundle", addedBundle.getBundleName());
		assertEquals("http://testBundle/bundle.p7b", addedBundle.getBundleURL());	
		assertEquals("12345", addedBundle.getCheckSum());
		assertEquals(5, addedBundle.getRefreshInterval());
		assertTrue(now.compareTo(addedBundle.getCreateTime()) <= 0);
		assertNull(addedBundle.getLastRefreshAttempt());
		assertNull(addedBundle.getLastSuccessfulRefresh());
		assertEquals(0, addedBundle.getLastRefreshError());
		assertNull(addedBundle.getSigningCertificateData());
	}	

}
