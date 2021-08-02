package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import org.nhindirect.config.store.Domain;
import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleDomainReltn;

import reactor.test.StepVerifier;


public class TrustBundleRepository_disassociateTrustBundlesFromDomainTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testDisassociateTrustBundlesFromDomain_associateDomainAndBundle_assertAssociationRemoved()
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
		
		reltnRepo.deleteByDomainId(domain.getId()).block();
		
		bundles = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(0, bundles.size());
	}
}
