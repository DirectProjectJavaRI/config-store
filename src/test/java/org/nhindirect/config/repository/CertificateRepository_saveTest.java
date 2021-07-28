package org.nhindirect.config.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.nhindirect.common.crypto.CryptoExtensions;
import org.nhindirect.config.SpringBaseTest;
import org.nhindirect.config.model.utils.CertUtils;
import org.nhindirect.config.store.Certificate;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.test.StepVerifier;

public class CertificateRepository_saveTest extends SpringBaseTest
{	
	@Autowired
	private CertificateRepository repo;
	
	static
	{
		CryptoExtensions.registerJCEProviders();
	}	
	
	@BeforeEach
	public void cleanDataBase()
	{
		repo.deleteAll().block();
	}
	
	@Test
	public void saveWithCertAndPrivKeyData() throws Exception
	{
		
		final byte[] certData = FileUtils.readFileToByteArray(new File("./src/test/resources/certs/gm2552.der"));
		final byte[] keyData = FileUtils.readFileToByteArray(new File("./src/test/resources/certs/gm2552Key.der"));
		
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
