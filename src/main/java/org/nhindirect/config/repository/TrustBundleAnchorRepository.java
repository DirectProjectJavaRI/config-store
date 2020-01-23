package org.nhindirect.config.repository;

import org.nhindirect.config.store.TrustBundleAnchor;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TrustBundleAnchorRepository extends ReactiveCrudRepository<TrustBundleAnchor, Long>
{
	@Transactional
	@Query("select * from trustbundleanchor a where a.trustBundleId = :trustBundleId")
	public Flux<TrustBundleAnchor> findByTrustBundleId(Long trustBundleId);
	
	@Transactional
	@Query("delete from trustbundleanchor where trustBundleId = :trustBundleId")
	public Mono<Void> deleteByTrustBundleId(Long trustBundleId);	
}
