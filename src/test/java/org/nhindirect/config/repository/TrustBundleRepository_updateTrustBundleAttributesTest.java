package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;


import org.junit.Test;
import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleAnchor;

public class TrustBundleRepository_updateTrustBundleAttributesTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testTrustBundleAttributes_updateCert_assertCertUpdated() throws Exception
	{
		
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		TrustBundleAnchor anchor = new TrustBundleAnchor();
		anchor.setData(loadCertificateData("secureHealthEmailCACert.der"));
		
		bundle.setSigningCertificateData(anchor.toCertificate().getEncoded());
		tbRepo.save(bundle);
		
		
		TrustBundle updatedBundle = tbRepo.findById(bundle.getId()).get();
		
		assertEquals(bundle.getBundleName(), updatedBundle.getBundleName());
		assertEquals(bundle.getBundleURL(), updatedBundle.getBundleURL());
		assertEquals(bundle.getRefreshInterval(), updatedBundle.getRefreshInterval());
		assertEquals(bundle.getTrustBundleAnchors().size(), updatedBundle.getTrustBundleAnchors().size());
		assertEquals(anchor.toCertificate(), updatedBundle.toSigningCertificate());
		
	}
	
	@Test
	public void testTrustBundleAttributes_updateCert_setNull_assertCertNull() throws Exception
	{
		
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setSigningCertificateData(loadCertificateData("secureHealthEmailCACert.der"));
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		bundle.setSigningCertificateData(null);
		tbRepo.save(bundle);
		
		TrustBundle updatedBundle = tbRepo.findById(bundle.getId()).get();
		
		assertEquals(bundle.getBundleName(), updatedBundle.getBundleName());
		assertEquals(bundle.getBundleURL(), updatedBundle.getBundleURL());
		assertEquals(bundle.getRefreshInterval(), updatedBundle.getRefreshInterval());
		assertEquals(bundle.getTrustBundleAnchors().size(), updatedBundle.getTrustBundleAnchors().size());
		assertNull(updatedBundle.getSigningCertificateData());
		
	}
	
	@Test
	public void testUpdateTrustBundleSigningCertificate_updateBundleName_assertNameUpdate() throws Exception
	{
		
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		bundle.setBundleName("New Test Bundle Name");
		tbRepo.save(bundle);
		
		TrustBundle updatedBundle = tbRepo.findById(bundle.getId()).get();
		
		assertEquals("New Test Bundle Name", updatedBundle.getBundleName());
		assertEquals(bundle.getBundleURL(), updatedBundle.getBundleURL());
		assertEquals(bundle.getRefreshInterval(), updatedBundle.getRefreshInterval());
		assertNull(updatedBundle.getSigningCertificateData());
		
		assertEquals(bundle.getTrustBundleAnchors().size(), updatedBundle.getTrustBundleAnchors().size());
	}	
	
	@Test
	public void testUpdateTrustBundleSigningCertificate_updateBundleURL_assertURLUpdate() throws Exception
	{
		
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		bundle.setBundleURL("http://testBundle/bundle.p7b333");
		tbRepo.save(bundle);
		
		TrustBundle updatedBundle = tbRepo.findById(bundle.getId()).get();
		
		assertEquals(bundle.getBundleName(), bundle.getBundleName());
		assertEquals("http://testBundle/bundle.p7b333", updatedBundle.getBundleURL());
		assertEquals(bundle.getRefreshInterval(), updatedBundle.getRefreshInterval());
		assertNull(updatedBundle.getSigningCertificateData());
		
		assertEquals(bundle.getTrustBundleAnchors().size(), updatedBundle.getTrustBundleAnchors().size());
	}	
	
	@Test
	public void testUpdateTrustBundleSigningCertificate_updateBundleRefreshInterval_assertIntervalUpdate() throws Exception
	{
		
		final TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://testBundle/bundle.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("12345");
		bundle.setCreateTime(Calendar.getInstance());
		
		tbRepo.save(bundle);
		
		bundle.setRefreshInterval(7);;
		
		tbRepo.save(bundle);
		
		TrustBundle updatedBundle = tbRepo.findById(bundle.getId()).get();
		
		assertEquals(bundle.getBundleName(), updatedBundle.getBundleName());
		assertEquals(bundle.getBundleURL(), updatedBundle.getBundleURL());
		assertEquals(7, updatedBundle.getRefreshInterval());
		assertNull(updatedBundle.getSigningCertificateData());
		
		assertEquals(bundle.getTrustBundleAnchors().size(), updatedBundle.getTrustBundleAnchors().size());
	}
}
