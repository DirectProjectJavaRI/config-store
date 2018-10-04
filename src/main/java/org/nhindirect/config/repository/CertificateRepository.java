package org.nhindirect.config.repository;

import java.util.List;

import org.nhindirect.config.store.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CertificateRepository extends JpaRepository<Certificate, Long>
{
	public List<Certificate> findByOwnerIgnoreCase(String owner);
	
	public Certificate findByOwnerIgnoreCaseAndThumbprint(String owner, String tp);
	
	public List<Certificate> findByThumbprint(String tb);
	
	@Transactional
	public void deleteByOwnerIgnoreCase(String owner);
	
	@Transactional
	public void deleteByIdIn(List<Long> ids);
}
