package org.nhindirect.config.repository;


import org.nhindirect.config.store.TrustBundle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrustBundleRepository extends JpaRepository<TrustBundle, Long>
{
	public TrustBundle findByBundleNameIgnoreCase(String bundleName);
}
