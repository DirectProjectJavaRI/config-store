package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.nhindirect.config.store.Domain;
import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleDomainReltn;

import reactor.test.StepVerifier;

public class TrustBundleRepository_getTrustBundlesByDomainTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testGetTrustBundlesByDomain_associationsExist_assertBundlesRetrieved()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		dmRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://test/url/bundle");
		bundle.setCheckSum("1234");
		tbRepo.save(bundle)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		TrustBundleDomainReltn reltn = new TrustBundleDomainReltn();
		reltn.setDomainId(domain.getId());
		reltn.setTrustBundleId(bundle.getId());
		reltn.setIncoming(true);
		reltn.setOutgoing(true);
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Collection<TrustBundleDomainReltn> bundles = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(1, bundles.size());
	}
	
	@Test
	public void testGetTrustBundlesByDomain_multipleAssociationsExist_assertBundlesRetrieved()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		dmRepo.save(domain)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		TrustBundle bundle1 = new TrustBundle();
		bundle1.setBundleName("Test Bundle");
		bundle1.setBundleURL("http://test/url/bundle");
		bundle1.setCheckSum("1234");
		tbRepo.save(bundle1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
	
		
		TrustBundle bundle2 = new TrustBundle();
		bundle2.setBundleName("Test Bundle2");
		bundle2.setBundleURL("http://test/url/bundle2");
		bundle2.setCheckSum("1234");
		tbRepo.save(bundle2)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		TrustBundleDomainReltn reltn = new TrustBundleDomainReltn();
		reltn.setDomainId(domain.getId());
		reltn.setTrustBundleId(bundle1.getId());
		reltn.setIncoming(true);
		reltn.setOutgoing(true);
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		
		reltn = new TrustBundleDomainReltn();
		reltn.setDomainId(domain.getId());
		reltn.setTrustBundleId(bundle2.getId());
		reltn.setIncoming(true);
		reltn.setOutgoing(true);
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		final Collection<TrustBundleDomainReltn> bundles = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(2, bundles.size());
		
		Iterator<TrustBundleDomainReltn> bundleIter = bundles.iterator();
		assertEquals(bundle1.getId(), bundleIter.next().getTrustBundleId());
		assertEquals(bundle2.getId(), bundleIter.next().getTrustBundleId());
	}	
	
	
	@Test
	public void testGetTrustBundlesByDomain_multipleAssociationsExist_oneToEachDomain_assertBundlesRetrieved()
	{
		Domain domain1 = new Domain();
		domain1.setDomainName("Test Domain");
		dmRepo.save(domain1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Domain domain2 = new Domain();
		domain2.setDomainName("Test Domain 2");
		dmRepo.save(domain2)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		TrustBundle bundle1 = new TrustBundle();
		bundle1.setBundleName("Test Bundle");
		bundle1.setBundleURL("http://test/url/bundle");
		bundle1.setCheckSum("1234");
		tbRepo.save(bundle1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		TrustBundle bundle2 = new TrustBundle();
		bundle2.setBundleName("Test Bundle2");
		bundle2.setBundleURL("http://test/url/bundle2");
		bundle2.setCheckSum("1234");
		tbRepo.save(bundle2)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		TrustBundleDomainReltn reltn = new TrustBundleDomainReltn();
		reltn.setDomainId(domain1.getId());
		reltn.setTrustBundleId(bundle1.getId());
		reltn.setIncoming(true);
		reltn.setOutgoing(true);
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		
		reltn = new TrustBundleDomainReltn();
		reltn.setDomainId(domain2.getId());
		reltn.setTrustBundleId(bundle2.getId());
		reltn.setIncoming(true);
		reltn.setOutgoing(true);
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Collection<TrustBundleDomainReltn> bundles = reltnRepo.findByDomainId(domain1.getId()).collectList().block();
		assertEquals(1, bundles.size());
		
		Iterator<TrustBundleDomainReltn> bundleIter = bundles.iterator();
		assertEquals(bundle1.getId(), bundleIter.next().getTrustBundleId());
		
		bundles = reltnRepo.findByDomainId(domain2.getId()).collectList().block();
		assertEquals(1, bundles.size());
		
		bundleIter = bundles.iterator();
		assertEquals(bundle2.getId(), bundleIter.next().getTrustBundleId());

	}	
	

	@Test
	public void testGetTrustBundlesByDomain_multipleAssociationsExist_bundleToMultipeDomains_assertBundlesRetrieved()
	{
		Domain domain1 = new Domain();
		domain1.setDomainName("Test Domain 1");
		dmRepo.save(domain1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Domain domain2 = new Domain();
		domain2.setDomainName("Test Domain 2");
		dmRepo.save(domain2)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		TrustBundle bundle1 = new TrustBundle();
		bundle1.setBundleName("Test Bundle1");
		bundle1.setBundleURL("http://test/url/bundle1");
		bundle1.setCheckSum("1234");
		tbRepo.save(bundle1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		TrustBundleDomainReltn reltn = new TrustBundleDomainReltn();
		reltn.setDomainId(domain1.getId());
		reltn.setTrustBundleId(bundle1.getId());
		reltn.setIncoming(true);
		reltn.setOutgoing(true);
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		reltn = new TrustBundleDomainReltn();
		reltn.setDomainId(domain2.getId());
		reltn.setTrustBundleId(bundle1.getId());
		reltn.setIncoming(true);
		reltn.setOutgoing(true);
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Collection<TrustBundleDomainReltn> bundles = reltnRepo.findByDomainId(domain1.getId()).collectList().block();
		assertEquals(1, bundles.size());
		
		Iterator<TrustBundleDomainReltn> bundleIter = bundles.iterator();
		assertEquals(bundle1.getId(), bundleIter.next().getTrustBundleId());
		
		bundles = reltnRepo.findByDomainId(domain2.getId()).collectList().block();
		assertEquals(1, bundles.size());
		
		bundleIter = bundles.iterator();
		assertEquals(bundle1.getId(), bundleIter.next().getTrustBundleId());

	}
	
	@Test
	public void testGetTrustBundlesByDomain_noBundlesInDomain_assertBundlesNotRetrieved()
	{
		Domain domain1 = new Domain();
		domain1.setDomainName("Test Domain 1");
		dmRepo.save(domain1)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		Collection<TrustBundleDomainReltn> bundles = reltnRepo.findByDomainId(domain1.getId()).collectList().block();
		assertEquals(0, bundles.size());
	}	
}
