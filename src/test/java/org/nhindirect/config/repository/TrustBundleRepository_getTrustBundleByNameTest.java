package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;
import org.nhindirect.config.store.TrustBundle;

public class TrustBundleRepository_getTrustBundleByNameTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testTetTrustBundleByName_emptyStore_assertNoBundleReturned()
	{
		assertNull(tbRepo.findByBundleNameIgnoreCase("Test Bundle"));
	}
	
	@Test
	public void testTetTrustBundleByName_singleBundleInStore_nameNotInStore_assertNoBundleReturned()
	{
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		assertNull(tbRepo.findByBundleNameIgnoreCase("Test Bundle X"));
	}
	
	@Test
	public void testTetTrustBundleByName_singleBundleInStore_assertBundleReturned()
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
		
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		TrustBundle addedBundle = tbRepo.findByBundleNameIgnoreCase("Test BundLE");
		
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
