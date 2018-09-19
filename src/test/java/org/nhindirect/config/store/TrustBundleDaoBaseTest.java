package org.nhindirect.config.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.security.Security;
import java.util.Collection;
import java.util.List;


import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.nhindirect.config.store.dao.DomainDao;
import org.nhindirect.config.store.dao.TrustBundleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
public abstract class TrustBundleDaoBaseTest
{
	@Autowired
	protected TrustBundleDao tbDao;	
	
	@Autowired
	protected DomainDao dmDao;		
	
	protected static final String certBasePath = "src/test/resources/certs/"; 
	
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
	
	protected static byte[] loadCertificateData(String certFileName) throws Exception
	{
		File fl = new File(certBasePath + certFileName);
		
		return FileUtils.readFileToByteArray(fl);
	}
	
	@Before
	public void setUp()
	{
		clearBundles();
		
		clearDomains();
	}
	
	protected void clearBundles()
	{
		Collection<TrustBundle> bundles = tbDao.getTrustBundles();
		
		assertNotNull(bundles);
		
		if (!bundles.isEmpty()) 
		{
			final long[] ids = new long[bundles.size()];
			
			int idx = 0;
			for (TrustBundle bundle : bundles)
				ids[idx++] = bundle.getId();
			
			tbDao.deleteTrustBundles(ids);
		}
		
		bundles = tbDao.getTrustBundles();
		
		assertTrue(bundles.isEmpty());
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
