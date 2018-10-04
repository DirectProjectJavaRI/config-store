package org.nhindirect.config.repository;

import static org.junit.Assert.assertTrue;


import java.io.File;


import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.nhindirect.common.crypto.CryptoExtensions;
import org.nhindirect.config.SpringBaseTest;
import org.nhindirect.config.repository.TrustBundleRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class TrustBundleDaoBaseTest extends SpringBaseTest
{
	@Autowired
	protected TrustBundleRepository tbRepo;	
	
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
	
	@Before
	public void setUp()
	{
		clearRetlns();
		
		clearBundles();
		
		clearDomains();
	}
	
	@After
	public void tearDown()
	{
		clearRetlns();
		
		clearBundles();
		
		clearDomains();
	}
	
	protected void clearRetlns()
	{
		reltnRepo.deleteAll();
		
		assertTrue(reltnRepo.findAll().isEmpty());
	}
	
	protected void clearBundles()
	{
		tbRepo.deleteAll();
		
		assertTrue(tbRepo.findAll().isEmpty());
	}
	
	
	protected void clearDomains()
	{
		dmRepo.deleteAll();
		
		assertTrue(dmRepo.findAll().isEmpty());
	}
	
}
