package org.nhindirect.config.store;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("certpolicygroupreltn")
@Data
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
}
