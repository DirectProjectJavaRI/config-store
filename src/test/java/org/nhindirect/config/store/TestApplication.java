package org.nhindirect.config.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"org.nhindirect.config.store"})
public class TestApplication
{
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }    
}
