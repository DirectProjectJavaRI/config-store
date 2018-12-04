package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

import org.junit.Test;
import org.nhindirect.config.store.TrustBundle;

public class TrustBundleRepository_getTrustBundleByIdTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testTetTrustBundleById_emptyStore_assertNoBundleReturned()
	{
		assertEquals(Optional.empty(), tbRepo.findById(1234L));
	}
	
	@Test
	public void testTetTrustBundleById_singleBundleInStore_idNotInStore_assertNoBundleReturned()
	{
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		
		assertEquals(Optional.empty(),tbRepo.findById(1234L));
	}
	
	@Test
	public void testTetTrustBundleById_singleBundleInStore_assertBundleReturned()
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
		
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setCheckSum("12345");
		bundle.setRefreshInterval(5);
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		Optional<TrustBundle> op = tbRepo.findById(bundle.getId());
		TrustBundle addedBundle = op.get();
		
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
}
