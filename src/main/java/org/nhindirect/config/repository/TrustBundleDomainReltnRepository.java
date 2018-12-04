package org.nhindirect.config.repository;

import java.util.Collection;

import org.nhindirect.config.store.Domain;
import org.nhindirect.config.store.TrustBundle;
import org.nhindirect.config.store.TrustBundleDomainReltn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TrustBundleDomainReltnRepository extends JpaRepository<TrustBundleDomainReltn, Long>
{
	public Collection<TrustBundleDomainReltn> findByDomain(Domain domain);
	
	@Transactional
	public void deleteByDomain(Domain domain);
	
	@Transactional
	public void deleteByTrustBundle(TrustBundle trustBundle);
	
	@Transactional
	public void deleteByDomainAndTrustBundle(Domain domain, TrustBundle trustBundle);
}
