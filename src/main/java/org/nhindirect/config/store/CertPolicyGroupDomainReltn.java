package org.nhindirect.config.store;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("certpolicygroupdomainreltn")
public class CertPolicyGroupDomainReltn 
{
	@Id
	private Long id;
	
	@Column("domain_id")
	private Long domainId;
	
	@Column("policy_group_id")
	private Long policyGroupId;
	
	public CertPolicyGroupDomainReltn()
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
    * Gets the value of the policy group.
    * 
    * @return The value of the policy group.
    */
   public Long getCertPolicyGroupId() 
   {
       return policyGroupId;
   }
   
   /**
    * Sets the value of the policy group.
    * 
    * @param policyGroupId The value of the policy group.
    */
   public void setCertPolicyGroupId(Long policyGroupId)
   {
	   this.policyGroupId = policyGroupId;
   }
   
   
   /**
    * Gets the value of the domain.
    * 
    * @return The value of the domain.
    */
   public Long getDomainId() 
   {
       return domainId;
   }
   
   /**
    * Sets the value of the domain.
    * 
    * @param domainId The value of the domain.
    */
   public void setDomainId(Long domainId)
   {
	   this.domainId = domainId;
   }    
}
