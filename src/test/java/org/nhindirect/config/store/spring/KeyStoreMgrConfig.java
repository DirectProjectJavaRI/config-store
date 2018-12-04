package org.nhindirect.config.store.spring;

import org.nhindirect.common.crypto.KeyStoreProtectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("keyStoreMgr")
public class KeyStoreMgrConfig
{
    @Bean
    public KeyStoreProtectionManager keyStoreProtectionManager()
    {
    	return new org.nhindirect.common.crypto.impl.BootstrappedKeyStoreProtectionManager("12345", "67890");
    }
}
