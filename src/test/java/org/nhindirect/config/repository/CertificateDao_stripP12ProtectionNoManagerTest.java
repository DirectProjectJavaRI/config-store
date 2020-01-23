package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.nhindirect.common.crypto.CryptoExtensions;
import org.nhindirect.config.SpringBaseTest;
import org.nhindirect.config.model.utils.CertUtils;
import org.nhindirect.config.store.Certificate;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.test.StepVerifier;


public class CertificateDao_stripP12ProtectionNoManagerTest extends SpringBaseTest
{
	private static final String certBasePath = "src/test/resources/certs/"; 
	
	@Autowired
	private CertificateRepository repo;
	
	static
	{
		CryptoExtensions.registerJCEProviders();
	}	    	
	
	private static byte[] loadCertificateData(String certFileName) throws Exception
	{
		final File fl = new File(certBasePath + certFileName);
		
		return FileUtils.readFileToByteArray(fl);
	}

	
	@Before
	public void cleanDataBase()
	{
		repo.deleteAll().block();
	}
	
	
	protected Certificate populateCert(String certFile, String keyFile) throws Exception
	{
		
		final byte[] certData = (keyFile != null && !keyFile.isEmpty()) ? 
				CertificateRepositoryTest.loadPkcs12FromCertAndKey(certFile, keyFile) :
					loadCertificateData(certFile);
		
		Certificate cert = new Certificate();
		cert.setData(certData);
		cert.setOwner("gm2552@cerner.com");
		
		repo.save(cert)
		.as(StepVerifier::create)
		.expectNextCount(1)
		.verifyComplete();
		
		return cert;
	}
	
	@Test
	public void testStripP12ProtectionTest_NoP12ProtectionOrManager_assertP12Returned() throws Exception
	{
		populateCert("gm2552.der", "gm2552Key.der");
				
		Certificate cert = repo.findAll().blockFirst();
		
		assertTrue(cert.isPrivateKey());
		final byte[] certData = CertificateRepositoryTest.loadPkcs12FromCertAndKey("gm2552.der", "gm2552Key.der");

		CertUtils.CertContainer container = CertUtils.toCertContainer(certData);
		
		assertEquals(container.getCert(), CertUtils.toCertContainer(cert.getData()).getCert());
		
	}
	
	@Test
	public void testStripP12ProtectionTest_X509CertAndNoManager_assertX509Returned() throws Exception
	{
		populateCert("gm2552.der", null);
				
		Certificate cert = repo.findAll().blockFirst();
		
		assertFalse(cert.isPrivateKey());
		final byte[] certData = loadCertificateData("gm2552.der");

		CertUtils.CertContainer container = CertUtils.toCertContainer(certData);
		
		assertEquals(container.getCert(), CertUtils.toCertContainer(cert.getData()).getCert());
		
	}
	
	@Test
	public void testStripP12ProtectionTest_X509CertAndWrappedData_noMager_assertX509Returned() throws Exception
	{				

		final byte[] certData = loadCertificateData("gm2552.der");
		final byte[] keyData = loadCertificateData("gm2552Key.der");
		
		Certificate addCert = new Certificate();
		addCert.setData(CertUtils.certAndWrappedKeyToRawByteFormat(keyData, CertUtils.toX509Certificate(certData)));
		addCert.setOwner("gm2552@cerner.com");
		
		repo.save(addCert)
		.as(StepVerifier::create)
		.expectNextCount(1)
		.verifyComplete();

		
		final Certificate cert = repo.findAll().blockFirst();
		
		assertTrue(cert.isPrivateKey());

		CertUtils.CertContainer container = CertUtils.toCertContainer(certData);
		
		assertEquals(container.getCert(), CertUtils.toCertContainer(cert.getData()).getCert());
		
	}
	
}
