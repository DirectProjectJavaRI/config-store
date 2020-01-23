package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.nhindirect.config.store.TrustBundle;

import reactor.test.StepVerifier;

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
		bundle.setCreateTime(LocalDateTime.now());
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		List<TrustBundle> bundles = tbRepo.findAll().collectList().block();
		
		assertEquals(1, bundles.size());
		
		tbRepo.deleteAll(bundles)
		.as(StepVerifier::create) 
		.verifyComplete();
		
		bundles = tbRepo.findAll().collectList().block();
		
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
		bundle.setCreateTime(LocalDateTime.now());
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle2");
		bundle.setBundleURL("http://testBundle/bundle2.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("67890");
		bundle.setCreateTime(LocalDateTime.now());
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Collection<TrustBundle> bundles = tbRepo.findAll().collectList().block();
		
		assertEquals(2, bundles.size());
		
		tbRepo.deleteById(bundles.iterator().next().getId())
		.as(StepVerifier::create) 
		.verifyComplete();
		
		bundles = tbRepo.findAll().collectList().block();
		
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
		bundle.setCreateTime(LocalDateTime.now());
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle2");
		bundle.setBundleURL("http://testBundle/bundle2.p7b");
		bundle.setRefreshInterval(5);
		bundle.setCheckSum("67890");
		bundle.setCreateTime(LocalDateTime.now());
		
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		List<TrustBundle> bundles = tbRepo.findAll().collectList().block();
		
		assertEquals(2, bundles.size());
		
		tbRepo.deleteAll()
		.as(StepVerifier::create) 
		.verifyComplete();
		
		bundles = tbRepo.findAll().collectList().block();
		
		assertEquals(0, bundles.size());
	}	
		
}