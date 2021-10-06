package com.sparta.community;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {"spring.config.location=classpath:application-dev.properties"}
)
class CommunityApplicationTests {

    @Test
    void contextLoads() {
    }

}
