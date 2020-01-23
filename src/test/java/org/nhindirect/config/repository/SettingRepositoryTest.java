package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.nhindirect.config.SpringBaseTest;
import org.nhindirect.config.store.EntityStatus;
import org.nhindirect.config.store.Setting;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.test.StepVerifier;

public class SettingRepositoryTest extends SpringBaseTest
{	
	@Autowired
	private SettingRepository repo;	
	
	protected Setting newSetting(String name, String value)
	{
		final Setting retVal = new Setting();
		retVal.setName(name);
		retVal.setValue(value);
		retVal.setStatus(EntityStatus.ENABLED.ordinal());
		retVal.setUpdateTime(LocalDateTime.now());
		retVal.setUpdateTime(LocalDateTime.now());
		
		return retVal;
	}
	
	private void addSetting(String name, String value) throws Exception
	{
		repo.save(newSetting(name, value)).block();
	}	
	
	@Before
	public void cleanDataBase()
	{
		repo.deleteAll()
		.as(StepVerifier::create) 
		.verifyComplete();
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
	public void addSettings() throws Exception
	{

		addSetting("TestName1", "TestValue1");
		addSetting("TestName2", "TestValue2");
		
		Collection<Setting> settings = repo.findAll().collectList().block();
		
		assertEquals(2, settings.size());
	}
	
	@Test 
	public void testAddDuplicateSettings_AssertException() throws Exception
	{
		addSetting("TestName1", "TestValue1");
		
		boolean exceptionOccured = false;
		
		try
		{
			addSetting("TestName1", "TestValue2");
		}
		catch (Exception e)
		{
			exceptionOccured = true;
		}
		
		assertTrue(exceptionOccured);
	}	
	
	@Test 
	public void testGetAllSettings() throws Exception
	{
		
		addSetting("TestName1", "TestValue1");
		addSetting("TestName2", "TestValue2");
		
		Collection<Setting> settings = repo.findAll().collectList().block();
		
		assertEquals(2, settings.size());
		
		addSetting("TestName3", "TestValue3");
		addSetting("TestName4", "TestValue4");
		addSetting("TestName5", "TestValue5");
		
		settings = repo.findAll().collectList().block();
		
		assertEquals(5, settings.size());
		
	}	
	
	@Test 
	public void testGetSettingsByName() throws Exception
	{
		
		addSetting("TestName1", "TestValue1");
		addSetting("TestName2", "TestValue2");
		
		Setting setting = repo.findByNameIgnoreCase("TestName1".toUpperCase()).block();
		
		assertEquals("TestValue1",  setting.getValue());
		
		
		setting = repo.findByNameIgnoreCase("TestNAme2".toUpperCase()).block();
		
		assertEquals("TestName2",  setting.getName());
		assertEquals("TestValue2",  setting.getValue());

	}	
	
	
	@Test 
	public void testUpdateSetting() throws Exception
	{
		/**
		 *  Move up to biz logic
		 **/ 
		/*
		addSetting("TestName1", "TestValue1");
		addSetting("TestName2", "TestValue2");
		
		Collection<Setting> settings = repo.findByName(Arrays.asList("TestName1"));
		
		assertEquals(1, settings.size());
		Setting setting = settings.iterator().next();
		assertEquals("TestName1",  setting.getName());
		assertEquals("TestValue1",  setting.getValue());
		settingDao.update("TestName1", "TestUpdatedValue1");
		settings = settingDao.getByNames(Arrays.asList("TestName1"));
		assertEquals("TestName1",  setting.getName());
		assertEquals("TestUpdatedValue1",  setting.getValue());
		

		settingDao.update("TestName2", "TestUpdatedValue2");
		settings = settingDao.getByNames(Arrays.asList("TestName2"));
		setting = settings.iterator().next();
		assertEquals("TestName2",  setting.getName());
		assertEquals("TestUpdatedValue2",  setting.getValue());
		*/
	}	
	
}
