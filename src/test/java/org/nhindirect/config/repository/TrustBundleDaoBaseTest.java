package org.nhindirect.config.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.nhindirect.common.crypto.CryptoExtensions;
import org.nhindirect.config.SpringBaseTest;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.test.StepVerifier;

public abstract class TrustBundleDaoBaseTest extends SpringBaseTest
{
	@Autowired
	protected TrustBundleRepository tbRepo;	
	
	@Autowired
	protected TrustBundleAnchorRepository tbAncRepo;	
	
	@Autowired
	protected DomainRepository dmRepo;		
	
	@Autowired
	protected TrustBundleDomainReltnRepository reltnRepo;		
	
	protected static final String certBasePath = "src/test/resources/certs/"; 
	
	static
	{
		CryptoExtensions.registerJCEProviders();
	}	
	
	protected static byte[] loadCertificateData(String certFileName) throws Exception
	{
		File fl = new File(certBasePath + certFileName);
		
		return FileUtils.readFileToByteArray(fl);
	}
	
	@BeforeEach
	public void setUp()
	{
		super.setUp();
		
		clearRetlns();
		
		clearBundles();
		
		clearDomains();
	}
	
	@AfterEach
	public void tearDown()
	{
		clearRetlns();
		
		clearBundles();
		
		clearDomains();
	}
	
	protected void clearRetlns()
	{
		reltnRepo.deleteAll().block();
		
		reltnRepo.findAll()
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
	}
	
	protected void clearBundles()
	{
		tbAncRepo.deleteAll().block();
		
		tbAncRepo.findAll()
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
		
		tbRepo.deleteAll().block();
		
		tbRepo.findAll()
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
	}
	
	
	protected void clearDomains()
	{
		dmRepo.deleteAll().block();
		
		dmRepo.findAll()
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
	}
	
}
