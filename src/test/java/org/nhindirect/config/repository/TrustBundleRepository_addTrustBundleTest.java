package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.nhindirect.common.cert.Thumbprint;
import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleAnchor;
import org.springframework.dao.DataIntegrityViolationException;

import reactor.test.StepVerifier;

public class TrustBundleRepository_addTrustBundleTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testAddTrustBundle_addBundle_noAnchors_assertAdded()
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
	public void testAddTrustBundle_addBundle_withAnchors_assertAdded() throws Exception
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
		
		TrustBundleAnchor anchor = new TrustBundleAnchor();
		anchor.setData(loadCertificateData("secureHealthEmailCACert.der"));
		anchor.setTrustBundleId(addedBundle.getId());
		
		tbAncRepo.save(anchor)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		
		assertEquals("Test Bundle", addedBundle.getBundleName());
		assertEquals("http://testBundle/bundle.p7b", addedBundle.getBundleURL());
		assertEquals("12345", addedBundle.getCheckSum());
		assertEquals(5, addedBundle.getRefreshInterval());
		assertTrue(now.compareTo(addedBundle.getCreateTime()) <= 0);
		assertNull(addedBundle.getLastRefreshAttempt());
		assertNull(addedBundle.getLastSuccessfulRefresh());
		assertEquals(0 ,addedBundle.getLastRefreshError());
		assertNull(addedBundle.getSigningCertificateData());
		
		List<TrustBundleAnchor> addedAnchors = tbAncRepo.findByTrustBundleId(addedBundle.getId()).collectList().block();
		
		assertEquals(1, addedAnchors.size());
		

		TrustBundleAnchor addedAnchor = addedAnchors.get(0);

		assertNotNull(addedAnchor.toCertificate());
		assertEquals(anchor.toCertificate(), addedAnchor.toCertificate());
		assertEquals(Thumbprint.toThumbprint(anchor.toCertificate()).toString(), addedAnchor.getThumbprint());
		assertEquals(anchor.toCertificate().getNotAfter().getTime(), Timestamp.valueOf(addedAnchor.getValidEndDate()).getTime());
		assertEquals(anchor.toCertificate().getNotBefore().getTime(), Timestamp.valueOf(addedAnchor.getValidStartDate()).getTime());	
		assertEquals(anchor.getTrustBundleId(), addedBundle.getId());
	}	
	
	
	@Test
	public void testAddTrustBundle_addExistingBundle_assertException()
	{
		
		TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(LocalDateTime.now());
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		List<TrustBundle> bundles = tbRepo.findAll().collectList().block();
		
		assertEquals(1, bundles.size());
		
		boolean exceptionOccured = false;
		
		try
		{
			bundle = new TrustBundle();
			bundle.setBundleName("Test Bundle");
			bundle.setBundleURL("http://testBundle/bundle.p7b");
			bundle.setRefreshInterval(5);
			bundle.setCheckSum("12345");
			bundle.setCreateTime(LocalDateTime.now());
			
			tbRepo.save(bundle).block();
		}
		catch (DataIntegrityViolationException ex)
		{
			exceptionOccured = true;
		}
		
		assertTrue(exceptionOccured);
		bundles = tbRepo.findAll().collectList().block();
		
		assertEquals(1, bundles.size());		
	}	
}
