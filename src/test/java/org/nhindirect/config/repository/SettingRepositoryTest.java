package org.nhindirect.config.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.nhindirect.config.SpringBaseTest;
import org.nhindirect.config.store.EntityStatus;
import org.nhindirect.config.store.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class SettingRepositoryTest extends SpringBaseTest
{	
	@Autowired
	private SettingRepository repo;	
	
	protected Setting newSetting(String name, String value)
	{
		final Setting retVal = new Setting();
		retVal.setName(name);
		retVal.setValue(value);
		retVal.setStatus(EntityStatus.ENABLED);
		retVal.setUpdateTime(Calendar.getInstance());
		retVal.setUpdateTime(Calendar.getInstance());
		
		return retVal;
	}
	
	private void addSetting(String name, String value) throws Exception
	{
		repo.save(newSetting(name, value));
	}	
	
	@Before
	public void cleanDataBase()
	{
		repo.deleteAll();
	}
	
	@Test
	public void testCleanDatabase() throws Exception 
	{
		Collection<Setting> settings = repo.findAll();
		
		assertEquals(0, settings.size());
	}
	
	@Test 
	public void addSettings() throws Exception
	{

		addSetting("TestName1", "TestValue1");
		addSetting("TestName2", "TestValue2");
		
		Collection<Setting> settings = repo.findAll();
		
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
		catch (DataIntegrityViolationException e)
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
		
		Collection<Setting> settings = repo.findAll();
		
		assertEquals(2, settings.size());
		
		addSetting("TestName3", "TestValue3");
		addSetting("TestName4", "TestValue4");
		addSetting("TestName5", "TestValue5");
		
		settings = repo.findAll();
		
		assertEquals(5, settings.size());
		
	}	
	
	@Test 
	public void testGetSettingsByName() throws Exception
	{
		
		addSetting("TestName1", "TestValue1");
		addSetting("TestName2", "TestValue2");
		
		Collection<Setting> settings = repo.findByNameIgnoreCaseIn(Arrays.asList("TestName1".toUpperCase()));
		
		assertEquals(1, settings.size());
		Setting setting = settings.iterator().next();
		assertEquals("TestName1",  setting.getName());
		assertEquals("TestValue1",  setting.getValue());
		
		
		settings = repo.findByNameIgnoreCaseIn(Arrays.asList("TestNAme2".toUpperCase()));
		
		assertEquals(1, settings.size());
		setting = settings.iterator().next();
		assertEquals("TestName2",  setting.getName());
		assertEquals("TestValue2",  setting.getValue());
		
		
		settings = repo.findByNameIgnoreCaseIn(Arrays.asList("TestName1".toUpperCase(), "TestName2".toUpperCase()));
		
		assertEquals(2, settings.size());

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
