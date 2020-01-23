package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleAnchor;

import reactor.test.StepVerifier;

public class TrustBundleRepository_updateTrustBundleAnchorsTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testUpdateTrustBundleAnchors_addNewAnchors_assertNewAnchors() throws Exception
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
		
		bundle = tbRepo.findAll().collectList().block().get(0);
		
		final TrustBundleAnchor anchor = new TrustBundleAnchor();
		anchor.setData(loadCertificateData("secureHealthEmailCACert.der"));
		anchor.setTrustBundleId(bundle.getId());
		
		tbAncRepo.save(anchor)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		final List<TrustBundleAnchor> addedAnchors = tbAncRepo.findByTrustBundleId(bundle.getId()).collectList().block();
		assertEquals(1, addedAnchors.size());
		
		final TrustBundleAnchor addedAnchor = addedAnchors.get(0);
		assertEquals(anchor.toCertificate(), addedAnchor.toCertificate());
	}
	
}
