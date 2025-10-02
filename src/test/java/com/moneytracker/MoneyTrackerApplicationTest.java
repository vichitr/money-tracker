package com.moneytracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MoneyTrackerApplicationTest {

    @Test
    void contextLoads() {
        // This test ensures that the Spring application context loads successfully
        // If there are any configuration issues, this test will fail
    }
}
