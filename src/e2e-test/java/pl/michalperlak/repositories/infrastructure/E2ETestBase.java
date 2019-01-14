package pl.michalperlak.repositories.infrastructure;

import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.michalperlak.repositories.RepositoriesApplication;

public class E2ETestBase {
    private static ConfigurableApplicationContext applicationContext;

    protected E2ETestBase() {
    }

    @BeforeClass
    public static void startServer() {
        applicationContext = SpringApplication.run(RepositoriesApplication.class);
    }

    @AfterAll
    public static void stopServer() {
        applicationContext.close();
    }
}
