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

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * The JPA Domain class
 */
@Table
public class Domain 
{

	public static long MAGIC_POSTMASTER_ID = -2;
	
	@Column("domainName")
    private String domainName;

    @Column("createTime")
    private LocalDateTime createTime;

    @Column("updateTime")
    private LocalDateTime updateTime;

    @Column("postmasterAddressId")
    private Long postmasterAddressId;

    @Id
    private Long id;

    private int status = EntityStatus.NEW.ordinal();

    /**
     * Construct a Domain.
     */
    public Domain() {
    }

    /**
     * Construct a Domain.
     * 
     * @param aName
     *            The domain name.
     */
    public Domain(String aName) {
        setDomainName(aName);
        setCreateTime(LocalDateTime.now());
        setUpdateTime(LocalDateTime.now());
        setStatus(EntityStatus.NEW.ordinal());
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
     * @param anId
     *            The value of id.
     */
    public void setId(Long anId) {
        id = anId;
    }

    /**
     * Get the value of domainName.
     * 
     * @return the value of domainName.
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * Get the value of createTime.
     * 
     * @return the value of createTime.
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * Get the value of postmasterAddressId.
     * 
     * @return the value of postmasterAddressId.
     */
    public Long getPostmasterAddressId() {
        return postmasterAddressId;
    }

    /**
     * Set the value of postmasterAddressId.
     * 
     * @param anId
     *            The value of postmasterAddressId.
     */
    public void setPostmasterAddressId(Long anId) {
        postmasterAddressId = anId;
    }

    /**
     * Get the value of updateTime.
     * 
     * @return the value of updateTime.
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * Get the value of status.
     * 
     * @return the value of status.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Set the value of domainName.
     * 
     * @param aName
     *            The value of domainName.
     */
    public void setDomainName(String aName) {
        domainName = aName;
    }

    /**
     * Set the value of createTime.
     * 
     * @param timestamp
     *            The value of createTime.
     */
    public void setCreateTime(LocalDateTime timestamp) {
        createTime = timestamp;
    }

    /**
     * Set the value of updateTime.
     * 
     * @param timestamp
     *            The value of updateTime.
     */
    public void setUpdateTime(LocalDateTime timestamp) {

        updateTime = timestamp;
    }

    /**
     * Set the value of status.
     * 
     * @param aStatus
     *            The value of status.
     */
    public void setStatus(int aStatus) {
        status = aStatus;
    }

    /**
     * Verify the Domain is valid.
     * 
     * @return true if the Domain is valid, false otherwise.
     */
    public boolean isValid() {
        boolean result = false;
        if ((getDomainName() != null)
                && (getDomainName().length() > 0)
                && ((getStatus() == EntityStatus.ENABLED.ordinal())) || (getStatus()  == EntityStatus.DISABLED.ordinal()) || ((getStatus()
                        == EntityStatus.NEW.ordinal()) && (getId() == 0L))) {
            result = true;
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[ID: " + getId() + " | Domain: " + getDomainName() + " | Status: " + EntityStatus.values()[getStatus()].toString() + "]";
    }

    @Override
    public boolean equals(Object other)
    {
    	boolean result = false;
    	if (other == null)
    		return false;
    	
    	if (other instanceof Domain)
    	{
    		final Domain otherDomain = (Domain)other;
    		if (otherDomain.id == this.id && otherDomain.domainName.equals(domainName) && otherDomain.postmasterAddressId == postmasterAddressId
    				&& otherDomain.status == status)
    		{
    				result = true;
    		}
    	}
    	
    	return result;
    }
    
}
