package org.nhindirect.config.store;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("certpolicy")
@Data
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
    private LocalDateTime createTime = LocalDateTime.now(); 	
	 
}
