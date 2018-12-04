package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.nhindirect.config.SpringBaseTest;
import org.nhindirect.config.store.Anchor;
import org.nhindirect.config.store.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class AnchorRepositoryTest extends SpringBaseTest
{
	
	private static final String certBasePath = "src/test/resources/certs/"; 
	
	private static final String TEST_DOMAIN = "TestDomain1";
	
	@Autowired
	private AnchorRepository repo;
	
	private void addTestAnchors() throws Exception
	{
		Anchor anchor = new Anchor();
		anchor.setData(loadCertificateData("secureHealthEmailCACert.der"));
		anchor.setOwner(TEST_DOMAIN);
		anchor.setOutgoing(true);
		anchor.setIncoming(true);
		
		repo.save(anchor);

		anchor = new Anchor();
		anchor.setData(loadCertificateData("cacert.der"));
		anchor.setOwner(TEST_DOMAIN);
		anchor.setOutgoing(true);
		anchor.setIncoming(true);		

		repo.save(anchor);
		
	}
	
	private static byte[] loadCertificateData(String certFileName) throws Exception
	{
		File fl = new File(certBasePath + certFileName);
		
		return FileUtils.readFileToByteArray(fl);
	}
	
	@Before
	public void cleanDataBase()
	{
		repo.deleteAll();
	}
	
	@Test
	public void testCleanDatabase() throws Exception 
	{
		final List<Anchor> anchors = repo.findAll();
		
		assertEquals(0, anchors.size());
	}

	@Test
	public void testDeleteByIds() throws Exception 
	{

		addTestAnchors();
		
		// get all anchors
		List<Anchor> anchors = repo.findAll();
		assertNotNull(anchors);
		assertTrue(anchors.size() > 0);
		
		// now delete all by ids
		for(Anchor anchorToDel : anchors)
			repo.deleteById(anchorToDel.getId());

		// get all and make sure it is empty
		anchors = repo.findAll();
		
		assertEquals(0, anchors.size());
		
		
	}
	
	@Test
	public void testAddAnchor() throws Exception
	{
		addTestAnchors();
		
		// validate the anchor was created
		List<Anchor> anchors = repo.findAll();
		assertNotNull(anchors);
		assertEquals(2, anchors.size());
		
		Anchor retAnchor = anchors.get(0);
		
		assertEquals(retAnchor.getOwner(), TEST_DOMAIN);
		
	}
	
	@Test
	public void testGetByOwner() throws Exception
	{
		// clean out all anchors
		testCleanDatabase();
		
		addTestAnchors();
		
		List<String> owners = new ArrayList<String>();
		owners.add(TEST_DOMAIN.toUpperCase());
		
		List<Anchor> anchors = repo.findByOwnerInIgnoreCase(owners);
		assertNotNull(anchors);
		assertEquals(2, anchors.size());
		
		for (Anchor retAnchor : anchors)
			assertEquals(retAnchor.getOwner(), TEST_DOMAIN);
		
	}
	
	@Test
	public void testUpdateByIds() throws Exception
	{
		addTestAnchors();

		// get all domains
		List<Anchor> anchors = repo.findAll();
		
		assertEquals(2, anchors.size());
		
		// now update
		for (Anchor anchor : anchors)
			anchor.setStatus(EntityStatus.ENABLED);

		repo.saveAll(anchors);
		
		// get all domains again
		anchors = repo.findAll();
		
		assertEquals(2, anchors.size());
		
		for (Anchor anchor : anchors)
		{
			assertEquals(EntityStatus.ENABLED, anchor.getStatus());
			assertEquals(TEST_DOMAIN, anchor.getOwner());
		}
	}
	
	
	@Test
	public void testUpdateByOwner() throws Exception
	{

		addTestAnchors();
		
		List<Anchor> anchors = repo.findByOwnerIgnoreCase(TEST_DOMAIN.toUpperCase());
		
		assertEquals(2, anchors.size());
		
		// now update
		for (Anchor anchor : anchors)
			anchor.setStatus(EntityStatus.ENABLED);

		repo.saveAll(anchors);
		
		
		// get all domains again
		anchors = repo.findAll();
		
		assertEquals(2, anchors.size());
		
		for (Anchor anchor : anchors)
		{
			assertEquals(EntityStatus.ENABLED, anchor.getStatus());
			assertEquals(TEST_DOMAIN, anchor.getOwner());
		}
	}
}

