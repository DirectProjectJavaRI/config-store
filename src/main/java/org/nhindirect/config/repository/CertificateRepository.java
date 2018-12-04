package org.nhindirect.config.repository;

import java.util.List;

import org.nhindirect.config.store.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CertificateRepository extends JpaRepository<Certificate, Long>
{
	@Transactional
	public List<Certificate> findByOwnerIgnoreCase(String owner);
	
	@Transactional
	public Certificate findByOwnerIgnoreCaseAndThumbprint(String owner, String tp);
	
	@Transactional
	public List<Certificate> findByThumbprint(String tb);
	
	@Transactional
	public void deleteByOwnerIgnoreCase(String owner);
	
	@Transactional
	public void deleteByIdIn(List<Long> ids);
}
