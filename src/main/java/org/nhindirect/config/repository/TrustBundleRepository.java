package org.nhindirect.config.repository;


import javax.transaction.Transactional;

import org.nhindirect.config.store.TrustBundle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrustBundleRepository extends JpaRepository<TrustBundle, Long>
{
	@Transactional
	public TrustBundle findByBundleNameIgnoreCase(String bundleName);
}
