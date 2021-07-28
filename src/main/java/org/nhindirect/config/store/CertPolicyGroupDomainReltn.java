package org.nhindirect.config.store;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("certpolicygroupdomainreltn")
@Data
public class CertPolicyGroupDomainReltn 
{
	@Id
	private Long id;
	
	@Column("domain_id")
	private Long domainId;
	
	@Column("policy_group_id")
	private Long policyGroupId;
}
