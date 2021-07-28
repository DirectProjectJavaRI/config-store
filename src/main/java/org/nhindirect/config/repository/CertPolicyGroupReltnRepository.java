package org.nhindirect.config.repository;

import org.nhindirect.config.store.CertPolicyGroupReltn;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CertPolicyGroupReltnRepository extends ReactiveCrudRepository<CertPolicyGroupReltn, Long>
{
	public Flux<CertPolicyGroupReltn> findByPolicyId(Long policyId);
	
	public Flux<CertPolicyGroupReltn> findByPolicyGroupId(Long groupId);
	
	@Transactional
	public Mono<Void> deleteByPolicyId(Long policyId);
	
	@Transactional
	public Mono<Void> deleteByPolicyGroupId(Long groupId);
}
