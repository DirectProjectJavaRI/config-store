package org.nhindirect.config.repository;

import java.util.List;

import org.nhindirect.config.store.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SettingRepository extends JpaRepository<Setting, Long>
{
	@Query("select s from Setting s where upper(s.name) in :names")
	public List<Setting> findByNameIgnoreCaseIn(@Param("names") List<String> names);
	
	public Setting findByNameIgnoreCase(String name);
	
	@Transactional
	public void deleteByNameIgnoreCase(String name);
	
}

