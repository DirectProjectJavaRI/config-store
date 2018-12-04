package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.nhindirect.config.store.Domain;
import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleDomainReltn;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TrustBundleRepository_disassociateTrustBundleFromDomainsTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testDisassociateTrustBundleFromDomains_associateDomainAndBundle_assertAssociationRemoved()
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
		
		reltnRepo.deleteByTrustBundle(bundle);
		
		bundles = reltnRepo.findByDomain(domain);
		assertEquals(0, bundles.size());
	}
	

}
