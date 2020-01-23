package org.nhindirect.config.repository;

import org.nhindirect.config.store.CertPolicyGroupReltn;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CertPolicyGroupReltnRepository extends ReactiveCrudRepository<CertPolicyGroupReltn, Long>
{
	@Query("select * from certpolicygroupreltn r where r.certPolicyId = :policyId")
	@Transactional
	public Flux<CertPolicyGroupReltn> findByPolicyId(Long policyId);
	
	@Query("select * from certpolicygroupreltn r where r.certPolicyGroupId = :groupId")
	@Transactional
	public Flux<CertPolicyGroupReltn> findByGroupId(Long groupId);
	
	@Query("delete from certpolicygroupreltn where certPolicyId = :policyId")
	@Transactional
	public Mono<Void> deleteByPolicyId(Long policyId);
	
	@Query("delete from certpolicygroupreltn where certPolicyGroupId = :groupId")
	@Transactional
	public Mono<Void> deleteByGroupId(Long groupId);
}
