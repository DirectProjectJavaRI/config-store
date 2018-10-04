package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleAnchor;

public class TrustBundleRepository_updateTrustBundleAnchorsTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testUpdateTrustBundleAnchors_addNewAnchors_assertNewAnchors() throws Exception
	{
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		final TrustBundleAnchor anchor = new TrustBundleAnchor();
		anchor.setData(loadCertificateData("secureHealthEmailCACert.der"));
		anchor.setTrustBundle(bundle);
		
		bundle.setTrustBundleAnchors(Arrays.asList(anchor));
		bundle.setCheckSum("6789");

		tbRepo.save(bundle);
		
		final TrustBundle addedBundle = tbRepo.findById(bundle.getId()).get();
		assertEquals(1, addedBundle.getTrustBundleAnchors().size());
		
		final TrustBundleAnchor addedAnchor = addedBundle.getTrustBundleAnchors().iterator().next();
		assertEquals(anchor.toCertificate(), addedAnchor.toCertificate());
	}
	
	@Test
	public void testUpdateTrustBundleAnchors_addAdditionalAnchors_assertNewAnchors() throws Exception
	{
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
		
		final TrustBundleAnchor additionalAnchor = new TrustBundleAnchor();
		additionalAnchor.setData(loadCertificateData("umesh.der"));
		additionalAnchor.setTrustBundle(bundle);
		
		Collection<TrustBundleAnchor> newAnchors = new ArrayList<>(bundle.getTrustBundleAnchors());
		newAnchors.add(additionalAnchor);
		bundle.setTrustBundleAnchors(newAnchors);
		bundle.setCheckSum("6789");
		tbRepo.save(bundle);
		
		final TrustBundle addedBundle = tbRepo.findById(bundle.getId()).get();
		assertEquals("6789", addedBundle.getCheckSum());
		assertEquals(2, addedBundle.getTrustBundleAnchors().size());
		
		Iterator<TrustBundleAnchor> iter = addedBundle.getTrustBundleAnchors().iterator();
		
		TrustBundleAnchor addedAnchor = iter.next();
		assertEquals(anchor.toCertificate(), addedAnchor.toCertificate());
		
		addedAnchor = iter.next();
		assertEquals(additionalAnchor.toCertificate(), addedAnchor.toCertificate());
	}	
	
	@Test
	public void testUpdateTrustBundleAnchors_addSwapAnchors_assertNewAnchors() throws Exception
	{
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
		
		TrustBundle addedBundle = tbRepo.findById(bundle.getId()).get();
		assertEquals(1, addedBundle.getTrustBundleAnchors().size());
		
		final TrustBundleAnchor newAnchor = new TrustBundleAnchor();
		newAnchor.setData(loadCertificateData("umesh.der"));
		newAnchor.setTrustBundle(bundle);
		
		bundle.setTrustBundleAnchors(Arrays.asList(newAnchor));
		tbRepo.save(bundle);
		
		addedBundle = tbRepo.findById(bundle.getId()).get();
		assertEquals(1, addedBundle.getTrustBundleAnchors().size());
		
		TrustBundleAnchor addedAnchor = addedBundle.getTrustBundleAnchors().iterator().next();
		assertEquals(newAnchor.toCertificate(), addedAnchor.toCertificate());
		
	}
}
