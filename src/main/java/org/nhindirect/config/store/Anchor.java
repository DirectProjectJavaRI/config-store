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
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;

import org.nhindirect.common.cert.Thumbprint;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

/**
 * The JPA Domain class
 */
@Table
@Data
public class Anchor {

    private String owner;
    
    private String thumbprint;
    
    @Column("certificateId")
    private long certificateId;
    
    @Column("certificateData")
    private byte[] certificateData;
    
    @Id
    private Long id;
    
    @Column("createTime")
    private LocalDateTime createTime;
    
    @Column("validStartDate")
    private LocalDateTime validStartDate;
    
    @Column("validEndDate")
    private LocalDateTime validEndDate;
    
    /*
     * Map to EntityStatus ordinal
     * Needed for backward compatibility
     */
    private int status;
    
    @Column("forIncoming")
    private boolean incoming;
    
    @Column("forOutgoing")
    private boolean outgoing;

    public void setCertificateData(byte[] data) throws CertificateException
    {
    	setData(data);
    }
    
    /**
     * Set the value of certificateData.
     * 
     * @param data
     *            The value of certificateData.
     * @throws CertificateException
     */
    public void setData(byte[] data) throws CertificateException {
        certificateData = data;
        if (data == Certificate.NULL_CERT) {
            setThumbprint("");
        } else {
            loadCertFromData();
        }
    }
    private X509Certificate loadCertFromData() throws CertificateException {
        X509Certificate cert = null;
        try {
            validate();
            ByteArrayInputStream bais = new ByteArrayInputStream(certificateData);
            cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(bais);
            setThumbprint(Thumbprint.toThumbprint(cert).toString());
            bais.close();
        } catch (Exception e) {
            setData(Certificate.NULL_CERT);
            throw new CertificateException("Data cannot be converted to a valid X.509 Certificate", e);
        }
        
        return cert;
    }

    public X509Certificate toCertificate() throws CertificateException {
        X509Certificate cert = null;
        try {
            validate();
            ByteArrayInputStream bais = new ByteArrayInputStream(certificateData);
            cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(bais);
            bais.close();
        } catch (Exception e) {
            throw new CertificateException("Data cannot be converted to a valid X.509 Certificate", e);
        }
        
        return cert;
    }
    
    
    
    private boolean hasData() {
        return ((certificateData != null) && (!certificateData.equals(Certificate.NULL_CERT))) ? true : false;
    }

    /**
     * Validate the Anchor for the existance of data.
     * 
     * @throws CertificateException
     */
    public void validate() throws CertificateException {
        if (!hasData()) {
            throw new CertificateException("Invalid Certificate: no certificate data exists");
        }
    }

}
