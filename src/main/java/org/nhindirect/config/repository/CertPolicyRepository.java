package org.nhindirect.config.repository;

import java.util.List;

import org.nhindirect.config.store.CertPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertPolicyRepository extends JpaRepository<CertPolicy, Long>
{
	public CertPolicy findByPolicyNameIgnoreCase(String policyName);
	
	public void deleteByIdIn(List<Long> ids);
	
}
