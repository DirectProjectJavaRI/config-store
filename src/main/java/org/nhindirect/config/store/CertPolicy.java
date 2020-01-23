package org.nhindirect.config.store;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("certpolicy")
public class CertPolicy 
{
	@Id
	private Long id;
	
	@Column("policyName")
	private String policyName;
	
	private int lexicon;
	
	@Column("data")
	private byte[] policyData;
	
	@Column("createTime")
    private LocalDateTime createTime;  	
	
	public CertPolicy()
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
    
    public int getLexicon()
    {
    	return lexicon;
    }
    
    public void setLexicon(int lexicon)
    {
    	this.lexicon = lexicon;
    }
    
    /**
     * Get the value of policyName.
     * 
     * @return the value of policyName.
     */
    public String getPolicyName() 
    {
        return policyName;
    }    

    
    /**
     * Gets the value of policyName.
     * @param policyName Get the value of policyName.
     */
    public void setPolicyName(String policyName)
    {
    	this.policyName = policyName;
    }
    
    public byte[] getPolicyData()
    {
    	return policyData;
    }
    
    public void setPolicyData(byte[] policyData)
    {
    	this.policyData = policyData;
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
