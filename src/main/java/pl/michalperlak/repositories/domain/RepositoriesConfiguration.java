package pl.michalperlak.repositories.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.michalperlak.repositories.internal.ExternalRepositoriesClient;

@Configuration
class RepositoriesConfiguration {

    @Bean
    RepositoriesFacade repositoriesFacade(final ExternalRepositoriesClient repositoriesClient) {
        return new RepositoriesFacade(repositoriesClient);
    }
}
