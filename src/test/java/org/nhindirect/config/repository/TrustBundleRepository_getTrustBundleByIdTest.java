package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;
import org.nhindirect.config.store.TrustBundle;

import reactor.test.StepVerifier;

public class TrustBundleRepository_getTrustBundleByIdTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testTetTrustBundleById_emptyStore_assertNoBundleReturned()
	{
		assertEquals(null, tbRepo.findById(1234L).block());
	}
	
	@Test
	public void testTetTrustBundleById_singleBundleInStore_idNotInStore_assertNoBundleReturned()
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
		
		
		assertEquals(null,tbRepo.findById(1234L).block());
	}
	
	@Test
	public void testTetTrustBundleById_singleBundleInStore_assertBundleReturned()
	{
		final LocalDateTime now = LocalDateTime.now();
		
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setCheckSum("12345");
		bundle.setRefreshInterval(5);
		bundle.setCreateTime(LocalDateTime.now());
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		TrustBundle addedBundle = tbRepo.findById(bundle.getId()).block();
		
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
