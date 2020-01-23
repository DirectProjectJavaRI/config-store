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
import java.sql.Timestamp;
import java.time.LocalDateTime;


import org.nhindirect.common.cert.Thumbprint;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * JPA entity object for a trust bundle anchor
 * @author Greg Meyer
 * @since 1.2
 */
@Table("trustbundleanchor")
public class TrustBundleAnchor 
{
	@Id
    private Long id;
	
    @Column("trustBundleId")
    private Long trustBundleId;
    
    @Column("anchorData")
    private byte[] anchorData;
    
    private String thumbprint;
    
    @Column("validStartDate")
    private LocalDateTime validStartDate;
    
    @Column("validEndDate")
    private LocalDateTime validEndDate;
    
    /**
     * Get the value of id.
     * 
     * @return the value of id.
     */
    public Long getId() 
    {
        return id;
    }

    /**
     * Set the value of id.
     * 
     * @param id
     *            The value of id.
     */
    public void setId(Long id) 
    {
        this.id = id;
    } 
    
    /**
     * Get the value of thumbprint.
     * 
     * @return the value of thumbprint.
     */
    public String getThumbprint() 
    {
        return thumbprint;
    }

    /**
     * Set the value of thumbprint.
     * 
     * @param thumbprint
     *            The value of thumbprint.
     */
    public void setThumbprint(String thumbprint) 
    {
        this.thumbprint = thumbprint;
    }    
    
    /**
     * Get the value of the trust bundle Id.
     * 
     * @return the value of trust bundle Id.
     */
    public Long getTrustBundleId() 
    {
        return trustBundleId;
    }

    /**
     * Set the value of the trust bundle Id.
     * 
     * @param trustBundleId
     *            The value of the trust bundle Id.
     */
    public void setTrustBundleId(Long trustBundleId) 
    {
        this.trustBundleId = trustBundleId;

    }
    
    /**
     * Get the value of anchorData.
     * 
     * @return the value of anchorData Data.
     */
    public byte[] getData() 
    {
        return anchorData;
    }

    /**
     * Set the value of anchorData.
     * 
     * @param data
     *            The value of anchorData.
     * @throws CertificateException
     */
    public void setData(byte[] data) throws CertificateException 
    {
    	anchorData = data;
        if (data == Certificate.NULL_CERT) 
        {
            setThumbprint("");
        } 
        else 
        {
            loadCertFromData();
        }
    }    
    
    /**
     * Get the value of validStartDate.
     * 
     * @return the value of validStartDate.
     */
    public LocalDateTime getValidStartDate() 
    {
        return validStartDate;
    }

    /**
     * Set the value of validStartDate.
     * 
     * @param validStartDate
     *            The value of validStartDate.
     */
    public void setValidStartDate(LocalDateTime validStartDate) 
    {
        this.validStartDate = validStartDate;
    }

    /**
     * Get the value of validEndDate.
     * 
     * @return the value of validEndDate.
     */
    public LocalDateTime getValidEndDate() 
    {
        return validEndDate;
    }

    /**
     * Set the value of validEndDate.
     * 
     * @param validEndDate
     *            The value of validEndDate.
     */
    public void setValidEndDate(LocalDateTime validEndDate) {
        this.validEndDate = validEndDate;
    }    
    
    private X509Certificate loadCertFromData() throws CertificateException 
    {
        X509Certificate cert = null;
        try {
            validate();
            final ByteArrayInputStream bais = new ByteArrayInputStream(anchorData);
            cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(bais);
            setThumbprint(Thumbprint.toThumbprint(cert).toString());
            
            final LocalDateTime calEndTime = new Timestamp(cert.getNotAfter().getTime()).toLocalDateTime();
            final LocalDateTime calStartTime = new Timestamp(cert.getNotBefore().getTime()).toLocalDateTime();
            
            this.setValidEndDate(calEndTime);
            this.setValidStartDate(calStartTime);
            bais.close();
        } catch (Exception e) {
            setData(Certificate.NULL_CERT);
            throw new CertificateException("Data cannot be converted to a valid X.509 Certificate", e);
        }
        
        return cert;
    }

    /**
     * Converts the anchor data to an X509 certificate
     * @return The anchor data as an X509 certificate
     * @throws CertificateException
     */
    public X509Certificate toCertificate() throws CertificateException 
    {
        X509Certificate cert = null;
        try 
        {
            validate();
            final ByteArrayInputStream bais = new ByteArrayInputStream(anchorData);
            cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(bais);
            bais.close();
        } 
        catch (Exception e) 
        {
            throw new CertificateException("Data cannot be converted to a valid X.509 Certificate", e);
        }
        
        return cert;
    }   
    
    private boolean hasData()
    {
        return ((anchorData != null) && (!anchorData.equals(Certificate.NULL_CERT))) ? true : false;
    }
    
    /**
     * Validate the Anchor for the existance of data.
     * 
     * @throws CertificateException
     */
    public void validate() throws CertificateException 
    {
        if (!hasData()) 
        {
            throw new CertificateException("Invalid Certificate: no certificate data exists");
        }
    }    
}
