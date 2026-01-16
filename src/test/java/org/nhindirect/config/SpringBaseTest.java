package org.nhindirect.config;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.NONE)
@TestPropertySource("classpath:application.properties")
public abstract class SpringBaseTest
{	
	@BeforeEach
	public void setUp() 
	{

	}	
}
