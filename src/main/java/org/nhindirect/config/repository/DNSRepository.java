package org.nhindirect.config.repository;

import java.util.List;

import org.nhindirect.config.store.DNSRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface DNSRepository extends JpaRepository<DNSRecord, Long>
{
	public List<DNSRecord> findByNameIgnoreCase(String name);
	
	public List<DNSRecord> findByNameIgnoreCaseAndType(String name, int type);
	
	public List<DNSRecord> findByType(int type);
	
	@Transactional
	public void deleteByIdIn(List<Long> ids);
}
