package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;

import org.nhindirect.config.store.TrustBundle;

import reactor.test.StepVerifier;

public class TrustBundleRepositry_getBundlesTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testGetTrustBundles_emptyBundleStore_assertNotBundlesRetrieved()
	{
		tbRepo.findAll()
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
	}
	
	@Test
	public void testGetTrustBundles_singleEntryInBundleStore_noAnchors_assertBundlesRetrieved()
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
		
		final Collection<TrustBundle> bundles = tbRepo.findAll().collectList().block();
		
		assertEquals(1, bundles.size());
		
		TrustBundle addedBundle = bundles.iterator().next();
		
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
	
	@Test
	public void testGetTrustBundles_singleEntryInBundleStore_signingCert_noAnchors_assertBundlesRetrieved() throws Exception
	{
		final LocalDateTime now = LocalDateTime.now();
		
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setSigningCertificateData(loadCertificateData("secureHealthEmailCACert.der"));
		bundle.setCheckSum("12345");
		bundle.setCreateTime(LocalDateTime.now());
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		final Collection<TrustBundle> bundles = tbRepo.findAll().collectList().block();
		
		assertEquals(1, bundles.size());
		
		TrustBundle addedBundle = bundles.iterator().next();
		
		assertEquals("Test Bundle", addedBundle.getBundleName());
		assertEquals("http://testBundle/bundle.p7b", addedBundle.getBundleURL());
		assertEquals("12345", addedBundle.getCheckSum());
		assertEquals("12345", addedBundle.getCheckSum());
		assertEquals(5, addedBundle.getRefreshInterval());
		assertTrue(now.compareTo(addedBundle.getCreateTime()) <= 0);
		assertNull(addedBundle.getLastRefreshAttempt());
		assertNull(addedBundle.getLastSuccessfulRefresh());
		assertEquals(0, addedBundle.getLastRefreshError());
		assertNotNull(addedBundle.getSigningCertificateData());
		assertNotNull(addedBundle.toSigningCertificate());
	}	
	
	@Test
	public void testGetTrustBundles_multipeEntriesInBundleStore_noAnchors_assertBundlesRetrieved()
	{
		final LocalDateTime now = LocalDateTime.now();
		
		TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle1");
		bundle.setBundleURL("http://testBundle/bundle1.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(LocalDateTime.now());
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		
		bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle2");
		bundle.setBundleURL("http://testBundle/bundle2.p7b");
		bundle.setRefreshInterval(6);
		bundle.setCheckSum("67890");
		bundle.setCreateTime(LocalDateTime.now());
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		
		final Collection<TrustBundle> bundles = tbRepo.findAll().collectList().block();
		
		assertEquals(2, bundles.size());
		
		Iterator<TrustBundle> iter = bundles.iterator();
		
		TrustBundle addedBundle = iter.next();
		
		assertEquals("Test Bundle1", addedBundle.getBundleName());
		assertEquals("http://testBundle/bundle1.p7b", addedBundle.getBundleURL());	
		assertEquals("12345", addedBundle.getCheckSum());
		assertEquals(5, addedBundle.getRefreshInterval());
		assertTrue(now.compareTo(addedBundle.getCreateTime()) <= 0);
		assertNull(addedBundle.getLastRefreshAttempt());
		assertNull(addedBundle.getLastSuccessfulRefresh());
		assertEquals(0, addedBundle.getLastRefreshError());
		assertNull(addedBundle.getSigningCertificateData());
		
		
		addedBundle = iter.next();
		
		assertEquals("Test Bundle2", addedBundle.getBundleName());
		assertEquals("http://testBundle/bundle2.p7b", addedBundle.getBundleURL());	
		assertEquals("67890", addedBundle.getCheckSum());
		assertEquals(6, addedBundle.getRefreshInterval());
		assertTrue(now.compareTo(addedBundle.getCreateTime()) <= 0);
		assertNull(addedBundle.getLastRefreshAttempt());
		assertNull(addedBundle.getLastSuccessfulRefresh());
		assertEquals(0, addedBundle.getLastRefreshError());
		assertNull(addedBundle.getSigningCertificateData());	
	}	
	
}