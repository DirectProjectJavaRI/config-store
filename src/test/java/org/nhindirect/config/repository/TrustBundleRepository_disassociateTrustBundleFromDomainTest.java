package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.nhindirect.config.store.Domain;
import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleDomainReltn;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

@Transactional
public class TrustBundleRepository_disassociateTrustBundleFromDomainTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testDisassociateTrustBundleFromDomain_associateDomainAndBundle_assertAssociationRemoved()
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
		
		List<TrustBundleDomainReltn> bundles = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(1, bundles.size());
		
		TrustBundleDomainReltn foudnReltn = bundles.get(0);
		assertEquals(foudnReltn.getDomainId(), domain.getId());
		assertEquals(foudnReltn.getTrustBundleId(), bundle.getId());
		
		reltnRepo.deleteByDomainId(domain.getId()).block();
		
		bundles = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(0, bundles.size());
	}
	
	@Test
	public void testDisassociateTrustBundleFromDomainByBothIds_associateDomainAndBundle_assertAssociationRemoved()
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
		
		List<TrustBundleDomainReltn> bundles = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(1, bundles.size());
		
		TrustBundleDomainReltn foudnReltn = bundles.get(0);
		assertEquals(foudnReltn.getDomainId(), domain.getId());
		assertEquals(foudnReltn.getTrustBundleId(), bundle.getId());
		
		reltnRepo.deleteByDomainIdAndTrustBundleId(domain.getId(), bundle.getId()).block();
		
		bundles = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(0, bundles.size());
	}

}