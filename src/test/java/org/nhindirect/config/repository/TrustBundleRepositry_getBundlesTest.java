package org.nhindirect.config.repository;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;


import org.junit.Test;
import org.nhindirect.common.cert.Thumbprint;
import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleAnchor;

public class TrustBundleRepositry_getBundlesTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testGetTrustBundles_emptyBundleStore_assertNotBundlesRetrieved()
	{
		final Collection<TrustBundle> bundles = tbRepo.findAll();
		
		assertTrue(bundles.isEmpty());
	}
	
	@Test
	public void testGetTrustBundles_singleEntryInBundleStore_noAnchors_assertBundlesRetrieved()
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
		
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		final Collection<TrustBundle> bundles = tbRepo.findAll();
		
		assertEquals(1, bundles.size());
		
		TrustBundle addedBundle = bundles.iterator().next();
		
		assertEquals("Test Bundle", addedBundle.getBundleName());
		assertEquals("http://testBundle/bundle.p7b", addedBundle.getBundleURL());	
		assertEquals("12345", addedBundle.getCheckSum());
		assertEquals(5, addedBundle.getRefreshInterval());
		assertTrue(now.getTimeInMillis() <= addedBundle.getCreateTime().getTimeInMillis());
		assertNull(addedBundle.getLastRefreshAttempt());
		assertNull(addedBundle.getLastSuccessfulRefresh());
		assertNull(addedBundle.getLastRefreshError());
		assertNull(addedBundle.getSigningCertificateData());
		assertTrue(addedBundle.getTrustBundleAnchors().isEmpty());
	}	
	
	@Test
	public void testGetTrustBundles_singleEntryInBundleStore_signingCert_noAnchors_assertBundlesRetrieved() throws Exception
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
		
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setSigningCertificateData(loadCertificateData("secureHealthEmailCACert.der"));
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		final Collection<TrustBundle> bundles = tbRepo.findAll();
		
		assertEquals(1, bundles.size());
		
		TrustBundle addedBundle = bundles.iterator().next();
		
		assertEquals("Test Bundle", addedBundle.getBundleName());
		assertEquals("http://testBundle/bundle.p7b", addedBundle.getBundleURL());
		assertEquals("12345", addedBundle.getCheckSum());
		assertEquals("12345", addedBundle.getCheckSum());
		assertEquals(5, addedBundle.getRefreshInterval());
		assertTrue(now.getTimeInMillis() <= addedBundle.getCreateTime().getTimeInMillis());
		assertNull(addedBundle.getLastRefreshAttempt());
		assertNull(addedBundle.getLastSuccessfulRefresh());
		assertNull(addedBundle.getLastRefreshError());
		assertNotNull(addedBundle.getSigningCertificateData());
		assertTrue(addedBundle.getTrustBundleAnchors().isEmpty());
		
		assertNotNull(addedBundle.toSigningCertificate());
	}	
	
	@Test
	public void testGetTrustBundles_multipeEntriesInBundleStore_noAnchors_assertBundlesRetrieved()
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
		
		TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle1");
		bundle.setBundleURL("http://testBundle/bundle1.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		
		bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle2");
		bundle.setBundleURL("http://testBundle/bundle2.p7b");
		bundle.setRefreshInterval(6);
		bundle.setCheckSum("67890");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		
		final Collection<TrustBundle> bundles = tbRepo.findAll();
		
		assertEquals(2, bundles.size());
		
		Iterator<TrustBundle> iter = bundles.iterator();
		
		TrustBundle addedBundle = iter.next();
		
		assertEquals("Test Bundle1", addedBundle.getBundleName());
		assertEquals("http://testBundle/bundle1.p7b", addedBundle.getBundleURL());	
		assertEquals("12345", addedBundle.getCheckSum());
		assertEquals(5, addedBundle.getRefreshInterval());
		assertTrue(now.getTimeInMillis() <= addedBundle.getCreateTime().getTimeInMillis());
		assertNull(addedBundle.getLastRefreshAttempt());
		assertNull(addedBundle.getLastSuccessfulRefresh());
		assertNull(addedBundle.getLastRefreshError());
		assertNull(addedBundle.getSigningCertificateData());
		assertTrue(addedBundle.getTrustBundleAnchors().isEmpty());
		
		
		addedBundle = iter.next();
		
		assertEquals("Test Bundle2", addedBundle.getBundleName());
		assertEquals("http://testBundle/bundle2.p7b", addedBundle.getBundleURL());	
		assertEquals("67890", addedBundle.getCheckSum());
		assertEquals(6, addedBundle.getRefreshInterval());
		assertTrue(now.getTimeInMillis() <= addedBundle.getCreateTime().getTimeInMillis());
		assertNull(addedBundle.getLastRefreshAttempt());
		assertNull(addedBundle.getLastSuccessfulRefresh());
		assertNull(addedBundle.getLastRefreshError());
		assertNull(addedBundle.getSigningCertificateData());
		assertTrue(addedBundle.getTrustBundleAnchors().isEmpty());		
	}	
	
	@Test
	public void testGetTrustBundles_singleEntryInBundleStore_singleAnchor_assertBundlesRetrieved() throws Exception
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
				
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		TrustBundleAnchor anchor = new TrustBundleAnchor();
		anchor.setData(loadCertificateData("secureHealthEmailCACert.der"));
		anchor.setTrustBundle(bundle);
		
		bundle.setTrustBundleAnchors(Arrays.asList(anchor));
		
		tbRepo.save(bundle);
		
		final Collection<TrustBundle> bundles = tbRepo.findAll();
		
		assertEquals(1, bundles.size());
		
		TrustBundle addedBundle = bundles.iterator().next();
		
		assertEquals("Test Bundle", addedBundle.getBundleName());
		assertEquals("http://testBundle/bundle.p7b", addedBundle.getBundleURL());	
		assertEquals("12345", addedBundle.getCheckSum());
		assertEquals(5, addedBundle.getRefreshInterval());
		assertTrue(now.getTimeInMillis() <= addedBundle.getCreateTime().getTimeInMillis());
		assertNull(addedBundle.getLastRefreshAttempt());
		assertNull(addedBundle.getLastSuccessfulRefresh());
		assertNull(addedBundle.getLastRefreshError());
		assertNull(addedBundle.getSigningCertificateData());
		assertEquals(1, addedBundle.getTrustBundleAnchors().size());
		

		TrustBundleAnchor addedAnchor = addedBundle.getTrustBundleAnchors().iterator().next();

		assertNotNull(addedAnchor.toCertificate());
		assertEquals(anchor.toCertificate(), addedAnchor.toCertificate());
		assertEquals(Thumbprint.toThumbprint(anchor.toCertificate()).toString(), addedAnchor.getThumbprint());
		assertEquals(anchor.toCertificate().getNotAfter(), addedAnchor.getValidEndDate().getTime());
		assertEquals(anchor.toCertificate().getNotBefore(), addedAnchor.getValidStartDate().getTime());	
		assertEquals(anchor.getTrustBundle().getBundleName(), bundle.getBundleName());
		assertEquals(anchor.getTrustBundle().getId(), addedBundle.getId());
	}	
	
}