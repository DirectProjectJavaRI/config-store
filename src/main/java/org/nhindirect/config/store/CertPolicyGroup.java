package org.nhindirect.config.store;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("certpolicygroup")
public class CertPolicyGroup 
{
	@Id
	private Long id;
	
	@Column("policyGroupName")
	private String policyGroupName;
	
	@Column("createTime")
    private LocalDateTime createTime;  	
		
	public CertPolicyGroup()
	{
		createTime = LocalDateTime.now();
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
     * Get the value of policyGroupName.
     * 
     * @return the value of policyGroupName.
     */
    public String getPolicyGroupName() 
    {
        return policyGroupName;
    }    

    
    /**
     * Gets the value of policyGroupName.
     * @param policyGroupName Get the value of policyGroupName.
     */
    public void setPolicyGroupName(String policyGroupName)
    {
    	this.policyGroupName = policyGroupName;
    }
    
    /**
     * Get the value of createTime.
     * 
     * @return the value of createTime.
     */
    public LocalDateTime getCreateTime() 
    {
        return createTime;
    }

    /**
     * Set the value of createTime.
     * 
     * @param timestamp
     *            The value of createTime.
     */
    public void setCreateTime(LocalDateTime timestamp) 
    {
        createTime = timestamp;
    }    
}
