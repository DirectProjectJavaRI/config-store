package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.nhindirect.config.store.Domain;
import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleDomainReltn;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

@Transactional
public class TrustBundleRepository_disassociateTrustBundleFromDomainsTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testDisassociateTrustBundleFromDomains_associateDomainAndBundle_assertAssociationRemoved()
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
		
		reltnRepo.deleteByTrustBundleId(bundle.getId()).block();
		
		bundles = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(0, bundles.size());
	}
	

}
