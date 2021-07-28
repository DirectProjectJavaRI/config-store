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

package org.nhindirect.config.store;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.Enumeration;

import org.nhindirect.common.cert.Thumbprint;
import org.nhindirect.common.crypto.CryptoExtensions;
import org.nhindirect.config.model.utils.CertUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

/**
 * The JPA Certificate class
 */
@Table
@Data
public class Certificate 
{

	static
	{
		CryptoExtensions.registerJCEProviders();
	}	
	
    public static final byte[] NULL_CERT = new byte[] {};

    private String owner;
    private String thumbprint;
    
    @Id
    private Long id;
    
    @Column("certificateData")
    private byte[] data;
    
    @Column("createTime")
    private LocalDateTime createTime = LocalDateTime.now();
    
    @Column("validStartDate")
    private LocalDateTime validStartDate;
    
    @Column("validEndDate")
    private LocalDateTime validEndDate;
    
    /*
     * Map to EntityStatus ordinal
     * Needed for backward compatibility
     */
    private int status;
    
    
    @Column("privateKey")
    private boolean privateKey;

    /**
     * Set the value of data.
     * 
     * @param data
     *            The value of data.
     * @throws CertificateException
     */
    public void setData(byte[] data) throws CertificateException {
        this.data = data;
        if (data == NULL_CERT) {
            setThumbprint("");
        } else {
        	loadCertFromEmptyProtectedData();
        }
    }

    public void setRawData(byte[] data)
    {
    	this.data = data;
    }
    
    /**
     * Validate the Certificate for the existance of data.
     * 
     * @throws CertificateException
     */
    public void validate() throws CertificateException {
        if (!hasData()) {
            throw new CertificateException("Invalid Certificate: no certificate data exists");
        }
    }

    private boolean hasData() {
        return ((data != null) && (!data.equals(NULL_CERT))) ? true : false;
    }

    /**
     * Clear the data of a Certificate.
     */
    public void clearData() {
        try {
            setData(NULL_CERT);
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void loadCertFromEmptyProtectedData() throws CertificateException
    {
    	loadCertFromData("".toCharArray(), "".toCharArray());
    }
    
    private void loadCertFromData(char[] keyStorePassPhrase, char[] privateKeyPassPhrase) throws CertificateException {
        X509Certificate cert = null;
        CertUtils.CertContainer container = null;
        try {
            validate();

            try
            {
                container = CertUtils.toCertContainer(data, keyStorePassPhrase, keyStorePassPhrase);
            	cert = container.getCert();
            }
            catch (Exception e)
            {
            	/*no-op*/
            }
            
            if (cert == null)
            {
            	// might be a URL for IPKIX
            	try
            	{
            		@SuppressWarnings("unused")
            		final URL url = new URL(new String(data, "ASCII"));
            	}
            	catch (Exception e)
            	{
            		// may not be a URL.. may be an encrypted stream that can't be accessed
            		// set the thumbprint to empty because the cert must be decrtyped
            	}
            	setThumbprint("");
            }
            else
            {
            	setThumbprint(Thumbprint.toThumbprint(cert).toString());
            	setPrivateKey(container != null && (container.getKey() != null || container.getWrappedKeyData() != null));
            }
            
            
        } catch (Exception e) {
            setData(NULL_CERT);
            throw new CertificateException("Data cannot be converted to a valid X.509 Certificate or IPKIX URL", e);
        }
    }
    
    /**
     * @deprecated
     * @return
     * @throws CertificateException
     */
    public CertContainer toCredential() throws CertificateException
    {
    	return toCredential("".toCharArray(), "".toCharArray());
    }
    
    /**
     * @deprecated
     * @return
     * @throws CertificateException
     */
    public CertContainer toCredential(char[] keyStorePass, char[] privateKeyPass) throws CertificateException
    {
    	CertContainer certContainer = null;
        try 
        {
            validate();
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            
            // lets try this a as a PKCS12 data stream first
            try
            {
            	KeyStore localKeyStore = KeyStore.getInstance("PKCS12", CryptoExtensions.getJCEProviderName());
            	
            	localKeyStore.load(bais, keyStorePass);
            	Enumeration<String> aliases = localKeyStore.aliases();


        		// we are really expecting only one alias 
        		if (aliases.hasMoreElements())        			
        		{
        			String alias = aliases.nextElement();
        			X509Certificate cert = (X509Certificate)localKeyStore.getCertificate(alias);
        			
    				// check if there is private key
    				Key key = localKeyStore.getKey(alias, privateKeyPass);
    				if (key != null && key instanceof PrivateKey) 
    				{
    					certContainer = new CertContainer(cert, key);
    					
    				}
        		}
            }
            catch (Exception e)
            {
            	// must not be a PKCS12 stream, go on to next step
            }
   
            if (certContainer == null)            	
            {
            	//try X509 certificate factory next       
                bais.reset();
                bais = new ByteArrayInputStream(data);

            	X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(bais);
            	certContainer = new CertContainer(cert, null);
            }
            bais.close();
        } 
        catch (Exception e) 
        {
            throw new CertificateException("Data cannot be converted to a valid X.509 Certificate", e);
        }
        
        return certContainer;
    }
    
    /**
     * @deprecated
     * @see org.nhindirect.config.model.utils.CertUtils.CertContainer
     * @author Greg Meyer
     */
    public static class CertContainer
    {
		private final X509Certificate cert;
    	private final Key key;
    	
    	public CertContainer(X509Certificate cert, Key key)
    	{
    		this.cert = cert;
    		this.key = key;
    	}
    	
    	public X509Certificate getCert() 
    	{
			return cert;
		}

		public Key getKey() 
		{
			return key;
		}
	
    }
}
