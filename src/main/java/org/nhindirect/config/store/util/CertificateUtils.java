/* 
Copyright (c) 2010, NHIN Direct Project
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
   in the documentation and/or other materials provided with the distribution.  
3. Neither the name of the The NHIN Direct Project (nhindirect.org) nor the names of its contributors may be used to endorse or promote 
   products derived from this software without specific prior written permission.
   
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.nhindirect.config.store.util;

import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    			LocalDateTime startDate = new Timestamp(xcert.getNotBefore().getTime()).toLocalDateTime();
    			cert.setValidStartDate(startDate);
    		}
    		if (cert.getValidEndDate() == null && xcert != null)
    		{
    			LocalDateTime endDate = new Timestamp(xcert.getNotAfter().getTime()).toLocalDateTime();
    			cert.setValidEndDate(endDate);
    		}

    		if (cert.getStatus() < 0)
    			cert.setStatus(EntityStatus.NEW.ordinal());
    		
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
