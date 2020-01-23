package org.nhindirect.config;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.NONE)
@TestPropertySource("classpath:bootstrap.properties")
public abstract class SpringBaseTest
{	
	@Before
	public void setUp() 
	{

	}	
}
