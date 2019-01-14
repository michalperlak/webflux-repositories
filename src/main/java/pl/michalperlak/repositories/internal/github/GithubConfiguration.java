package pl.michalperlak.repositories.internal.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource("classpath:github.properties")
class GithubConfiguration {

    @Value("${github.repositories.api.url}")
    private String repositoriesApiUrl;

    @Value("${github.api.version.accept.header}")
    private String apiVersionHttpHeader;

    @Bean
    GithubRepositoriesClient repositoriesClient() {
        return new GithubRepositoriesClient(webClient());
    }

    private WebClient webClient() {
        return WebClient.builder()
                .baseUrl(repositoriesApiUrl)
                .defaultHeader(HttpHeaders.ACCEPT, apiVersionHttpHeader)
                .build();
    }
}
