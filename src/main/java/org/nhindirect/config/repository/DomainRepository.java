package org.nhindirect.config.repository;

import java.util.List;

import org.nhindirect.config.store.Domain;
import org.nhindirect.config.store.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DomainRepository extends JpaRepository<Domain, Long>
{
	public Domain findByDomainNameIgnoreCase(String domainName);
	
	public List<Domain> findByDomainNameContainingIgnoreCase(String domainName);
	
	public List<Domain> findByDomainNameContainingIgnoreCaseAndStatus(String domainName,  EntityStatus status);
	
	@Query("select d from Domain d where upper(d.domainName) in :domainNames")
	public List<Domain> findByDomainNameInIgnoreCase(@Param("domainNames") List<String> domainNames);
	
	@Query("select d from Domain d where upper(d.domainName) in :domainNames and d.status = :status")
	public List<Domain> findByDomainNameInIgnoreCaseAndStatus(@Param("domainNames") List<String> domainNames, @Param("status") EntityStatus status);
	
	public List<Domain> findByStatus(EntityStatus status);	
	
	@Transactional
	public void deleteByDomainNameIgnoreCase(String domainName);
}
