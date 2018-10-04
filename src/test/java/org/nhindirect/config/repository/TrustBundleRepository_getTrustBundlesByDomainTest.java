package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.nhindirect.config.store.Domain;
import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleDomainReltn;

public class TrustBundleRepository_getTrustBundlesByDomainTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testGetTrustBundlesByDomain_associationsExist_assertBundlesRetrieved()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		domain = dmRepo.save(domain);
		
		TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://test/url/bundle");
		bundle.setCheckSum("1234");
		bundle = tbRepo.save(bundle);
		
		TrustBundleDomainReltn reltn = new TrustBundleDomainReltn();
		reltn.setDomain(domain);
		reltn.setTrustBundle(bundle);
		reltn.setIncoming(true);
		reltn.setOutgoing(true);
		
		reltnRepo.save(reltn);
		
		Collection<TrustBundleDomainReltn> bundles = reltnRepo.findByDomain(domain);
		assertEquals(1, bundles.size());
	}
	
	@Test
	public void testGetTrustBundlesByDomain_multipleAssociationsExist_assertBundlesRetrieved()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		domain = dmRepo.save(domain);
		
		TrustBundle bundle1 = new TrustBundle();
		bundle1.setBundleName("Test Bundle");
		bundle1.setBundleURL("http://test/url/bundle");
		bundle1.setCheckSum("1234");
		bundle1 = tbRepo.save(bundle1);
	
		
		TrustBundle bundle2 = new TrustBundle();
		bundle2.setBundleName("Test Bundle2");
		bundle2.setBundleURL("http://test/url/bundle2");
		bundle2.setCheckSum("1234");
		bundle2 = tbRepo.save(bundle2);
		
		TrustBundleDomainReltn reltn = new TrustBundleDomainReltn();
		reltn.setDomain(domain);
		reltn.setTrustBundle(bundle1);
		reltn.setIncoming(true);
		reltn.setOutgoing(true);
		
		reltnRepo.save(reltn);
		
		
		reltn = new TrustBundleDomainReltn();
		reltn.setDomain(domain);
		reltn.setTrustBundle(bundle2);
		reltn.setIncoming(true);
		reltn.setOutgoing(true);
		
		reltnRepo.save(reltn);
		
		final Collection<TrustBundleDomainReltn> bundles = reltnRepo.findByDomain(domain);
		assertEquals(2, bundles.size());
		
		Iterator<TrustBundleDomainReltn> bundleIter = bundles.iterator();
		assertEquals(bundle1.getBundleName(), bundleIter.next().getTrustBundle().getBundleName());
		assertEquals(bundle2.getBundleName(), bundleIter.next().getTrustBundle().getBundleName());
	}	
	
	
	@Test
	public void testGetTrustBundlesByDomain_multipleAssociationsExist_oneToEachDomain_assertBundlesRetrieved()
	{
		Domain domain1 = new Domain();
		domain1.setDomainName("Test Domain");
		domain1 = dmRepo.save(domain1);
		
		Domain domain2 = new Domain();
		domain2.setDomainName("Test Domain 2");
		domain2 = dmRepo.save(domain2);
		
		TrustBundle bundle1 = new TrustBundle();
		bundle1.setBundleName("Test Bundle");
		bundle1.setBundleURL("http://test/url/bundle");
		bundle1.setCheckSum("1234");
		bundle1 = tbRepo.save(bundle1);
		
		TrustBundle bundle2 = new TrustBundle();
		bundle2.setBundleName("Test Bundle2");
		bundle2.setBundleURL("http://test/url/bundle2");
		bundle2.setCheckSum("1234");
		bundle2 = tbRepo.save(bundle2);
		
		TrustBundleDomainReltn reltn = new TrustBundleDomainReltn();
		reltn.setDomain(domain1);
		reltn.setTrustBundle(bundle1);
		reltn.setIncoming(true);
		reltn.setOutgoing(true);
		
		reltnRepo.save(reltn);
		
		
		reltn = new TrustBundleDomainReltn();
		reltn.setDomain(domain2);
		reltn.setTrustBundle(bundle2);
		reltn.setIncoming(true);
		reltn.setOutgoing(true);
		
		reltnRepo.save(reltn);
		
		Collection<TrustBundleDomainReltn> bundles = reltnRepo.findByDomain(domain1);
		assertEquals(1, bundles.size());
		
		Iterator<TrustBundleDomainReltn> bundleIter = bundles.iterator();
		assertEquals(bundle1.getBundleName(), bundleIter.next().getTrustBundle().getBundleName());
		
		bundles = reltnRepo.findByDomain(domain2);
		assertEquals(1, bundles.size());
		
		bundleIter = bundles.iterator();
		assertEquals(bundle2.getBundleName(), bundleIter.next().getTrustBundle().getBundleName());

	}	
	

	@Test
	public void testGetTrustBundlesByDomain_multipleAssociationsExist_bundleToMultipeDomains_assertBundlesRetrieved()
	{
		Domain domain1 = new Domain();
		domain1.setDomainName("Test Domain 1");
		domain1 = dmRepo.save(domain1);
		
		Domain domain2 = new Domain();
		domain2.setDomainName("Test Domain 2");
		domain2 = dmRepo.save(domain2);
		
		TrustBundle bundle1 = new TrustBundle();
		bundle1.setBundleName("Test Bundle1");
		bundle1.setBundleURL("http://test/url/bundle1");
		bundle1.setCheckSum("1234");
		bundle1 = tbRepo.save(bundle1);
		
		TrustBundleDomainReltn reltn = new TrustBundleDomainReltn();
		reltn.setDomain(domain1);
		reltn.setTrustBundle(bundle1);
		reltn.setIncoming(true);
		reltn.setOutgoing(true);
		
		reltnRepo.save(reltn);
		
		reltn = new TrustBundleDomainReltn();
		reltn.setDomain(domain2);
		reltn.setTrustBundle(bundle1);
		reltn.setIncoming(true);
		reltn.setOutgoing(true);
		
		reltnRepo.save(reltn);
		
		Collection<TrustBundleDomainReltn> bundles = reltnRepo.findByDomain(domain1);
		assertEquals(1, bundles.size());
		
		Iterator<TrustBundleDomainReltn> bundleIter = bundles.iterator();
		assertEquals(bundle1.getBundleName(), bundleIter.next().getTrustBundle().getBundleName());
		
		bundles = reltnRepo.findByDomain(domain2);
		assertEquals(1, bundles.size());
		
		bundleIter = bundles.iterator();
		assertEquals(bundle1.getBundleName(), bundleIter.next().getTrustBundle().getBundleName());

	}
	
	@Test
	public void testGetTrustBundlesByDomain_noBundlesInDomain_assertBundlesNotRetrieved()
	{
		Domain domain1 = new Domain();
		domain1.setDomainName("Test Domain 1");
		domain1 = dmRepo.save(domain1);
		
		Collection<TrustBundleDomainReltn> bundles = reltnRepo.findByDomain(domain1);
		assertEquals(0, bundles.size());
	}	
}
