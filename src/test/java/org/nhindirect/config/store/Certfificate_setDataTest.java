package org.nhindirect.config.store;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.nhindirect.config.model.utils.CertUtils;
import org.nhindirect.config.repository.CertificateRepositoryTest;
import org.nhindirect.config.store.Certificate;

public class Certfificate_setDataTest 
{
	@Test
	public void testSetData_setWithProtectedData() throws Exception 
	{
		final byte[] certData = CertificateRepositoryTest.loadPkcs12FromCertAndKey("gm2552.der", "gm2552Key.der");
		
		final byte[] protectedCertData =  CertUtils.changePkcs12Protection(certData, "".toCharArray(), "".toCharArray(), 
				"12345".toCharArray(), "67890".toCharArray());
		
		Certificate cert = new Certificate();
		cert.setData(protectedCertData);
		// just make sure an exception didn't happen here
	}
	
	@Test
	public void testSetData_setCertAndKeyData() throws Exception 
	{
		final byte[] certData = FileUtils.readFileToByteArray(new File("./src/test/resources/certs/gm2552.der"));
		final byte[] keyData = FileUtils.readFileToByteArray(new File("./src/test/resources/certs/gm2552Key.der"));
		
		final byte[] rawCertData = CertUtils.certAndWrappedKeyToRawByteFormat(keyData, CertUtils.toX509Certificate(certData));
		
		Certificate cert = new Certificate();
		cert.setData(rawCertData);
		// just make sure an exception didn't happen here
	}
}
