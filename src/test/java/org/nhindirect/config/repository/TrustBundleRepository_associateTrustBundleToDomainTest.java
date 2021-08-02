package org.nhindirect.config.repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.nhindirect.config.store.Domain;
import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleDomainReltn;
import org.springframework.dao.DataIntegrityViolationException;

import reactor.test.StepVerifier;


public class TrustBundleRepository_associateTrustBundleToDomainTest extends TrustBundleDaoBaseTest
{
	@Test
	public void testAssociateTrustBundleToDomain_associateDomainAndBundle_assertAssociationAdded()
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
		reltn.setOutgoing(false);
		
		reltnRepo.save(reltn)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
		final List<TrustBundleDomainReltn> bundleReltn = reltnRepo.findByDomainId(domain.getId()).collectList().block();
		assertEquals(1, bundleReltn.size());
		reltn = bundleReltn.get(0);
		assertTrue(reltn.isIncoming());
		assertFalse(reltn.isOutgoing());
	}

	@Test
	public void testAssociateTrustBundleToDomain_unknownDomain_assertException()
	{
		Assertions.assertThrows(DataIntegrityViolationException.class, () ->
		{
			Domain domain = new Domain();
			domain.setDomainName("Test Domain");
			domain.setId(12345L);
			
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
			reltn.setOutgoing(false);
	
			reltnRepo.save(reltn).block();
		});

	}

	
	@Test
	public void testAssociateTrustBundleToDomain_unknownTrustBundle_assertException()
	{
		Assertions.assertThrows(DataIntegrityViolationException.class, () ->
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
			bundle.setId(12345L);
			
			TrustBundleDomainReltn reltn = new TrustBundleDomainReltn();
			reltn.setDomainId(domain.getId());
			reltn.setTrustBundleId(bundle.getId());
			reltn.setIncoming(true);
			reltn.setOutgoing(false);
			
			reltnRepo.save(reltn).block();	
		});
	}	
}
