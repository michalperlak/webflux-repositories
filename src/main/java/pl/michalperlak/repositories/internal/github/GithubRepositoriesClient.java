package pl.michalperlak.repositories.internal.github;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import pl.michalperlak.repositories.internal.ExternalRepositoriesClient;
import pl.michalperlak.repositories.internal.ExternalRepository;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.time.Duration;

class GithubRepositoriesClient implements ExternalRepositoriesClient {

    private final WebClient webClient;

    GithubRepositoriesClient(final WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<ExternalRepository> getRepository(final String owner, final String name) {

        return webClient.method(HttpMethod.GET)
                .uri(buildUri(owner, name))
                .exchange()
                .timeout(RESPONSE_TIMEOUT)
                .flatMap(GithubRepositoriesClient::getDataObjectFromResponse);
    }

    private static Mono<ExternalRepository> getDataObjectFromResponse(final ClientResponse clientResponse) {
        if (clientResponse.statusCode().is2xxSuccessful()) {
            return clientResponse.bodyToMono(ExternalRepository.class);
        }
        return Mono.empty();
    }

    private String buildUri(final String owner, final String repositoryName) {
        return MessageFormat.format("/{0}/{1}", owner, repositoryName);
    }

    private static final Duration RESPONSE_TIMEOUT = Duration.ofMillis(2000);
}
