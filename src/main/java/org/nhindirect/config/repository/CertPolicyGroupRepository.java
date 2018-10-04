package org.nhindirect.config.repository;

import java.util.List;

import org.nhindirect.config.store.CertPolicyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CertPolicyGroupRepository extends JpaRepository<CertPolicyGroup, Long>
{
	public CertPolicyGroup findByPolicyGroupNameIgnoreCase(String groupName);
	
	@Transactional
	public void deleteByIdIn(List<Long> ids);
}
