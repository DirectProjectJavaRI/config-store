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

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * JPA entity object for a trust bundle to domain relationship
 * @author Greg Meyer
 * @since 1.2
 */
@Table("trustbundledomainreltn")
public class TrustBundleDomainReltn 
{
	@Id
	private Long id;
	
	@Column("domain_id")
	private Long domainId;
	
	@Column("trust_bundle_id")
	private Long trustBundleId;
	
	@Column("forIncoming")
    private boolean incoming;
    
	@Column("forOutgoing")
    private boolean outgoing;
	
	public TrustBundleDomainReltn()
	{
		
	}
	
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
    * Gets the value of the trust bundle id
    * 
    * @return The value of the trust bundld id
    */
   public Long getTrustBundleId() 
   {
       return trustBundleId;
   }
   
   /**
    * Sets the value of the trust bundle id
    * 
    * @param bundleId The value of the trust bundle id
    */
   public void setTrustBundleId(Long bundleId)
   {
	   this.trustBundleId= bundleId;
   }
   
   
   /**
    * Gets the value of the domain id
    * 
    * @return The value of the domain id
    */
   public Long getDomainId() 
   {
       return domainId;
   }
   
   /**
    * Sets the value of the domain id
    * 
    * @param domainId The value of the domain id
    */
   public void setDomainId(Long domainId)
   {
	   this.domainId = domainId;
   }  
   
   /**
    * Get the value of incoming.
    * 
    * @return the value of incoming.
    */
   public boolean isIncoming() 
   {
       return incoming;
   }

   /**
    * Set the value of incoming.
    * 
    * @param incoming
    *            The value of incoming.
    */
   public void setIncoming(boolean incoming) 
   {
       this.incoming = incoming;
   } 
   
   /**
    * Get the value of outgoing.
    * 
    * @return the value of outgoing.
    */
   public boolean isOutgoing() {
       return outgoing;
   }

   /**
    * Set the value of outgoing.
    * 
    * @param outgoing
    *            The value of outgoing.
    */
   public void setOutgoing(boolean outgoing) {
       this.outgoing = outgoing;
   }
}
