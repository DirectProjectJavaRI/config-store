package org.nhindirect.config.repository;

import java.util.List;

import org.nhindirect.config.store.Address;
import org.nhindirect.config.store.Domain;
import org.nhindirect.config.store.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends JpaRepository<Address, Long>
{
	public Address findByEmailAddressIgnoreCase(String emailAddress);
	
	public List<Address> findByEndpointIgnoreCase(String endpoint);
	
	@Query("select a from Address a where upper(a.emailAddress) in :emailAddresses and a.status = :status")
	public List<Address> findByEmailAddressInIgnoreCaseAndStatus(@Param("emailAddresses") List<String> emailAddresses, @Param("status") EntityStatus status);
	
	public List<Address> findByDomain(Domain domain);
	
	public void deleteByEmailAddressIgnoreCase(String emailAddress);
}
