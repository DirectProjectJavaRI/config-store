package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;


import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.nhindirect.config.SpringBaseTest;
import org.nhindirect.config.store.Anchor;
import org.nhindirect.config.store.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.test.StepVerifier;

public class AnchorRepositoryTest extends SpringBaseTest
{
	
	private static final String certBasePath = "src/test/resources/certs/"; 
	
	private static final String TEST_DOMAIN = "TestDomain1";
	
	@Autowired
	private AnchorRepository repo;
	
	@Before
	public void setUp()
	{
		super.setUp();
		
		repo.deleteAll().block();
	}
	
	private void addTestAnchors() throws Exception
	{
		Anchor anchor = new Anchor();
		anchor.setData(loadCertificateData("secureHealthEmailCACert.der"));
		anchor.setOwner(TEST_DOMAIN);
		anchor.setOutgoing(true);
		anchor.setIncoming(true);
		
		repo.save(anchor)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();

		anchor = new Anchor();
		anchor.setData(loadCertificateData("cacert.der"));
		anchor.setOwner(TEST_DOMAIN);
		anchor.setOutgoing(true);
		anchor.setIncoming(true);		

		repo.save(anchor)
		.as(StepVerifier::create) 
		.expectNextCount(1) 
		.verifyComplete();
		
	}
	
	private static byte[] loadCertificateData(String certFileName) throws Exception
	{
		File fl = new File(certBasePath + certFileName);
		
		return FileUtils.readFileToByteArray(fl);
	}
	
	@Test
	public void testCleanDatabase() throws Exception 
	{
		repo.findAll()
		.as(StepVerifier::create)
		.expectNextCount(0)
		.verifyComplete();
	}

	@Test
	public void testDeleteByIds() throws Exception 
	{

		addTestAnchors();
		
		// get all anchors
		List<Anchor> anchors = repo.findAll().collectList().block();
		
		assertNotNull(anchors);
		assertTrue(anchors.size() > 0);
		
		// now delete all by ids
		for(Anchor anchorToDel : anchors)
			repo.deleteById(anchorToDel.getId())
			.as(StepVerifier::create) 
			.verifyComplete();
			

		// get all and make sure it is empty
		repo.findAll()
		.as(StepVerifier::create) 
		.expectNextCount(0) 
		.verifyComplete();
	}
	
	@Test
	public void testAddAnchor() throws Exception
	{
		addTestAnchors();
		
		// validate the anchor was created
		List<Anchor> anchors = repo.findAll().collectList().block();
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
		
		List<Anchor> anchors = repo.findByOwnerIgnoreCase(TEST_DOMAIN).collectList().block();
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
		List<Anchor> anchors = repo.findAll().collectList().block();
		
		assertEquals(2, anchors.size());
		
		// now update
		for (Anchor anchor : anchors)
			anchor.setStatus(EntityStatus.ENABLED.ordinal());

		repo.saveAll(anchors)
		.as(StepVerifier::create) 
		.expectNextCount(2) //
		.verifyComplete();
		
		// get all domains again
		anchors = repo.findAll().collectList().block();
		
		assertEquals(2, anchors.size());
		
		for (Anchor anchor : anchors)
		{
			assertEquals(EntityStatus.ENABLED.ordinal(), anchor.getStatus());
			assertEquals(TEST_DOMAIN, anchor.getOwner());
		}
	}
	
	
	@Test
	public void testUpdateByOwner() throws Exception
	{

		addTestAnchors();
		
		List<Anchor> anchors = repo.findByOwnerIgnoreCase(TEST_DOMAIN.toUpperCase()).collectList().block();
		
		assertEquals(2, anchors.size());
		
		// now update
		for (Anchor anchor : anchors)
			anchor.setStatus(EntityStatus.ENABLED.ordinal());

		repo.saveAll(anchors)
		.as(StepVerifier::create) 
		.expectNextCount(2) //
		.verifyComplete();
		
		
		// get all domains again
		anchors = repo.findByOwnerIgnoreCase(TEST_DOMAIN.toUpperCase()).collectList().block();
		
		assertEquals(2, anchors.size());
		
		for (Anchor anchor : anchors)
		{
			assertEquals(EntityStatus.ENABLED.ordinal(), anchor.getStatus());
			assertEquals(TEST_DOMAIN, anchor.getOwner());
		}
	}
}

