/* 
Copyright (c) 2010, NHIN Direct Project
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
   in the documentation and/or other materials provided with the distribution.  
3. Neither the name of the The NHIN Direct Project (nhindirect.org) nor the names of its contributors may be used to endorse or promote 
   products derived from this software without specific prior written permission.
   
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.nhindirect.config.repository;

import java.util.List;

import org.nhindirect.config.store.Domain;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DomainRepository extends ReactiveCrudRepository<Domain, Long>
{
	public Mono<Domain> findByDomainNameIgnoreCase(String domainName);
	
	@Query("select * from domain d where upper(d.domainName) like upper(:domainName)")
	public Flux<Domain> findByDomainNameContainingIgnoreCase(String domainName);
	
	@Query("select * from domain d where upper(d.domainName) like upper(:domainName) and d.status = :status")
	public Flux<Domain> findByDomainNameContainingIgnoreCaseAndStatus(String domainName,  int status);
	
	public Flux<Domain> findByDomainNameInIgnoreCase(List<String> domainNames);
	
	public Flux<Domain> findByDomainNameInIgnoreCaseAndStatus(List<String> domainNames, int status);
	
	public Flux<Domain> findByStatus(int status);	
	
	@Transactional
	public Mono<Void> deleteByDomainNameIgnoreCase(String domainName);
}
