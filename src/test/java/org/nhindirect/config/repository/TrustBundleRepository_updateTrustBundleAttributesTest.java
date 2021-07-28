package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleAnchor;

import reactor.test.StepVerifier;

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
		bundle.setCreateTime(LocalDateTime.now());
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		TrustBundleAnchor anchor = new TrustBundleAnchor();
		anchor.setData(loadCertificateData("secureHealthEmailCACert.der"));
		
		bundle.setSigningCertificateData(anchor.toCertificate().getEncoded());
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		
		TrustBundle updatedBundle = tbRepo.findById(bundle.getId()).block();
		
		assertEquals(bundle.getBundleName(), updatedBundle.getBundleName());
		assertEquals(bundle.getBundleURL(), updatedBundle.getBundleURL());
		assertEquals(bundle.getRefreshInterval(), updatedBundle.getRefreshInterval());
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
		bundle.setCreateTime(LocalDateTime.now());
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		
		bundle.setSigningCertificateData(null);
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		
		TrustBundle updatedBundle = tbRepo.findById(bundle.getId()).block();
		
		assertEquals(bundle.getBundleName(), updatedBundle.getBundleName());
		assertEquals(bundle.getBundleURL(), updatedBundle.getBundleURL());
		assertEquals(bundle.getRefreshInterval(), updatedBundle.getRefreshInterval());
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
		bundle.setCreateTime(LocalDateTime.now());
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		bundle.setBundleName("New Test Bundle Name");
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		TrustBundle updatedBundle = tbRepo.findById(bundle.getId()).block();
		
		assertEquals("New Test Bundle Name", updatedBundle.getBundleName());
		assertEquals(bundle.getBundleURL(), updatedBundle.getBundleURL());
		assertEquals(bundle.getRefreshInterval(), updatedBundle.getRefreshInterval());
		assertNull(updatedBundle.getSigningCertificateData());
		
	}	
	
	@Test
	public void testUpdateTrustBundleSigningCertificate_updateBundleURL_assertURLUpdate() throws Exception
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
		
		bundle.setBundleURL("http://testBundle/bundle.p7b333");
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		TrustBundle updatedBundle = tbRepo.findById(bundle.getId()).block();
		
		assertEquals(bundle.getBundleName(), bundle.getBundleName());
		assertEquals("http://testBundle/bundle.p7b333", updatedBundle.getBundleURL());
		assertEquals(bundle.getRefreshInterval(), updatedBundle.getRefreshInterval());
		assertNull(updatedBundle.getSigningCertificateData());
	}	
	
	@Test
	public void testUpdateTrustBundleSigningCertificate_updateBundleRefreshInterval_assertIntervalUpdate() throws Exception
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
		
		bundle.setRefreshInterval(7);
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		TrustBundle updatedBundle = tbRepo.findById(bundle.getId()).block();
		
		assertEquals(bundle.getBundleName(), updatedBundle.getBundleName());
		assertEquals(bundle.getBundleURL(), updatedBundle.getBundleURL());
		assertEquals(7, updatedBundle.getRefreshInterval());
		assertNull(updatedBundle.getSigningCertificateData());
		
	}
}
