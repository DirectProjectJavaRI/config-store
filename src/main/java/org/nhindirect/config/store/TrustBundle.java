/* 
Copyright (c) 2010, NHIN Direct Project
All rights reserved.

Authors:
   Greg Meyer      gm2552@cerner.com
 
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
in the documentation and/or other materials provided with the distribution.  Neither the name of the The NHIN Direct Project (nhindirect.org). 
nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.nhindirect.config.store;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

/**
 * JPA entity object for a trust bundle
 * @author Greg Meyer
 * @since 1.2
 */
@Table("trustbundle")
@Data
public class TrustBundle 
{
	@Id
	private Long id;
	
	@Column("bundleName")
	private String bundleName;
	
	@Column("bundleURL")
	private String bundleURL;
	
	@Column("signingCertificateData")	
    private byte[] signingCertificateData;
	
	@Column("refreshInterval")	
    private int refreshInterval = 0;
	
	@Column("lastRefreshAttempt")		
    private LocalDateTime lastRefreshAttempt;
    
	@Column("lastRefreshError")	
    private int lastRefreshError;
	
	@Column("lastSuccessfulRefresh")	
    private LocalDateTime lastSuccessfulRefresh;

	@Column("createTime")	
    private LocalDateTime createTime = LocalDateTime.now();
	
	@Column("getCheckSum")	
    private String checkSum = "";
    
    /**
     * Converts the signing data into an X509 certificate
     * @return The signing data as an X509 certificate
     * @throws CertificateException
     */
    public X509Certificate toSigningCertificate() throws CertificateException 
    {
        X509Certificate cert = null;
        try 
        {
            validate();
            ByteArrayInputStream bais = new ByteArrayInputStream(signingCertificateData);
            cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(bais);
            bais.close();
        } 
        catch (Exception e) 
        {
            throw new CertificateException("Data cannot be converted to a valid X.509 Certificate", e);
        }
        
        return cert;
    } 
    
    /**
     * Validates that the bundle has valid and complete data
     * @throws CertificateException
     */
    public void validate() throws CertificateException 
    {
        if (!hasData()) 
        {
            throw new CertificateException("Invalid Certificate: no certificate data exists");
        }
    } 
    
    private boolean hasData()
    {
        return ((signingCertificateData != null) && (!signingCertificateData.equals(Certificate.NULL_CERT))) ? true : false;
    }
}
