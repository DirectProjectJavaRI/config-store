package org.nhindirect.config.store.util;

import java.security.cert.X509Certificate;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nhindirect.common.crypto.KeyStoreProtectionManager;
import org.nhindirect.config.model.exceptions.CertificateConversionException;
import org.nhindirect.config.model.utils.CertUtils;
import org.nhindirect.config.store.Certificate;
import org.nhindirect.config.store.CertificateException;
import org.nhindirect.config.store.EntityStatus;

public class CertificateUtils
{
	private static final Log log = LogFactory.getLog(CertificateUtils.class);
	
    public static Certificate stripP12Protection(Certificate cert, KeyStoreProtectionManager kspMgr)
    {
    	
    	log.debug("Attempting to strip p12 protection for certificate with id " + cert.getId());
    	
    	if (cert.isPrivateKey() && kspMgr != null)
    	{
        	log.debug("isPrivateKey = true ");
    		
    		final char[] emptyProtection = "".toCharArray();
    		try
    		{
    			log.debug("Attempting to convert to a container that is wrapped");
    			if (CertUtils.toCertContainer(cert.getData()) != null)
    				return cert;
    		}
    		catch (CertificateConversionException e)
    		{
    			log.trace("CertificateConversionException error when converting wrapped data.", e);
    			// no-op.. this just means that the certificate is probably protected by pass phrases
    			// need to convert
    		}
    		catch (Throwable t)
    		{
    			log.debug("Throwable error when converting wrapped data.", t);
    		}
    		
    		log.debug("Appears to not be wrapped.  Attempting to convert by changing p12 protection.");
			try
			{
	    		final String oldKeystorePassPhrase = new String(kspMgr.getKeyStoreProtectionKey().getEncoded());
				final String oldPrivateKeyPassPhrase = new String(kspMgr.getKeyStoreProtectionKey().getEncoded());
	    		
	    		// decrypt the key store and private key
				final byte[] data = CertUtils.changePkcs12Protection(cert.getData(), oldKeystorePassPhrase.toCharArray(), 
								oldPrivateKeyPassPhrase.toCharArray(), emptyProtection, emptyProtection);
				
				cert.setData(data);
			}
			catch (Exception e)
			{
				throw new RuntimeException("Error stripping P12 protection data", e);
			}
    	}

    	return cert;  // this is just a plain jane X509 Certificate
    }
    
    public static Certificate applyCertRepositoryAttributes(Certificate cert, KeyStoreProtectionManager kspMgr)
    {
    	try
    	{
    		CertUtils.CertContainer container = null;
    		X509Certificate xcert = null;
    		try
    		{
    			// this might be an X509Certificate or a P12 key store.. assume there is no protection for P12 key stores... 
    			container = CertUtils.toCertContainer(cert.getData());
    			xcert = container.getCert();
    		}
    		catch (Exception e)
    		{
    			// probably not a certificate but an IPKIX URL
    		}
    		
    		if (cert.getValidStartDate() == null && xcert != null)
    		{
    			Calendar startDate = Calendar.getInstance();
    			startDate.setTime(xcert.getNotBefore());
    			cert.setValidStartDate(startDate);
    		}
    		if (cert.getValidEndDate() == null && xcert != null)
    		{
    			Calendar endDate = Calendar.getInstance();
    			endDate.setTime(xcert.getNotAfter());
    			cert.setValidEndDate(endDate);
    		}

    		if (cert.getStatus() == null)
    			cert.setStatus(EntityStatus.NEW);
    		
    		cert.setPrivateKey(container != null && (container.getKey() != null || container.getWrappedKeyData() != null));
    		
    		// if the key store protection manager is set and this is a P12 file, convert the cert data into a protected P12 file
    		if (cert.isPrivateKey() && kspMgr != null && container.getKey() != null)
    		{
				try
				{
					final String newKeystorePassPhrase = new String(kspMgr.getKeyStoreProtectionKey().getEncoded());
					final String newPrivateKeyPassPhrase = new String(kspMgr.getPrivateKeyProtectionKey().getEncoded());
					
					cert.setRawData(CertUtils.changePkcs12Protection(cert.getData(), "".toCharArray(), "".toCharArray(), 
							newKeystorePassPhrase.toCharArray(), newPrivateKeyPassPhrase.toCharArray()));
				}
				catch (Exception e)
				{
					throw new RuntimeException("Error converting P12 to encrypted/protected format", e);
				}
    		}
    	}
    	catch (CertificateException e)
    	{
    		
    	}
    	
    	return cert;
    }
}
