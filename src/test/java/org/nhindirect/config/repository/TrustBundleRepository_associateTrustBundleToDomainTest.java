package org.nhindirect.config.repository;

import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.nhindirect.config.store.Domain;
import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleDomainReltn;
import org.springframework.dao.DataIntegrityViolationException;

public class TrustBundleRepository_associateTrustBundleToDomainTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testAssociateTrustBundleToDomain_associateDomainAndBundle_assertAssociationAdded()
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
		reltn.setOutgoing(false);
		
		reltnRepo.save(reltn);
		
		final Collection<TrustBundleDomainReltn> bundleReltn = reltnRepo.findByDomain(domain);
		assertEquals(1, bundleReltn.size());
		reltn = bundleReltn.iterator().next();
		assertTrue(reltn.isIncoming());
		assertFalse(reltn.isOutgoing());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void testAssociateTrustBundleToDomain_unknownDomain_assertException()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		domain.setId(12345L);
		
		TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://test/url/bundle");
		bundle.setCheckSum("1234");
		bundle = tbRepo.save(bundle);
		
		TrustBundleDomainReltn reltn = new TrustBundleDomainReltn();
		reltn.setDomain(domain);
		reltn.setTrustBundle(bundle);
		reltn.setIncoming(true);
		reltn.setOutgoing(false);

		reltnRepo.save(reltn);

	}

	@Test(expected=DataIntegrityViolationException.class)
	public void testAssociateTrustBundleToDomain_unknownTrustBundle_assertException()
	{
		Domain domain = new Domain();
		domain.setDomainName("Test Domain");
		domain = dmRepo.save(domain);
		
		TrustBundle bundle = new TrustBundle();
		bundle.setBundleName("Test Bundle");
		bundle.setBundleURL("http://test/url/bundle");
		bundle.setCheckSum("1234");
		bundle.setId(12345L);
		
		TrustBundleDomainReltn reltn = new TrustBundleDomainReltn();
		reltn.setDomain(domain);
		reltn.setTrustBundle(bundle);
		reltn.setDomain(domain);
		reltn.setIncoming(true);
		reltn.setOutgoing(false);
		
		reltnRepo.save(reltn);				
	}	
}
