package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.nhindirect.common.crypto.CryptoExtensions;
import org.nhindirect.config.SpringBaseTest;
import org.nhindirect.config.store.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CertificateRepositoryTest extends SpringBaseTest
{
	private static final String certBasePath = "src/test/resources/certs/"; 
	
	@Autowired
	private CertificateRepository repo;
	
	static
	{
		CryptoExtensions.registerJCEProviders();
	}	
	
	@Before
	public void cleanDataBase()
	{
		repo.deleteAll();
	}     	
	
	private static byte[] loadCertificateData(String certFileName) throws Exception
	{
		File fl = new File(certBasePath + certFileName);
		
		return FileUtils.readFileToByteArray(fl);
	}
	
	public static byte[] loadPkcs12FromCertAndKey(String certFileName, String keyFileName) throws Exception
	{
		byte[] retVal = null;
		try
		{
			KeyStore localKeyStore = KeyStore.getInstance("PKCS12", CryptoExtensions.getJCEProviderName());
			
			localKeyStore.load(null, null);
			
			byte[] certData = loadCertificateData(certFileName);
			byte[] keyData = loadCertificateData(keyFileName);
			
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream inStr = new ByteArrayInputStream(certData);
			java.security.cert.Certificate cert = cf.generateCertificate(inStr);
			inStr.close();
			
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PKCS8EncodedKeySpec keysp = new PKCS8EncodedKeySpec ( keyData );
			Key privKey = kf.generatePrivate (keysp);
			
			char[] array = "".toCharArray();
			
			localKeyStore.setKeyEntry("privCert", privKey, array,  new java.security.cert.Certificate[] {cert});
			
			ByteArrayOutputStream outStr = new ByteArrayOutputStream();
			localKeyStore.store(outStr, array);
			
			retVal = outStr.toByteArray();
			
			outStr.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return retVal;
	}
	
	@Test
	public void testCleanDatabase() throws Exception 
	{

		assertEquals(0, repo.findAll().size());
	}
	
	@Test 
	public void testAddPublicCert() throws Exception
	{
	
		byte[] certData = loadCertificateData("gm2552.der");
		
		Certificate cert = new Certificate();
		cert.setData(certData);
		cert.setOwner("gm2552@cerner.com");
		
		repo.save(cert);
				
		Collection<Certificate> certificates = repo.findAll();
		assertEquals(1, certificates.size());
	}
	
	@Test 
	public void testAddPKIXURL() throws Exception
	{
		testCleanDatabase();
		
		Certificate cert = new Certificate();
		cert.setData("http://localhost/test.der".getBytes());
		cert.setOwner("gm2552@cerner.com");
		
		repo.save(cert);
				
		Collection<Certificate> certificates = repo.findAll();
		assertEquals(1, certificates.size());
		
		Certificate addedCert = certificates.iterator().next();
		
		assertEquals("", addedCert.getThumbprint());
		assertEquals("http://localhost/test.der", new String(addedCert.getData()));
		
	}
	
	@Test 
	public void testAddPrivateCert() throws Exception
	{
		testCleanDatabase();
	
		byte[] certData = loadPkcs12FromCertAndKey("gm2552.der", "gm2552Key.der");
		
		Certificate cert = new Certificate();
		cert.setData(certData);
		cert.setOwner("gm2552@cerner.com");
		
		repo.save(cert);
				
		Collection<Certificate> certificates = repo.findAll();
		assertEquals(1, certificates.size());
	}	
	
	@Test 
	public void testGetByOwner() throws Exception
	{
		testCleanDatabase();
	
		byte[] certData = loadPkcs12FromCertAndKey("gm2552.der", "gm2552Key.der");
		
		Certificate cert = new Certificate();
		cert.setData(certData);
		cert.setOwner("gm2552@cerner.com");
		
		repo.save(cert);
				
		Collection<Certificate> certificates = repo.findByOwnerIgnoreCase("gm2552@cerner.com");
		assertEquals(1, certificates.size());
		cert = certificates.iterator().next();
		
		assertEquals("gm2552@cerner.com", cert.getOwner());
		
		
		repo.deleteAll();
		
		certData = loadCertificateData("gm2552.der");
		
		cert = new Certificate();
		cert.setData(certData);
		cert.setOwner("gm2552@cerner.com");
		
		repo.save(cert);
				
		certificates = repo.findByOwnerIgnoreCase("gm2552@cerner.com");
		assertEquals(1, certificates.size());
		cert = certificates.iterator().next();
		
		assertEquals("gm2552@cerner.com", cert.getOwner());
	}	
	
	@Test 
	public void testGetById() throws Exception
	{
		testCleanDatabase();
	
		byte[] certData = loadPkcs12FromCertAndKey("gm2552.der", "gm2552Key.der");
		
		Certificate cert = new Certificate();
		cert.setData(certData);
		cert.setOwner("gm2552@cerner.com");
		
		repo.save(cert);
				
		Collection<Certificate> certificates = repo.findByOwnerIgnoreCase("gm2552@cerner.com");
		assertEquals(1, certificates.size());
		cert = certificates.iterator().next();
		certificates = repo.findAllById(Arrays.asList(cert.getId()));
		
		assertEquals(1, certificates.size());
		cert = certificates.iterator().next();
		
		assertEquals("gm2552@cerner.com", cert.getOwner());
		
	}		
	
	@Test 
	public void testDeleteByOwner() throws Exception
	{
		testCleanDatabase();
	
		byte[] certData = loadPkcs12FromCertAndKey("gm2552.der", "gm2552Key.der");
		
		Certificate cert = new Certificate();
		cert.setData(certData);
		cert.setOwner("gm2552@cerner.com");
		
		repo.save(cert);
				
		Collection<Certificate> certificates = repo.findByOwnerIgnoreCase("gm2552@cerner.COM");
		assertEquals(1, certificates.size());
		cert = certificates.iterator().next();		
		assertEquals("gm2552@cerner.com", cert.getOwner());
		
		repo.deleteByOwnerIgnoreCase("gm2552@cerner.com");
		certificates = repo.findByOwnerIgnoreCase("gm2552@cerner.com");
		assertEquals(0, certificates.size());
	}		
	
	@Test 
	public void testDeleteById() throws Exception
	{
		testCleanDatabase();
	
		byte[] certData = loadPkcs12FromCertAndKey("gm2552.der", "gm2552Key.der");
		
		Certificate cert = new Certificate();
		cert.setData(certData);
		cert.setOwner("gm2552@cerner.com");
		
		repo.save(cert);
				
		Collection<Certificate> certificates = repo.findByOwnerIgnoreCase("gm2552@cerner.com");
		assertEquals(1, certificates.size());
		cert = certificates.iterator().next();		
		assertEquals("gm2552@cerner.com", cert.getOwner());
		
		repo.deleteById(cert.getId());
		certificates = repo.findByOwnerIgnoreCase("gm2552@cerner.com");
		assertEquals(0, certificates.size());
	}		
}
