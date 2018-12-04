package org.nhindirect.config.repository;

import java.util.Collection;

import org.nhindirect.config.store.CertPolicyGroup;
import org.nhindirect.config.store.CertPolicyGroupDomainReltn;
import org.nhindirect.config.store.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CertPolicyGroupDomainReltnRepository extends JpaRepository<CertPolicyGroupDomainReltn, Long>
{
	public Collection<CertPolicyGroupDomainReltn> findByDomain(Domain domain);
	
	@Transactional
	public void deleteByDomainAndCertPolicyGroup(Domain domain, CertPolicyGroup policyGroup);
	
	@Transactional
	public void deleteByDomain(Domain domain);
	
	@Transactional
	public void deleteByCertPolicyGroup(CertPolicyGroup policyGroup);
}
