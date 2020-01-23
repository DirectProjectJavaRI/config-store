package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;
import org.nhindirect.config.store.BundleRefreshError;
import org.nhindirect.config.store.TrustBundle;

import reactor.test.StepVerifier;

public class TrustBundleRepository_updateLastUpdateErrorTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testUpdateLastUpdateError_updateUpdate_assertErrorUpdate()
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
		
		bundle.setLastRefreshError(BundleRefreshError.SUCCESS.ordinal());
		bundle.setLastRefreshAttempt(now);
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		TrustBundle updatedBundle = tbRepo.findAll().collectList().block().get(0);
		
		assertEquals( BundleRefreshError.SUCCESS.ordinal(), updatedBundle.getLastRefreshError());
		assertEquals(now, updatedBundle.getLastRefreshAttempt());
		
		bundle.setLastRefreshError(BundleRefreshError.NOT_FOUND.ordinal());
		bundle.setLastRefreshAttempt(now);
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		updatedBundle = tbRepo.findAll().collectList().block().get(0);
		
		assertEquals( BundleRefreshError.NOT_FOUND.ordinal(), updatedBundle.getLastRefreshError());	
	}

}
