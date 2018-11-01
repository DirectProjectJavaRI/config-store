package org.nhindirect.config.repository;

import java.util.List;

import org.nhindirect.config.store.Anchor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AnchorRepository extends JpaRepository<Anchor, Long>
{
	@Transactional
	public List<Anchor> findByOwnerIgnoreCase(String owner);
	
	@Transactional
	@Query("select a from Anchor a where upper(a.owner) in :owners")
	public List<Anchor> findByOwnerInIgnoreCase(@Param("owners") List<String> owners);
	
	@Transactional
	public void deleteByOwnerIgnoreCase(String owner);
	
	@Transactional
	public void deleteByIdIn(List<Long> ids);
	
}
