package com.sourcery.gymapp.userProfile;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "spring.liquibase.enabled=false")
@ActiveProfiles("test")
class UserProfileApplicationTests {

    @Test
    void contextLoads() {
    }

}
