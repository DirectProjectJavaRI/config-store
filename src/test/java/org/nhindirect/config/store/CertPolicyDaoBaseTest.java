package org.nhindirect.config.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.security.Security;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.nhindirect.config.store.dao.CertPolicyDao;
import org.nhindirect.config.store.dao.DomainDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
public abstract class CertPolicyDaoBaseTest 
{
	@Autowired
	protected CertPolicyDao polDao;	
	
	@Autowired
	protected DomainDao dmDao;		
	
	static
	{
		try
		{
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		}
		catch (Exception e)
		{
			
		}
	}	
	
	
	@Before
	public void setUp()
	{
		clearPolicies();
		
		clearDomains();
	}
	
	protected void clearPolicies()
	{
		// remove policies
		Collection<CertPolicy> policies = polDao.getPolicies();
		
		assertNotNull(policies);
		
		if (!policies.isEmpty()) 
		{
			final long[] ids = new long[policies.size()];
			
			int idx = 0;
			for (CertPolicy policy : policies)
				ids[idx++] = policy.getId();
			
			polDao.deletePolicies(ids);
		}
		
		policies = polDao.getPolicies();
		
		assertTrue(policies.isEmpty());
		
		// remove groups
		Collection<CertPolicyGroup> policyGroups = polDao.getPolicyGroups();
		
		assertNotNull(policyGroups);
		
		if (!policyGroups.isEmpty()) 
		{
			final long[] ids = new long[policies.size()];
			
			int idx = 0;
			for (CertPolicyGroup group : policyGroups)
				ids[idx++] = group.getId();
			
			polDao.deletePolicyGroups(ids);
		}
		
		policyGroups = polDao.getPolicyGroups();
		
		assertTrue(policyGroups.isEmpty());
	}
	
	protected void clearDomains()
	{
		List<Domain> domains = dmDao.searchDomain(null, null);
		
		if (domains != null) 
			for (Domain dom : domains)
				dmDao.delete(dom.getDomainName());
						
		domains = dmDao.searchDomain(null, null);
		assertEquals(0, domains.size());
	}
}
