package org.nhindirect.config.store;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("certpolicygroup")
@Data
public class CertPolicyGroup 
{
	@Id
	private Long id;
	
	@Column("policyGroupName")
	private String policyGroupName;
	
	@Column("createTime")
    private LocalDateTime createTime = LocalDateTime.now();
		
}
