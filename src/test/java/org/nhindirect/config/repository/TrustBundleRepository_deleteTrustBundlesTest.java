package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;


import org.junit.Test;
import org.nhindirect.config.store.TrustBundle;

public class TrustBundleRepository_deleteTrustBundlesTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testDeleteTrustBundlesTest_singleBundle_assertBundleDeleted()
	{
		
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		Collection<TrustBundle> bundles = tbRepo.findAll();
		
		assertEquals(1, bundles.size());
		
		tbRepo.deleteAll(bundles);
		
		bundles = tbRepo.findAll();
		
		assertEquals(0, bundles.size());
	}
	
	@Test
	public void testDeleteTrustBundlesTest_multipleBundles_assertSingleBundleDeleted()
	{
		
		TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle2");
		bundle.setBundleURL("http://testBundle/bundle2.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("67890");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		Collection<TrustBundle> bundles = tbRepo.findAll();
		
		assertEquals(2, bundles.size());
		
		tbRepo.deleteById(bundles.iterator().next().getId());
		
		bundles = tbRepo.findAll();
		
		assertEquals(1, bundles.size());
	}
	
	@Test
	public void testDeleteTrustBundlesTest_multipleBundles_assertAllBundlesDeleted()
	{
		
		TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle2");
		bundle.setBundleURL("http://testBundle/bundle2.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("67890");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		Collection<TrustBundle> bundles = tbRepo.findAll();
		
		assertEquals(2, bundles.size());
		
		Iterator<TrustBundle> iter = bundles.iterator();
		tbRepo.deleteAll(Arrays.asList(iter.next(), iter.next()));
		
		bundles = tbRepo.findAll();
		
		assertEquals(0, bundles.size());
	}	
		
}