package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;
import org.nhindirect.config.store.BundleRefreshError;
import org.nhindirect.config.store.TrustBundle;

public class TrustBundleRepository_updateLastUpdateErrorTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testUpdateLastUpdateError_updateUpdate_assertErrorUpdate()
	{
		final Calendar now = Calendar.getInstance(Locale.getDefault());
		
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		bundle.setLastRefreshError(BundleRefreshError.SUCCESS);
		bundle.setLastRefreshAttempt(now);
		
		tbRepo.save(bundle);
		
		TrustBundle updatedBundle = tbRepo.findById(bundle.getId()).get();
		
		assertEquals( BundleRefreshError.SUCCESS, updatedBundle.getLastRefreshError());
		assertEquals(now, updatedBundle.getLastRefreshAttempt());
		
		bundle.setLastRefreshError(BundleRefreshError.NOT_FOUND);
		bundle.setLastRefreshAttempt(now);
		tbRepo.save(bundle);
		
		updatedBundle = tbRepo.findById(bundle.getId()).get();
		
		assertEquals( BundleRefreshError.NOT_FOUND, updatedBundle.getLastRefreshError());	
	}

}
