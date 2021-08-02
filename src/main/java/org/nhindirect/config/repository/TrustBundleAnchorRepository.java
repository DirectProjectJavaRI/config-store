package org.nhindirect.config.repository;

import org.nhindirect.config.store.TrustBundleAnchor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TrustBundleAnchorRepository extends ReactiveCrudRepository<TrustBundleAnchor, Long>
{
	public Flux<TrustBundleAnchor> findByTrustBundleId(Long trustBundleId);
	
	@Transactional
	public Mono<Void> deleteByTrustBundleId(Long trustBundleId);	
}
