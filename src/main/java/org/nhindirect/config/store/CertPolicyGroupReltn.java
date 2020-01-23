package org.nhindirect.config.store;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("certpolicygroupreltn")
public class CertPolicyGroupReltn 
{
	@Id
	private Long id;
	
	@Column("certPolicyGroupId")
	private Long policyGroupId;
	
	@Column("certPolicyId")
	private Long policyId;
	
	@Column("policyUse")
	private int policyUse;
	
	private boolean incoming;
	
	private boolean outgoing;

	public CertPolicyGroupReltn()
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
    
    public Long getCertPolicyGroupId()
    {
    	return policyGroupId;
    }
    
    public void setCertPolicyGroupId(Long policyGroupId)
    {
    	this.policyGroupId = policyGroupId;
    }
    
    public Long getCertPolicyId()
    {
    	return policyId;
    }  
    
    public void setCertPolicyId(Long policyId)
    {
    	this.policyId = policyId;
    } 
    
    public int getPolicyUse()
    {
    	return policyUse;
    }

    public void setPolicyUse(int policyUse)
    {
    	this.policyUse = policyUse;
    }
    
    public boolean isIncoming() 
    {
        return incoming;
    }


    public void setIncoming(boolean incoming) 
    {
        this.incoming = incoming;
    }


    public boolean isOutgoing() 
    {
        return outgoing;
    }

    public void setOutgoing(boolean outgoing) 
    {
        this.outgoing = outgoing;
    }
}
