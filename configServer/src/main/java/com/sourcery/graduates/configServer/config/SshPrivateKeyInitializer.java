package com.sourcery.graduates.configServer.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SshPrivateKeyInitializer {

    @PostConstruct
    public void init() {
        String encodedKey = System.getenv("SSH_PRIVATE_KEY_BASE64");
        if (encodedKey != null) {
            String decodedKey = new String(java.util.Base64.getDecoder().decode(encodedKey));
            System.setProperty("SSH_PRIVATE_KEY", decodedKey);
        }
    }
}
